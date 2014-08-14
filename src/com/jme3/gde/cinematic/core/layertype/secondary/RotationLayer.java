/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype.secondary;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.gui.GuiManager;

/**
 *
 * @author MAYANK
 */
public class RotationLayer extends Layer{

    public RotationLayer(String name, SpatialLayer parent) {
        super(name,parent,LayerType.SECONDARY);
        getType().setMetaData(GuiManager.SECONDARY_ROTATION);
    }

    
    
}
