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
public class CinematicDataObject extends MultiDataObject implements Savable,Serializable{
    private boolean modified = false;
    private String test = "BASE";
    private CinematicClip cinematicData;
    private InstanceContent lookupContents;
    private AbstractLookup lookup;
    //ProjectAssetManager mgr = null;
    
    public CinematicDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        lookupContents = new InstanceContent();
        lookup = new AbstractLookup(lookupContents);
        findAssetManager();
        //registerEditor("application/jme3cinematic", false);
    }
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
       // System.out.println("PROJECT MANAGER : " + pm.toString());
         //       System.out.println("PROJECT NAME : " + pm.isProject(primaryFile));
           //     System.out.println("ASSETMANAGER" + mgr.toString());
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
            try {
            File file = FileUtil.toFile(getPrimaryFile());
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            test = "PASSED.Just before writing.";
            out.writeObject(this);
            test = "FAILED.Just after writing";
            getCookieSet().assign(SaveCookie.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            /*BinaryExporter exporter = BinaryExporter.getInstance();
            Spatial wrapper = SpatialWrapper.packCinematicForExport(getOuterClass());
            exporter.save(wrapper,FileUtil.toFile(getPrimaryFile()));*/
           // test = "file exporter. reload to see data object's value"; 
            
            
            
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
        test = capsule.readString("TEST", null);
        if(test!=null)
            test = "REAd successful. method works";
    }
    public String showTestResult(){
        return test;
    }
    
    public InstanceContent getLookupContents(){
        return lookupContents;
    }
    public ProjectAssetManager getAssetManger(){
        return null;//mgr;
    }
}
