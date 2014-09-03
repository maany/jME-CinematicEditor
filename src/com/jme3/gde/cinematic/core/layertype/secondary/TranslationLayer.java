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
public class TranslationLayer extends Layer{
    
    public TranslationLayer(String name,Layer parent){
        super(name,parent,LayerType.SECONDARY);
        getType().setMetaData(GuiManager.SECONDARY_TRANSLATION);
    }

    @Override
    public Sheet createSheet() {
        Sheet sheet = super.createSheet(); //To change body of generated methods, choose Tools | Templates.
        Sheet.Set spatialSet = getParent().createSheet().get("SpatialLayer");
        sheet.put(spatialSet);

        Sheet.Set translationSet = Sheet.createPropertiesSet();
        translationSet.setName("TranslationLayer");
        translationSet.setDisplayName("Translation");
        try {
            Node.Property xTranslation = new PropertySupport.Reflection(this, Float.class, "getXTranslation", "setXTranslation");
            xTranslation.setName("X Local Translation");
            translationSet.put(xTranslation);
            Node.Property yTranslation = new PropertySupport.Reflection(this, Float.class, "getYTranslation", "setYTranslation");
            yTranslation.setName("Y Local Translation");
            translationSet.put(yTranslation);
            Node.Property zTranslation = new PropertySupport.Reflection(this, Float.class, "getZTranslation", "setZTranslation");
            zTranslation.setName("Z Local Translation");
            translationSet.put(zTranslation);

        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        sheet.put(translationSet);
        return sheet;
    }
 
        public float getXTranslation() {
        final Spatial spat = getSpatial();
        Future<Float> xTranslation = SceneApplication.getApplication().enqueue(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return spat.getLocalTranslation().x;
            }
        });
        float val = 1;
        try {
             val = xTranslation.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally{
            return val;
        }
    }

    public float getYTranslation(){
        final Spatial spat = getSpatial();
        Future<Float> yTranslation = SceneApplication.getApplication().enqueue(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return spat.getLocalTranslation().y;
            }
        });
        float val = 1;
        try {
            val = yTranslation.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return val;
        }
    }
    public float getZTranslation(){
        final Spatial spat = getSpatial();
        Future<Float> zTranslation = SceneApplication.getApplication().enqueue(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return spat.getLocalTranslation().z;
            }
        });
        float val = 1;
        try {
            val = zTranslation.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return val;
        }
    }
    
 

    public void setXTranslation(final java.lang.Float x) {

        final Spatial spat = getSpatial();
        SceneApplication.getApplication().enqueue(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final float y = spat.getLocalTranslation().y;
                final float z = spat.getLocalTranslation().z;
                spat.setLocalTranslation(x, y, z);
                return true;
            }
        });

    }

    public void setYTranslation(final java.lang.Float y) {

        final Spatial spat = getSpatial();
        SceneApplication.getApplication().enqueue(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final float x = spat.getLocalTranslation().x;
                final float z = spat.getLocalTranslation().z;
                spat.setLocalTranslation(x, y, z);
                return true;
            }
        });

    }
    public void setZTranslation(final java.lang.Float z) {

        final Spatial spat = getSpatial();

        SceneApplication.getApplication().enqueue(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final float y = spat.getLocalTranslation().y;
                final float x = spat.getLocalTranslation().x;
                spat.setLocalTranslation(x, y, z);
                return true;
            }
        });

    }
    private Spatial getSpatial() {
        SpatialLayer parent = (SpatialLayer) getParent();
        Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(parent.getFile());
        if (spat == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find spatial to display secondary data for layer :{0}", parent.getName());
        }
        return spat;
    }

    
    
}
