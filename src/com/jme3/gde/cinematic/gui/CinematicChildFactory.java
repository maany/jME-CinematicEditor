/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import com.jme3.gde.cinematic.core.Layer;
import java.util.List;
import org.openide.nodes.ChildFactory;

/**
 *
 * @author MAYANK
 */
public class CinematicChildFactory extends ChildFactory<Layer>{

    @Override
    protected boolean createKeys(List<Layer> toPopulate) {
        return true;
    }
    
}
