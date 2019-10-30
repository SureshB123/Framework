package com.Shell.qa.Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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
	
	@FindBy(xpath="//div[@class='c-site-layout__primary-area']/div[2]/button ")
	WebElement ShellMenu;
	
	@FindBy(xpath="//ul[@class='c-primary-nav']/li[1]/a")
	WebElement HomePageButton;
	
	@FindBy(xpath="//ul[@class='c-primary-nav']/li[3]/a")
	WebElement ReportsButton;
	
	@FindBy(xpath="//ul[@class='c-primary-nav']/li[4]/a")
	WebElement FinanceButton;
	
	@FindBy(xpath=" //ul[contains(@class,'c-primary-nav')]//li[2]/a")
	WebElement CardsButton;
	
	
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
	
	
	public static void TC82953_TC82954(String SinglePayerMultiAccount) {
		try {

			//LanguageChangeAndMakeAccountsUnFavorite_DefaultCompany(LoggedInUSer);
			LoginToAccount(SinglePayerMultiAccount);

			IsDisplayed("HomePage_TotalOutstanding");
			IsDisplayed("HomePage_ViewAccountLink");
			IsDisplayed("HomePage_ViewInvoicesLink");
			IsDisplayed("HomePage_ManageCardsLink");
			 
			// Validation moving to different pages and account name in account switcher should be same as per selected account
			click("AllAccounts_Xpath");
			String AllAccounts_FirstRecord = GetText("AllAccounts_FirstRecord");
			String AllAccounts_FirstRecordAccountNumber = GetText("AllAccounts_FirstRecord_AccountNumber");
			String Account = AllAccounts_FirstRecord + " " + AllAccounts_FirstRecordAccountNumber;
			click("AllAccounts_FirstRecord");
			Wait("AllAccounts_Xpath");
			String AccountSelected = GetText("AllAccounts_SelectedAccount");
			Assert.assertEquals(Account, AccountSelected, "Account selected from drop down " + Account+ " is not displayed in account switcher at Transactions page");

		

		} catch (Exception e) {
			
			Assert.fail("Error while executing TC TC82953_TC82954: Verify if logged in user have access to single company, TC 82954:Verify if Fleet Manager can distinguish each Account from the other and the actions the Fleet Manager can take for each Account should be clear");
		}
	}
	
	
	//Cards Page
	
	public static CardsPage Home() throws InterruptedException{
		click("ShellMenu_Xpath");
		click("Cards_Xpath");
		Thread.sleep(20000);
		String URL=GetCurrentURL();
		if(URL.equals(prop.getProperty("CardList_URL"))) {
			return new CardsPage();
		}else {
			return null;	
		}
	}

}
