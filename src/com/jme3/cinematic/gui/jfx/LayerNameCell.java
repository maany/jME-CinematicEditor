/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.cinematic.gui.jfx;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.gui.GuiManager;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author MAYANK
 */
public class LayerNameCell extends TableCell<Layer, String> {

    private TextField textField;

    @Override
    public void startEdit() {

        super.startEdit();



        if (textField == null) {

            createTextField();

        }

        setText(null);

        setGraphic(textField);

        textField.selectAll();

    }

    @Override
    public void cancelEdit() {

        super.cancelEdit();

        setText((String) getItem());

        setGraphic(null);

    }

    @Override
    public void updateItem(String item, boolean empty) {

        super.updateItem(item, empty);
       
        if (empty) {

            setText(null);

            setGraphic(null);

        } else {

            if (isEditing()) {

                if (textField != null) {

                    textField.setText(getString());

                }

                setText(null);

                setGraphic(textField);

            } else {
                /*
                 * Set indentation of children
                 */
                String indentation = GuiManager.INDENTATION;
                Layer layer = getTableView().getItems().get(getTableRow().getIndex());
                for(int i=0;i<layer.getDepth();i++)
                {
                    indentation += indentation;
                }
                setText(indentation + getString());
                
                setGraphic(null);

            }

        }

    }

    private void createTextField() {

        textField = new TextField(getString());

        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {

                if (t.getCode() == KeyCode.ENTER) {

                    commitEdit(textField.getText());
                    Layer layer = getTableView().getItems().get(getTableRow().getIndex());
                    layer.setName(textField.getText());
                    //System.out.println("Layer new Name " + layer.getName() + " parent : " +layer.getParent().getName());

                } else if (t.getCode() == KeyCode.ESCAPE) {

                    cancelEdit();

                }

            }
        });

    }

    private String getString() {

        return getItem() == null ? "" : getItem().toString();

    }
}
