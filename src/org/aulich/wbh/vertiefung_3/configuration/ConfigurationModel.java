package org.aulich.wbh.vertiefung_3.configuration;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("configuration")
public class ConfigurationModel {
    @XStreamAlias("rootpath")
    private String rootPath = "";
    @XStreamAlias("indexdirectory")
    private String indexDirectory = "";


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
}
