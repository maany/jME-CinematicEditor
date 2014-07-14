/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.cinematic.gui.jfx;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.DurationChangeListener;
import com.jme3.gde.cinematic.core.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Provides the graphical representation of {@link com.jme3.gde.cinematic.core.Event}. 
 * Manages features like changing duration, start point, assigning color to event via the Editor
 * @author MAYANK
 */
public class EventControl extends Button implements DurationChangeListener{
    private Event event;
    private double refWidth;
    private double refStartX;
    private double magnification=1;
    public EventControl(Event event) {
        super(event.getName());
        this.event = event;
        this.setMaxWidth(USE_PREF_SIZE);
        this.setMaxHeight(USE_PREF_SIZE);
        this.setAlignment(Pos.CENTER);
        this.setLayoutX(0);
        this.setLayoutY(0);
        CinematicEditorManager.getInstance().getCurrentClip().getDurationChangeListeners().add(this);
        initActions();
        
    }
    /**
     * Renders the event in the default position in the Eventstrip. It takes into account the magnification 
     * and duration of clip and produces a control whose dimensions act as a reference for changes that occur 
     * due to magnification of modification of duration.
     */
    public void render(double magnification) {
        double clipDuration = CinematicEditorManager.getInstance().getCurrentClip().getDuration();
        EventStrip eventStrip = (EventStrip)getParent();
        double editorRefWidth = eventStrip.getTimeline().getPrefWidth()/magnification;
        refWidth = event.getDuration()*editorRefWidth/clipDuration;
        refStartX = event.getStartPoint()*editorRefWidth/clipDuration;
        setPrefWidth(refWidth);
        setTranslateX(refStartX);
        
    }
    /**
     * When duration changes, event startPoint and width are adjusted automatically by this method
     */
     @Override
    public void durationChanged() {
         EventStrip eventStrip = (EventStrip) getParent();
        double mag = eventStrip.getTimeline().getMagnification().doubleValue();
         render(mag);
    }

    /**
     * When magnification or duration changes, the event's start point and width will change. 
     * This method refactors the startPoint and width to keep the event in sync with the timebar
     * @param magnification 
     */
     
    public void refactorDisplay(double magnification){
        //double clipDuration = CinematicEditorManager.getInstance().getCurrentClip().getDuration();
        this.magnification = magnification;
        double width = magnification*refWidth;
        double startPoint = magnification*refStartX;
        setPrefWidth(width);
        setTranslateX(startPoint);
    }

    private void initActions() {
        setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                if(t.getCode()== KeyCode.DELETE) {
                    EventStrip eventStrip = (EventStrip)getParent();
                    eventStrip.getTimeline().removeEvent(getEvent());
                }
                    
            }
        });
    }
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
