/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import com.jme3.gde.cinematic.gui.jfx.EventStrip;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.cinematic.gui.LayerLAF;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author MAYANK
 */
public class Layer {
    private int depth=0;
    private String name;
    private List<Layer> children;
    private Layer parent; // for child to parent link
    private List<Layer> descendants;
    private List<Layer> visibleDescendants;
    private List<Event> events;
    private LayerLAF laf; // look and feel
    private List<Integer> index;
    private boolean showChildren = false;
    private boolean tempLeaf = false;
    
    /**
     * Use for creating a Child
     * @param name
     * @param parent 
     */
   
    
    public Layer(String name,Layer parent) {
        this.name = name;
        this.parent = parent;
        children = new ArrayList<>();
        descendants = new ArrayList<>();
        visibleDescendants = new ArrayList<>();
        
        events = new ArrayList<>();
        laf = new LayerLAF(GuiManager.DEFAULT_LAYER_COLOR,GuiManager.DEFAULT_LAYER_COLLAPSED_STATE,this);
        
        if(parent!=null)
        {
            depth = parent.getDepth()+1;
            parent.addChild(this);
        }
        else
        {
            depth=0;
            
        }
    }

    public Layer(String name,Layer parent,Color color,boolean collapsed) {
        this(name,parent);
        laf = new LayerLAF(color,collapsed,this);
    }
    /**
     * Avoid direct usage. Used by constructor to establish parent to child link.
     * @param child 
     */
    public void addChild(Layer child)
    {
        children.add(child);
    }
    
    public boolean hasChildren()
    {
        if(children.size()>0)
            return true;
        else
            return false;
    }
    /**
     * layer.findAllDescendents(Layer) seems not right, but it is needed as this is recursive function.
     * @param layer
     * @return 
     */
    public List<Layer> findAllDescendants() {
        //descendants = new ArrayList<>();
        if (!this.tempLeaf) {
            System.out.println(this.getName() + " is not a temp leaf");
            int index=1;
            for (Layer child : this.getChildren()) {
                ;
                System.out.println("Child " + index + " of " + this.getName() +" is " +child.getName());
                index++;
                    descendants.add(child);
                    descendants.addAll(child.findAllDescendants());
                    //System.out.println("Recieved value in " + this.getName());
                   // break;
                
                //System.out.println("Continueing in " + this.getName());
                //continue;
                // if (!descendants.contains(child)) {
                   // System.out.println("Adding " + child.getName() + " as descendant of " + this.getName());
                    //descendants.add(child);
             //   }
                //descendants.addAll(child.findAllDescendants());
                //if(reverseTraversal ==true)
                 //   return true;
            } 
            tempLeaf = true; 
            System.out.println( getName() +"is a temp leaf now. Returning descendants : " + descendants + " to " + this.getParent());
            return descendants;
        } else {
            //System.out.println(this.getName() + " is a leaf node ");
            //tempLeaf = true;
            //descendants.add(this);
            return descendants;
            //this.getParent().getDescendants().add(this);
        }
        //if(this.parent!=null)
        //System.out.println("Descendants of : " + this.getName() +"are " + this.getDescendants() );
        //return true;
        //return null;
        
    }
      
    public List<Layer> findAllVisibleDescendants () {
        visibleDescendants = new ArrayList<>();
        if(this.hasChildren() && !this.getLaf().isCollapsed()) {
           // System.out.println(this.getName() + " has children and is not collapsed");
            int index=0;
            for(Layer child:this.getChildren())
            {
                index++;
             //   System.out.println("Child " +index + " of " + this.getName() + " is " + child.getName() );
              //  System.out.println("Adding " + child.getName() + " as a Visible Descendant of " + this.getName());
                visibleDescendants.add(child);
                visibleDescendants.addAll(child.findAllVisibleDescendants());
                
             /*   if(this.getLaf().isCollapsed()) {
                    if(!visibleDescendants.contains(this) )
                    visibleDescendants.add(this);
                    
                }
                else {
                    if(!visibleDescendants.contains(this) )
                    visibleDescendants.add(this);
                    child.findAllDescendants();
                } */
            }
        } else if(this.hasChildren() && this.getLaf().isCollapsed()){
           // System.out.println( this.getName()+" has children but is collapsed. Returning  empty Array List as Visible descendants to " + this.getParent());
            visibleDescendants = new ArrayList<>(); //just a precaution;
        }
        return getVisibleDescendants();
    }
    @Override 
    public String toString() {
        return "Name : " + getName() + " depth : " + getDepth();
    }
    /*
     * Getters and Setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Layer> getChildren() {
        return children;
    }

    public void setChildren(List<Layer> children) {
        this.children = children;
    }
    public Layer getParent() {
        return parent;
    }

    public void setParent(Layer parent) {
        this.parent = parent;
    }
    
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<Layer> getDescendants() {
        tempLeaf = false;
        findAllDescendants();
        return descendants;
    }

    public void setDescendants(List<Layer> descendants) {
        this.descendants = descendants;
    }

    public LayerLAF getLaf() {
        return laf;
    }

    public void setLaf(LayerLAF laf) {
        this.laf = laf;
    }

    public List<Layer> getVisibleDescendants() {
        return visibleDescendants;
    }

    public void setVisibleDescendants(List<Layer> visibleDescendants) {
        this.visibleDescendants = visibleDescendants;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
   
}
