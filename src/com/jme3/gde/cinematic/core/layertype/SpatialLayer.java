/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.secondary.RotationLayer;
import com.jme3.gde.cinematic.core.layertype.secondary.ScaleLayer;
import com.jme3.gde.cinematic.core.layertype.secondary.TranslationLayer;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.scene.Spatial;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
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
    private RotationLayer rotationLayer;
    private ScaleLayer scaleLayer;
    public SpatialLayer(String name, Layer parent) {
        super(name,parent,LayerType.SPATIAL);
        /* Create Secondary Layers */
        translationLayer = new TranslationLayer("Translation",this);
        rotationLayer = new RotationLayer("Rotation",this);
        scaleLayer = new ScaleLayer("Scale",this);
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
    public Map<String, ArrayList<Button>> createTabsAndEvents() {
        return super.createTabsAndEvents(); //To change body of generated methods, choose Tools | Templates.
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
            
           /* Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(file); 
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
      //  System.out.println("**********************haha***********************");
        try {
        File oldFile = this.file;
        this.file = file;
        if(assetManager==null)
            assetManager = CinematicEditorManager.getInstance().getCurrentDataObject().getContentLookup().lookup(ProjectAssetManager.class);
            System.out.println("Absolute Asset Path : " + file.getAbsolutePath());
            String relativeAssetPath = assetManager.getRelativeAssetPath(file.getAbsolutePath());
            System.out.println("Relative Asset Path : " + relativeAssetPath); 
        /*String path = assetManager.getAssetFolder().getPath();
        System.out.println("AssetFolderPath : " + path);
        Path assetFolderPath = Paths.get(path);
        Path relativeAssetPath = assetFolderPath.relativize(Paths.get(file.getPath()));
        System.out.println("Relative Path : " + relativeAssetPath.toUri()); */
        String ext = getExtension(relativeAssetPath);
        if (ext.toLowerCase().equals("j3o")) {
            boolean test = setRelativePath(file.getAbsolutePath(),assetManager.getAssetFolder().getPath());
            System.out.println("Test : " + test);
            System.out.println("Firing path change");
            firePropertyChange("path", oldFile, this.file);
        }
        } catch(Exception ex) {
           // System.out.println("EXCEPTION HERE");
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
    /**
     * Method to return relative path for the Asset. for eg :
     * C:\Users\MAYANK\Documents\Jme_MK\Maany\assets\Models\myTeapot.j3o should
     * return Models\myTeapot.j3o Find a better algorithm. Already tried using
     * assetManager.getRelativeAssetPath() + using java.io.file.Path
     *
     * @param path
     * @return
     */
    private boolean setRelativePath(String path,String assetFolderPath){
        //System.out.println("********************Setting Relative Path************************");
        String[] pathArray = path.split("\\\\");
        //System.out.println("path : "+ path + "     pathArray : " + pathArray.length);
        String[] assetFolderPathArray = assetFolderPath.split("/");
        //System.out.println("assetFolderPath : " + assetFolderPath+"     assetFolderArray : " + assetFolderPathArray.length);
        int i=0;
        String relativePath = "";
        for(String piece:assetFolderPathArray){
            if(!piece.equals(pathArray[i])){
                return false;
            } else if(pathArray[i].equals("assets")) {
                for(int j=i+1;j<pathArray.length;j++)
                    relativePath+="\\"+ pathArray[j];
                file = new java.io.File(relativePath);
                return true;
            }
            System.out.println("Final Relative Path : " + relativePath);
            i++;
        }
        return false;   
    }
}
