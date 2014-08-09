/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype.secondary;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.math.Vector3f;

/**
 *
 * @author MAYANK
 */
public class TranslationLayer extends Layer{
    private Vector3f translationValue;
    public TranslationLayer(String name,Layer parent){
        super(name,parent,LayerType.SECONDARY);
        getType().setMetaData(GuiManager.SECONDARY_TRANSLATION);
    }

    public Vector3f getTranslationValue() {
        return translationValue;
    }

    public void setTranslationValue(Vector3f translationValue) {
        this.translationValue = translationValue;
    }
    
    
}
