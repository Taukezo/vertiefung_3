package org.aulich.wbh.vertiefung_3.programs.simplethread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.programs.BaseProgram;
import org.aulich.wbh.vertiefung_3.report.ReportThread;
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

    private BaseProgram program;

    public SimpleIndexer(FileFiFoStack fileFiFoStack, IndexWriter indexWriter, BaseProgram program) {
        this.fileFiFoStack = fileFiFoStack;
        this.indexWriter = indexWriter;
        documentHandler = new DocumentHandler();
        this.program = program;
    }

    @Override
    public void run() {
        logger.info("run()");
        try {
            int i = 0;
            File file = fileFiFoStack.synchronizedGetNext();
            while (file != null) {
                logger.debug("Performing file " + file.getName());
                indexWriter.addDocument(documentHandler.getDocument(file));
                file = fileFiFoStack.synchronizedGetNext();
                i++;
            }
            logger.info("Number of files processed " + i);
            program.addReportThread(new ReportThread(Thread.currentThread().getName(), i));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
