package org.aulich.wbh.vertiefung_3.programs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.aulich.wbh.vertiefung_3.programs.simplethread.SimpleIndexer;
import org.aulich.wbh.vertiefung_3.programs.simplethread.SimpleIndexerExtended;
import org.aulich.wbh.vertiefung_3.programs.simplethread.SimpleWriter;
import org.aulich.wbh.vertiefung_3.utils.FileFiFoStack;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunSimpleThreadExtended extends BaseProgram {
    private static final Logger logger = LogManager.getLogger(RunSimpleThreadExtended.class);
    List<Thread> threadList = new ArrayList<Thread>();
    public RunSimpleThreadExtended() {
        this.getReport().getReportModel().setClassName(this.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        logger.info("Program starts ... ");

        RunSimpleThreadExtended runSimpleThread = new RunSimpleThreadExtended();
        try {
            runSimpleThread.doAll();
        } catch (Exception e) {
            logger.error(e);
        }
        logger.info("... Program stopped");
    }

    @Override
    public void doOnce() throws Exception {
        // Get new IndexWriter and cleanup the filesystem at the configured position
        IndexWriter indexWriter = this.getIndexWriterNewIndex();

        // Start a new thread with this IndexWriter
        SimpleWriter simpleWriter = new SimpleWriter(indexWriter);
        Thread writerThread = new Thread(simpleWriter);
        writerThread.start();

        // Create a queue for all the document to process
        FileFiFoStack myQueue = new FileFiFoStack(new File(this.getCfgM().getRootPath()));

        // Create an instance for all configured Indexers and start them
        for (int x = 0; x < getCfgM().getNumberOfSimpleThreads(); x++) {
            Thread t = new Thread(new SimpleIndexerExtended(myQueue, simpleWriter, this));
            threadList.add(t);
            t.start();
        }

        // Wait, until all indexing-threads are completed
        for(Thread t : threadList) {
            t.join();
        }

        // Notify writer thread to stop
        indexWriter.close();
        logger.debug("Try to notify indexer thread");
        simpleWriter.shutDown();
        logger.debug("Notification succeeded");
        this.setNumberOfFiles(myQueue.getNumberOfFiles());

    }
}
