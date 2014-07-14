/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;




import com.jme3.gde.cinematic.core.Layer;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author MAYANK
 */
public class CinematicEditorUI extends AnchorPane{
    @FXML
    private TimelineControl timeline ;
    @FXML
    private LayerContainerControl layerContainer ;
    private ScrollBar vbar;
    private ScrollPane timelineScrollPane;
    private VBox timelineScrollPaneVBox;
    private TableView layerContainerTableView;
    private Layer root;
    public static final double ROW_HEIGHT = 30.0; // if changed here, change it in layer_container.css as well
    public CinematicEditorUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CinematicEditorUI.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            
        }catch(Exception ex)
        {
            System.out.println("Failed to load CinematicEditorUI.fxml");
        }
        
    }

    
    public void initCinematicEditorUI() {
        timeline.initTimeline();
        timeline.setCinematicEditor(this);
        layerContainer.initLayerContainer();
        layerContainer.setCinematicEditor(this);
        initControls();
        syncHeight();
        syncVerticalScrolling();
        //syncContent();
    }
    public void start(Stage stage) throws Exception {
       /* layerContainer = new LayerContainerControl();
        timeline = new TimelineControl();
        Scene scene;
        scene = new Scene(layerContainer,440,205);
        stage.setScene(scene);
        stage.show();
        layerContainer.initLayerContainer();
        ScrollBar vScrollBar = layerContainer.getVScrollBar();
        System.out.println("ScrolBar : " + vScrollBar.toString()); */
        //   timelineControl.initTimeline();
        //timeline.test();
        //timeline.getCurrentTime().setValue(10);
    }
    
    private void initControls() {
        vbar = layerContainer.getVScrollBar();
        timelineScrollPane = timeline.getTimelineScrollPane();
        timelineScrollPaneVBox = timeline.getTimelineScrollPaneVBox();
    }
/**
 * Links the heights of the timeline and layerContainer and keeps them in sync when layers are added or removed. 
 * This is required for synchronized vertical scrolling
 */
    private void syncHeight() {
        layerContainerTableView = layerContainer.getLayerContainer();
        double size = layerContainerTableView.getItems().size()*CinematicEditorUI.ROW_HEIGHT;
        timelineScrollPaneVBox.setPrefHeight(size);
    }
    /**
     * Links the vertical scrollbars of the layerContainer with the vertical scrollbar of the timeline
     * as we need only 1 scrollbar for vertical scrolling of both components. If the heights are same for 
     * layerContainer and timeline, then scrolling will be synchronized.
     */
    private void syncVerticalScrolling() {
        timelineScrollPane.vmaxProperty().bindBidirectional(vbar.maxProperty());
        timelineScrollPane.vminProperty().bindBidirectional(vbar.minProperty());
        timelineScrollPane.vvalueProperty().bindBidirectional(vbar.valueProperty());
        
    }
    /**
     * A public method to be called everytime the height of editor changes. Known cases : when a new layer view is added/removed.
     */
    public void syncHeightAndVerticallScrolling() {
        syncHeight();
        syncVerticalScrolling();
    }

    /**
     * Creates a new layer, attached it to root and renders the layer view if the parent is not collapsed.
     * @param layer 
     */
    public void addNewLayer(Layer layer){
        Layer parent = null;
        
        parent = layer.getParent();
            System.out.println("PARENT : " + parent.getName());
        
        if(!parent.getLaf().isCollapsed()) {
            int index = getIndex(parent);
            index+= parent.getVisibleDescendants().size();
            index++;
            addLayerView(index, layer);
        }
        
    }
    /**
     * deletes the layer and removes layer view + descendant layer view.
     * Note : root cannot be removed
     * @param index
     * @param layer 
     */
    public void removeLayer(Layer layer) {
        layer.getParent().getChildren().remove(layer);
        removeLayerView(getIndex(layer));
        hideChildren(layer);
    }
    /**
     * creates the layer view of an existing layer. The layer view consists of a layerContainer part and a timeline part.
     * @param layer the layer for which the layer view is to be created
     */
    private void addLayerView(int index,Layer layer) {
         layerContainer.addLayerView(index,layer);
         timeline.addLayerView(index,layer);
         syncHeightAndVerticallScrolling();
    }
    /**
     * hides the layer view for the Layer at given index in the layerContainer
     * hides only 1 layer view
     * @param index 
     */
    
    public void removeLayerView(int index) {
        layerContainer.removeLayerView(index);
        timeline.removeLayerView(index);
        syncHeightAndVerticallScrolling();
    }
    /**
     * show visible children and visible descendants
     * @param parent 
     */
    public void showChildren(Layer parent) {
        int index = getIndex(parent) + 1;
        System.out.println("Index of " + parent.getName() + " is " + (index-1) + " and its collapsed? " + parent.getLaf().isCollapsed());
        List<Layer> visibleDescendants = parent.findAllVisibleDescendants();
        
        System.out.println("Size of Visible Descendants for " + parent.getName() + " is " + visibleDescendants.size());
        System.out.println("Visible decendants of " + parent.getName() + " are " + visibleDescendants);
        for(Layer layer:visibleDescendants) {
            
            addLayerView(index, layer);
            index++;
        }
    }
    /**
     * hides the layer view and all descendant layer views
     * @param parent 
     */
    public void hideChildren(Layer parent){
        int index = getIndex(parent) + 1;
        List<Layer> visibleDescendants = parent.findAllVisibleDescendants();
        parent.getLaf().setCollapsed(true);
        for (Layer descendant : visibleDescendants) {
            removeLayerView(getIndex(descendant));
            index++;
        }
    }
    /**
     * Based on the root it creates layers in the editor
     * @param root 
     */
    public void initView(Layer root) {
        int index = 0;
        this.root = root;
        this.addLayerView(index,root);
        if (!root.getLaf().isCollapsed()) {
            for (Layer child : root.findAllVisibleDescendants()) {
                index++;
                addLayerView(index,child);
            }
        }
    }
    /**
     * Finds all visibile layers and shows them in the editor in proper heirarchy. 
     * If layer views get messed up, this method should be used to reload the entire view
     */
    public void syncView() {
        int index=0;
        for(Layer layer:(List<Layer>)layerContainerTableView.getItems())
        {
            removeLayerView(index);
            index++;
        }
        index = 0;
        for(Layer layer:root.findAllVisibleDescendants()){
            addLayerView(index,layer);
            index++;
        }
            
    }
    /**
     * return the index of layer in the TableView
     * @param layer
     * @return 
     */
    public int getIndex(Layer layer) {
        int index =-1;
            for(int i=0;i<layerContainerTableView.getItems().size();i++)
            {
                Layer inViewLayer = (Layer) layerContainerTableView.getItems().get(i);
                if(inViewLayer==layer)
                    index=i; 
            }
        return index;
    }
    public Layer getRoot() {
        return root;
    }

    public void setRoot(Layer root) {
        this.root = root;
    }

    public LayerContainerControl getLayerContainer() {
        return layerContainer;
    }

    public void setLayerContainer(LayerContainerControl layerContainer) {
        this.layerContainer = layerContainer;
    }

    public TimelineControl getTimeline() {
        return timeline;
    }

    public void setTimeline(TimelineControl timeline) {
        this.timeline = timeline;
    }
    
    
    
}
