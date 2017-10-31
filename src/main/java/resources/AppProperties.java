package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import util.FileUtil;

/**
 * Load and hold the properties as singleton so that unnecessary file IO can be
 * avoided.
 */
// Not in use
public class AppProperties {

	private static Properties prop;

	/**
	 * For the first time, this loads the properties and store it in a field.
	 * For the second time and later, this does nothing other than default
	 * constructor's job.
	 * 
	 * @throws IOException
	 *             If failed to load the properties file
	 */
	public AppProperties() throws Error {
		if (prop != null) {
			return;
		}

		prop = new Properties();

		String basepath = FileUtil.getMainClassPath();

		try (FileInputStream fs = new FileInputStream(basepath + "/config.properties")) {
			prop.load(fs);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	/**
	 * Get the property's value for the key
	 * 
	 * @param key
	 *            property's key
	 * @return property's value
	 */
	public String get(String key) {
		return prop.getProperty(key);
	}
}
