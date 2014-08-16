/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.eventtype;

import com.jme3.gde.cinematic.core.Event;
import com.jme3.gde.cinematic.core.EventType;
import com.jme3.gde.cinematic.core.Layer;

/**
 * Animation Events indicate presence of any of the Animation Channels of the Character
 * in the Cinematic.
 * @author MAYANK
 */
public class AnimationEvent extends Event{
    private String channelName;
    public AnimationEvent(String channelName,Layer layer,double startPoint, double duration) {
        super(channelName, layer, startPoint, duration);
        // name and channelName are same
        this.channelName = channelName;
        setType(EventType.ANIMATION);
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    
    
}
