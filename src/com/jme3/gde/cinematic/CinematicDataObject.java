/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_Cinematic_LOADER=Files of Cinematic"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Cinematic_LOADER",
        mimeType = "application/jme3cinematic",
        extension = {"j3c", "J3c", "J3C"})
@DataObject.Registration(
        mimeType = "application/jme3cinematic",
        iconBase = "com/jme3/gde/cinematic/icons/cinematic_clip_icon.png",
        displayName = "#LBL_Cinematic_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class CinematicDataObject extends MultiDataObject implements Savable{
    
    public String teststring = "level 0";
    private boolean modified = false;
    public CinematicDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("application/jme3cinematic", false);
        teststring = "Constructor";
        System.out.println("CONSTRUCTOR : " + teststring);
        //JOptionPane.showMessageDialog(null,"CONSTRUCTOR : " + teststring);
        
    }
    public void setModified() {
        getCookieSet().assign(SaveCookie.class,new SaveCookie(){

            @Override
            public void save() throws IOException {
                System.out.println("SAVE : Saving ");
                JOptionPane.showMessageDialog(null,"SAVE : " + teststring);
                BinaryExporter exporter = BinaryExporter.getInstance();
                File file = FileUtil.toFile(getPrimaryFile());
                try {
                    exporter.save(getInstance(),file);
                    getCookieSet().assign(SaveCookie.class);
                    teststring = "setModified";
                    JOptionPane.showMessageDialog(null,"Save Successful. Current State : " + getInstance().teststring);
                }catch(IOException ex){
                    Logger.logMsg(Logger.ERROR, "Failed to save. previous state : " + teststring);
                }
            }
        
        });
    }

    public CinematicDataObject getInstance(){
        return CinematicDataObject.this;
    } 
    @Override
    protected int associateLookup() {
        return 1;
    }

    

    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule capsule = je.getCapsule(this);
        teststring+=" writing";
        capsule.write(teststring,"teststring","write failed");
        System.out.println("WRITE : " + teststring);
        JOptionPane.showMessageDialog(null,"WRITE: " + teststring);
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
        InputCapsule capsule = ji.getCapsule(this);
        teststring = capsule.readString("teststring","read or write failed");
        teststring+=" read";
        System.out.println("READ : " + teststring); 
        JOptionPane.showMessageDialog(null,"READ : " + teststring);
    }
    
}
