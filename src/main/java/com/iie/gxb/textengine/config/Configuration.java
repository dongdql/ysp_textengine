/**
 * 
 */
package com.iie.gxb.textengine.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 管理配置文件
 * 
 *
 */
public class Configuration {
	static Properties props = null;
	public static final String file = "conf/config.properties";
	public static final String DEFAULT_CONF_FILE = System.getProperty("user.dir") + "/config.properties";

	public Configuration() {
		props = new Properties();
		try {
			FileInputStream confFile = new FileInputStream(new File(DEFAULT_CONF_FILE));
			try {
				props.load(confFile);
				confFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getValue(String key) {
		return props.getProperty(key);
	}
	public void setDeadlineValue(String value) {
		try {

			props.setProperty("DEADLINE", value);

			FileOutputStream fos = new FileOutputStream(DEFAULT_CONF_FILE);

			props.store(fos, null);

			fos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	/**
	 * clear properties.
	 */
	public void clear() {
		props.clear();
	}

	/**
	 * @return
	 */
	private static Configuration config = null;

	public static Configuration getInstance() {
		if (config == null) {
			config = new Configuration();
		}
		return config;
	}
}
