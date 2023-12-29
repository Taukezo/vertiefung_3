package org.aulich.wbh.vertiefung_3.configuration;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("configuration")
public class ConfigurationModel {
    @XStreamAlias("rootpath")
    private String rootPath = "";
    @XStreamAlias("indexdirectory")
    private String indexDirectory = "";
    @XStreamAlias("numberofcycles")
    private int numberOfCycles = 0;
    @XStreamAlias("numberofsimplethreads")
    private int numberOfSimpleThreads = 0;
    @XStreamAlias("calculatecyclesfrom")
    private int calculateCyclesFrom = 1;


    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }

    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    public void setNumberOfCycles(int numberOfCycles) {
        this.numberOfCycles = numberOfCycles;
    }

    public int getNumberOfSimpleThreads() {
        return numberOfSimpleThreads;
    }

    public void setNumberOfSimpleThreads(int numberOfSimpleThreads) {
        this.numberOfSimpleThreads = numberOfSimpleThreads;
    }

    public int getCalculateCyclesFrom() {
        return calculateCyclesFrom;
    }

    public void setCalculateCyclesFrom(int calculateCyclesFrom) {
        this.calculateCyclesFrom = calculateCyclesFrom;
    }
}
