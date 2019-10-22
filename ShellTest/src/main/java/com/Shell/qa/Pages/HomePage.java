package com.Shell.qa.Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.Shell.qa.Base.TestBase;
import com.relevantcodes.extentreports.LogStatus;


public class HomePage extends TestBase {
	
	
	@FindBy(xpath="//div[@class='c-title-description__description ']")
	WebElement HomePage_CompanyName;
	
	@FindBy(xpath="//ul[@class='c-task-list']/li[1]/a")
	WebElement HomePage_OrderCardLink;
	
	@FindBy(xpath="//ul[@class='c-task-list']/li[2]/a")
	WebElement HomePage_BlockCardsLink;
	
	@FindBy(xpath="//ul[@class='c-task-list']/li[3]/a")
	WebElement HomePage_CancelCardsLink;
	
	@FindBy(xpath="//ul[@class='c-task-list']/li[4]/a")
	WebElement HomePage_DownLoadTransactionsLink;
	
	@FindBy(xpath="//div[@class='o-grid u-color-block--padded']/div[1]//a/div[1]")
	WebElement HomePage_CardActivityText;
	
	@FindBy(xpath="//div[@class='o-grid u-color-block--padded']/div[1]//a")
	WebElement HomePage_CardActivityLink;
	
	@FindBy(xpath="//a/div[contains(text(),'Cards')]")
	WebElement HomePage_CardsText;
	
	@FindBy(xpath="//*[@id=\"main-content\"]/div[2]/div[1]/div[3]/div/a")
	WebElement HomePage_CardsLink;
	
	@FindBy(xpath="//div[@class='o-grid u-color-block--padded']/div[3]//a/div[1]")
	WebElement HomePage_FinanceText;
	
	@FindBy(xpath="//div[@class='o-grid u-color-block--padded']/div[3]//a")
	WebElement HomePage_FinanceLink;
	
	public HomePage() {
		PageFactory.initElements(driver,this);
	}
	
	//Actions on HomePage
	
	
	public boolean ValidateCompanyName() {
		return HomePage_CompanyName.isDisplayed();
	}
	
	public boolean ValidateOrderACardLink() {
		return HomePage_OrderCardLink.isDisplayed();
	}
	public boolean ValidateBlockCard() {
		return HomePage_BlockCardsLink.isDisplayed();
	}
	public boolean ValidateCancelCard() {
		return HomePage_CancelCardsLink.isDisplayed();
	}
	public boolean ValidateTransactionsLink() {
		return HomePage_DownLoadTransactionsLink.isDisplayed();
	}
	public boolean ValidateCardActivityLink() {
		return HomePage_CardActivityLink.isDisplayed();
	}
	public boolean ValidateCardsLink() {
		return HomePage_CardsLink.isDisplayed();
	}
	
	public boolean ValidateFinanceLink() {
		return HomePage_FinanceLink.isDisplayed();
	}
	
	
	// TC 78561: Verify the list of companies on Home page for logged in user
	
	
		public void TC78561(String AccountWithSetasDefault) {
			try {
				LanguageChange(AccountWithSetasDefault);
				click("ShellMenu_Xpath");
				click("HomePage_Xpath");
			
				/*if (isDisplayedValid("HomePage_ShellMobilityHub_Text")) {
					click("ShellMenu_Xpath");
					if (isDisplayedValid("Intersticial_HomePage_CompanyName_LeftHandPane")) {
						String CompanyName = GetText("Intersticial_HomePage_CompanyName_LeftHandPane");
						extentTest.log(LogStatus.PASS, "Selected Company Name: " + CompanyName + " is displayed");
						click("Intersticial_HomePage_CompanyName_LeftHandPane");
						List<WebElement> TotalCompaniesSize = driver.findElements(By.xpath(prop.getProperty("HomePage_TotalCompaniesCount_LeftPane")));
						int TotalCompaniesCount = TotalCompaniesSize.size();
						extentTest.log(LogStatus.PASS, "Count of companies displayed on homepage " + TotalCompaniesCount);
						
						// Validating if countries are in alphabetical order
						Sorting("HomePage_CountriesCount_LeftPane", "Conrties");
					
					} else {
						extentTest.log(LogStatus.FAIL, "Selected Company Name is not displayed");
					}

				} else if (isDisplayedValid("Intersticial_FirstCompanyNameandNumber")) {
					click("Intersticial_FirstCompanystatus");
					click("Intersticial_FirstCompanyNameandNumber");
					click("ShellMenu_Xpath");
					if (isDisplayedValid("Intersticial_HomePage_CompanyName_LeftHandPane")) {
						String CompanyName = GetText("Intersticial_HomePage_CompanyName_LeftHandPane");
						extentTest.log(LogStatus.PASS, "Selected Company Name: " + CompanyName + " is displayed");
						click("Intersticial_HomePage_CompanyName_LeftHandPane");
						List<WebElement> TotalCompaniesSize = driver
								.findElements(By.xpath(prop.getProperty("HomePage_TotalCompaniesCount_LeftPane")));
						int TotalCompaniesCount = TotalCompaniesSize.size();
						extentTest.log(LogStatus.PASS, "Count of companies displayed on homepage " + TotalCompaniesCount);
						
						// Validating if countries are in alphabetical order
						Sorting("HomePage_CountriesCount_LeftPane", "Conrties");
					} else {
						extentTest.log(LogStatus.FAIL, "Selected Company Name is not displayed");
					}
				}*/

			} catch (Exception e) {
				extentTest.log(LogStatus.FAIL,
						"Error while executing TC 78561: Verify the list of companies on Home page for logged in user");
			}
		}
	//Cards Page
	
	public CardsPage Home() throws InterruptedException{
		HomePage_CardsLink.click();
		Thread.sleep(20000);
		String URL=GetCurrentURL();
		if(URL.equals(prop.getProperty("CardList_URL"))) {
			return new CardsPage();
		}else {
			return null;	
		}
	}

}
