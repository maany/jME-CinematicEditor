/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.animation.AnimControl;
import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.CharacterLayer;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.cinematic.library.CinematicLibrary;
import com.jme3.gde.cinematic.scene.CinematicEditorController;
import com.jme3.gde.core.assets.AssetDataObject;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.gde.core.assets.SpatialAssetDataObject;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.core.sceneexplorer.nodes.JmeNode;
import com.jme3.gde.core.sceneexplorer.nodes.JmeSpatial;
import com.jme3.gde.core.sceneexplorer.nodes.NodeUtility;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup.Result;

/**
 *  
 * @author MAYANK
 */
public class CinematicEditorManager {

    private static CinematicEditorManager instance = null;
    private CinematicClip currentClip;
    private CinematicDataObject currentDataObject = null;
    private ProjectAssetManager assetManager = null;
    private SceneRequest sentRequest;
    private SceneRequest currentRequest;
    private Logger logger;
    private boolean loaded = false;
    private CinematicEditorManager() {
        logger = Logger.getLogger(CinematicEditorManager.class.getName());
        //TODO init a blank data object. (This would make the assetManager available to the cinematicEditor.) Then save the data object in the end.
        
    }

    public static CinematicEditorManager getInstance() {
        if (instance == null) {
            instance = new CinematicEditorManager(); 
        } 
        return instance;
    }

    
    /**
     * This method is to be called whenever 1)CinematicEditorTopComponent is
     * opened for the first time with no clip loaded 2) New CinematicDataObject
     * is created 3) Existing CinematicDataObject is loaded. This method will
     * assign values to currentDataObject and currentCinematicClip accordingly
     *
     * @param cinematicDataObject
     */
    public void init(CinematicDataObject cinematicDataObject){
        setCurrentDataObject(cinematicDataObject);
        try{
            currentClip = currentDataObject.getCinematicClip();
        } catch(NullPointerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"No CinematicDataObject loaded. Please save the clip to a data object before exiting.");
            createNewCinematicClip();
            getAssetManager();
        } finally {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Initialized Cinematic Editor with\n"
                    + "Current Data Object: {0}\n"
                    + "Current Cinematic Clip: {1}",new Object[]{currentDataObject.getName(),currentClip.getName()});
        }
    }
    /**
     * This method is used to create a standalone cinematic clip which does not
     * belong to any data object. use case : when no j3c is loaded and user
     * creates a cinematic. i.e cinematic editor is openend for the first time/
     * or no data object is loaded in the editor
     * TODO : on saving, save it to a
     * new j3c using wizard.
     */
    private void createNewCinematicClip(){
        Layer root = new Layer(GuiManager.DEFAULT_NAME, null, LayerType.ROOT);
        CinematicClip clip = new CinematicClip(GuiManager.DEFAULT_NAME, root, (int) GuiManager.DEFAULT_DURATION);
        CinematicEditorManager.getInstance().setCurrentDataObject(null);
        CinematicEditorManager.getInstance().setCurrentClip(clip);
    }
    
    /**
     * This method loads all the graphical/viewable 3d content of the currently
     * loaded cinematic in the OGLWindow Never call directly. Used by
     * {@link CinematicEditorTopComponent}
     */
    void loadViewableCinematicData() {
        assert currentClip != null;
        //assert currentDataObject != null;
        int viewableCount = 0;
        getAssetManager();
        List<Layer> allDescendants = currentClip.getRoot().findAllDescendants();
        for (Layer child : allDescendants) {
            System.out.println("CURRENT CHILD : " + child.getName());
            if (child.getType() == LayerType.SPATIAL || child instanceof SpatialLayer) {
                try {
                    SpatialLayer layer;
                    if (child.getType() == LayerType.CHARACTER) {
                        layer = (CharacterLayer) child;
                    } else {
                        layer = (SpatialLayer) child;
                    }
                    loadSpatial(layer);
                    viewableCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                   // JOptionPane.showMessageDialog(null, "if condition satisfied while loading");
                }
            
            // handle audio/gui etc loading appropriately
        }
        if (viewableCount == 0) {
            if(currentDataObject!=null)
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "No viewable OGL data found in {0}", getCurrentDataObject().getName());
            java.awt.EventQueue.invokeLater(new Runnable(){

                @Override
                public void run() {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                    cinematicEditor.open();
                    cinematicEditor.requestActive();
                }
            });
  
            }
            loaded = true;
        }
    }
    /**
     * Loads a spatial into the OGL window. If the TopComponent/ OGL Window are not open, it
     * internally calls {@link CinematicEditorManager#initSceneViewerWithSpatial(com.jme3.scene.Spatial, java.lang.String)
     * }. This method can be used to load both Characters and normal Spatials. On successful loading, the spatial is registered with the library{@link CinematicLibrary} of the DataObject;
     *
     * @param layer
     */
    public void loadSpatial(final SpatialLayer layer) {
        System.out.println("****Load Spatial called for : " + layer.getName());
        String path = null;
        final Spatial spat;
        try {
            path = layer.getFile().getPath();
            getAssetManager();
            spat = assetManager.loadModel(path);
            if (spat == null) {
                logger.log(Level.SEVERE, "Cannot load Spatial for SpatialLayer {0}" + ".\n"
                        + " Please verify the path {1} exists and is a valid .j3o binary", new Object[]{layer.getName(), path});
                return;
            }

            if (sentRequest == currentRequest && sentRequest != null) {

                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        
                        final CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                        CinematicEditorController editorController = cinematicEditor.getEditorController();
                        if (editorController != null) {
                            System.out.println("*** Add model is being invoked for :" + spat.getName());
                            AssetDataObject spatialAssetDataObject = null;
                            try {
                                spatialAssetDataObject = getSpatialAssetDataObject(layer);
                                cinematicEditor.getEditorController().addModel((SpatialAssetDataObject) spatialAssetDataObject,new Vector3f(0,0,0));
                            } catch (DataObjectNotFoundException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            
                            
                        }
                    }
                });
                currentDataObject.getLibrary().getSpatialMap().put(layer.getFile(), spat);
            } else { 
            /* No Scene Opened. Create scene request*/
                System.out.println("INIT for " +layer.getName());
            initSceneViewerWithSpatial(spat, layer);
            currentDataObject.getLibrary().getSpatialMap().put(layer.getFile(),spat);
            } 
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NullPointerException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Adding {0} to spatial map", layer.getFile().getPath());
        
        
    }
    /**
     * Creates a new {@link SceneRequest} and launches the OGL Window loaded
     * with {@link Spatial} spat and {@link CinematicEditorTopComponent}
     *
     * @param spat
     * @param layer
     * @throws DataObjectNotFoundException
     */
    private void initSceneViewerWithSpatial(Spatial spat,SpatialLayer layer) throws DataObjectNotFoundException {
        
       
        AssetDataObject dataObject = getSpatialAssetDataObject(layer);
        Node node;
        if (spat instanceof Node) {
            node = (Node) spat;
        } else {
            node = new Node();
            node.attachChild(spat);
            
        }
        JmeNode jmeNode = NodeUtility.createNode(node, dataObject, false);
        //assert jmeNode!=null:"see CinematicEditorManager#initSceneViewerWithSpatial";
        SceneRequest request = new SceneRequest(this, jmeNode, assetManager);
        request.setDataObject(dataObject);
        // request.setHelpCtx(ctx);
        this.sentRequest = request;
        request.setWindowTitle("Cinematic Editor - " + currentDataObject.getName());
        request.setToolNode(new Node("CinematicEditorToolNode"));
        SceneApplication.getApplication().openScene(request);
        
        
    }



    
   
    /*
     * Getters and Setters
     */
    
    
    /**
     * Shortcut way to get hold of currently loaded cinematic clip in the
     * editor. Longer method involves getting reference of the currentDataObject
     * from TopComponent. Then obtaining its cinematicClip
     *
     * @return
     */
    public CinematicClip getCurrentClip() {
        if (currentClip == null) {
            Layer root = new Layer(GuiManager.DEFAULT_NAME, null,LayerType.ROOT);
            currentClip = new CinematicClip(GuiManager.DEFAULT_NAME, root);
        }
        return currentClip;
    }
    private void setCurrentClip(CinematicClip currentClip) {
        this.currentClip = currentClip;
    }

    /**
     * Return the instance of CinematicEditorTopComponent loaded in Netbeans
     * Platform's {@link WindowManager}.
     * @return 
     */
    //TODO modify needs modifications.
    /*   public CinematicEditorTopComponent getCinematicEditorTopComponent() {
    cinematicEditorTopComponent = (CinematicEditorTopComponent) WindowManager.getDefault().findTopComponent(CinematicEditorTopComponent.PREFFERED_ID);
    if (cinematicEditorTopComponent == null) {
    cinematicEditorTopComponent = new CinematicEditorTopComponent();
    }
    return cinematicEditorTopComponent;
    }
     */
    public CinematicDataObject getCurrentDataObject() {
        return currentDataObject;
    }

    private void setCurrentDataObject(CinematicDataObject currentDataObject) {
        this.currentDataObject = currentDataObject;
    }
    private ProjectAssetManager getAssetManager() {
        if (currentDataObject == null) {
            return null;
        }
        if (assetManager == null) {
            assetManager = currentDataObject.getContentLookup().lookup(ProjectAssetManager.class);
        }
        return assetManager;
    }

    public SceneRequest getSentRequest() {
        return sentRequest;
    }

    public void setSentRequest(SceneRequest sentRequest) {
        this.sentRequest = sentRequest;
    }

    public SceneRequest getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(SceneRequest currentRequest) {
        this.currentRequest = currentRequest;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    /**
     * Method returns the {@link AssetDataObject} of the spatial represented by {@link SpatialLayer}.
     * Used mainly to load new spatials in the OLG window
     * @param layer
     * @return
     * @throws DataObjectNotFoundException 
     */
    private AssetDataObject getSpatialAssetDataObject(SpatialLayer layer) throws DataObjectNotFoundException{
        
        String absolutePath = assetManager.getAssetFolder().getPath() + layer.getFile().getPath();
        File file = new File(absolutePath);
        FileObject fileObject = FileUtil.toFileObject(file);
        AssetDataObject dataObject = (AssetDataObject) DataObject.find(fileObject);
        return dataObject;
    }
    
     
    
    
}
