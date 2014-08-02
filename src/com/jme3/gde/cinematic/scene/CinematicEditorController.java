/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.scene;

import com.jme3.gde.core.sceneexplorer.nodes.JmeNode;
import com.jme3.gde.scenecomposer.SceneEditorController;
import org.openide.loaders.DataObject;

/**
 *
 * @author MAYANK
 */
public class CinematicEditorController extends SceneEditorController{

    public CinematicEditorController(JmeNode jmeNode, DataObject dataObject) {
        super(jmeNode,dataObject);
    }

    @Override
    public void setTerrainLodCamera() {
        super.setTerrainLodCamera();
    }
    
}
