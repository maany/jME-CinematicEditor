/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.secondary.TranslationLayer;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.scene.Spatial;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;


/**
 *
 * @author MAYANK
 */
public class SpatialLayer extends Layer{
    private File file;
    private ProjectAssetManager assetManager;
    
    private TranslationLayer translationLayer;
    public SpatialLayer(String name, Layer parent) {
        super(name,parent,LayerType.SPATIAL);
        /* Create Secondary Layers */
        translationLayer = new TranslationLayer("Translation",this);
        initSecondaryLayers();
    }
    private void initSecondaryLayers(){
        try {
        Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(file);
        translationLayer.setTranslationValue(spat.getLocalTranslation());
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Spatial Layer : {0} has no spatial attached to it.", getName());
            //ex.printStackTrace();
        }
        
    }
    @Override
    public Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set spatialSet = Sheet.createPropertiesSet();
        spatialSet.setDisplayName("Spatial");
        spatialSet.setName("SpatialLayer");
        try {
            Property pathProp = new PropertySupport.Reflection(this,java.io.File.class,"getFile","setFile");
            pathProp.setDisplayName("Path");
            spatialSet.put(pathProp);
            
/*            Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(file); 
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
            } */
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        sheet.put(spatialSet);
        return sheet;

}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       //JOptionPane.showMessageDialog(null,"Property Change Detected for : " + evt.getPropertyName());
        
            super.propertyChange(evt);
            if (evt.getPropertyName().equals("path")) {
                JOptionPane.showMessageDialog(null,"PATH CHANGED"); 
                System.out.println("PATH CHANGED");
                Map<File, Spatial> spatialMap = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap();
                if (spatialMap.containsKey(evt.getOldValue())) {
                    spatialMap.remove(evt.getOldValue());
                }
                CinematicEditorManager.getInstance().loadSpatial(this);
                initSecondaryLayers();
            }
        
    }

    /**
     * Getters and Setters
     */
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        System.out.println("**********************haha***********************");
        try {
        File oldFile = this.file;
        this.file = file;
        assetManager = CinematicEditorManager.getInstance().getCurrentDataObject().getLookup().lookup(ProjectAssetManager.class);
        String relativeAssetPath = assetManager.getRelativeAssetPath(file.getAbsolutePath());
        
        /*String path = assetManager.getAssetFolder().getPath();
        System.out.println("AssetFolderPath : " + path);
        Path assetFolderPath = Paths.get(path);
        Path relativeToAssetFolder = assetFolderPath.relativize(Paths.get(file.getPath()));
        System.out.println("Relative Path : " + relativeToAssetFolder.toUri()); */
        String ext = getExtension(relativeAssetPath.toString());
        if (ext.toLowerCase().equals("j3o")) {
            this.file = new File(relativeAssetPath);
            System.out.println("Firing path change");
            firePropertyChange("path", oldFile, this.file);
        }
        } catch(Exception ex) {
            System.out.println("EXCEPTION HERE");
            ex.printStackTrace();
        }
    }
    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}
