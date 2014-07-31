/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.gde.cinematic.core.CinematicClip;
import java.io.IOException;
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
    "LBL_Cinematic_LOADER=Files of Cinematic Editor"
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
    /*@ActionReference(
            path = "Loaders/application/jme3cinematic/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200), */
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
    private boolean modified = false;
    private String test = "BASE";
    private CinematicClip cinematicData;
    public CinematicDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        //registerEditor("application/jme3cinematic", false);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }
    public void modify(){
        test = "File just got modified";
        getCookieSet().assign(SaveCookie.class, saveCookie);
    }
    /*
     * Getters and Setters
     */
    public boolean isModified(){
        return modified;
    }
    public void setModified(boolean modify){
        modified = modify;
        if (modified) {
            modify();
        } else {
            getCookieSet().assign(SaveCookie.class);
        }
    }
    SaveCookie saveCookie = new SaveCookie(){

        @Override
        public void save() throws IOException {
            BinaryExporter exporter = BinaryExporter.getInstance();
            exporter.save(getOuterClass(),FileUtil.toFile(getPrimaryFile()));
            test = "file exporter. reload to see data object's value";
            getCookieSet().assign(SaveCookie.class);
        }
    
    };

    private CinematicDataObject getOuterClass(){
        return this;
    }
    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule capsule = je.getCapsule(this);
        capsule.write(test, "TEST", "wrote_null");
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
        InputCapsule capsule = ji.getCapsule(this);
        test = capsule.readString("TEST", "read_null");
    }
    public String showTestResult(){
        return test;
    }
}
