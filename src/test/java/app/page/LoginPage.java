package app.page;


/*Page object Model - Pages included here*/

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPage {
WebDriver driver;
	
@FindBy(xpath = "//*[@id='User']")
private WebElement userId;

@FindBy(xpath = "//*[@id='Password']")
private WebElement password;

@FindBy(xpath = "//*[@id='F1']/table/tbody/tr/td/div/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/div[14]/input")
private WebElement loginButton;

@FindBy(xpath = "//*[@id='drop_mainmenu']")
private WebElement mainMenu;

@FindBy(xpath = "//*[@title='Sale Number']")
private WebElement saleNum;


@FindBy(xpath = "//*[@id='listRRpt_WSJ']/table/tbody/tr/td[2]")
private WebElement viewJobs;

@FindBy(id = "e1menuAppIframe")
private WebElement menuIframe;
 


public LoginPage(WebDriver driver) {
	this.driver = driver;
	 PageFactory.initElements(driver, this);
}


public void checkLogin(String user,String pwd) {
	try {
	Thread.sleep(2000);
	userId.clear();
	userId.sendKeys(user);
	Thread.sleep(2000);
	password.clear(); 
	password.sendKeys(pwd);
	Thread.sleep(2000);
	loginButton.click();
	Thread.sleep(3000);
  }
	catch (Exception ex) {
		System.out.println("checkLogin Exception" + ex);
}


   }

public void navigateToSale(){
	naviWorkWithSaleHeader();
}


public boolean checkSaleLoad(){
	boolean isPresent = false; 
	try {
	
	Thread.sleep(1000);
	driver.switchTo().frame(menuIframe);
	Thread.sleep(1000); 
	if(saleNum.isDisplayed()) {
	 Assert.assertEquals(true, saleNum.isDisplayed(), "Sale Page Loaded successfully");
	 isPresent = true;
	 }else {
		 isPresent = false;
	 } 
	
	}catch (Exception ex) {
		System.out.println("checkSalePageLoad Exception" + ex);
		 isPresent = false; 
	} 
	return isPresent;
}



public void naviWorkWithSaleHeader() {
	try {
		System.out.println("inside naviWorkWithSaleHeader");
		mainMenu.click();
		Actions action = new Actions(driver);
		Thread.sleep(3000);
		action.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'Christie') and contains(text(),'London')]")))
				.click().build().perform();
		Thread.sleep(6000); 
		action.moveToElement(
				driver.findElement(By.xpath("(//span[text()='CCSA'])[2]")))
				.click().build().perform();
		Thread.sleep(5000);
		action.moveToElement(driver.findElement(By
				.xpath("(//a[contains(text(),'Work') and contains(text(),'With') and contains(text(),'Sale') and contains(text(),'Header')])[2]")))
				.click().build().perform();
	} catch (Exception ex) {
		System.out.println(
				"Exception inside Navigate to Working with Sale Header"
						+ ex);
	}
}

public boolean checkHomePageLoad() {
	boolean isPresent = false; 
	try {
	
	Thread.sleep(1000);
	//driver.switchTo().frame(menuIframe);
	//Thread.sleep(1000);
	if(viewJobs.isDisplayed()) {
	 Assert.assertEquals(true, viewJobs.isDisplayed(), "Login Succesfull");
	 isPresent = true;
	 }else {
		 isPresent = false;
	 } 
	
	}catch (Exception ex) {
		System.out.println("checkHomePageLoad Exception" + ex);
		 isPresent = false; 
	} 
	return isPresent;
}
}
