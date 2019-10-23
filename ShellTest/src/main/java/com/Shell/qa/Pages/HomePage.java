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
	
	
	public void TC82953_TC82954(String SinglePayerMultiAccount) {
		try {

			//LanguageChangeAndMakeAccountsUnFavorite_DefaultCompany(LoggedInUSer);
			LoginToAccount(SinglePayerMultiAccount);

			isDisplayedValid("HomePage_TotalOutstanding");
			isDisplayedValid("HomePage_ViewAccountLink");
			isDisplayedValid("HomePage_ViewInvoicesLink");
			isDisplayedValid("HomePage_ManageCardsLink");
			 
			// Validation moving to different pages and account name in account switcher should be same as per selected account
			click("AllAccounts_Xpath");
			String AllAccounts_FirstRecord = GetText("AllAccounts_FirstRecord");
			String AllAccounts_FirstRecordAccountNumber = GetText("AllAccounts_FirstRecord_AccountNumber");
			String Account = AllAccounts_FirstRecord + " " + AllAccounts_FirstRecordAccountNumber;
			click("AllAccounts_FirstRecord");
			String AccountSelected = GetText("AllAccounts_SelectedAccount");
			Assert.assertEquals(Account, AccountSelected, "Account selected from drop down " + Account+ " is not displayed in account switcher at Transactions page");

			// Click Transactions from home page widget

			Refresh();
			List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("HomePage_NavigationLinks_Size")));
			int Contents = Size.size();
			int Transactions = 0;
			for (int i = 2; i <= Contents + 1; i++) {
				 String Text =GetTextMutipleParameters("HomePage_NavigationLinks1", i, "HomePage_NavigationLinks2");
				// Validate if 'Transactions' should be displayed
				if (Text.equalsIgnoreCase("Transactions")) {
					Transactions = 1;
					clickMutipleParameters("HomePage_NavigationWidgetLink1", i, "HomePage_NavigationWidgetLink2");
					
					// Validate Account selected on home page at Transaction page
					AccountSelected = GetText("AllAccounts_SelectedAccount");
					
					Assert.assertEquals(Account, AccountSelected, "Account selected from drop down " + Account+ " is not displayed in account switcher at Transactions page");

					break;
				}

			}
			
			Assert.assertEquals(Transactions, Transactions == 0, " 'Transactions' widged is not there for user " + SinglePayerMultiAccount + " on home page");
				

			// Click Cards from home page

			GoToURL("HomePage_URL");
			Size = driver.findElements(By.xpath(prop.getProperty("HomePage_NavigationLinks_Size")));
			Contents = Size.size();
			int Cards = 0;
			for (int k = 2; k <= Contents + 1; k++) {
				 String Text =GetTextMutipleParameters("HomePage_NavigationLinks1", k, "HomePage_NavigationLinks2");
				// Validate if 'Cards' should be displayed
				if (Text.equalsIgnoreCase("Cards")) {
					Cards = 1;
					clickMutipleParameters("HomePage_NavigationWidgetLink1", k, "HomePage_NavigationWidgetLink2");
			
					// Validate Account selected on home page at Transaction page
					AccountSelected = GetText("AllAccounts_SelectedAccount");
					Assert.assertEquals(Account, AccountSelected, "Account selected from drop down " + Account+ " is not displayed in account switcher at Transactions page");
					break;
				}

			}
			
			Assert.assertEquals(Cards, Cards == 0, " 'Cards' widged is not there for user " + SinglePayerMultiAccount + " on home page");
				

			// Click Invoices from home page

			GoToURL("HomePage_URL");
			Size = driver.findElements(By.xpath(prop.getProperty("HomePage_NavigationLinks_Size")));
			Contents = Size.size();
			int Invoice = 0;
			for (int j = 2; j <= Contents + 1; j++) {
				 String Text =GetTextMutipleParameters("HomePage_NavigationLinks1", j, "HomePage_NavigationLinks2");
				// Validate if 'Invoices' should be displayed
				if (Text.equalsIgnoreCase("Invoices")) {
					Invoice = 1;
					clickMutipleParameters("HomePage_NavigationWidgetLink1", j, "HomePage_NavigationWidgetLink2");
					
					// Validate Account selected on home page at Transaction  page
					AccountSelected = GetText("AllAccounts_SelectedAccount");
					Assert.assertEquals(Account, AccountSelected, "Account selected from drop down " + Account+ " is not displayed in account switcher at Transactions page");
					break;
				}

			}

			Assert.assertEquals(Invoice, Invoice == 0, " 'Invoices' widged is not there for user " + SinglePayerMultiAccount + " on home page");
				

		} catch (Exception e) {
			
			Assert.fail("Error while executing TC TC82953_TC82954: Verify if logged in user have access to single company, TC 82954:Verify if Fleet Manager can distinguish each Account from the other and the actions the Fleet Manager can take for each Account should be clear");
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
