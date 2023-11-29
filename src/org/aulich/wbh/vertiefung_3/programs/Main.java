package org.aulich.wbh.vertiefung_3.programs;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;
import org.aulich.wbh.vertiefung_3.utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * Header fehlt...
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Program starts ... ");
        Instant start = Instant.now();
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

        // Go trough the file-system ...
        int i = 0;
        try {
            FileFiFoStack myQue = new FileFiFoStack(new File(rootPath));
            while ((f = myQue.getNext()) != null) {
                Document doc = new Document();
                doc.add(new StringField(FieldName.PATH, f.getAbsolutePath(), Field.Store.YES));
                doc.add(new StringField(FieldName.NAME, f.getName(), Field.Store.YES));
                doc.add(new LongPoint(FieldName.SIZE, f.length()));
                doc.add(new StringField(FieldName.SIZE, String.valueOf(f.length()), Field.Store.YES));
                doc.add(new StringField(FieldName.TYPE, FileUtils.getExtensionByStringHandling(f), Field.Store.YES));
                // Fulltext-Index & meta-data
                TikaDocument tikaDoc = new TikaDocument(f);
                if (tikaDoc.isAvailable()) {
                    doc.add(new TextField(FieldName.FULLTEXT, tikaDoc.getContentReader()));
                    for (MetaData m : tikaDoc.getMetaData()) {
                        if (m.value() != null && !"".equals(m.value())) {
                            doc.add(new StringField("tikameta_" + m.key(), m.value(), Field.Store.YES));
                        }
                    }
                }

                writer.addDocument(doc);
                i++;
                if (i > 100000) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found or accessible: " + e.getMessage());
        } catch (TikaException e) {
            throw new RuntimeException(e);
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
