/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.filetype;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author MAYANK
 */
public class CinematicSaver implements SaveCookie {

    private CinematicDataObject data;

    public CinematicSaver(CinematicDataObject data) {
        this.data = data;
    }

    @Override
    public void save() throws IOException {
        Runnable saveThread = new Runnable() {
            ProgressHandle handle;
            @Override
            public void run() {
                handle = ProgressHandleFactory.createHandle("saving cinematic : " + data.getName());
                handle.start();
                FileOutputStream fout = null;
                ObjectOutputStream out = null;
                try {
                    fout = new FileOutputStream(FileUtil.toFile(data.getPrimaryFile()));
                    out = new ObjectOutputStream(fout);
                    out.writeObject(data);
                    data.setModified(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        out.close();
                        fout.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    handle.finish();
                }
            }
        };
        new Thread(saveThread).start();
    }
}
