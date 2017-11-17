package app.test; 
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import app.page.GooglePage;
import framework.core.EnvParameters;
import framework.core.EnvParameters.OSType;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;


/**
 * Unit test for simple App.
 */
public class GoogleTesting  {
	WebDriver driver;
	String dataURL = "http://www.google.com";

	//SoftAssert softAssert = getSoftAssert();
	Boolean flag = false;
	
	@FindBy(xpath ="//input[@name= 'q']")
	private  WebElement txtSearchBox ;
	
	@FindBy(xpath ="//div[@id ='resultStats']")
	private  WebElement txtResultCount;
	
	
@BeforeTest
private void setupChromeDriver() throws Exception {

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
			}else if (EnvParameters.getOSname() == OSType.linux) {
				targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
						+ File.separator + "drivers" + File.separator + "chrome"
						+ File.separator + "linux" + File.separator
						+ "HtmlUnitDriver");  
			}
			System.setProperty(ChromProp, targetChromedriver.getAbsolutePath());
			driver = new ChromeDriver();   
		}
	

	@Test
	public void testOpenGoogle() {
		
		GooglePage objGooglePage = new GooglePage(driver);
		System.out.println("google open");
		objGooglePage.openApplication(dataURL); 
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flag = objGooglePage.getTitle().contains("Google");
		System.out.println("flag"+flag);
		driver.findElement(By.xpath("//input[@name= 'q']")).sendKeys("Auto");
		driver.findElement(By.xpath("//input[@name= 'q']")).sendKeys(Keys.ENTER);
		////txtSearchBox.sendKeys("Automation"); 
		//txtSearchBox.sendKeys(Keys.ENTER);
		//softAssert.assertTrue(flag, "search page opened");
		//softAssert.assertAll();		

	} 

	
	@AfterMethod()
	public void postTestCase() {
		if (driver != null) {
			driver.quit();
		}
	} 
}
