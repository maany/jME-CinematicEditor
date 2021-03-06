/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype.secondary;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 *
 * @author MAYANK
 */
public class RotationLayer extends Layer{

    public RotationLayer(String name, SpatialLayer parent) {
        super(name,parent,LayerType.SECONDARY);
        getType().setMetaData(GuiManager.SECONDARY_ROTATION);
    }
 @Override
    public Sheet createSheet() {
        Sheet sheet = super.createSheet(); //To change body of generated methods, choose Tools | Templates.
        Sheet.Set spatialSet = getParent().createSheet().get("SpatialLayer");
        sheet.put(spatialSet);

        Sheet.Set scaleSet = Sheet.createPropertiesSet();
        scaleSet.setDisplayName("Scale");
        scaleSet.setName("ScaleLayer");
        
        try {
            Node.Property xScale = new PropertySupport.Reflection(this, Float.class, "getXScale", "setXScale");
            xScale.setName("X Local Scale");
            scaleSet.put(xScale);
            Node.Property yScale = new PropertySupport.Reflection(this, Float.class, "getYScale", "setYScale");
            yScale.setName("Y Local Scale");
            scaleSet.put(yScale);
            Node.Property zScale = new PropertySupport.Reflection(this, Float.class, "getZScale", "setZScale");
            zScale.setName("Z Local Scale");
            scaleSet.put(zScale);

        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        sheet.put(scaleSet);
        return sheet;

    }

    public float getXScale() {
        final Spatial spat = getSpatial();
        Future<Float> xScale = SceneApplication.getApplication().enqueue(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return spat.getLocalScale().x;
            }
        });
        float val = 1;
        try {
             val = xScale.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally{
            return val;
        }
    }

    public float getYScale(){
        final Spatial spat = getSpatial();
        Future<Float> yScale = SceneApplication.getApplication().enqueue(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return spat.getLocalScale().y;
            }
        });
        float val = 1;
        try {
            val = yScale.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return val;
        }
    }
    public float getZScale(){
        final Spatial spat = getSpatial();
        Future<Float> zScale = SceneApplication.getApplication().enqueue(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return spat.getLocalScale().z;
            }
        });
        float val = 1;
        try {
            val = zScale.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return val;
        }
    }
    
    
    public void setXScale(final java.lang.Float x) {
        
        final Spatial spat = getSpatial();
        
        SceneApplication.getApplication().enqueue(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final float y = spat.getLocalScale().y;
                final float z = spat.getLocalScale().z;
                spat.setLocalScale(x, y, z);
                return true;
            }
        });
        
    }
  
    public void setYScale(final java.lang.Float y) {
        
        final Spatial spat = getSpatial();
        
        SceneApplication.getApplication().enqueue(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final float x = spat.getLocalScale().x;
                final float z = spat.getLocalScale().z;
                spat.setLocalScale(x, y, z);
                return true;
            }
        });
        
    }
    
    public void setZRotation(final java.lang.Float z) {
        
        final Spatial spat = getSpatial();
        
        SceneApplication.getApplication().enqueue(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final float y = spat.getLocalScale().y;
                final float x = spat.getLocalScale().x;
                spat.setLocalScale(x, y, z);
                return true;
            }
        });
        
    }
    private Spatial getSpatial() {
        SpatialLayer parent = (SpatialLayer) getParent();
        Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(parent.getFile());
        if(spat==null)
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find spatial to display secondary data for layer :{0}", parent.getName());
        return spat;
    }
    
    
}
