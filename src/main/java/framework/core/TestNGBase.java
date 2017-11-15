package framework.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;
import framework.core.EnvParameters.OSType;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
//import testlink.api.java.client.TestLinkAPIClient;
//import testlink.api.java.client.TestLinkAPIException;
//import testlink.api.java.client.TestLinkAPIResults;
import testlink.api.java.client.TestLinkAPIResults;



/**
 * TestNGBase serves as the base class for all Test Classes
 * providing generic setup and tear down functionalities
 * 
 * @author
 * 
 */
public class TestNGBase  {

	protected WebDriver driver;
	public static String TEST_MANAGEMENT_TOOL_URL;
	public static String TEST_MANAGEMENT_TOOL_DEV_KEY;
	public static String TEST_MANAGEMENT_TOOL_PROJ_NAME;
	public static String TEST_MANAGEMENT_TOOL_PLAN_NAME;
	public static String TEST_MANAGEMENT_TOOL_BUILD_NAME;
    public static  String TEST_MANAGEMENT_TOOL_DLL_PATH;
	
	
	public static  String TEST_MANAGEMENT_TOOL_USERNAME;
	public static  String TEST_MANAGEMENT_TOOL_PWD;
	
	public static  String TEST_MANAGEMENT_TOOL_AUTHKEY1;
	
	public static  String TEST_MANAGEMENT_TOOL_AUTHKEY2;
	
	public static  String TEST_MANAGEMENT_TOOL_DIR_PATH; 
	public static  String EXCEL_TEST_DETAILS_PATH; 
	public static String TEST_NAME;
	public  Boolean updateInTestLink = false;
	



	public SoftAssert getSoftAssert(){
		return new SoftAssert();
	}
	
	

	

	public void setTestLinkParams(String TOOL_URL, String TOOL_DEV_KEY, String TOOL_PROJ_NAME, String TOOL_PLAN_NAME,
			String TOOL_BUILD_NAME,String TESTNAME) {
		
		TEST_MANAGEMENT_TOOL_URL = TOOL_URL;
		TEST_MANAGEMENT_TOOL_DEV_KEY = TOOL_DEV_KEY;
		TEST_MANAGEMENT_TOOL_PROJ_NAME  = TOOL_PROJ_NAME;
		TEST_MANAGEMENT_TOOL_PLAN_NAME = TOOL_PROJ_NAME;
		TEST_MANAGEMENT_TOOL_PLAN_NAME = TOOL_PLAN_NAME;
		TEST_MANAGEMENT_TOOL_BUILD_NAME = TOOL_BUILD_NAME;
		TEST_NAME = TESTNAME;
	}
	
	public void setALMParams(String TOOL_URL, String PROJECT_NAME,String JACOB_DLL_PATH, String USER_NAME,
			String PWD,String AUTHKEY1,String AUTHKEY2,String DIR_PATH,String TEST_EXCEL) {
		
		TEST_MANAGEMENT_TOOL_URL = TOOL_URL;
		TEST_MANAGEMENT_TOOL_PROJ_NAME = PROJECT_NAME;
		TEST_MANAGEMENT_TOOL_DLL_PATH = JACOB_DLL_PATH;
		TEST_MANAGEMENT_TOOL_USERNAME  = USER_NAME;
		TEST_MANAGEMENT_TOOL_PWD = PWD;
		TEST_MANAGEMENT_TOOL_AUTHKEY1 = AUTHKEY1;
		TEST_MANAGEMENT_TOOL_AUTHKEY2 = AUTHKEY2;
		TEST_MANAGEMENT_TOOL_DIR_PATH = DIR_PATH;
		EXCEL_TEST_DETAILS_PATH = TEST_EXCEL;
	}
	
	/**
	 * Runs before every test method to setup the driver instance
	 * based on the browser type parameter
	 * @param browser
	 */
	@Parameters({ "Browser","BrowserPlatform","BrowserVersion" })
	@BeforeMethod(alwaysRun = true)
	public void testSetup(ITestContext tc,Method m,@Optional String browser,
			 @Optional String browserPlatform, @Optional String browserVersion) {	
		
		if (StringUtils.isEmpty(browser)) {			
			browser=EnvParameters.WEB_BROWSER_TYPE;			
		}		
		if (browser.equals("*chrome")) {
			try {
				driver =	setupChromeDriver();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (browser.equals("*iexplore")) {
			try {
				driver =	setupIEDriver();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	//	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		tc.setAttribute(m.getName(), driver);
		ITestNGMethod tm=null;
		for (ITestNGMethod f: tc.getAllTestMethods()){
			if (f.getMethodName().equalsIgnoreCase(m.getName())){
				tm=f;
				break;
			}
		}
		if (tm!=null){
			tc.setAttribute(tm.getInstance().toString(), driver);
		}
		
			driver.manage().window().maximize();
		
		

	}
	
	
	/**
	 * Runs after every test method to quit the driver instance
	 * 
	 * @param _result
	 * 
	 *            If the test management tool is test link , it will update the
	 *            result in test link after the test run with the properties
	 *            defined
	 */
	@AfterMethod(alwaysRun = true)
	public void postTestCase(ITestResult _result, Method testmethod) {	

		String res = null;		
		if(null!= TEST_MANAGEMENT_TOOL_URL && TEST_MANAGEMENT_TOOL_URL.contains("testlink")){		
			Reporter.log("Logging execution result  in test management tool testlink::",true);
			if (_result.isSuccess()) {
				res = TestLinkAPIResults.TEST_PASSED;
			} else {
				res = TestLinkAPIResults.TEST_FAILED;
			}
		Reporter.log("connecting  testlink::",true);
		try {
			if(null== TEST_NAME || TEST_NAME.isEmpty()){
				TEST_NAME = testmethod.getName();
			}
			
		TestLinkAPIClient testlinkAPIClient = new TestLinkAPIClient(TEST_MANAGEMENT_TOOL_DEV_KEY,
				TEST_MANAGEMENT_TOOL_URL);			
			Reporter.log("updating result in testlink::",true);
			testlinkAPIClient.reportTestCaseResult(TEST_MANAGEMENT_TOOL_PROJ_NAME,TEST_MANAGEMENT_TOOL_PLAN_NAME, TEST_NAME,TEST_MANAGEMENT_TOOL_BUILD_NAME,null, res);
			Reporter.log("updated result in testlink for-"+testmethod.getName()+",result is::"+res,true);
		} catch (SecurityException | TestLinkAPIException e) {			
			Reporter.log("Exeption while updating results in test management tool::"+e.getMessage(),true);
		    e.printStackTrace();
		}
		}
		if (driver != null) {
			driver.quit();
		}
	}

	/**
	 * Runs after every test class to kill the Chrome/IE drivers,
	 * if not properly exited
	 * @throws Exception
	 */


	/**
	 * Method to setup Chrome Driver based on the OS type
	 * @return 
	 * @throws Exception
	 */
	private WebDriver setupChromeDriver() throws Exception {

		String ChromProp = "webdriver.chrome.driver";
		new EnvParameters();
		File targetChromedriver = null;
		if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {			
			targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "chrome"
					+ File.separator + "win" + File.separator
					+ "chromedriver.exe");
		}

		else if (EnvParameters.getOSname() == OSType.mac) {
			targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "chrome"
					+ File.separator + "mac" + File.separator + "chromedriver");
		} else if (EnvParameters.getOSname() == OSType.linux) {
			targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "chrome"
					+ File.separator + "linux" + File.separator
					+ "chromedriver");
		}
		System.setProperty(ChromProp, targetChromedriver.getAbsolutePath());
		return driver = new ChromeDriver();
	}
		
	/**
	 * Method to setup IE driver based on the OS type
	 * @return 
	 * @throws Exception
	 */
	private  WebDriver setupIEDriver() throws Exception {
		String ieProperty = "webdriver.ie.driver";

		File targetIEdriver32 = null;

		// dont need it for other OS because IE is not available
		if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {
			targetIEdriver32 = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "ie"
					+ File.separator + "win32" + File.separator
					+ "IEDriverServer.exe");
		}
		System.setProperty(ieProperty, targetIEdriver32.getAbsolutePath());
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
	    ieCapabilities.setCapability(
	            InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
	           true);
		return  new InternetExplorerDriver(ieCapabilities);
		
	}
	
	public void takeScreenShot(String ImageFileName) {

		File targetLoc = new File(ImageFileName);
		if (!targetLoc.getParentFile().exists()) {
			targetLoc.getParentFile().mkdirs();
		}
		File srcFile = null;
		String browser = EnvParameters.WEB_BROWSER_TYPE;

		try {
			
			// now you can take the screenshot you need into a temporary file
			if (StringUtils.isNotEmpty(browser) && browser.equalsIgnoreCase("*chrome")) {
				srcFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			} else if (StringUtils.isNotEmpty(browser) && browser.equalsIgnoreCase("*firefox")) {
				srcFile = ((FirefoxDriver) driver).getScreenshotAs(OutputType.FILE);
			} else if (StringUtils.isNotEmpty(browser) && browser.equalsIgnoreCase("*iexplore")) {
				srcFile = ((InternetExplorerDriver) driver).getScreenshotAs(OutputType.FILE);
			}

			try {
				FileUtils.copyFile(srcFile, targetLoc);			
				Reporter.log(
						"Final Screenshot:<br><a href='file:///" + targetLoc + "' target='new'> <img src='file:///"
								+ targetLoc + "' width='300px' height='200px' /></a><br> ",true);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		}

	}
	
}