/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.cinematic.gui.jfx;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Event;
import com.jme3.gde.cinematic.core.Layer;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author MAYANK
 */
public class StartHere extends Application{
    CinematicEditorUI cinematicEditor;
    TimelineControl timeline;
    LayerContainerControl layerContainer;
    Scene scene;
    Layer root;
    @Override
    public void start(Stage stage) throws Exception {
        launchCinematicEditor(stage);
        //launchTimeline(stage);
        //launchLayerContainer(stage);
        
    }
    public void launchCinematicEditor(Stage stage) {
        cinematicEditor = new CinematicEditorUI();
        scene = new Scene(cinematicEditor,660,220);
        scene.getStylesheets().add(getClass().getResource("layer_container.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        initRoot();
        cinematicEditor.initCinematicEditorUI();
        cinematicEditor.initView(root);
        
    }
    public void initRoot() {
        CinematicClip clip = new CinematicClip("MyClip");
        CinematicEditorManager.getInstance().setCurrentClip(clip);
        CinematicEditorManager.getInstance().getCurrentClip().setDuration(30);
        root = new Layer("MyClip-root",null);
        clip.setRoot(root);
        Layer child = new Layer("Child",root);
        //for(int i=1;i<=10;i++){
        //    child = new Layer("Child" + i,root);
         //}
        root.getLaf().setCollapsed(false);
        child.getLaf().setCollapsed(false);
        Layer sibling = new Layer("Sibling",root);
        Layer grandChild = new Layer("GrandChild",child);
        Layer grandChildCousin = new Layer("GrandChildCousin",sibling);
        Event childEvent = new Event();
        childEvent.setName("Child-E");
        childEvent.setDuration(5);
        childEvent.setLayer(child);
        childEvent.setStartPoint(5);
        child.getEvents().add(childEvent);
        
        Event sibEvent = new Event("sibEv", sibling, 21, 8);
        
        
      /*  System.out.println("Root : " + root.getLaf().isCollapsed());
        System.out.println("Child : " + child.getLaf().isCollapsed());
        System.out.println("Sibling : " + sibling.getLaf().isCollapsed());
        System.out.println("GC : " + grandChild.getLaf().isCollapsed());
        System.out.println("GCC : " + grandChildCousin.getLaf().isCollapsed());
        sibling.getLaf().setCollapsed(false);
       List<Layer> visibleDescendants = sibling.findAllVisibleDescendants();
        System.out.println("Visible Descendants :" + visibleDescendants.size());
        for(Layer visible:visibleDescendants) 
        System.out.println("VisibleList : " + visible.getName());
        System.out.println("AGAIN : " + sibling.findAllVisibleDescendants());
        System.out.println("AGAIN : " + sibling.findAllVisibleDescendants());
        */
       /* List<Layer> descendants = root.getDescendants();
        System.out.println("Descendants : "+ descendants.size());
        for(Layer descendant:descendants)
            System.out.println("Descendant : " + descendant.getName()); 
            * */
    }
    public void launchTimeline(Stage stage){
        timeline = new TimelineControl();
        scene = new Scene(timeline,440,220);
        
        stage.setScene(scene);
        stage.show();
        timeline.initTimeline();
    }
    public void launchLayerContainer(Stage stage){
        layerContainer = new LayerContainerControl();
        scene = new Scene(layerContainer,220,220);
        scene.getStylesheets().add(getClass().getResource("layer_container.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        layerContainer.initLayerContainer();
    }
    public static void main(String args[]) {
        launch(args);
    }
}
