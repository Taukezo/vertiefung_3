import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.aulich.wbh.vertiefung_3.FileFiFoStack;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;
import org.aulich.wbh.vertiefung_3.utils.FileUtils;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.debug("Debug log message ");
        ConfigurationModel cfgM = Configuration.getConfiguration().getConfigurationModel();
        String rootPath = Configuration.getConfiguration().getConfigurationModel().getRootPath();
        File f;

        File indexDir = new File(cfgM.getIndexDirectory());
        // Check existance of index-directory
        if (!FileUtils.createOrCleanDirectory(indexDir.toPath().toString())) {
            logger.error("Could not get access to index-directoty " + cfgM.getIndexDirectory() + ". Execution will stop now ...");
            return;
        }

        // Create a new index
        Directory indexDirectory =
                FSDirectory.open(indexDir.toPath());

        // Go trough the file-system ...
        try {
            FileFiFoStack myQue = new FileFiFoStack(new File(rootPath));
            int i = 0;
            while ((f = myQue.getNext()) != null) {
                //logger.debug(f.getAbsolutePath() + " - " + f.length());
                i++;
            }
            logger.debug("That's it, number of files: " + i);
        } catch (FileNotFoundException e) {
            logger.error("So geht das nicht: " + e.getMessage());
        }

    }
}