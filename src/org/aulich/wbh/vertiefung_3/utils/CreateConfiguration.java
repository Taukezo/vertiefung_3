package org.aulich.wbh.vertiefung_3.utils;

import org.aulich.wbh.vertiefung_3.configuration.Configuration;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;

/**
 * This is (more or less) just a helper to create an example configuration file.
 *
 * @author Thomas Aulich
 */
public class CreateConfiguration {
    public static void main(String[] args) {
        Configuration config = Configuration.getConfiguration();
        ConfigurationModel cfgModel = new ConfigurationModel();
        cfgModel.setRootPath("C:\\WBH-ExportDokumente\\AULARC_Scanning");
        cfgModel.setIndexDirectory("C:\\WBH-ExportDokumente\\index");
        config.setConfigurationModel(cfgModel);
        config.save();
    }
}
