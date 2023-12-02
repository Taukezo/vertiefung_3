package org.aulich.wbh.vertiefung_3.programs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;
import org.aulich.wbh.vertiefung_3.utils.FileUtils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class BaseProgram implements ProgramInterface{
    private static final Logger logger = LogManager.getLogger(BaseProgram.class);
    private ConfigurationModel cfgM;
    private IndexWriter indexWriter;

    public BaseProgram() {
        cfgM = Configuration.getConfiguration().getConfigurationModel();
    }

    public IndexWriter getIndexWriterNewIndex() throws Exception {
        File indexDir = new File(cfgM.getIndexDirectory());
        // Check existence of index-directory
        if (!FileUtils.createOrCleanDirectory(indexDir.toPath().toString())) {
            logger.error("Could not get access to index-directory " + cfgM.getIndexDirectory() + ". Execution will stop now ...");
            throw new Exception("Could not get access to index-directory " + cfgM.getIndexDirectory());
        }
        // Create a new index
        Directory index =
                FSDirectory.open(indexDir.toPath());
        IndexWriterConfig indexConfig = new IndexWriterConfig();
        return new IndexWriter(index, indexConfig);
    }

    public ConfigurationModel getCfgM() {
        return cfgM;
    }

    public void setCfgM(ConfigurationModel cfgM) {
        this.cfgM = cfgM;
    }

    @Override
    public void doOnce() throws Exception {
        // TODO Has to be overridden in subclasses ...
    }

    @Override
    public void doAll() throws Exception {
        int numberofcycles = this.getCfgM().getNumberOfCycles();
        for (int i = 1;i<=numberofcycles;i++) {
            Instant start = Instant.now();
            doOnce();
            Instant stop = Instant.now();
            logger.info("Cycle " + i + ", elapsed time: " + Duration.between(start, stop).toMillis());
        }
    }
}
