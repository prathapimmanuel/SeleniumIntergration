package app.test; 

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import app.page.LoginPage;
import atu.alm.wrapper.ALMServiceWrapper; 

import atu.alm.wrapper.HasAttachmentFeature;

import atu.alm.wrapper.ITestCase;
import atu.alm.wrapper.ITestCaseRun;
import atu.alm.wrapper.enums.StatusAs;
import framework.core.FunctionalLibrary;
import framework.core.ReadFromExcel;
import framework.core.TestMgmtToolUtil;
import framework.core.TestNGBase;

public class NavigateToSaleTest extends TestNGBase{

	Properties prop = new Properties(); 

	@Test(description = "JDE Navigate to Sale Screen",priority = 1)
	public void JDEInvalidLoginValidation() throws Exception {
		prop.load(new FileInputStream("Data.properties"));
		Thread.sleep(2000);   
		driver.get(prop.getProperty("URL"));
		Thread.sleep(2000); 
		FunctionalLibrary libLoad = new FunctionalLibrary();
        TestMgmtToolUtil testTool =  new TestMgmtToolUtil();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.checkLogin(prop.getProperty("validUser"),prop.getProperty("validPwd"));
			loginPage.navigateToSale();
			boolean saleLoad = loginPage.checkSaleLoad();
			libLoad.captureScreenShot("TC12",  
					driver);     
			if(saleLoad) 
			{ 			
			testTool.updateExecResutinTestMgmtToolWithParams(StatusAs.PASSED,"TestSet2");
		}      
	} 

}
