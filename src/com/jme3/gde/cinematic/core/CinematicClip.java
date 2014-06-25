/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import com.jme3.gde.cinematic.gui.GuiManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MAYANK
 */
public class CinematicClip{
    
    private double duration= GuiManager.DEFAULT_DURATION;
    private List<DurationChangeListener> durationChangeListeners = new ArrayList<>();
    private String name;
    private Layer root;
    
    public CinematicClip(String name)
    {
        this.name = name;
    }
    public CinematicClip(String name,Layer root)
    {
        this.name = name;
        this.root = root;
    }
    public CinematicClip(String name,Layer root,int duration)
    {
        this.name = name;
        this.root = root;
        this.duration = duration;
    }
    private void notifyDurationChangeListeners(){
        for(DurationChangeListener listener:durationChangeListeners)
            listener.durationChanged();
    }
    public Layer getRoot() {
        return root;
    }

    public void setRoot(Layer root) {
        this.root = root;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
        notifyDurationChangeListeners();
        
    }

    public List<DurationChangeListener> getDurationChangeListeners() {
        return durationChangeListeners;
    }

    public void setDurationChangeListeners(List<DurationChangeListener> durationChangeListeners) {
        this.durationChangeListeners = durationChangeListeners;
    }
    
}
