/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.cinematic.gui.jfx;

import com.jme3.gde.cinematic.core.Layer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 *
 * @author MAYANK
 */
public class EnabledCell extends TableCell<Layer, Boolean> {

    private CheckBox checkbox;

    public EnabledCell() {
        checkbox = new CheckBox();
        setAlignment(Pos.CENTER);
        checkbox.setAlignment(Pos.CENTER);
        /* true by default */
        checkbox.setSelected(true); // bind
        setGraphic(checkbox);
    }

    
    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            //System.out.println("Empty");
        } else {
           setGraphic(checkbox);
           if(item.booleanValue()==true)
               checkbox.setSelected(true);
           else
               checkbox.setSelected(false);
           //System.out.println("Item : " + item);
           checkbox.selectedProperty().addListener(new ChangeListener(){

               @Override
               public void changed(ObservableValue ov, Object t, Object t1) {
                   if(ov.getValue() instanceof Boolean)
                       if((Boolean)ov.getValue()==true)
                       {
                           getTableView().getItems().get(getTableRow().getIndex()).getLaf().enable();
                       } else {
                           getTableView().getItems().get(getTableRow().getIndex()).getLaf().disable();
                       }
                   
               }
           });
           
        }
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
        
    }
    
}
