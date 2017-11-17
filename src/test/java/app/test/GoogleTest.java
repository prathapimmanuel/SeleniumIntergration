package app.test;

import org.testng.annotations.Test;

import framework.core.TestMgmtToolUtil;
import framework.core.TestNGBase;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import app.page.GooglePage;

/**
 * Unit test for simple App.
 */
public class GoogleTest extends TestNGBase {

	String dataURL = "http://www.google.com";

	SoftAssert softAssert = getSoftAssert();

	Boolean flag = false;

	//@Test
	public void testOpenGoogle() {
		
		GooglePage objGooglePage = new GooglePage(driver);
		TestMgmtToolUtil  objTestMgmtToolUtil = new TestMgmtToolUtil();
		objGooglePage.openApplication(dataURL);
		flag = objGooglePage.getTitle().contains("Google");
		softAssert.assertTrue(flag, "search page opened");
		softAssert.assertAll();		
		//objTestMgmtToolUtil.updateExecResutinTestMgmtTool(null,null);

	}

	@Test

	public void testSearchGoogle() {

		GooglePage objGooglePage = new GooglePage(driver);
		TestMgmtToolUtil  objTestMgmtToolUtil = new TestMgmtToolUtil();
		objGooglePage.openApplication(dataURL);
		objGooglePage.doSearch("Test Automation");
		flag = objGooglePage.getTitle().contains("Auto mation");
		Assert.assertTrue(flag, "search succeeds");
		softAssert.assertAll();
		//objTestMgmtToolUtil.updateExecResutinTestMgmtTool(null,null);
	}
	
	@Test

	public void testValidatesearch() {

		GooglePage objGooglePage = new GooglePage(driver);
		TestMgmtToolUtil  objTestMgmtToolUtil = new TestMgmtToolUtil();
		objGooglePage.openApplication(dataURL);
		objGooglePage.doSearch("Test Automation");
		flag = !objGooglePage.getResultsCount().isEmpty();
		Assert.assertTrue(flag, "search results available");
		softAssert.assertAll();
		//objTestMgmtToolUtil.updateExecResutinTestMgmtTool(null,null);
		
	}
}
