/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import org.openide.windows.WindowManager;

/**
 *  
 * @author MAYANK
 */
public class CinematicEditorManager {

    private static CinematicEditorManager instance = null;
    private CinematicClip currentClip;
    private CinematicEditorTopComponent cinematicEditorTopComponent;

    private CinematicEditorManager() {
    }

    public static CinematicEditorManager getInstance() {
        if (instance == null) {
            instance = new CinematicEditorManager();
        }
        return instance;
    }

    /**
     * Shortcut way to get hold of currently loaded cinematic clip in the
     * editor. Longer method involves getting reference of the currentDataObject
     * from TopComponent. Then obtaining its cinematicClip
     *
     * @return
     */
    public CinematicClip getCurrentClip() {
        if (currentClip == null) {
            Layer root = new Layer("Root", null);
            currentClip = new CinematicClip("Untitled", root);
        }
        return currentClip;
    }
    /**
     * Whenever loading a new CinematicDataObject into the editor, this method
     * updates the currentClip in CinematicEditorManager so that
     * {@link #getCurrentClip()} can be used to quickly access
     * the currently loaded {@link CinematicClip}.
     *
     * @param currentClip the currently loaded cinematic clip in the cinematic
     * editor
     */
    public void setCurrentClip(CinematicClip currentClip) {
        this.currentClip = currentClip;
    }
    /**
     * Return the instance of CinematicEditorTopComponent loaded in Netbeans
     * Platform's {@link WindowManager}. 
     * @return 
     */
    //TODO modify needs modifications.
 /*   public CinematicEditorTopComponent getCinematicEditorTopComponent() {
        cinematicEditorTopComponent = (CinematicEditorTopComponent) WindowManager.getDefault().findTopComponent(CinematicEditorTopComponent.PREFFERED_ID);
        if (cinematicEditorTopComponent == null) {
            cinematicEditorTopComponent = new CinematicEditorTopComponent();
        }
        return cinematicEditorTopComponent;
    }
*/
    public void setCinematicEditorTopComponent(CinematicEditorTopComponent cinematicEditorTopComponent) {
        this.cinematicEditorTopComponent = cinematicEditorTopComponent;
    }
}
