/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import com.jme3.gde.cinematic.core.Layer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author MAYANK
 */
public class ChildVisibilityCell extends TableCell<Layer,Boolean>{
    private Button button;
    private CinematicEditorUI cinematicEditor;
    

    ChildVisibilityCell(final CinematicEditorUI cinematicEditor) {
        this.cinematicEditor = cinematicEditor;
        button = new Button();
        this.setAlignment(Pos.CENTER);
        button.setAlignment(Pos.CENTER);
        
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                int index = getTableRow().getIndex();
                Layer layer = getTableView().getItems().get(index);
                if (layer.getLaf().isCollapsed()) {
                    layer.getLaf().setCollapsed(false);
                    System.out.println("current layer : " + layer.getName() + " was collapsed."  + " Now it is collapsed? : "+ layer.getLaf().isCollapsed()+" Showing its children"  );
                    cinematicEditor.showChildren(layer);
                    // getTableView().getItems().addAll(index+1,layer.getChildren());
                } else {
                    System.out.println("*********************************************************************");
                    System.out.println("Curent layer : " + layer.getName() + " was not collapsed. Hiding its children");
                    System.out.println("Removing from " + (index+1) + " to " + (layer.getChildren().size()+index));
                    cinematicEditor.hideChildren(layer);
                    //for(int i=index+1;i<index+1+layer.getChildren())
                    //getTableView().getItems().remove(index+1,index + layer.getChildren().size()+1);
                }

            }
             });
             
    }
    
    
/**
 * item gives value of isCollapsed property of LayerLAF for current layer
 * @param item
 * @param empty 
 */
    @Override
    public void updateItem(Boolean item, boolean empty) {
        // if layer row is empty or layer does not have children, hide childVisibility button
        if (empty || !getTableView().getItems().get(getTableRow().getIndex()).hasChildren()) {
            setGraphic(null);
        } else {
            setGraphic(button);
            // if layer is collapsed, set V button icon
            if (item.booleanValue() == true) {
                button.setText(">");
            } else { // if layer is not collapsed set > button icon 
                button.setText("v");
            }
        }
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
   
    
}
