/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.eventtype;

import com.jme3.animation.LoopMode;
import com.jme3.gde.cinematic.core.Event;
import com.jme3.gde.cinematic.core.EventType;
import com.jme3.gde.cinematic.core.Layer;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 * Animation Events indicate presence of any of the Animation Channels of the Character
 * in the Cinematic.
 * @author MAYANK
 */
public class AnimationEvent extends Event{
    private String channelName;
    private LoopMode loopMode;
    public AnimationEvent(String channelName,Layer layer,double startPoint, double duration) {
        super(channelName, layer, startPoint, duration);
        // name and channelName are same
        this.channelName = channelName;
        setType(EventType.ANIMATION);
        loopMode = LoopMode.DontLoop;
    }

    @Override
    public Sheet createSheet() {
        Sheet sheet = super.createSheet(); //To change body of generated methods, choose Tools | Templates.
        Sheet.Set animationSet = Sheet.createPropertiesSet();
        animationSet.setDisplayName("Animation Control");
        animationSet.setName("AnimationEvent");
        try {
            Property channelNameProp = new PropertySupport.Reflection(this,String.class,"getChannelName",null);
            channelNameProp.setName("Animation Name");
            animationSet.put(channelNameProp);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        sheet.put(animationSet);
        return sheet;
    }

    
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public LoopMode getLoopMode() {
        return loopMode;
    }

    public void setLoopMode(LoopMode loopMode) {
        this.loopMode = loopMode;
    }
    
    
}
