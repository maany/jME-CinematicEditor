/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.gui.jfx.CinematicEditorUI;
import com.jme3.gde.cinematic.core.Layer;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

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
public final class CinematicEditorTopComponent extends TopComponent {

    private CinematicEditorUI cinematicEditor; 
    public CinematicEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_CinematicEditorTopComponent());
        setToolTipText(Bundle.HINT_CinematicEditorTopComponent());
        /*
         * Very important
         */
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                cinematicEditor = new CinematicEditorUI();
                Scene scene = new Scene(cinematicEditor,880,220);
                scene.getStylesheets().add(CinematicEditorUI.class.getResource("layer_container.css").toExternalForm());
                cinematicJFXPanel.setScene(scene);
                cinematicJFXPanel.setVisible(true);
                cinematicEditor.initCinematicEditorUI();
                cinematicEditor.initView(new Layer("Root-Test",null));
            }
        });
        
        CinematicApplication cinematicApplication = CinematicApplication.getInstance();
        System.out.println("Starting Scene Viewer");
        cinematicApplication.launch();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cinematicJFXPanel = new javafx.embed.swing.JFXPanel();
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cinematicJFXPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CinematicEditorTopComponent.class, "CinematicEditorTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(cinematicJFXPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(100, 100, 100))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(cinematicJFXPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javafx.embed.swing.JFXPanel cinematicJFXPanel;
    private javax.swing.JButton jButton1;
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
}
