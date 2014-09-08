/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import com.jme3.gde.cinematic.CinematicEditorTopComponent;
import com.jme3.gde.cinematic.scene.CinematicEditorToolController;
import com.jme3.gde.cinematic.scene.tools.MoveTool;
import com.jme3.gde.cinematic.scene.tools.RotateTool;
import com.jme3.gde.cinematic.scene.tools.ScaleTool;
import com.jme3.gde.cinematic.scene.tools.SelectTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    public MainToolbarControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_toolbar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        initActions();
    }
    private void initActions(){
        scaleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("SCALE TOOl");
                        ScaleTool tool = new ScaleTool();
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);

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
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);

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
                        System.out.println("***Select TOOl");
                        SelectTool tool = new SelectTool();
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);

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
                        System.out.println("***ROTATE TOOl");
                        RotateTool tool = new RotateTool();
                        CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorToolController toolController = cinematicEditor.getCinematicLookup().lookup(CinematicEditorToolController.class);
                        toolController.showEditTool(tool);

                    }
                });
            }
        });
    }
}
