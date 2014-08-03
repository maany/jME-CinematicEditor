/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.eventtype;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.events.SoundEvent;
import com.jme3.gde.cinematic.core.Event;

/**
 * TODO : update UI on change of path. 
 * @see {@link #setPath(java.lang.String) }
 * @author MAYANK
 */
public class AudioEvent extends Event{
    private SoundEvent soundEvent;
    private String path;
    private boolean isStream;
    public AudioEvent(String path,boolean isStream,float duration,LoopMode loopMode){
        super();
        this.path = path;
        this.isStream = isStream;
        super.setDuration(duration);
        soundEvent = new SoundEvent(path,isStream,duration,loopMode);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        float duration = soundEvent.getDuration();
        LoopMode loopMode = soundEvent.getLoopMode();
        this.path = path;
        soundEvent = new SoundEvent(path,isStream,duration,loopMode);
        //TODO update UI
    }
    
    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public void setSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }
    
}
