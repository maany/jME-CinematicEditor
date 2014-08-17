/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

/**
 *
 * @author MAYANK
 */
public class MainToolbarControl extends HBox{
    public MainToolbarControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_toolbar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
}
