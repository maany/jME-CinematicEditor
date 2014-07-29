/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "CinematicEditor",
        id = "com.jme3.gde.cinematic.OpenCinematic")
@ActionRegistration(
        iconBase = "com/jme3/gde/cinematic/icons/cinematic_editor_icon.png",
        displayName = "#CTL_OpenCinematic")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = -100),
    @ActionReference(path = "Loaders/application/jme3cinematic/Actions", position = -90, separatorAfter = -40)
})
@Messages("CTL_OpenCinematic=Open in Cinematic Editor")
public final class OpenCinematic implements ActionListener {

    private final CinematicDataObject context;

    public OpenCinematic(CinematicDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
        JOptionPane.showMessageDialog(null,"OPEN-ACTION "+ context.teststring);
        System.out.println("OPEN-ACTION " + context.teststring);
        
        Runnable call = new Runnable(){
            CinematicEditorTopComponent cinematicEditor;
            ProgressHandle handle ;
            @Override
            public void run() {
                try {
                handle = ProgressHandleFactory.createHandle("Opening Cinematic Editor");
                handle.start();
                java.awt.EventQueue.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        //To change body of generated methods, choose Tools | Templates.
                        cinematicEditor = (CinematicEditorTopComponent) WindowManager.getDefault().findTopComponent(CinematicEditorTopComponent.getID());
                if(cinematicEditor==null)
                    cinematicEditor= new CinematicEditorTopComponent();
                cinematicEditor.loadCinematicFromFile(context);
                    }
                
                });
                
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                finally{
                    //cinematicEditor.loadCinematicFromFile(context);
                    JOptionPane.showMessageDialog(null, "CONTEXT LOADING COMPLETE");
                    System.out.println("LOADING IN CINEMATIC EDITOR : complete");
                    handle.finish();
                }
                
            }
        
        };
        new Thread(call).start();
    }
}
