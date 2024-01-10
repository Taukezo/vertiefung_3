package org.aulich.wbh.vertiefung_3.programs.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

public class SimpleWriter implements Runnable {

    private static final Logger logger = LogManager.getLogger(SimpleWriter.class);

    private IndexWriter indexWriter = null;

    public SimpleWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    @Override
    public void run() {
        try {
            logger.info("Waiting for shutdown ...");
            synchronized (this) {
                wait();
            }
            logger.info("... shutdown request received.");


            logger.debug("... getting notified and shut down now");
        } catch (InterruptedException e) {
            logger.error("Problem while waiting", e);
        }
    }

    public synchronized void addDocumentSynchronized(Document document) throws IOException {
        logger.debug("Writing document to index...");
        indexWriter.addDocument(document);
    }

    public void shutDown() {
        synchronized (this) {
            notify();
        }
    }

}
