/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;

/**
 *
 * @author MAYANK
 */
public class CinematicEditorManager {
    private static CinematicEditorManager instance = null;
    private CinematicClip currentClip;

    
    private CinematicEditorManager(){}
    public static CinematicEditorManager getInstance()
    {
        if(instance==null)
            instance = new CinematicEditorManager();
        return instance;
    }
    
    public CinematicClip getCurrentClip() {
        if(currentClip==null) {
            Layer root = new Layer("Root",null);
            currentClip = new CinematicClip("Untitled", root);
        }
        return currentClip;
    }

    public void setCurrentClip(CinematicClip currentClip) {
        this.currentClip = currentClip;
    }
}
