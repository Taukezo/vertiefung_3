package org.aulich.wbh.vertiefung_3.programs.simplethread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;
import org.aulich.wbh.vertiefung_3.programs.BaseProgram;
import org.aulich.wbh.vertiefung_3.report.ReportThread;
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
    private BaseProgram program;

    public SimpleIndexerExtended(FileFiFoStack fileFiFoStack, SimpleWriter simpleWriter, BaseProgram program) {
        this.fileFiFoStack = fileFiFoStack;
        this.simpleWriter = simpleWriter;
        documentHandler = new DocumentHandler();
        this.program = program;
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
            program.addReportThread(new ReportThread(Thread.currentThread().getName(), i));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
