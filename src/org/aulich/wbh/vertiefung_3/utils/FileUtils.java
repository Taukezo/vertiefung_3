package org.aulich.wbh.vertiefung_3.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.nio.file.Files;

public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    public static boolean createOrCleanDirectory(String path) {
        logger.debug("Path for index is '" + path + "'");
        File filePath = new File(path);
        if (filePath.exists()) {
            deleteDir(filePath);
            logger.debug("Directory '" + path + "' successfully removed");
        }
        filePath.mkdir();
        if (filePath.exists()) {
            logger.debug("Directory '" + path + "' successfully created");
            return true;
        } else {
            logger.warn("Could not create directory '" + path + "'");
            return false;
        }
    }

    static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    public static String getExtensionByStringHandling(File file) {
        String filename = file.getName().toLowerCase();
        if (filename.contains(".") && filename.lastIndexOf(".") != filename.length()) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return ".";
    }
}
