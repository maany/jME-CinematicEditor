/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.library;

import com.jme3.scene.Spatial;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the Map mapping the path to their respective objects.
 * @author MAYANK
 */
public class CinematicLibrary {
    private Map<File,Spatial> spatialMap;

    public CinematicLibrary() {
        spatialMap = new HashMap<>();
    }

    public Map<File, Spatial> getSpatialMap() {
        return spatialMap;
    }

    public void setSpatialMap(Map<File, Spatial> spatialMap) {
        this.spatialMap = spatialMap;
    }

    
   
}
