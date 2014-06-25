/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import com.jme3.gde.cinematic.core.Layer;
import java.awt.Color;

/**
 *
 * @author MAYANK
 */
public class LayerLAF {
    private Color color;
    private boolean enabled = true;
    private boolean collapsed = true;
    private Layer layer;
    /**
     * Used by LayerContainer's enabled column
     */
    public void disable(){
     //override this method depending on layer type   
        System.out.println("Disabling Layer");
    }
    public void enable(){
        System.out.println("Enabling Layer");
    }
    public LayerLAF(Color color,boolean collapsed,Layer layer)
    {
        this.color = color;
        this.collapsed = collapsed;
        this.layer = layer;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
/**
 * Returns the collapsed state of the layer. If layer has no children, it gets collapsed automatically
 * @return 
 */
    public boolean isCollapsed() {
        if(!layer.hasChildren())
            collapsed = true;
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled==false)
            disable();
        else
            enable();
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
}
