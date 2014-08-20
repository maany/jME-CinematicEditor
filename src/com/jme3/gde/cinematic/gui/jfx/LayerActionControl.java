/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layer_action_control.fxml"));
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
        if(data==null || data.isEmpty()) {
           Logger.getLogger(this.getClass().getName()).log(Level.INFO,"No content found to display in LayerActionControl");
            TilePane tabContent = createEmptyTab("Events");
            Label label = new Label("Nothing to display");
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefSize(tabPane.getPrefWidth(),tabContent.getPrefHeight());
            tabContent.getChildren().add(label);
           return;
        }
        
        try {
            for (Map.Entry<String, ArrayList<Button>> entry : data.entrySet()) {
                String tabName = entry.getKey();
                ArrayList<Button> buttonList = entry.getValue();
               // System.out.println("READING MAP :");
                //System.out.println("Tab Name : " + tabName);
                //System.out.println("Tab Content : " + buttonList);
                TilePane tabContent = createEmptyTab(tabName); //create tab and add it to tabPane
                for (Button button : buttonList) {
                    if(!tabContent.getChildren().contains(button))
                        tabContent.getChildren().add(button);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Creates a new tab,adds it to the tabPane and configures the UI. It
     * returns the TilePane where we can add buttons/stuff to be displayed in
     * the tabPane. No need to worry about creating relations b/w tabPane and
     * tabs as this method takes care of it.
     *
     * @param tabName name of the new tab
     * @return the TilePane where buttons can be added
     */
    private TilePane createEmptyTab(String tabName){
        Tab tab = new Tab(tabName);
        ScrollPane tabContentScrollPane = new ScrollPane();
        TilePane tabContent = new TilePane();
        tabContent.setPrefWidth(tabPane.getPrefWidth());
        tabContentScrollPane.setContent(tabContent);
        tab.setContent(tabContentScrollPane);
        tabPane.getTabs().add(tab);
        return tabContent;
        
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
