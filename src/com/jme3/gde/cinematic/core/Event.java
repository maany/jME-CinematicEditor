/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

/**
 * 
 * @author MAYANK
 */
public class Event {
    private Layer layer;
    private String name;
    private double duration;
    private double startPoint;
    
    public Event() {
        
    }
    /**
     * Creates an event, initialized the following parameterss and sets parent-child relation b/w layer and event
     * @param name
     * @param layer
     * @param startPoint
     * @param duration 
     */
    public Event(String name, Layer layer,double startPoint,double duration ) {
        this.layer = layer;
        this.name = name;
        this.duration = duration;
        this.startPoint = startPoint;
        if(!layer.getEvents().contains(this))
        layer.getEvents().add(this);
    }
    
    public void offset(double offset) {
        startPoint+=offset;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(double startPoint) {
        this.startPoint = startPoint;
    }

    
    
    
}
