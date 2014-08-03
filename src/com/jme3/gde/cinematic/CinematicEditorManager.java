/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.audio.AudioData;
import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.eventtype.AudioEvent;
import com.jme3.gde.cinematic.core.layertype.SoundLayer;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
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
     * Shortcut way to get hold of currently loaded cinematic clip in the
     * editor. Longer method involves getting reference of the currentDataObject
     * from TopComponent. Then obtaining its cinematicClip
     *
     * @return
     */
    public CinematicClip getCurrentClip() {
        if (currentClip == null) {
            Layer root = new Layer("Root", null);
            currentClip = new CinematicClip("Untitled", root);
        }
        return currentClip;
    }
    /**
     * Whenever loading a new CinematicDataObject into the editor, this method
     * updates the currentClip in CinematicEditorManager so that
     * {@link #getCurrentClip()} can be used to quickly access
     * the currently loaded {@link CinematicClip}.
     *
     * @param currentClip the currently loaded cinematic clip in the cinematic
     * editor
     */
    public void setCurrentClip(CinematicClip currentClip) {
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

    public void setCurrentDataObject(CinematicDataObject currentDataObject) {
        this.currentDataObject = currentDataObject;
    }

    /**
     * Never call directly. Used by {@link CinematicEditorTopComponent}
     */
    void loadCinematicData(CinematicEditorTopComponent tc) {
        assert currentClip!=null ;
        assert currentDataObject!=null;
        getAssetManager();
        SceneApplication.getApplication().addSceneListener(tc);
        List<Layer> allDescendants = currentClip.getRoot().findAllDescendants();
        for(Layer child:allDescendants) {
            if (child.getType() == LayerType.SPATIAL && child instanceof SpatialLayer) {
                try {
                    SpatialLayer layer = ((SpatialLayer) child);
                    loadSpatial(layer);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    JOptionPane.showMessageDialog(null, "if condition satisfied while loading");
                }
            }
            // handle audio/gui etc loading appropriately
        }
    }
    /**
     * Loads a spatial into the OGL window and creates a layer space in the
     * Cinematic Editor. If the TopComponent/ OGL Window are not open, it
     * internally calls {@link CinematicEditorManager#initSceneViewerWithSpatial(com.jme3.scene.Spatial, java.lang.String)
     * }
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
    public void initSceneViewerWithSpatial(Spatial spat,String path) throws DataObjectNotFoundException {
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
    
    public void loadSound(SoundLayer layer)
    {
        List<AudioEvent> audioEvents = layer.getAudioEvents();
        for(AudioEvent event: audioEvents){
            String path = event.getPath();
            AudioData loadAudio = assetManager.loadAudio("");
        }
     
        
    }
    /**
     * Sample parent-child tree for testing the editor.
     * @return 
     */
    public static Layer getSampleDataStructure(){
        Layer root = new Layer("root",null);
        SpatialLayer child = new SpatialLayer("child",root);
        child.setPath("C:\\Users\\MAYANK\\Documents\\Jme_MK\\Maany\\assets\\Models\\myTeapot.j3o");
        return root;
    } 

    public void initManager(CinematicClip data, CinematicDataObject context) {
        this.currentClip = data;
        this.currentDataObject = context;
        getAssetManager();
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
