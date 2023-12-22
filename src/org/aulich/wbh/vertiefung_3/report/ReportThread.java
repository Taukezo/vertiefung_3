package org.aulich.wbh.vertiefung_3.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("reportthread")
public class ReportThread {
    private String name;
    @XStreamAlias("numberoffiles")
    private int numberOfFiles;

    public ReportThread(String name, int numberOfFiles) {
        this.name = name;
        this.numberOfFiles = numberOfFiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }
}
