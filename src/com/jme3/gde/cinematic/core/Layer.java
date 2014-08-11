/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import com.jme3.gde.cinematic.CinematicEditorTopComponent;
import com.jme3.gde.cinematic.gui.CinematicLayerNode;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.cinematic.gui.LayerLAF;
import com.jme3.gde.cinematic.gui.jfx.CinematicEditorUI;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.openide.ErrorManager;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.WeakListeners;


/**
 *
 * @author MAYANK
 */
public class Layer implements Serializable,PropertyChangeListener {
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
    private LayerType type;
    private Node nodeDelegate;
    private List<PropertyChangeListener> listeners = Collections.synchronizedList(new LinkedList());
    public Layer(){
        
    }
    /**
     * Use for creating a Child
     * @param name
     * @param parent 
     */
   
    
    private Layer(String name,Layer parent) {
        this.name = name;
        this.parent = parent;
        this.type = LayerType.UNDEFINED;
        children = new ArrayList<>();
        descendants = new ArrayList<>();
        visibleDescendants = new ArrayList<>();
        
        events = new ArrayList<>();
        laf = new LayerLAF(GuiManager.DEFAULT_LAYER_COLOR, GuiManager.DEFAULT_LAYER_COLLAPSED_STATE, this);

        if (parent != null) {
            depth = parent.getDepth() + 1;
            parent.addChild(this);
        } else {
            depth = 0;
        }
        nodeDelegate = new CinematicLayerNode(this);
        addPropertyChangeListener(WeakListeners.propertyChange(this,this));
        
    }

    public Layer(String name,Layer parent,LayerType type){
        this(name,parent);
        this.type= type;
    }

    public Layer(String name, Layer parent, LayerType type, Color color, boolean collapsed) {
        this(name, parent, type);
        laf = new LayerLAF(color, collapsed, this);
    }
   
   /**
     * Used by constructor to establish parent to child link.
     * @param child 
     */
    private void addChild(Layer child)
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
    /**
     * returns the Sheet used by the Property Inspector of Netbeans Platform.
     * set's name is set to name of the class (Layer in this case), so
     * subclasses can use this name to get the this set.
     *
     * @return
     */
    public Sheet createSheet(){
        Sheet sheet =  Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setDisplayName("Layer");
        set.setName("Layer");
        try{
            Node.Property nameProp = new PropertySupport.Reflection(this,String.class,"name");
            nameProp.setName("Layer Name");
            set.put(nameProp);
            
            Node.Property typeProp = new PropertySupport.Reflection(this,LayerType.class,"getType",null);
            typeProp.setName("Layer Type");
            set.put(typeProp);
            
        }catch(NoSuchMethodException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Failed to load properties for layer : {0}", getName());
            ErrorManager.getDefault();
        }
        sheet.put(set);
        return sheet;
    }
    public void addPropertyChangeListener (PropertyChangeListener listener){
        listeners.add(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener){
        listeners.remove(listener);
    }
    protected void firePropertyChange(String propertyName,Object oldValue,Object newValue){
        for(PropertyChangeListener listener: listeners){
            listener.propertyChange(new PropertyChangeEvent(this,propertyName,oldValue,newValue));
        }
    }
    /**
     * Use this method to sync the javaFX UI and the Nodes whenever visible properties of layer change.
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("name")) {
            // update javaFx UI/ Node name
            
            Platform.runLater(new Runnable(){
                CinematicEditorUI cinematicEditorUI;
                @Override
                public void run() {
                    java.awt.EventQueue.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                            cinematicEditorUI = cinematicEditor.getCinematicEditorUI();
                        }
                    
                    });
                    
                }
                
            });
        
        }
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
        String oldName = this.name;
        this.name = name;
        firePropertyChange("name",oldName,name);
    }

    public List<Layer> getChildren() {
        return children;
    }

    public void setChildren(List<Layer> children) {
        this.children = children;
    }
    /**
     * returns the parent of the layer. If layer is {@link LayerType#ROOT , it returns the layer itself
     * @return 
     */
    public Layer getParent() {
        if (parent == null && type == LayerType.ROOT) {
           // System.out.println("Parent is Null for " + name);
            return this;
        } else {
           // System.out.println("Parent is not null for " + name);
            return parent;
        }
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

    public LayerType getType() {
        return type;
    }

    public void setType(LayerType type) {
        this.type = type;
    }

    public Node getNodeDelegate() {
        return nodeDelegate;
    }

    public void setNodeDelegate(Node nodeDelegate) {
        this.nodeDelegate = nodeDelegate;
    }

    
    
   
}
