/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Event;
import com.jme3.gde.cinematic.gui.jfx.CinematicEditorUI;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.gui.jfx.LayerActionControl;
import com.jme3.gde.cinematic.gui.jfx.MainToolbarControl;
import com.jme3.gde.cinematic.scene.CinematicEditorController;
import com.jme3.gde.cinematic.scene.CinematicEditorToolController;
import com.jme3.gde.cinematic.scene.tools.SelectTool;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.gde.core.assets.SpatialAssetDataObject;
import com.jme3.gde.core.scene.PreviewRequest;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneListener;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.scenecomposer.ComposerCameraController;
//import com.jme3.gde.scenecomposer.tools.SelectTool;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javax.swing.JOptionPane;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.jme3.gde.cinematic//CinematicEditor//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "CinematicEditorTopComponent",
        iconBase = "com/jme3/gde/cinematic/icons/cinematic_editor_icon.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "CinematicEditor", id = "com.jme3.gde.cinematic.CinematicEditorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CinematicEditorAction",
        preferredID = "CinematicEditorTopComponent")
@Messages({
    "CTL_CinematicEditorAction=CinematicEditor",
    "CTL_CinematicEditorTopComponent=CinematicEditor Window",
    "HINT_CinematicEditorTopComponent=This is a CinematicEditor window"
})
public final class CinematicEditorTopComponent extends TopComponent implements SceneListener,ExplorerManager.Provider{

    public static String PREFERRED_ID = "CinematicEditorTopComponent";
    private static CinematicEditorTopComponent instance;
    private CinematicEditorUI cinematicEditorUI;
    private LayerActionControl layerTabsAndEvents;
    private MainToolbarControl mainToolbar;
    private ProxyLookup lookup;
    private AbstractLookup cinematicLookup;
    private InstanceContent lookupContent;
    
    private ComposerCameraController camController;
    private CinematicEditorToolController toolController;
    private CinematicEditorController editorController;
    private ProjectAssetManager.ClassPathChangeListener listener;
    private ExplorerManager explorerManager;
    private Lookup.Result<Layer> layerSelectionResult;
    private Lookup.Result<Event> eventSelectionResult;
    /**
     * Contructor
     */
    public CinematicEditorTopComponent() {
        ProgressHandle handle = ProgressHandleFactory.createHandle("Starting Cinematic Editor...");
        handle.start();
        
        initComponents();
        setName(Bundle.CTL_CinematicEditorTopComponent());
        setToolTipText(Bundle.HINT_CinematicEditorTopComponent());
        
        /*
         * Very important. Relaods Javafx context which would otherwise close whenever JFXPanel resizes.
         */
        Platform.setImplicitExit(false);
        /*
         * Load Javafx UI into JFXPanel
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadCinematicEditorUI();
                loadLayerTabsAndEvents();
                loadToolbars();
                layerTabsAndEvents.setContent(null);
                
            }
        });
        /**
         * Load OGL data if any
         */
        java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                loadViewableCinematicData();
            }
         });
         /*
         * set up ExplorerManager
         */ 
        explorerManager = new ExplorerManager();
        explorerManager.setRootContext(CinematicEditorManager.getInstance().getCurrentClip().getRoot().getNodeDelegate());
        /*
         * set up lookup
         */
        lookupContent = new InstanceContent();
        cinematicLookup = new AbstractLookup(lookupContent); //InstanceContent is available through getLookup().lookup(InstanceContent.class);
        //selectedLayerLookup = Lookups.singleton(CinematicEditorManager.getInstance().getCurrentClip().getRoot());
        lookup = new ProxyLookup(cinematicLookup, ExplorerUtils.createLookup(explorerManager,getActionMap()));
        associateLookup(lookup);
        lookupContent.add(lookupContent);
        /*
         * Layer Selection listener
         */
        layerSelectionResult = cinematicLookup.lookupResult(Layer.class);
        layerSelectionResult.addLookupListener(new LookupListener(){

            @Override
            public void resultChanged(LookupEvent ev) {
                final Layer selectedLayer = cinematicLookup.lookup(Layer.class);
                //  Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Change in selection lookup detected : New selected Layer : {0}", selectedLayer.getName());
                explorerManager.setRootContext(CinematicEditorManager.getInstance().getCurrentClip().getRoot().getNodeDelegate());
                setActivatedNodes(new Node[]{selectedLayer.getNodeDelegate()});
                //System.out.println("Selected Node in lookup change listener : " + selectedLayer.getNodeDelegate().getDisplayName());
                // update LayerTabAndEvents in LayerActionController.java (jfx package)
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        layerTabsAndEvents.setContent(selectedLayer.createTabsAndEvents());
                    }
                });
            }
        });
        /*
         * Event Selection Listener
         */
        eventSelectionResult = cinematicLookup.lookupResult(Event.class);
        eventSelectionResult.addLookupListener(new LookupListener() {

            @Override
            public void resultChanged(LookupEvent ev) {
                final Event selectedEvent = cinematicLookup.lookup(Event.class);
                if (selectedEvent == null) {
                    return;
                }
                getExplorerManager().setRootContext(selectedEvent.getNodeDelegate());
                setActivatedNodes(new Node[]{selectedEvent.getNodeDelegate()});
            }
        });

        handle.finish();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cinematicJFXPanel = new javafx.embed.swing.JFXPanel();
        tabsJfxPanel = new javafx.embed.swing.JFXPanel();
        mainToolbarJfxPanel = new javafx.embed.swing.JFXPanel();
        jFXPanel2 = new javafx.embed.swing.JFXPanel();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setForeground(new java.awt.Color(153, 153, 153));

        cinematicJFXPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        tabsJfxPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        mainToolbarJfxPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jFXPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jFXPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(tabsJfxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cinematicJFXPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addComponent(mainToolbarJfxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainToolbarJfxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cinematicJFXPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(tabsJfxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javafx.embed.swing.JFXPanel cinematicJFXPanel;
    private javafx.embed.swing.JFXPanel jFXPanel2;
    private javafx.embed.swing.JFXPanel mainToolbarJfxPanel;
    private javafx.embed.swing.JFXPanel tabsJfxPanel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }
/**
 * Unload the cinematic clip and associated data object
 */
    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public synchronized static CinematicEditorTopComponent getDefault() {
        if (instance == null) {
            instance = new CinematicEditorTopComponent();
        }
        return instance;
    }
    public static CinematicEditorTopComponent findInstance() {
        TopComponent findTopComponent = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if(findTopComponent ==null) {
            Logger.getLogger(CinematicEditorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (findTopComponent instanceof CinematicEditorTopComponent) {
            return (CinematicEditorTopComponent) findTopComponent;
        }
        Logger.getLogger(CinematicEditorTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
        
    }
    
    /**
     * Initializes the JavaFX based CinematicEditorUI with the contents of the
     * {@link CinematicClip}. Always run in the JavaFX Runtime Environment using
     * by enquing in the thread {@link Platform#runLater(java.lang.Runnable) }
     * 
     * @param clip
     */
    public void loadCinematicEditorUI() {
        
        cinematicEditorUI = new CinematicEditorUI();
        Scene scene = new Scene(cinematicEditorUI, 880, 220);
        scene.getStylesheets().add(CinematicEditorUI.class.getResource("layer_container.css").toExternalForm());
        cinematicJFXPanel.setScene(scene);
        cinematicJFXPanel.setVisible(true);
        cinematicEditorUI.initCinematicEditorUI();
        Layer root;
        CinematicClip clip = CinematicEditorManager.getInstance().getCurrentClip();
        if (clip == null) {
            CinematicEditorManager.getInstance().init(null);
            clip = CinematicEditorManager.getInstance().getCurrentClip();
        }
        root = clip.getRoot();
        cinematicEditorUI.initView(root);
    }
    /**
     * loads the OGL content of the cinematic clip.
     */
    public void loadViewableCinematicData() {
        //JOptionPane.showMessageDialog(null,"LOADING VIEWABLE CINEMATIC DATA");
        cleanupControllers();
        SceneApplication.getApplication().addSceneListener(this);
        CinematicEditorManager.getInstance().loadViewableCinematicData();
        
    }
    /**
     * initializes javafx
     * {@link com.jme3.gde.cinematic.gui.jfx.LayerActionControl} AnchorPane into
     * JfxPanel. To be called by TopComponent's constructor
     */
    private void loadLayerTabsAndEvents(){
        layerTabsAndEvents = new LayerActionControl();
        Scene scene = new Scene(layerTabsAndEvents,180,180);
        scene.getStylesheets().add(CinematicEditorUI.class.getResource("layer_action_control.css").toExternalForm());
        tabsJfxPanel.setScene(scene);
        tabsJfxPanel.setVisible(true);
    }
    /**
     * initializes javafx based Main toolbar and LHS toolbar
     *
     */
    private void loadToolbars(){
        mainToolbar = new MainToolbarControl();
        Scene scene = new Scene(mainToolbar,880,30);
        mainToolbarJfxPanel.setScene(scene);
        mainToolbarJfxPanel.setVisible(true);
    }
    @Override
    public void sceneOpened(SceneRequest request) {
        try{
          //  JOptionPane.showMessageDialog(null, "Scene Opened Listener invoked");
            if(CinematicEditorManager.getInstance().getSentRequest()==request){
            CinematicEditorManager.getInstance().setCurrentRequest(request);
            if (editorController != null) {
                editorController.cleanup();
            }
            editorController = new CinematicEditorController(request.getJmeNode(), request.getDataObject());
            setActivatedNodes(new org.openide.nodes.Node[]{request.getDataObject().getNodeDelegate()});
            //setSceneInfo(request.getJmeNode(), editorController.getCurrentFileObject(), true);
            open();
            requestActive();
            if (camController != null) {
                camController.disable();
            }
            if (toolController != null) {
                toolController.cleanup();
            }
            toolController = new CinematicEditorToolController(request.getToolNode(), request.getManager(), request.getJmeNode());

            camController = new ComposerCameraController(SceneApplication.getApplication().getCamera(), request.getJmeNode());
            toolController.setEditorController(editorController);
            camController.setToolController(toolController);
            camController.setMaster(this);
            camController.enable();

            toolController.setCameraController(camController);
            SelectTool tool = new SelectTool();
            toolController.showEditTool(tool);
            toolController.setShowSelection(true);

            editorController.setToolController(toolController);
            toolController.refreshNonSpatialMarkers();
            toolController.setCamController(camController);

            editorController.setTerrainLodCamera();
            SceneRequest currentRequest = CinematicEditorManager.getInstance().getCurrentRequest();
            final SpatialAssetDataObject dobj = ((SpatialAssetDataObject) currentRequest.getDataObject());
            listener = new ProjectAssetManager.ClassPathChangeListener() {

                public void classPathChanged(final ProjectAssetManager manager) {
                    if (dobj.isModified()) {
                        Confirmation msg = new NotifyDescriptor.Confirmation(
                                "Classes have been changed, save and reload scene?",
                                NotifyDescriptor.OK_CANCEL_OPTION,
                                NotifyDescriptor.INFORMATION_MESSAGE);
                        Object result = DialogDisplayer.getDefault().notify(msg);
                        if (!NotifyDescriptor.OK_OPTION.equals(result)) {
                            return;
                        }
                        try {
                            dobj.saveAsset();
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                    Runnable call = new Runnable() {

                        public void run() {
                            ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Reloading Scene for Cinematic Clip..");
                            progressHandle.start();
                            try {
                                manager.clearCache();
                                final Spatial asset = dobj.loadAsset();
                                if (asset != null) {
                                    java.awt.EventQueue.invokeLater(new Runnable() {

                                        public void run() {
                                            /* handle appropriately */
                                            //CinematicEditorTopComponent editor = CinematicEditorTopComponent.findInstance();
                                            //CinematicEditorManager.getInstance().setCurrentDataObject(dobj);
                                            //CinematicEditorManager.getInstance().setCurrentDataObject(dobj);
                                            //CinematicEditorManager.getInstance().loadCinematicData(editor);
                                            //(asset, dobj, manager);
                                        }
                                    });
                                } else {
                                    Confirmation msg = new NotifyDescriptor.Confirmation(
                                            "Error opening " + dobj.getPrimaryFile().getNameExt(),
                                            NotifyDescriptor.OK_CANCEL_OPTION,
                                            NotifyDescriptor.ERROR_MESSAGE);
                                    DialogDisplayer.getDefault().notify(msg);
                                }
                            } finally {
                                progressHandle.finish();
                            }
                        }
                    };
                    new Thread(call).start();
                }
            };
        }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null,"Exception in opening scene");
            ex.printStackTrace();
        } 
    }

    @Override
    public void sceneClosed(SceneRequest request) {
        if (request.equals(CinematicEditorManager.getInstance().getCurrentRequest())) {
            setActivatedNodes(new org.openide.nodes.Node[]{});
            if (listener != null && request.getManager() != null) {
//                request.getManager().removeClassPathEventListener(listener);
                listener = null;
            }
            SceneApplication.getApplication().removeSceneListener(this);
            CinematicEditorManager.getInstance().setCurrentRequest(null);
            
            cleanupControllers();
        }
    }

    @Override
    public void previewCreated(PreviewRequest pr) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void cleanupControllers() {
        if (camController != null) {
            camController.disable();
            camController = null;
        }
        if (toolController != null) {
            toolController.cleanup();
            toolController = null;
        }
        if (editorController != null) {
            editorController.cleanup();
            editorController = null;
        }
    }

    protected CinematicEditorToolController getToolController() {
        return toolController;
    }

    protected CinematicEditorController getEditorController() {
        return editorController;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public InstanceContent getLookupContent() {
        return lookupContent;
    }

    public void setLookupContent(InstanceContent lookupContent) {
        this.lookupContent = lookupContent;
    }



    public AbstractLookup getCinematicLookup() {
        return cinematicLookup;
    }

    public CinematicEditorUI getCinematicEditorUI() {
        return cinematicEditorUI;
    }

    public void setCinematicEditorUI(CinematicEditorUI cinematicEditorUI) {
        this.cinematicEditorUI = cinematicEditorUI;
    }

    
    

    
    
}
