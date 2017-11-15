package framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * 
 * 
 * @author 
 */
public class EnvParameters {

	// General Config
	private static final String PROP_FILE = "EnvParameters.properties";

	public static  String WEB_BROWSER_TYPE;

	public static final String DATASHEEET_FILENAME;
	public static final String LOCATOR_FILENAME;
	public static final String XMLLOCATOR_FILENAME; // Added in version 2.0
	public static final String TEST_ROOT_DIR;

	

	public static final String TEST_MANAGEMENT_TOOL;
	
	private static Properties properties = new Properties();

	private static String CUSTOM_PEROPERTY;

	/**
	 * enumeration for Operating System Type
	 *
	 */
	public enum OSType {
		windows,
		mac,
		linux,
		windows64
	}

	static{
		// Loading General Configurations
		TEST_ROOT_DIR = System.getProperty("user.dir");
		CUSTOM_PEROPERTY = System.getProperty("customProperties");

		FileInputStream in = null;
		if (CUSTOM_PEROPERTY != null && !(CUSTOM_PEROPERTY.equalsIgnoreCase(""))) {
			File _customconfig = new File(CUSTOM_PEROPERTY);
			try {
				in = new FileInputStream(_customconfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				
			}
		} else {
			try{
				in = new FileInputStream(TEST_ROOT_DIR + File.separator	+ PROP_FILE);
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			
			}
		}
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}	

		// Load WEB_BROWSER from web.browser property
		if (System.getProperty("web.browser")!= null && !(System.getProperty("web.browser").equalsIgnoreCase(""))){
			WEB_BROWSER_TYPE = System.getProperty("web.browser");
		}else if (properties.getProperty("web.browser") != null && !(properties.getProperty("web.browser").equalsIgnoreCase(""))){
			WEB_BROWSER_TYPE = properties.getProperty("web.browser");
		}else{
			
		}

		

		


		// Load DATASHEEET_FILENAME from datasheet.filename
		if (System.getProperty("datasheet.filename")!= null && !(System.getProperty("datasheet.filename").equals(""))){
			DATASHEEET_FILENAME = System.getProperty("datasheet.filename");
		}else if (properties.getProperty("datasheet.filename") != null && !(properties.getProperty("datasheet.filename").equals(""))){
			DATASHEEET_FILENAME = properties.getProperty("datasheet.filename");
		}
		else{
			DATASHEEET_FILENAME = null;
		}

		// Load LOCATOR_FILENAME from locator.filename
		if (System.getProperty("locator.filename")!= null && !(System.getProperty("locator.filename").equals(""))){
			LOCATOR_FILENAME = System.getProperty("locator.filename");
		}else if (properties.getProperty("locator.filename") != null && !(properties.getProperty("locator.filename").equals(""))){
			LOCATOR_FILENAME = properties.getProperty("locator.filename");
		}
		else{
			LOCATOR_FILENAME = null;
		}

		// Load XMLLOCATOR_FILENAME from XMLlocator.filename
		if (System.getProperty("XMLlocator.filename")!= null && !(System.getProperty("XMLlocator.filename").equals(""))){
			XMLLOCATOR_FILENAME = System.getProperty("XMLlocator.filename");
		}else if (properties.getProperty("XMLlocator.filename") != null && !(properties.getProperty("XMLlocator.filename").equals(""))){
			XMLLOCATOR_FILENAME = properties.getProperty("XMLlocator.filename");
		}
		else{
			XMLLOCATOR_FILENAME = null;
		}

		
		
		if (properties.getProperty("test.management.tool") != null && !(properties.getProperty("test.management.tool").equals(""))){
			TEST_MANAGEMENT_TOOL = properties.getProperty("test.management.tool");
			
		}
		else{
			TEST_MANAGEMENT_TOOL = "";
			
		}
	}

	/**
	 * Gets the Operating System from system property
	 * 
	 * @return OSType enum
	 */
	public static OSType getOSname() {
		String osType = System.getProperty("os.name");
		// Reporter.log("System properties os.name is : "+osType, true);
		return getOSname(osType);
	}

	/**
	 * Gets the Operation system enumeration type from string
	 * an overloaded method
	 * 
	 * @param osType
	 * @return OSType enum
	 */
	public static OSType getOSname(String osType) {
		if (osType.toLowerCase().contains("win"))
			if (System.getenv("PROCESSOR_ARCHITECTURE").contains("86") && System.getenv("PROCESSOR_ARCHITEW6432") != null){
				return OSType.windows;
			}else{
				return OSType.windows;
			}
		else if (osType.toLowerCase().contains("win") && System.getProperty("os.arch").equalsIgnoreCase("x64"))
			return OSType.windows64;
		else if (osType.toLowerCase().contains("mac"))
			return OSType.mac;
		else if (osType.toLowerCase().contains("linux"))
			return OSType.linux;
		else
			return OSType.windows; // default to window
	}

	/**
	 * Description : this method will get the value of a property from the
	 * EnvParameters.properties
	 * 
	 * @param propertyname
	 *            [String]
	 * @return Property value [String]
	 * @Usage GetPropertyValue ("web.browser") will return the
	 *             browsername
	 */
	public static String GetPropertyValue(String propertyname) {
		return properties.getProperty(propertyname);
	}
}