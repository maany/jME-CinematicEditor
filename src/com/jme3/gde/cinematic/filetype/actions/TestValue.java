/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype.actions;

import com.jme3.gde.cinematic.filetype.CinematicDataObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CinematicEditor",
        id = "com.jme3.gde.cinematic.filetype.actions.TestValue")
@ActionRegistration(
        displayName = "#CTL_TestValue")
@ActionReference(path = "Loaders/application/jme3cinematic/Actions", position = 100)
@Messages("CTL_TestValue=show test value")
public final class TestValue implements ActionListener {

    private final CinematicDataObject context;

    public TestValue(CinematicDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
        JOptionPane.showMessageDialog(null, "Test Result : " + context.showTestResult());
    }
}
