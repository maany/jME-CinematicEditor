/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.Queue;

/**
 *
 * @author MAYANK
 */
public final class SpatialWrapper extends Spatial{
    private CinematicDataObject cinematicDataObject;

    private SpatialWrapper(CinematicDataObject cinematic) {
        this.cinematicDataObject = cinematic;
    }
    public static Spatial packCinematicForExport(CinematicDataObject cinematic){
        Spatial cinematicSpatial = new SpatialWrapper(cinematic);
        return cinematicSpatial;
        
    }
    public static CinematicDataObject unPackCinematicForImport(SpatialWrapper wrapper) {
        return wrapper.cinematicDataObject;
    }

    @Override
    public void updateModelBound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setModelBound(BoundingVolume bv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getVertexCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTriangleCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Spatial deepClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void depthFirstTraversal(SceneGraphVisitor sgv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void breadthFirstTraversal(SceneGraphVisitor sgv, Queue<Spatial> queue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int collideWith(Collidable cldbl, CollisionResults cr) throws UnsupportedCollisionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
