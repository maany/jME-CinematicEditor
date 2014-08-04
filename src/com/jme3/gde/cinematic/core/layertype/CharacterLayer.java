/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;

/**
 *
 * @author MAYANK
 */
public class CharacterLayer extends SpatialLayer{
    public CharacterLayer(String name,Layer parent){
        super(name,parent);
        setType(LayerType.CHARACTER);
    }
}
