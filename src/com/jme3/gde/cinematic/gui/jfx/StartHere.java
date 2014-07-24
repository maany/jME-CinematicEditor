/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

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
        cinematicEditor.getLayerContainer().setCinematicEditor(cinematicEditor);
        cinematicEditor.initView(root);
        
    }
    /**
     * Initialize the content of Cinematic Editor
     */
    public void initRoot() {
        CinematicClip clip = new CinematicClip("MyClip");
        clip.setDuration(30);
        CinematicEditorManager.getInstance().setCurrentClip(clip);
        root = new Layer("Root",null);
        clip.setRoot(root);
        
        Layer child = new Layer("Child",root);
        Layer sibling = new Layer("Sibling",root);
        Layer grandChild = new Layer("GrandChild",child);
        Layer grandChildCousin = new Layer("GrandChildCousin",sibling);
        
        Event event = new Event("E1", child, 5, 5);
        Event eventA = new Event("E2", sibling, 17, 7);
        
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
