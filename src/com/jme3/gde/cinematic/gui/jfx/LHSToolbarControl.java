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
public class LHSToolbarControl extends HBox{
    public LHSToolbarControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LHS_toolbar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
}
