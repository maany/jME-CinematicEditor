/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core;

import java.io.File;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
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
@MIMEResolver.NamespaceRegistration(
        displayName = "#LBL_Cinematic_LOADER",
        mimeType = "text/j3c+xml",
        elementNS = {"cinematic-clip"})
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
        registerEditor("text/j3c+xml", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(
            displayName = "#LBL_Cinematic_EDITOR",
            iconBase = "com/jme3/gde/cinematic/icons/cinematic_clip_icon.png",
            mimeType = "text/j3c+xml",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "Cinematic",
            position = 1000)
    @Messages("LBL_Cinematic_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }

    public void loadData() {
        FileObject primaryFileObject = getPrimaryFile();
        File primaryFile = FileUtil.toFile(primaryFileObject);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = (Document) builder.build(primaryFile);
            Element root = doc.getRootElement();
            System.out.println("ROOT : " + root.getName());
            
        } catch (JDOMException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    
    }
}
