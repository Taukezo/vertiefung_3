package org.aulich.wbh.vertiefung_3.report;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@XStreamAlias("report")
public class ReportModel {
    @XStreamAlias("classname")
    private String className;
    private Date date;
    private int cycles;
    private int threads;
    @XStreamAlias("calculatecyclesfrom")
    private int calculateCyclesFrom;

    private List<ReportCycle> reportCycles = new ArrayList<ReportCycle>();

    @XStreamAlias("durationaverage")
    private BigDecimal durationAverage;
    @XStreamAlias("durationstandarddeviation")
    private BigDecimal durationStandardDeviation;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public BigDecimal getDurationAverage() {
        return durationAverage;
    }

    public void setDurationAverage(BigDecimal durationAverage) {
        this.durationAverage = durationAverage;
    }

    public BigDecimal getDurationStandardDeviation() {
        return durationStandardDeviation;
    }

    public void setDurationStandardDeviation(BigDecimal durationStandardDeviation) {
        this.durationStandardDeviation = durationStandardDeviation;
    }

    public List<ReportCycle> getReportCycles() {
        return reportCycles;
    }

    public void setReportCycles(List<ReportCycle> reportCycles) {
        this.reportCycles = reportCycles;
    }
    public ReportCycle getCycle(int cycleNo){
        for (ReportCycle reportCycle:reportCycles){
            if (cycleNo==reportCycle.getCycleNo()){
                return reportCycle;
            }
        }
        return null;
    }

    public int getCalculateCyclesFrom() {
        return calculateCyclesFrom;
    }

    public void setCalculateCyclesFrom(int calculateCyclesFrom) {
        this.calculateCyclesFrom = calculateCyclesFrom;
    }
}
