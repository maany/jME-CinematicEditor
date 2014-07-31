/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author MAYANK
 */
public class CinematicSaver implements SaveCookie{

    private CinematicDataObject data;

    public CinematicSaver(CinematicDataObject data) {
        this.data = data;
    }
    
    @Override
    public void save() throws IOException {
        FileOutputStream fout = null;
        ObjectOutputStream out = null;
        try {
            fout = new FileOutputStream(FileUtil.toFile(data.getPrimaryFile()));
            out = new ObjectOutputStream(fout);
            data.test = "Just before writing.FAILED";
            out.writeObject(data);
            data.test = "just after writing.Pass if no exception";
            data.setModified(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
            fout.close();
            
        }
    }
    
}
