package app.test; 

/* This test is to validate both valid and invalid Login*/

import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import app.page.LoginPage;
import atu.alm.wrapper.enums.StatusAs;
import framework.core.FunctionalLibrary;
import framework.core.TestMgmtToolUtil;

public class JDELoginTest {
	WebDriver driver;
	Properties prop = new Properties(); 
 
	@BeforeTest 
	public void Startup() {
		System.out.println("inside startup");
		System.setProperty("webdriver.chrome.driver", 
				"D:\\Data Backup\\Selenium\\chromedriver.exe");
		driver = new ChromeDriver(); 
	}

	@Test(description = "JDE Invalid Login Validation",priority = 1)
	public void JDEInvalidLoginValidation() throws Exception {	
		driver.manage().window().maximize(); 
		prop.load(new FileInputStream("Data.properties"));
		driver.get(prop.getProperty("URL"));
		Thread.sleep(2000);
        TestMgmtToolUtil testTool =  new TestMgmtToolUtil();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.checkLogin(prop.getProperty("invalidUser"),prop.getProperty("invalidPwd"));
		boolean status = loginPage.checkHomePageLoad(); 
		FunctionalLibrary.captureScreenShot("TC11",  
				driver);   
		if(!status) { 
			testTool.updateExecResutinTestMgmtToolWithParams(StatusAs.FAILED,"TestSet1"); 
			Assert.fail("Login Failed");  
			//testTool.createDefect();  
		}   
	} 
		
	@Test(description = "JDE Valid Login Validation",priority = 2)
	public void JDELoginValidation() throws Exception { 
		driver.manage().window().maximize();
		prop.load(new FileInputStream("Data.properties"));
		driver.get(prop.getProperty("URL"));
		Thread.sleep(2000);
        TestMgmtToolUtil testTool =  new TestMgmtToolUtil(); 
      
		LoginPage loginPage = new LoginPage(driver);
		loginPage.checkLogin(prop.getProperty("validUser"),prop.getProperty("validPwd"));
		boolean status = loginPage.checkHomePageLoad();
		FunctionalLibrary.captureScreenShot("TC11", 
				driver); 
		if(status) { 
			testTool.updateExecResutinTestMgmtToolWithParams(StatusAs.PASSED,"TestSet1");   
		}  
	}   
	
	@AfterClass 
	public void teardown() {
		driver.quit();
	}
}
