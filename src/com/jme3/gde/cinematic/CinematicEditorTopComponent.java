/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.gui.jfx.CinematicEditorUI;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import com.jme3.gde.cinematic.scene.CinematicEditorController;
import com.jme3.gde.cinematic.scene.CinematicEditorToolController;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.gde.core.assets.SpatialAssetDataObject;
import com.jme3.gde.core.scene.PreviewRequest;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneListener;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.scenecomposer.ComposerCameraController;
import com.jme3.gde.scenecomposer.SceneComposerTopComponent;
import com.jme3.gde.scenecomposer.SceneEditorController;
import com.jme3.gde.scenecomposer.tools.SelectTool;
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
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
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
public final class CinematicEditorTopComponent extends TopComponent implements SceneListener{

    public static String PREFERRED_ID = "CinematicEditorTopComponent";
    private static CinematicEditorTopComponent instance;
    private CinematicEditorUI cinematicEditor;
    private Lookup lookup;
    private InstanceContent lookupContent;
    private ComposerCameraController camController;
    private CinematicEditorToolController toolController;
    private CinematicEditorController editorController;
    private ProjectAssetManager.ClassPathChangeListener listener;
    public CinematicEditorTopComponent() {
        ProgressHandle handle = ProgressHandleFactory.createHandle("Starting Cinematic Editor...");
        handle.start();

        initComponents();
        setName(Bundle.CTL_CinematicEditorTopComponent());
        setToolTipText(Bundle.HINT_CinematicEditorTopComponent());
        /*
         * set up lookup
         */
        lookupContent = new InstanceContent();
        //InstanceContent is available through getLookup().lookup(InstanceContent.class);
        lookup = new AbstractLookup(lookupContent);
        associateLookup(lookup);
        lookupContent.add(lookupContent);
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

                cinematicEditor = new CinematicEditorUI();
                Scene scene = new Scene(cinematicEditor, 880, 220);
                scene.getStylesheets().add(CinematicEditorUI.class.getResource("layer_container.css").toExternalForm());
                cinematicJFXPanel.setScene(scene);
                cinematicJFXPanel.setVisible(true);
                cinematicEditor.initCinematicEditorUI();
                cinematicEditor.initView(new Layer("Root-Test", null));
                lookupContent.add(cinematicEditor);
            }
        });
        // null coz initialized in different thread. check solultion?? 
        cinematicEditor = getLookup().lookup(CinematicEditorUI.class);

        if (cinematicEditor != null) {
            JOptionPane.showMessageDialog(null, "Yipee yayayya");
        } else {
            JOptionPane.showMessageDialog(null, "Cinematic Editor UI in top comp is null");
        }
        /*
         * Create Blank Scene Request, launch Viewer
         */
       // CinematicApplication cinematicApplication = CinematicApplication.getInstance();
      //  cinematicApplication.launch();
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
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cinematicJFXPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CinematicEditorTopComponent.class, "CinematicEditorTopComponent.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jTextField1.setText(org.openide.util.NbBundle.getMessage(CinematicEditorTopComponent.class, "CinematicEditorTopComponent.jTextField1.text")); // NOI18N
        jToolBar1.add(jTextField1);

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(CinematicEditorTopComponent.class, "CinematicEditorTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cinematicJFXPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(455, 455, 455))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cinematicJFXPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javafx.embed.swing.JFXPanel cinematicJFXPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

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

    public static synchronized CinematicEditorTopComponent getDefault() {
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

    

    public void loadCinematicData(CinematicClip data, CinematicDataObject context, ProjectAssetManager assetManager) {
        JOptionPane.showMessageDialog(null,"LOADING CINEMATIC DATA");
        CinematicEditorManager.getInstance().setCurrentClip(data);
        CinematicEditorManager.getInstance().setCurrentDataObject(context);
        CinematicEditorManager.getInstance().loadCinematicData();
        
    }

    
    @Override
    public void sceneOpened(SceneRequest request) {
      /*  try{
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
            //toolController.setCamController(camController);

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
                                            SceneComposerTopComponent composer = SceneComposerTopComponent.findInstance();
                                            composer.openScene(asset, dobj, manager);
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
            ex.printStackTrace();
        } */
    }

    @Override
    public void sceneClosed(SceneRequest sr) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void previewCreated(PreviewRequest pr) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    
    
}
