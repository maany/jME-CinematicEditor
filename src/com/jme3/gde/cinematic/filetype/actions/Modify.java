/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype.actions;

import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        context.setModified(true);
    }
}
