package org.aulich.wbh.vertiefung_3.programs.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aulich.wbh.vertiefung_3.indexing.DocumentHandler;

import java.io.File;
import java.util.concurrent.Callable;

public class CallableIndexer implements Callable<CallableIndexerResult> {
    private static final Logger logger = LogManager.getLogger(CallableIndexer.class);
    private final DocumentHandler documentHandler;
    private final File file;

    public CallableIndexer(File file) {
        documentHandler = new DocumentHandler();
        this.file = file;
    }

    @Override
    public CallableIndexerResult call() throws Exception {
        return (new CallableIndexerResult("xxx", documentHandler.getDocument(file)));
    }
}
