package org.aulich.wbh.vertiefung_3.programs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.programs.simplethread.SimpleIndexer;
import org.aulich.wbh.vertiefung_3.utils.FileFiFoStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunSimpleThread extends BaseProgram {
    private static final Logger logger = LogManager.getLogger(RunSimpleThread.class);

    public static void main(String[] args) {
        logger.info("Program starts ... ");

        RunSimpleThread runSimpleThread = new RunSimpleThread();
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
        IndexWriter writer = this.getIndexWriterNewIndex();

        // Create instance to a document helper
        DocumentHandler documentHandler = new DocumentHandler();

        // Create a queue for all the document to process
        FileFiFoStack myQueue = new FileFiFoStack(new File(this.getCfgM().getRootPath()));

        // Create an instance for all configured Indexers
        List<SimpleIndexer> indexerList = new ArrayList<SimpleIndexer>();
        for (int x = 0; x <= getCfgM().getNumberOfSimpleThreads(); x++) {
            Thread t = new Thread(new SimpleIndexer(myQueue, null));
            t.start();
        }
        /*
        Thread t1 = new Thread(new SimpleIndexer(myQueue, null));
        t1.start();
        Thread t2 = new Thread(new SimpleIndexer(myQueue, null));
        t2.start();
        Thread t3 = new Thread(new SimpleIndexer(myQueue, null));
        t3.start();
        t1.join();
        t2.join();
        t3.join();*/
    }
}
