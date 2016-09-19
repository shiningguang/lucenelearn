package org.itat.index;

import java.io.File;

/**
 *
 * @author Peter Karich, info@jetsli.de
 */
public class Helper {

    public static void deleteDir(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteDir(f);
            }
        }

        file.delete();
    }
}