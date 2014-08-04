/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype.actions;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.CinematicEditorTopComponent;
import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import com.jme3.gde.core.assets.ProjectAssetManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CinematicEditor",
        id = "com.jme3.gde.cinematic.filetype.actions.OpenCinematicEditor")
@ActionRegistration(
        iconBase = "com/jme3/gde/cinematic/icons/cinematic_editor_icon.png",
        displayName = "#CTL_OpenCinematicEditor")
@ActionReferences({
    @ActionReference(path = "Toolbars/jMonkeyPlatform-Tools", position = 410),
    @ActionReference(path = "Loaders/application/jme3cinematic/Actions", position = 0)
})
@Messages("CTL_OpenCinematicEditor=Open in Cinematic Editor")
public final class OpenCinematicEditor implements ActionListener {

    private final CinematicDataObject context;

    public OpenCinematicEditor(CinematicDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
        final ProjectAssetManager assetManager = context.getContentLookup().lookup(ProjectAssetManager.class);
        if (assetManager == null) {
            Logger.getLogger(OpenCinematicEditor.class.getName()).log(Level.WARNING,"AssetManager not found in lookup. Unable to open .j3c");
            return;
        }
        final CinematicClip cinematicClip = context.getCinematicClip();
        
        Runnable cinematicEditorUILaunchThread = new Runnable() {
            @Override
            public void run() {
                ProgressHandle handle = ProgressHandleFactory.createHandle("Loading UI elements contents in Cinematic Editor");
                handle.start();
                try {
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            final CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    cinematicEditor.loadCinematicEditorUI(cinematicClip);
                                }
                            });
                        }
                    });
                    
                    
                } catch (Exception ex) {
                    Logger.getLogger(OpenCinematicEditor.class.getName()).log(Level.SEVERE, "Cannot open CinematicEditorUI as an exception occured - "
                            + "\n{0}", ex.getMessage());
                    ex.printStackTrace();

                } finally {
                    handle.finish();
                }
            }
        };
        
        Runnable oglThread = new Runnable() {

            public void run() {
                ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Opening in Cinematic Editor");
                progressHandle.start();
                try {
                    assetManager.clearCache();
                    final CinematicClip data = context.getCinematicClip();
                    CinematicEditorManager.getInstance().setCurrentClip(data);
                    if (data != null) {
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                CinematicEditorTopComponent cinematicEditor = CinematicEditorTopComponent.findInstance();
                                cinematicEditor.loadViewableCinematicData(data,context, assetManager);
                            }
                        });
                    } else {
                        Confirmation msg = new NotifyDescriptor.Confirmation(
                                "Error opening " + context.getPrimaryFile().getNameExt(),
                                NotifyDescriptor.OK_CANCEL_OPTION,
                                NotifyDescriptor.ERROR_MESSAGE);
                        DialogDisplayer.getDefault().notify(msg);
                    }
                } finally {
                    progressHandle.finish();
                }
            }
        };
        new Thread(oglThread).start();
        new Thread(cinematicEditorUILaunchThread).start();
    }
    private OpenCinematicEditor getOuterClassObject() {
        return OpenCinematicEditor.this;
    }
}
