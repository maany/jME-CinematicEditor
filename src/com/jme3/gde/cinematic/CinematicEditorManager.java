/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.CharacterLayer;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import com.jme3.gde.cinematic.filetype.actions.OpenCinematicEditor;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.gde.cinematic.gui.jfx.CinematicEditorUI;
import com.jme3.gde.cinematic.scene.CinematicEditorController;
import com.jme3.gde.core.assets.AssetDataObject;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.core.sceneexplorer.nodes.JmeNode;
import com.jme3.gde.core.sceneexplorer.nodes.NodeUtility;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javax.swing.JOptionPane;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

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
    private CinematicEditorManager() {
        logger = Logger.getLogger(CinematicEditorManager.class.getName());
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
            if (child.getType() == LayerType.SPATIAL && child instanceof SpatialLayer) {
                try {
                    SpatialLayer layer = ((SpatialLayer) child);
                    loadSpatial(layer);
                    viewableCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    JOptionPane.showMessageDialog(null, "if condition satisfied while loading");
                }
            }
            // handle audio/gui etc loading appropriately
        }
        if (viewableCount == 0) {
            if(currentDataObject!=null)
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "No viewable OGL data found in {0}", getCurrentDataObject().getName());
            CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
            cinematicEditor.open();
            cinematicEditor.requestActive();
        }
    }
    /**
     * Loads a spatial into the OGL window. If the TopComponent/ OGL Window are not open, it
     * internally calls {@link CinematicEditorManager#initSceneViewerWithSpatial(com.jme3.scene.Spatial, java.lang.String)
     * }. This method can be used to load both Characters and normal Spatials
     *
     * @param layer
     */
    public void loadSpatial(SpatialLayer layer) {
        try {
            String path = layer.getPath();
            getAssetManager();
            Spatial spat = assetManager.loadModel(path);
            if (spat == null) {
                logger.log(Level.SEVERE, "Cannot load Spatial for SpatialLayer {0}" + ".\n"
                        + " Please verify the path {1} exists and is a valid .j3o binary", new Object[]{layer.getName(), path});
                return;
            }
            if (sentRequest == currentRequest && sentRequest != null) {

                CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                CinematicEditorController editorController = cinematicEditor.getEditorController();
                if (editorController != null) {
                    cinematicEditor.getEditorController().addModel(spat);
                    return;
                }
            }

            /* No Scene Opened. Create scene request*/
            initSceneViewerWithSpatial(spat, path);
            
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NullPointerException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    /**
     * Creates a new {@link SceneRequest} and launches the OGL Window loaded
     * with {@link Spatial} spat and {@link CinematicEditorTopComponent}
     *
     * @param spat
     * @param path
     * @throws DataObjectNotFoundException
     */
    private void initSceneViewerWithSpatial(Spatial spat,String path) throws DataObjectNotFoundException {
        String absolutePath = assetManager.getAssetFolder().getPath() + path;
        File file = new File(absolutePath);
        FileObject fileObject = FileUtil.toFileObject(file);
        AssetDataObject dataObject = (AssetDataObject) DataObject.find(fileObject);
        Node node;
        if (spat instanceof Node) {
            node = (Node) spat;
        } else {
            node = new Node();
            node.attachChild(spat);
        }
        JmeNode jmeNode = NodeUtility.createNode(node, dataObject, false);

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

    
     
    
    
}
