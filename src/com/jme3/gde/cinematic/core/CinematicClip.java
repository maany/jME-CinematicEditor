/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.gde.cinematic.gui.GuiManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MAYANK
 */
public class CinematicClip implements Savable{
    
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
    
    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule capsule = je.getCapsule(this);
        
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
        ji.getCapsule(this);
        
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
