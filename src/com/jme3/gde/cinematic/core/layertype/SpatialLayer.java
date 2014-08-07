/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import org.openide.nodes.Sheet;

/**
 *
 * @author MAYANK
 */
public class SpatialLayer extends Layer{
     
    private String path;

    public SpatialLayer(String name, Layer parent) {
        super(name,parent,LayerType.SPATIAL);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public Sheet createSheet(){
        System.out.println("SPATIAL __________---------------______________");
        return super.createSheet();

}
     
}
