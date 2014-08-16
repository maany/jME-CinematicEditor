/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import com.jme3.gde.cinematic.gui.CinematicEventNode;
import java.io.Serializable;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 *
 * @author MAYANK
 */
public class Event implements Serializable {

    private Layer layer;
    private String name;
    private double duration;
    private double startPoint;
    private EventType type;
    private Node nodeDelegate;
    public Event() {
    }

    /**
     * Creates an event, initialized the following parameterss and sets
     * parent-child relation b/w layer and event
     *
     * @param name
     * @param layer
     * @param startPoint
     * @param duration
     */
    public Event(String name, Layer layer, double startPoint, double duration) {
        this.layer = layer;
        this.name = name;
        this.duration = duration;
        this.startPoint = startPoint;
        if (!layer.getEvents().contains(this)) {
            layer.getEvents().add(this);
        }
        this.nodeDelegate = new CinematicEventNode(this);
    }

    /**
     * offset start time
     *
     * @param offset
     */
    public void offset(double offset) {
        startPoint += offset;
    }
    /**
     * Creates a sheet to be displayed in the Property Editor when the current
     * event is selected from the javafx UI.
     *
     * @return
     */
    public Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set eventSet = Sheet.createPropertiesSet();
        eventSet.setName("Event");
        eventSet.setDisplayName("Event");
        try {
            Property nameProp = new PropertySupport.Reflection(this, String.class, "getName", "setName");
            Property layerProp = new PropertySupport.Reflection(getLayer(), String.class, "getName", null);
            Property startProp = new PropertySupport.Reflection(this, Double.class, "getStartPoint", "setStartPoint");
            Property durationProp = new PropertySupport.Reflection(this, Double.class, "getDuration", "setDuration");

            nameProp.setName("Name");
            layerProp.setName("Parent Layer");
            startProp.setName("Start Time");
            durationProp.setName("Duration");

            eventSet.put(nameProp);
            eventSet.put(layerProp);
            eventSet.put(startProp);
            eventSet.put(durationProp);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        sheet.put(eventSet);
        return sheet;
    }

    /**
     * Creates a {@link Node} representation for the current event. The
     * nodeDelegate can be used by the PropertyEditor or other ExplorerViews to
     * display information about the {@link Event}
     *
     * @return
     */
    public Node getNodeDelegate() {
        return nodeDelegate;
    }

    public void setNodeDelegate(Node nodeDelegate) {
        this.nodeDelegate = nodeDelegate;
    }

   
    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Double startPoint) {
        this.startPoint = startPoint;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
