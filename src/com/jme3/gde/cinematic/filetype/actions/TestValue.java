/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype.actions;

import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.sun.media.jfxmedia.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CinematicEditor",
        id = "com.jme3.gde.cinematic.filetype.actions.TestValue")
@ActionRegistration(
        displayName = "#CTL_TestValue")
@ActionReference(path = "Loaders/application/jme3cinematic/Actions", position = 100)
@Messages("CTL_TestValue=show test value")
public final class TestValue implements ActionListener {
    private ProjectAssetManager assetManager;
    private CinematicDataObject context;

    public TestValue(CinematicDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
       // context.findAssetManager();
        //assetManager = context.getAssetManger();
        //String userHome = System.getProperty("user.home");
        //assetManager.registerLocator(FileUtil.toFile(context.getFolder().getPrimaryFile()).getAbsolutePath(), FileLocator.class);
        try{
        //assetManager.getAssetFolderName();
          //  Spatial loadModel = assetManager.loadModel(FileUtil.toFile(context.getPrimaryFile()).getAbsolutePath());
           // context = SpatialWrapper.unPackCinematicForImport((SpatialWrapper)loadModel);
           // context.showTestResult();
  //          File file = FileUtil.toFile(context.getPrimaryFile());
    //        FileInputStream fin = new FileInputStream(file);
//            ObjectInputStream in = new ObjectInputStream(fin);
            
            JOptionPane.showMessageDialog(null,"just loaded context from dataobject and displayin test result : " + context.test);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.logMsg(Logger.ERROR, "Failed to load cinematic context");
            
        }
        
    }
}
