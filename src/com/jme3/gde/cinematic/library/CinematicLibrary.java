/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.library;

import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the Map mapping the path to their respective objects.
 * @author MAYANK
 */
public class CinematicLibrary {
    private Map<String,Spatial> spatialMap;

    public CinematicLibrary() {
        spatialMap = new HashMap<>();
    }

    public Map<String, Spatial> getSpatialMap() {
        return spatialMap;
    }

    public void setSpatialMap(Map<String, Spatial> spatialMap) {
        this.spatialMap = spatialMap;
    }
   
}
