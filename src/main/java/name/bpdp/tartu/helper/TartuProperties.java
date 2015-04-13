package name.bpdp.tartu.helper;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;

public class TartuProperties {

	/**
	* Load a Properties object from a file.
	* 
	* @param resource
	* @return
	* @throws Exception
	*/

	public Properties loadProperties(String resource) throws Exception {

		Properties p = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream(resource);

		if (is != null) {
			p.load(is);
		} else {
			throw new FileNotFoundException("Blazegraph property file - RWStore.properties - not found in classpath");
		}

		return p;
	}

}
