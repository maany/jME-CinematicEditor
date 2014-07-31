/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.core.assets.ProjectAssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

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
public class CinematicDataObject extends MultiDataObject {

    private transient boolean modified;
    public String test;
    private transient CinematicClip cinematicData;
    private transient InstanceContent lookupContents;
    private transient AbstractLookup lookup;
    private transient CinematicSaver saver;
    //ProjectAssetManager mgr = null;

    /**
     * Constructor
     *
     * @param pf
     * @param loader
     * @throws DataObjectExistsException
     * @throws IOException
     */
    public CinematicDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        this.modified = false;
        this.test = "BASE";
        this.saver = new CinematicSaver(this);
        lookupContents = new InstanceContent();
        lookup = new AbstractLookup(lookupContents);
        findAssetManager();
        //registerEditor("application/jme3cinematic", false);
    }

    /**
     * Method to Locate the {@link ProjectAssetManager} and put it in lookup
     */
    public void findAssetManager() {
        FileObject primaryFile = getPrimaryFile();
        ProjectManager pm = ProjectManager.getDefault();

        Project project = null;
        while (primaryFile != null) {
            if (primaryFile.isFolder() && pm.isProject(primaryFile)) {
                try {
                    project = ProjectManager.getDefault().findProject(primaryFile);
                    if (project != null) {
                        lookupContents.add(project);
                        ProjectAssetManager mgr = project.getLookup().lookup(ProjectAssetManager.class);
                        if (mgr != null) {
                            lookupContents.add(mgr);
                            System.out.println("FOUND ASSETMANAGER.. ! null and added to lookupCOntents");
                            return;
                        }
                    }
                } catch (IOException ex) {
                } catch (IllegalArgumentException ex) {
                }
            }
            primaryFile = primaryFile.getParent();
        }
    }

    @Override
    protected int associateLookup() {
        return 1;
    }
    /*
     * Code dealing with saving Data Object
     */
    

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modify) {
        modified = modify;
        if (modified) {
            test = "File was modified";
            getCookieSet().assign(SaveCookie.class, saver);
        } else {
            getCookieSet().assign(SaveCookie.class);
        }
    }

    /*
     * Getters and Setters
     */
    
    /**
     * Returns instance of this class. Purpose is to provide access of the outer
     * class to inner class
     *
     * @return
     */
    private CinematicDataObject getOuterClass() {
        return this;
    }

    public InstanceContent getLookupContents() {
        return lookupContents;
    }
}
