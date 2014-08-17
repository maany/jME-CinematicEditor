/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.layertype.CharacterLayer;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 * The Interpreter for {@link CinematicClip}. It constructs a
 * {@link com.jme3.cinematic.Cinematic} Object based on the information stored
 * in {@link com.jme3.gde.cinematic.filetype.CinematicDataObject}
 *
 * @author MAYANK
 */
public class CinematicMonkey {
    private Cinematic cinematic;
    public Cinematic convertToCinematic(CinematicClip clip,Node rootNode){
        cinematic = new Cinematic(rootNode, (float) clip.getDuration());
        List<Layer> layers = clip.getRoot().findAllDescendants();
        for (Layer layer : layers) {
            LayerType type = layer.getType();
            if(type==LayerType.ROOT){
                continue;
            } else if(type==LayerType.CHARACTER){
                CharacterLayer characterLayer = (CharacterLayer)layer;
                processCharacterLayer(characterLayer);
            }
        }
        return null;
        
    }

    private void processCharacterLayer(CharacterLayer characterLayer) {
        Spatial character = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(characterLayer.getFile());
        List<Event> events = characterLayer.getEvents();
        for (Event event : events) {
            if(event.getType()==EventType.ANIMATION)
            {
                com.jme3.gde.cinematic.core.eventtype.AnimationEvent animationEvent = (com.jme3.gde.cinematic.core.eventtype.AnimationEvent)event;
                cinematic.addCinematicEvent(event.getStartPoint().floatValue(),new AnimationEvent(character,animationEvent.getChannelName(),animationEvent.getLoopMode()));
            }
        }
    }
}
