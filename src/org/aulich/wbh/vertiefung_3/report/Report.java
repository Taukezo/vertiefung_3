package org.aulich.wbh.vertiefung_3.report;

import com.thoughtworks.xstream.XStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {
    private static final Logger logger = LogManager.getLogger(Report.class);
    private static final String fileNamePattern = "yyyy-MM-dd.HH-mm-ss";

    private ReportModel reportModel;

    public Report() {
        reportModel = new ReportModel();

    }

    public boolean save() {
        // Calculate results
        doCalculations();

        // Physically save the report to disk
        Date date = reportModel.getDate();
        DateFormat df = new SimpleDateFormat(fileNamePattern);
        String fileName = reportModel.getSystemName() + "-" + reportModel.getClassName() + "-" + df.format(date) + ".xml";
        File configurationFile =
                new File(Configuration.getConfiguration().getConfigurationModel().getReportPath()
                        + File.separator + fileName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Report.class);
        OutputStream outputStream = null;
        Writer writer = null;
        try {
            outputStream = new FileOutputStream(configurationFile);
            writer = new OutputStreamWriter(outputStream,
                    StandardCharsets.UTF_8);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"reports.xsl\"?>\n");
            writer.flush();
            xStream.toXML(reportModel, outputStream);
            writer.close();
            outputStream.close();
        } catch (Exception exp) {
            return false;
        }
        writer = null;
        outputStream = null;
        return true;
    }

    private void doCalculations() {
        double durationAverage = 0.0;
        double fileAverage = 0.0;
        BigDecimal durationStandardDeviation = new BigDecimal("0.0");
        BigDecimal fileStandardDeviation = new BigDecimal("0.0");
        // Calculate the average
        int cycleSum = 0;
        int threadSum = 0;
        int calculateCyclesFrom = Configuration.getConfiguration().getConfigurationModel().getCalculateCyclesFrom();
        long durationSum = 0;
        int fileSum = 0;
        for (ReportCycle reportCycle : reportModel.getReportCycles()) {
            if (reportCycle.getCycleNo() >= calculateCyclesFrom) {
                durationSum += reportCycle.getDuration();
                cycleSum++;
                for (ReportThread reportThread : reportCycle.getReportThreads()) {
                    threadSum++;
                    fileSum += reportThread.getNumberOfFiles();
                    ;
                }
            }
        }
        if (cycleSum > 0) {
            durationAverage = (double) durationSum / cycleSum;
            fileAverage = (double) fileSum / threadSum;
            double durationHelper = 0.0;
            double fileHelper = 0.0;
            // Calculate the deviation on duration
            for (ReportCycle reportCycle : reportModel.getReportCycles()) {
                if (reportCycle.getCycleNo() >= calculateCyclesFrom) {
                    durationHelper += Math.pow((durationAverage - reportCycle.getDuration()), 2);
                    for (ReportThread reportThread : reportCycle.getReportThreads()) {
                        fileHelper += Math.pow((fileAverage - reportThread.getNumberOfFiles()), 2);
                    }
                }
            }
            durationHelper = (double) Math.sqrt(durationHelper / cycleSum);
            durationStandardDeviation = new BigDecimal(durationHelper).setScale(1, RoundingMode.HALF_UP);
            fileHelper = (double) Math.sqrt(fileHelper / threadSum);
            fileStandardDeviation = new BigDecimal(fileHelper).setScale(1, RoundingMode.HALF_UP);
        }
        reportModel.setDurationAverage(new BigDecimal(durationAverage).setScale(1, RoundingMode.HALF_UP));
        reportModel.setThreadFilesAverage(new BigDecimal(fileAverage).setScale(1, RoundingMode.HALF_UP));
        reportModel.setDurationStandardDeviation(durationStandardDeviation);
        reportModel.setThreadFilesStandardDeviation(fileStandardDeviation);

    }

    public ReportModel getReportModel() {
        return reportModel;
    }

    public void setReportModel(ReportModel reportModel) {
        this.reportModel = reportModel;
    }
}
