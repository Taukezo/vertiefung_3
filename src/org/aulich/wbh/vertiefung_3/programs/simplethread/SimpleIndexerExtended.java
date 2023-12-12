package org.aulich.wbh.vertiefung_3.programs.simplethread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.utils.FileFiFoStack;

import java.io.File;

/**
 *
 */
public class SimpleIndexerExtended implements Runnable {
    private static final Logger logger = LogManager.getLogger(SimpleIndexerExtended.class);
    private DocumentHandler documentHandler = null;
    private SimpleWriter simpleWriter = null;
    private FileFiFoStack fileFiFoStack = null;
    private boolean shutDown = false;

    public SimpleIndexerExtended(FileFiFoStack fileFiFoStack, SimpleWriter simpleWriter) {
        this.fileFiFoStack = fileFiFoStack;
        this.simpleWriter = simpleWriter;
        documentHandler = new DocumentHandler();
    }

    public void shutdown() {
        shutDown = true;
    }

    @Override
    public void run() {
        logger.info("run()");
        try {
            int i=0;
            File file = fileFiFoStack.synchronizedGetNext();
            while (file != null) {
                logger.debug("Performing file " + file.getName());
                simpleWriter.addDocumentSynchronized(documentHandler.getDocument(file));
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
