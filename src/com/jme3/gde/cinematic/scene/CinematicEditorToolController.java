/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.scene;

import com.jme3.asset.AssetManager;
import com.jme3.gde.core.sceneexplorer.nodes.JmeNode;
import com.jme3.gde.scenecomposer.ComposerCameraController;
import com.jme3.gde.scenecomposer.SceneComposerToolController;
import com.jme3.scene.Node;

/**
 *
 * @author MAYANK
 */
public class CinematicEditorToolController extends SceneComposerToolController{
    public CinematicEditorToolController(Node node,AssetManager assetManager,JmeNode jmeNode) {
        super(node,assetManager,jmeNode);
    }
    @Override
   public void refreshNonSpatialMarkers(){
       super.refreshNonSpatialMarkers();
   } 

    public void setCamController(ComposerCameraController camController) {
       
    }
   
}
