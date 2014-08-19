/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.animation.AnimControl;
import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.eventtype.AnimationEvent;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.cinematic.gui.jfx.CinematicEditorUI;
import com.jme3.gde.cinematic.gui.jfx.LayerActionControl;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.openide.util.Exceptions;

/**
 *
 * @author MAYANK
 */
public class CharacterLayer extends SpatialLayer{
    private Collection<String> animationNames;
    public CharacterLayer(String name,Layer parent){
        super(name,parent);
        setType(LayerType.CHARACTER);
    }
/**
 * Creates a Map for displaying tabs and tabContent through {@link LayerActionControl}
 * @return 
 */
    @Override
    public Map<String, ArrayList<Button>> createTabsAndEvents() {
        Map<String, ArrayList<Button>> tabsAndEvents = super.createTabsAndEvents(); //To change body of generated methods, choose Tools | Templates.
        String tab = "Animation Events";
        ArrayList<Button> animationChannels = new ArrayList<>();
        final Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(getFile());
        
        SceneApplication.getApplication().enqueue(new Callable<Collection<String>>() {

            @Override
            public Collection<String> call() throws Exception {
                animationNames =  spat.getControl(AnimControl.class).getAnimationNames();
                return animationNames;
            }
        } );
        
        for (final String animationName : animationNames) {
            Button btn = new Button(animationName);
            animationChannels.add(btn);
            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    CinematicEditorUI cinematicEditorUI = CinematicEditorUI.getInstance();
                    if(cinematicEditorUI!=null){
                       // System.out.println("CINEMATICEDITORUI not null in CharacterLayer#createTabsAndEvents");
                        float currentTime = cinematicEditorUI.getTimeline().getCurrentTime().floatValue();

                        Future<Float> futureDuration = SceneApplication.getApplication().enqueue(new Callable<Float>() {
                            public Float call() throws Exception {
                                float duration = spat.getControl(AnimControl.class).getAnimationLength(animationName);
                                return duration;
                            }
                        });
                        float duration = GuiManager.BLANK_EVENT_DURATION;
                        try {
                            duration = futureDuration.get();
                        } catch (InterruptedException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (ExecutionException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                        AnimationEvent event = new AnimationEvent(animationName,CharacterLayer.this,currentTime,duration);
                        cinematicEditorUI.getTimeline().addEvent(event,CharacterLayer.this);
                    }
                }
            });
            animationChannels.add(btn);
        }
        tabsAndEvents.put(tab, animationChannels);
        return tabsAndEvents;
    }

    
    
}
