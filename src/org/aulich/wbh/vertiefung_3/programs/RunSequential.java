package org.aulich.wbh.vertiefung_3.programs;

import org.apache.lucene.index.IndexWriter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.report.ReportThread;
import org.aulich.wbh.vertiefung_3.utils.*;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of a sequential algorithm to index a given bunch of document in a Lucene index.
 *
 * @author Thomas Aulich
 * @version 1.0
 */
public class RunSequential extends BaseProgram {
    private static final Logger logger = LogManager.getLogger(RunSequential.class);


    public static void main(String[] args) throws IOException {
        logger.info("Program starts ... ");

        RunSequential runSequential = new RunSequential();
        try {

            runSequential.doAll();
        } catch (Exception e) {
            logger.error(e);
        }

        logger.info("... Program stopped");
    }

    public RunSequential() {
        this.getReport().getReportModel().setClassName(this.getClass().getSimpleName());
    }

    @Override
    public void doOnce() throws Exception {
        // Get new IndexWriter and cleanup the filesystem at the configured position
        IndexWriter indexWriter = this.getIndexWriterNewIndex();

        // Create instance to a document helper
        DocumentHandler documentHandler = new DocumentHandler();

        // Create a queue for all the document to process
        FileFiFoStack myQueue = new FileFiFoStack(new File(this.getCfgM().getRootPath()));

        // Perform the queue
        int i = 0;
        File f;
        while ((f = myQueue.getNext()) != null) {
            indexWriter.addDocument(documentHandler.getDocument(f));
            i++;
        }
        logger.debug("That's it, number of files: " + i);
        this.setNumberOfFiles(i);
        this.getReport().getReportModel().setThreads(1);
        this.addReportThread(new ReportThread(Thread.currentThread().getName(), i));
        indexWriter.close();
    }
}
