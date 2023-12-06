package org.aulich.wbh.vertiefung_3.programs.simplethread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.utils.FileFiFoStack;

import java.io.File;

/**
 *
 */
public class SimpleIndexer implements Runnable {
    private static final Logger logger = LogManager.getLogger(SimpleIndexer.class);
    private DocumentHandler documentHandler;
    private IndexWriter indexWriter;
    private FileFiFoStack fileFiFoStack;
    private boolean shutDown = false;

    public SimpleIndexer(FileFiFoStack fileFiFoStack, IndexWriter indexWriter) {
        this.fileFiFoStack = fileFiFoStack;
        this.indexWriter = indexWriter;
        documentHandler = new DocumentHandler();
    }

    public void shutdown() {
        shutDown = true;
    }

    @Override
    public void run() {
        try {
            int i=0;
            File file = fileFiFoStack.synchronizedGetNext();
            while (file != null) {
                logger.debug("Performing file " + file.getName());
                file = fileFiFoStack.synchronizedGetNext();
                i++;
            }
            logger.info("Number of files processed " + i);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //try {
        //    waitForShutdown();
        //} catch (InterruptedException e) {
        //    logger.error("Shutdown exception", e);
        //}
    }
    private synchronized void  waitForShutdown() throws InterruptedException {
        wait();
    }

}
