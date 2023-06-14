package org.aulich.wbh.vertiefung_3.configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Global singleton configuration bean.
 *
 * @author Thomas Aulich
 */
public class Configuration {
    public static final String XML_CONFIGURATION_FILENAME = "Configuration.xml";
    private static Configuration instance;
    private ConfigurationModel configurationModel = new ConfigurationModel();
    private long lastLoaded;

    /**
     * Private constructor follows the singleton-pattern.
     */
    private Configuration() {
        reloadConfiguration();
    }

    /**
     * Following the singleton pattern this method provides the object.
     *
     * @return INSTANCE
     */
    public static synchronized Configuration getConfiguration() {
        if (instance == null) {
            instance = new Configuration();
        }
        if (instance != null) {
            instance.reloadConfiguration();
        }
        return instance;
    }

    private void reloadConfiguration() {
        File configurationFile = getConfigurationFile();
        if (configurationFile == null || !configurationFile.exists()
                || !configurationFile.isFile()) {
            return;
        }
        if (configurationFile.lastModified() > lastLoaded) {
            XStream xStream = new XStream();
            xStream.addPermission(NoTypePermission.NONE);
            xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
            xStream.allowTypeHierarchy(Collection.class);
            xStream.allowTypesByWildcard(
                    new String[] {"org.aulich.wbh.vertiefung_3.configuration.**"});
            xStream.processAnnotations(ConfigurationModel.class);
            configurationModel =
                    (ConfigurationModel) xStream.fromXML(configurationFile);
            if (configurationModel == null) {
                configurationModel = new ConfigurationModel();
            } else {
                lastLoaded = configurationFile.lastModified();
            }
        }
    }

    private File getConfigurationFile() {
        File configurationFile = new File(XML_CONFIGURATION_FILENAME);
        return configurationFile;
    }

    /**
     * Save configuration on disk.
     *
     * @return did the save succeed or not
     */
    public boolean save() {
        File configurationFile = new File(XML_CONFIGURATION_FILENAME);
        XStream xStream = new XStream();
        xStream.processAnnotations(Configuration.class);
        OutputStream outputStream = null;
        Writer writer = null;
        try {
            outputStream = new FileOutputStream(configurationFile);
            writer = new OutputStreamWriter(outputStream,
                    Charset.forName("UTF-8"));
            xStream.toXML(configurationModel, outputStream);
            writer.close();
            outputStream.close();
        } catch (Exception exp) {
            return false;
        }
        writer = null;
        outputStream = null;
        return true;
    }

    public ConfigurationModel getConfigurationModel() {
        return configurationModel;
    }

    public void setConfigurationModel(ConfigurationModel configurationModel) {
        this.configurationModel = configurationModel;
    }
}
