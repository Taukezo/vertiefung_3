package org.aulich.wbh.vertiefung_3.programs;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;
import org.aulich.wbh.vertiefung_3.indexing.ApplicationDocument;
import org.aulich.wbh.vertiefung_3.utils.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * Implementation of a sequential algorithm to index a given bunch of document in a Lucene index.
 *
 * @author Thomas Aulich
 * @version 1.0
 */
public class RunSequential {
    private static final Logger logger = LogManager.getLogger(RunSequential.class);

    public static void main(String[] args) throws IOException {
        logger.info("Program starts ... ");
        ConfigurationModel cfgM = Configuration.getConfiguration().getConfigurationModel();
        String rootPath = Configuration.getConfiguration().getConfigurationModel().getRootPath();
        File f;

        File indexDir = new File(cfgM.getIndexDirectory());
        // Check existence of index-directory
        if (!FileUtils.createOrCleanDirectory(indexDir.toPath().toString())) {
            logger.error("Could not get access to index-directory " + cfgM.getIndexDirectory() + ". Execution will stop now ...");
            return;
        }
        // Create a new index
        Directory index =
                FSDirectory.open(indexDir.toPath());
        IndexWriterConfig indexConfig = new IndexWriterConfig();
        IndexWriter writer = new IndexWriter(index, indexConfig);
        // Measuring time starts here
        Instant start = Instant.now();
        // Go trough the file-system ...
        int i = 0;
        try {
            FileFiFoStack myQue = new FileFiFoStack(new File(rootPath));
            while ((f = myQue.getNext()) != null) {
                ApplicationDocument appDoc = new ApplicationDocument(f);
                Document doc = appDoc.getDocument();
                writer.addDocument(doc);
                i++;
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found or accessible: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.debug("That's it, number of files: " + i);
        writer.close();
        index.close();
        Instant stop = Instant.now();
        logger.info("Program stopped, elapsed time: " + Duration.between(start, stop).toMillis());
    }
}
