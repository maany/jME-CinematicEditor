/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.core.assets.ProjectAssetManager;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataFolder;
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
    private CinematicClip cinematicClip;
    private InstanceContent lookupContents;
    private AbstractLookup contentLookup;
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
        this.saver = new CinematicSaver(this);
        this.cinematicClip = new CinematicClip();
        cinematicClip.setName(getPrimaryFile().getName());
        Layer root = new Layer(getPrimaryFile().getName(),null);
        cinematicClip.setRoot(root);
        lookupContents = new InstanceContent();
        contentLookup = new AbstractLookup(lookupContents);
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
            getCookieSet().assign(SaveCookie.class, saver);
        } else {
            getCookieSet().assign(SaveCookie.class);
        }
    }
    /**
     * Whenever a new j3c is created this method will initialize it with a sample cinematic clip
     * with root layer named same as the j3c name.
     *
     * @param df
     * @param name
     * @return
     * @throws IOException
     */
    @Override
    protected DataObject handleCreateFromTemplate(DataFolder df, String name) throws IOException {
        CinematicDataObject cinematicDataObject = (CinematicDataObject)super.handleCreateFromTemplate(df, name); //To change body of generated methods, choose Tools | Templates.
        Layer root = new Layer(null,null);
        root.setName(cinematicDataObject.getName());
        cinematicDataObject.getCinematicClip().setRoot(root);
        return cinematicDataObject;
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

    public CinematicClip getCinematicClip() {
        return cinematicClip;
    }

    public void setCinematicClip(CinematicClip cinematicClip) {
        this.cinematicClip = cinematicClip;
    }

    public AbstractLookup getContentLookup() {
        return contentLookup;
    }

    public void setContentLookup(AbstractLookup contentLookup) {
        this.contentLookup = contentLookup;
    }

  
    
    
}
