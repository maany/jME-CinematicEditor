/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author MAYANK
 */
public class CanvasViewport extends JScrollPane{
    private TimelineCanvas timeline = null;
    private LayerContainer layerContainer = null;
    public CanvasViewport(){
        //timeline = new TimelineCanvas();
        layerContainer = new LayerContainer();
        setRowHeaderView(layerContainer);
        setViewportView(timeline);
    }
public static void main(String[] args)
    {
        System.out.println("Running");
        JFrame panel = new JFrame();
        panel.add(new CanvasViewport());
        panel.setVisible(true);
        
    }
}

    