package org.aulich.wbh.vertiefung_3.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("reportcycle")
public class ReportCycle {
    private long duration;
    @XStreamAlias("cycleNo")
    private int cycleNo;
    @XStreamAlias("numberoffiles")
    private int numberOfFiles;
    @XStreamAlias("reportthreads")
    private List<ReportThread> reportThreads = new ArrayList<ReportThread>();

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(int cycleNo) {
        this.cycleNo = cycleNo;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public void incrementNumberOfFiles() {
        numberOfFiles += 1;
    }

    public List<ReportThread> getReportThreads() {
        return reportThreads;
    }

    public void setReportThreads(List<ReportThread> reportThreads) {
        this.reportThreads = reportThreads;
    }
}
