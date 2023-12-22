package org.aulich.wbh.vertiefung_3.programs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;
import org.aulich.wbh.vertiefung_3.report.Report;
import org.aulich.wbh.vertiefung_3.report.ReportCycle;
import org.aulich.wbh.vertiefung_3.report.ReportThread;
import org.aulich.wbh.vertiefung_3.utils.FileUtils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class BaseProgram implements ProgramInterface {
    private static final Logger logger = LogManager.getLogger(BaseProgram.class);
    private ConfigurationModel cfgM;
    private IndexWriter indexWriter;

    private int numberOfFiles;

    private int actualCycle;

    private Report report;

    public BaseProgram() {
        cfgM = Configuration.getConfiguration().getConfigurationModel();
        report = new Report();
        report.getReportModel().setDate(new Date());
        report.getReportModel().setCycles(cfgM.getNumberOfCycles());
        report.getReportModel().setThreads(cfgM.getNumberOfSimpleThreads());
        report.getReportModel().setCalculateCyclesFrom(cfgM.getCalculateCyclesFrom());
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
        int numberOfCycles = this.getCfgM().getNumberOfCycles();
        for (actualCycle = 1; actualCycle <= numberOfCycles; actualCycle++) {
            ReportCycle reportCycle = new ReportCycle();
            reportCycle.setCycleNo(actualCycle);
            report.getReportModel().getReportCycles().add(reportCycle);
            numberOfFiles = 0;
            Instant start = Instant.now();
            doOnce();
            Instant stop = Instant.now();
            logger.info("Cycle " + actualCycle + ", elapsed time: " + Duration.between(start, stop).toMillis());
            reportCycle.setDuration(Duration.between(start, stop).toMillis());
            reportCycle.setNumberOfFiles(numberOfFiles);
        }
        report.save();
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public void setIndexWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public synchronized void addReportThread(ReportThread reportThread) {
        report.getReportModel().getCycle(actualCycle).getReportThreads().add(reportThread);
    }
}
