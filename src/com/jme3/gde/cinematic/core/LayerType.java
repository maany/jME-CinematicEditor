/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

/**
 *
 * @author MAYANK
 */
public enum LayerType {
    SOUND,SPATIAL,CHARACTER,MOTION_PATH,ROOT,GUI,TRANSITION,SECONDARY,UNDEFINED;
    /**
     * Stores the path of assets for primary layers and the type for secondary layers
     */
    private String metaData;

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }


}
