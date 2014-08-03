/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.eventtype.AudioEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MAYANK
 */
public class SoundLayer extends Layer{
    private List<AudioEvent> audioEvents;
    public SoundLayer(){
        super.setType(LayerType.SOUND);
        audioEvents = new ArrayList<>();
    }

    public List<AudioEvent> getAudioEvents() {
        return audioEvents;
    }

    public void setAudioEvents(List<AudioEvent> audioEvents) {
        this.audioEvents = audioEvents;
    }

   
    
}
