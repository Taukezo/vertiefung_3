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
}
