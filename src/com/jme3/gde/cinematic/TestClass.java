/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.cinematic.gui.jfx.CinematicEditorUI;
import com.jme3.gde.cinematic.core.Layer;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javax.swing.*;

/**
 *
 * @author MAYANK
 */
public class TestClass extends JFrame{

    public TestClass() {
        setPreferredSize(new Dimension(660,400));
        add(new JButton("Testing"));
        final JFXPanel panel = new JFXPanel();
        panel.setPreferredSize(new Dimension(660,220));
        panel.setVisible(true);
        add(panel);
        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                CinematicEditorUI cinematicEditor = new CinematicEditorUI();
                AnchorPane anchor = new AnchorPane();
                anchor.getChildren().add(new Button("Click me"));
                Scene scene = new Scene(anchor,660,220);
                panel.setScene(scene);
                panel.setVisible(true);
                cinematicEditor.initCinematicEditorUI();
                cinematicEditor.initView(new Layer("root",null));
            }
        
        });
    }
}
