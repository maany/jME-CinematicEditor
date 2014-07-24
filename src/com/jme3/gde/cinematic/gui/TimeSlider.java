/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author MAYANK
 */
public class TimeSlider extends JPanel{
    private int startPoint = 0;
    private int offset = 0;
    private int currentFrame = 0;
    private JTable layerContainer = null;
    private JTable timeline = null;
    private JScrollPane topContainer = null;

    public TimeSlider(JScrollPane topContainer,JTable layerContainer,JTable timeline){
        this.layerContainer = layerContainer;
        this.timeline = timeline;
        this.topContainer = topContainer;
    }
    /**
     * Puts timeSlider on the 1st Frame of timeline
     */
    public void initTimeSlider()
    {

        // put timeSlider on firstFrame;
        startPoint = topContainer.getBounds().x;
        for(int i=0;i<layerContainer.getColumnCount();i++)
        {
            TableColumn column = layerContainer.getColumnModel().getColumn(i);
            startPoint += column.getWidth();
        }
        // code to stick 
    }
    public void setPosition(int frame)
    {
        
    }
}
