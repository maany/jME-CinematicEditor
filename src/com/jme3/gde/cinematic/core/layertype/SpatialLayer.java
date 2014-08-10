/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.secondary.TranslationLayer;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 *
 * @author MAYANK
 */
public class SpatialLayer extends Layer{
    private String path;
    
    private TranslationLayer translation;
    public SpatialLayer(String name, Layer parent) {
        super(name,parent,LayerType.SPATIAL);
        /* Create Secondary Layers */
        translation = new TranslationLayer("Translation",this);
        initSecondaryLayers();
    }
    private void initSecondaryLayers(){
        try {
        Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(path);
        translation.setTranslationValue(spat.getLocalTranslation());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    @Override
    public Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set spatialSet = Sheet.createPropertiesSet();
        spatialSet.setDisplayName("Spatial");
        spatialSet.setName("SpatialLayer");
        try {
            Property pathProp = new PropertySupport.Reflection(this,String.class,"getPath","setPath");
            pathProp.setDisplayName("Path");
            spatialSet.put(pathProp);
            
            Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(path); 
            if (spat != null) {
                Property localTranslationProp = new PropertySupport.Reflection(spat, Vector3f.class, "getLocalTranslation",null );
                Property localRotationProp = new PropertySupport.Reflection(spat, Vector3f.class, "getLocalRotation", null);
                Property localScaleProp = new PropertySupport.Reflection(spat, Vector3f.class, "getLocalScale", null);

                localTranslationProp.setDisplayName("Local Translation");
                localRotationProp.setDisplayName("Local Rotation");
                localScaleProp.setDisplayName("Local Scale");

                spatialSet.put(localTranslationProp);
                spatialSet.put(localRotationProp);
                spatialSet.put(localScaleProp);
            }
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        sheet.put(spatialSet);
        return sheet;

}
    
    
     /**
      * Getters and Setters
      */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if(!this.path.equals(path)){
            Map<String, Spatial> spatialMap = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap();
            spatialMap.remove(path);
            this.path = path;
            CinematicEditorManager.getInstance().loadSpatial(this);
            initSecondaryLayers();
        }
    }
}
