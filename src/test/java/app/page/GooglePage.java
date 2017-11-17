package app.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class GooglePage {
	
	@FindBy(xpath ="//input[@name= 'q']")
	private  WebElement txtSearchBox ;
	
	@FindBy(xpath ="//div[@id ='resultStats']")
	private  WebElement txtResultCount;
	private WebDriver driver;


	
	public GooglePage(final WebDriver driver) {
		this.driver = driver;	
		
	}
	
	public void openApplication(String url){
		driver.get(url);
	}
	
	public String getTitle(){
		return driver.getTitle();
	}
	
	public void doSearch(String searchText){
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		} 
		txtSearchBox.sendKeys(searchText);
		txtSearchBox.sendKeys(Keys.ENTER);
	}
	
	public String getResultsCount(){
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		return txtResultCount.getText();
	}
}
