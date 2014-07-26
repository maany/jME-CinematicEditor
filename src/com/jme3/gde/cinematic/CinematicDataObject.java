/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.core.assets.BinaryModelDataObject;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.File;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_Cinematic_LOADER=Files of Cinematic"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Cinematic_LOADER",
        mimeType = "text/j3c+xml",
        extension = {"j3c", "J3C"})
@DataObject.Registration(
        mimeType = "text/j3c+xml",
        iconBase = "com/jme3/gde/cinematic/icons/cinematic_clip_icon.png",
        displayName = "#LBL_Cinematic_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/text/j3c+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class CinematicDataObject extends MultiDataObject {

    public CinematicDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        //registerEditor("text/j3c+xml", false);
        
        /*
         * add file change listener
         */
        this.getPrimaryFile().addFileChangeListener(new FileChangeAdapter(){

            @Override
            public void fileChanged(FileEvent fe) {
                super.fileChanged(fe); //To change body of generated methods, choose Tools | Templates.
                System.out.println("FILE CHANGE DETECTED MAN");
            }
        
        });
    }

    @Override
    protected int associateLookup() {
        return 1;
    }
    /**
     * load the data from the j3c file and initialize the editor
     */
    public void loadData() {
        FileObject primaryFileObject = getPrimaryFile();
        File primaryFile = FileUtil.toFile(primaryFileObject);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(primaryFile);
            Logger.logMsg(Logger.INFO, "Read successful");
        } catch (JDOMException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    
}
