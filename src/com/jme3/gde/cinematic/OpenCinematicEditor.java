/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CinematicEditor",
        id = "com.jme3.gde.cinematic.OpenCinematicEditor")
@ActionRegistration(
        iconBase = "com/jme3/gde/cinematic/icons/cinematic_editor_icon.png",
        displayName = "#CTL_OpenCinematicEditor")
@ActionReferences({
    @ActionReference(path = "Toolbars/jMonkeyPlatform-Tools", position = 410),
    @ActionReference(path = "Loaders/text/j3c+xml/Actions", position = 0)
})
@Messages("CTL_OpenCinematicEditor=Open Cinematic Editor")
public final class OpenCinematicEditor implements ActionListener {

    private final CinematicDataObject context;

    public OpenCinematicEditor(CinematicDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }
}
