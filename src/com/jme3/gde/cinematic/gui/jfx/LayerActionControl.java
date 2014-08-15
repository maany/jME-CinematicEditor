/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import java.util.ArrayList;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
   private TabPane tabPane;
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

    public void setContent(Map<String,ArrayList<Button>> data) {
        cleanup();
        tabPane.getTabs().add(new Tab("TESTING"));
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                JOptionPane.showMessageDialog(null, "Setting Content. Test Passed");
            }
        });
        if(data==null)
            return;
        try {
            for (Map.Entry<String, ArrayList<Button>> entry : data.entrySet()) {
                String tabName = entry.getKey();
                ArrayList<Button> buttonList = entry.getValue();
                Tab tab = new Tab(tabName);
                AnchorPane tabContent = new AnchorPane();
                for (Button button : buttonList) {
                    tabContent.getChildren().add(button);
                }
                tab.setContent(tabContent);
                tabPane.getTabs().add(tab);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    private void cleanup() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        tabPane.getTabs().removeAll(tabPane.getTabs());
    }

  
    
}
