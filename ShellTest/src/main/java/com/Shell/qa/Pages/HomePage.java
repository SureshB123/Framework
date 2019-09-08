package com.Shell.qa.Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.Shell.qa.Base.TestBase;


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
