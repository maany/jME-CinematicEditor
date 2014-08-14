/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.core.layertype.secondary;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.Layer;
import com.jme3.gde.cinematic.core.LayerType;
import com.jme3.gde.cinematic.core.layertype.SpatialLayer;
import com.jme3.gde.cinematic.gui.GuiManager;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 *
 * @author MAYANK
 */
public class ScaleLayer extends Layer {
   // private float xScale;
    public ScaleLayer(String name, SpatialLayer parent) {
        super(name, parent, LayerType.SECONDARY);
        getType().setMetaData(GuiManager.SECONDARY_SCALE);
    }

    @Override
    public Sheet createSheet() {
        Sheet sheet = super.createSheet(); //To change body of generated methods, choose Tools | Templates.
        Sheet.Set spatialSet = getParent().createSheet().get("SpatialLayer");
        sheet.put(spatialSet);

        Sheet.Set scaleSet = Sheet.createPropertiesSet();
        scaleSet.setDisplayName("Scale");
        scaleSet.setName("ScaleLayer");
        try {
            Property xScale = new PropertySupport.Reflection(this, Float.class, "getXScale", "setXScale");
            xScale.setName("X Local Scale");
            scaleSet.put(xScale);

        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        sheet.put(scaleSet);
        return sheet;

    }

    public float getXScale() {
        Spatial spat = getSpatial();
        return spat.getLocalScale().x;
    }

    public void setXScale(java.lang.Float x) {
        
        Spatial spat = getSpatial();
        float y =spat.getLocalScale().y;
        float z = spat.getLocalScale().z;
        spat.setLocalScale(x, y, z);
    }

    private Spatial getSpatial() {
        SpatialLayer parent = (SpatialLayer) getParent();
        Spatial spat = CinematicEditorManager.getInstance().getCurrentDataObject().getLibrary().getSpatialMap().get(parent.getFile());
        if(spat==null)
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find spatial to display secondary data for layer :{0}", parent.getName());
        return spat;
    }
}
