/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

/**
 *
 * @author MAYANK
 */
public enum HierarchyLevel {
   ADAM(1),PARENT(2),CHILD(3),GRAND_CHILD(4),GREAT_GRAND_CHILD(5),CUSTOM;
   
   private int level;
    
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
   HierarchyLevel(int level)
   {
       this.level = level;
   }
   HierarchyLevel()
   {
       
   }
}
