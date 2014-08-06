/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author MAYANK
 */
public class CinematicLayerNode extends AbstractNode{
    
    public CinematicLayerNode(){
        super(Children.create(new CinematicChildFactory(),true));
        
    }
    public CinematicLayerNode(Children children,Lookup lookup) {
        super(children,lookup);
        
    }
}
