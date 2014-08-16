/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype.actions;

import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.layertype.CharacterLayer;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CinematicEditor",
        id = "com.jme3.gde.cinematic.filetype.actions.Modify")
@ActionRegistration(
        displayName = "#CTL_Modify")
@ActionReference(path = "Loaders/application/jme3cinematic/Actions", position = 200)
@Messages("CTL_Modify=Modify Clip")
public final class Modify implements ActionListener {

    private final CinematicDataObject context;

    public Modify(CinematicDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
        Layer root = context.getCinematicClip().getRoot();
        CharacterLayer child = new CharacterLayer("child",root);
            child.setFile(new File("\\Models\\Jaime\\Jaime.j3o"));
        //CharacterLayer character = new CharacterLayer("char",root);
        //character.setFile(new File("\\Models\\wobble\\Cylinder001.mesh.xml"));
        context.getCinematicClip().getRoot().getChildren();
        context.setModified(true);
    }
}
