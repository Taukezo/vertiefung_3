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
        String fileName = reportModel.getClassName() + "-" + df.format(date) + ".xml";
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
        BigDecimal durationStandardDeviation = new BigDecimal("0.0");
        // Calculate the average
        int i = 0;
        int calculateCyclesFrom = Configuration.getConfiguration().getConfigurationModel().getCalculateCyclesFrom();
        long durationSum = 0;
        for (ReportCycle reportCycle : reportModel.getReportCycles()) {
            if (reportCycle.getCycleNo() >= calculateCyclesFrom) {
                durationSum += reportCycle.getDuration();
                i++;
            }
        }
        if (i > 0) {
            durationAverage = (double) durationSum / i;
            double helper = 0.0;
            // Caclulate the deviation
            for (ReportCycle reportCycle : reportModel.getReportCycles()) {
                if (reportCycle.getCycleNo() >= calculateCyclesFrom) {
                    helper += Math.pow((durationAverage - reportCycle.getDuration()), 2);
                }
            }
            helper = (double) Math.sqrt(helper / i);
            durationStandardDeviation = new BigDecimal(helper).setScale(1, RoundingMode.HALF_UP);
        }
        reportModel.setDurationAverage(new BigDecimal(durationAverage).setScale(1, RoundingMode.HALF_UP));
        reportModel.setDurationStandardDeviation(durationStandardDeviation);

    }

    public ReportModel getReportModel() {
        return reportModel;
    }

    public void setReportModel(ReportModel reportModel) {
        this.reportModel = reportModel;
    }
}
