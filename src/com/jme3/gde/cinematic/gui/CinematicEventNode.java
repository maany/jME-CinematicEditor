/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import com.jme3.gde.cinematic.core.Event;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author MAYANK
 */
public class CinematicEventNode extends AbstractNode{
    private Event event;
    public CinematicEventNode(Event event) {
        super(Children.create(new ChildFactory<Event>() {
            @Override
            protected boolean createKeys(List<Event> toPopulate) {
                return true;
            }
            
        }, true));
        this.event = event;
    }

    public CinematicEventNode(Children children, Lookup lookup) {
        super(children, lookup);
    }
/**
 * Gets the Property Sheet from {@link Event} and forwards it to Explorer and PropertySheet API
 * @return 
 */
    @Override
    protected Sheet createSheet() {
        System.out.println("Fetching Property sheet");
        return event.createSheet();
    }
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
}
