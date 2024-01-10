package org.aulich.wbh.vertiefung_3.programs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.aulich.wbh.vertiefung_3.programs.runnable.SimpleIndexer;
import org.aulich.wbh.vertiefung_3.utils.FileFiFoStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunSimpleThread extends BaseProgram {
    private static final Logger logger = LogManager.getLogger(RunSimpleThread.class);
    List<Thread> threadList = new ArrayList<Thread>();

    public RunSimpleThread() {
        this.getReport().getReportModel().setClassName(this.getClass().getSimpleName());
    }

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
        IndexWriter indexWriter = this.getIndexWriterNewIndex();

        // Create a queue for all the document to process
        FileFiFoStack myQueue = new FileFiFoStack(new File(this.getCfgM().getRootPath()));

        // Create an instance for all configured Indexers and start them
        for (int x = 0; x < getCfgM().getNumberOfSimpleThreads(); x++) {
            Thread t = new Thread(new SimpleIndexer(myQueue, indexWriter, this));
            threadList.add(t);
            t.start();
        }

        // Wait, until all threads are completed
        for(Thread t : threadList) {
            t.join();
        }
        //this.addReportThread(1, new ReportThread());
        this.setNumberOfFiles(myQueue.getNumberOfFiles());
        indexWriter.close();
    }
}
