package org.aulich.wbh.vertiefung_3.programs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.programs.threadpool.CallableIndexer;
import org.aulich.wbh.vertiefung_3.programs.threadpool.CallableIndexerResult;
import org.aulich.wbh.vertiefung_3.report.ReportThread;
import org.aulich.wbh.vertiefung_3.utils.FileFiFoStack;

import java.io.File;
import java.util.concurrent.*;

public class RunThreadPool extends BaseProgram {
    private static final Logger logger = LogManager.getLogger(RunThreadPool.class);

    public RunThreadPool() {
        this.getReport().getReportModel().setClassName(this.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        logger.info("Program starts ... ");

        RunThreadPool runThreadPool = new RunThreadPool();
        try {
            runThreadPool.doAll();
        } catch (Exception e) {
            logger.error(e);
        }
        logger.info("... Program stopped");
    }

    @Override
    public void doOnce() throws Exception {
// Get new IndexWriter and cleanup the filesystem at the configured position
        IndexWriter indexWriter = this.getIndexWriterNewIndex();

        // Create instance to a document helper
        DocumentHandler documentHandler = new DocumentHandler();

        // Create a queue for all the document to process
        FileFiFoStack myQueue = new FileFiFoStack(new File(this.getCfgM().getRootPath()));

        final ExecutorService executorService = Executors.newFixedThreadPool(this.getCfgM().getNumberOfSimpleThreads());
        final CompletionService<CallableIndexerResult> completionService = new ExecutorCompletionService<>(executorService);

        // Submit all tasks to ExecutorService
        int totalTasks = 0;
        File f;
        while ((f = myQueue.getNext()) != null) {
            completionService.submit(new CallableIndexer(f));
            totalTasks++;
        }

        // Collect all results from CompletionService
        for(int i = 0; i < totalTasks; ++i) {

            try {
                final Future<CallableIndexerResult> value = completionService.take();
                System.out.println("received value: " + value.get());
            } catch (ExecutionException e) {
                logger.error("Error while processing task. ", e);
            } catch (InterruptedException e) {
                logger.error("interrupted while waiting for result", e);
            }
        }

        this.setNumberOfFiles(totalTasks);
        this.getReport().getReportModel().setThreads(this.getCfgM().getNumberOfSimpleThreads());
        this.addReportThread(new ReportThread(Thread.currentThread().getName(), 1));
    }
}
