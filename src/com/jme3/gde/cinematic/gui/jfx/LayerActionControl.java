/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import com.jme3.gde.cinematic.core.Layer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

/**
 * Automatically creates tabs and sub features for selected layer and displays
 * them to the left of the timeline UI
 *
 * @author MAYANK
 */
public class LayerActionControl extends AnchorPane{
   @FXML
   private TabPane tabs;
    public LayerActionControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layer_actions_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }

    public void setContent(Layer layer) {
        tabs.getTabs().add(new Tab("TESTING"));
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                JOptionPane.showMessageDialog(null, "Setting Content. Test Passed");
            }
        });

    }

    public TabPane getTabs() {
        return tabs;
    }

    public void setTabs(TabPane tabs) {
        this.tabs = tabs;
    }
    
}
