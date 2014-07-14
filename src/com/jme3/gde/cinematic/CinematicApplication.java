/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.gde.cinematic.core.CinematicClip;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.core.scene.PreviewRequest;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneListener;
import com.jme3.gde.core.scene.SceneRequest;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;


/**
 * Acts as a wrapper for interactions of Cinematic Editor with the {@link SceneApplication}.
 * @author MAYANK
 */
public class CinematicApplication  implements SceneListener{
    private SceneRequest cinematicRequest = null;
    private static CinematicApplication cinematicApplication = null;
    public static CinematicApplication getInstance(){
        if(cinematicApplication ==null)
            cinematicApplication = new CinematicApplication();
        return cinematicApplication;
    }
    private CinematicApplication(){
        
    }
    public void launch() {
        CinematicClip cinematicClip = CinematicEditorManager.getInstance().getCurrentClip();
        Layer root = cinematicClip.getRoot();
        SceneApplication.getApplication().addSceneListener(this);
        /*
         * Cleanup Scene
         */
        
        /*
         * Create Scene Request
         */
       
        // System.out.println( "OPEN PROJECTS ***** : "+ OpenProjects.PROPERTY_OPEN_PROJECTS);
       // SimpleApplication currentApp = (SimpleApplication)Lookup.getDefault().lookup(Application.class);
       // System.out.println("Found application with models : " + currentApp.getRootNode().getName());
        
        //cinematicRequest = new SceneRequest(cinematicClip,assetManager);
        /*
         * SceneApplication.getApplication.openScene(request goes here);
         */
        
        loadAssets(root);
    }

    private void loadAssets(Layer root) {
        for(Layer layer:root.getVisibleDescendants())
        {
            System.out.println("Loading into viewer : " + layer.getName());
        }
        
    }

    @Override
    public void sceneOpened(SceneRequest sr) {
        System.out.println("Cinematic Scene Opened");
    }

    @Override
    public void sceneClosed(SceneRequest sr) {
        System.out.println("Cinematic Scene Closed");
    }

   @Override
    public void previewCreated(PreviewRequest pr) {
        System.out.println("Cinematic Scene Preview Created");
    }
}
