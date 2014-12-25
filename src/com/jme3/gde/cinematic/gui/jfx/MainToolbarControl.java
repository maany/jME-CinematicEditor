/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import com.jme3.gde.cinematic.CinematicEditorTopComponent;
import com.jme3.gde.cinematic.core.CinematicMonkey;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.cinematic.scene.CinematicEditorToolController;
import com.jme3.gde.cinematic.scene.tools.MoveTool;
import com.jme3.gde.cinematic.scene.tools.RotateTool;
import com.jme3.gde.cinematic.scene.tools.ScaleTool;
import com.jme3.gde.cinematic.scene.tools.SelectTool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 *
 * @author MAYANK
 */
public class MainToolbarControl extends HBox{
    @FXML
    Button scaleButton;
    @FXML
    Button selectButton;
    @FXML
    Button moveButton;
    @FXML
    Button rotateButton;
    @FXML
    ToggleButton playToggleButton;
    @FXML
    ToggleButton stopToggleButton;
    @FXML
    ToggleButton pauseToggleButton;
    private boolean playState = false;
    public MainToolbarControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_toolbar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        initPlaybackControl();
        initActions();
    }

   private void initPlaybackControl() {
        ToggleGroup playbackControlToolbar = new ToggleGroup();
        playToggleButton.setToggleGroup(playbackControlToolbar);
        stopToggleButton.setToggleGroup(playbackControlToolbar);
        pauseToggleButton.setToggleGroup(playbackControlToolbar);
        
        playToggleButton.setUserData(GuiManager.PLAYSTATE_PLAY);
        pauseToggleButton.setUserData(GuiManager.PLAYSTATE_PAUSE);
        stopToggleButton.setUserData(GuiManager.PLAYSTATE_STOP);
        
        playbackControlToolbar.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                String action = t1.getUserData().toString();
                if (action != null) {
                    switch (action) {
                        case GuiManager.PLAYSTATE_PLAY:
                            startPlayback();
                            break;
                        case GuiManager.PLAYSTATE_PAUSE:
                            pausePlayback();
                            break;
                        case GuiManager.PLAYSTATE_STOP:
                            stopPlayback();
                            break;
                    }
                }
            }
        });
    }
   
   private void startPlayback(){
       CinematicMonkey monkey = new CinematicMonkey();
       monkey.startPlayback();
   }
   
   private void pausePlayback(){}
   
   private void stopPlayback(){}
   
   
   private void initActions() {
        scaleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("**Thread Name from javafx : " + Thread.currentThread().getName());
                            System.out.println("SCALE TOOl");
                        ScaleTool tool = new ScaleTool();
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("***MOVE TOOl");
                        MoveTool tool = new MoveTool();
                        try {
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        System.out.println("***Select TOOl");
                        SelectTool tool = new SelectTool();
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);
                        }catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        rotateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        System.out.println("***ROTATE TOOl");
                        RotateTool tool = new RotateTool();
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        
        
    }


}
