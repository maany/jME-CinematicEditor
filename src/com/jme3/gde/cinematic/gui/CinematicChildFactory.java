/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author MAYANK
 */
public class CinematicChildFactory extends ChildFactory<Layer>{
    Layer layer;

    public CinematicChildFactory(Layer layer) {
        this.layer = layer;
    }

    @Override
    protected Node createNodeForKey(Layer key) {
        //return super.createNodeForKey(key); //To change body of generated methods, choose Tools | Templates.
        Node node = new CinematicLayerNode(key);
        node.setDisplayName(key.getName());
        return node;
    }

    @Override
    protected boolean createKeys(final List<Layer> toPopulate) {
        // get selected layer from lookup.. then add its children to toPopulate
        Layer root = CinematicEditorManager.getInstance().getCurrentClip().getRoot();
        toPopulate.addAll(root.findAllDescendants());
        java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
               // Layer selectedLayer = CinematicEditorTopComponent.findInstance().getCinematicLookup().lookup(Layer.class);
              //  System.out.println("selectedLayer inside ChildFactory : " + layer);
            //    toPopulate.addAll(layer.getChildren());
            }
        
        });
        
        return true;
    }

    
    
}
