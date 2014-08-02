/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
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
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.windows.WindowManager;

/**
 *  
 * @author MAYANK
 */
public class CinematicEditorManager {

    private static CinematicEditorManager instance = null;
    private CinematicClip currentClip;
    private CinematicDataObject currentDataObject = null;
    private SceneRequest sentRequest;
    private SceneRequest currentRequest;
    private CinematicEditorManager() {
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
        SceneApplication.getApplication().addSceneListener(tc);
        List<Layer> allDescendants = currentClip.getRoot().findAllDescendants();
        for(Layer child:allDescendants) {
            if(child.getType()==LayerType.SPATIAL && child instanceof SpatialLayer)
            {
                try{
                String path = ((SpatialLayer)child).getPath();
                ProjectAssetManager assetManager = currentDataObject.getContentLookup().lookup(ProjectAssetManager.class);
                Spatial spat = assetManager.loadModel(path);
                Project project = currentDataObject.getContentLookup().lookup(Project.class);
                String absolutePath = assetManager.getAssetFolder().getPath() + path;
                    System.out.println("ABSOLUTE PATH : " + absolutePath);
                File file = new File(absolutePath);
                FileObject fileObject = FileUtil.toFileObject(file);
                AssetDataObject dataObject = (AssetDataObject)DataObject.find(fileObject);
       
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
        request.setWindowTitle("Cinematic Editor - " + assetManager.getRelativeAssetPath(dataObject.getPrimaryFile().getPath()));
        request.setToolNode(new Node("CinematicEditorToolNode"));
        SceneApplication.getApplication().openScene(request);
                    
                    if(dataObject!=null)
                        JOptionPane.showMessageDialog(null,"SUCCESS. WE ARE  GETTING THE DATA OBJECT");
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    JOptionPane.showMessageDialog(null,"if condition satisfied while loading");
                }
            }
            // handle audio/gui etc loading appropriately
        }
    }
    
    public static Layer sampleDataStructure(){
        Layer root = new Layer("root",null);
        SpatialLayer child = new SpatialLayer("child",root);
        child.setPath("C:\\Users\\MAYANK\\Documents\\Jme_MK\\Maany\\assets\\Models\\myTeapot.j3o");
        return root;
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
