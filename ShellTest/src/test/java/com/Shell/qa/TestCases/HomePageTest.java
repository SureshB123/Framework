package com.Shell.qa.TestCases;


import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.Shell.qa.Base.TestBase;
import com.Shell.qa.Pages.CardsPage;
import com.Shell.qa.Pages.HomePage;
import com.Shell.qa.Pages.LoginPage;
import com.Shell.qa.Utility.POMDataUtil;
import com.relevantcodes.extentreports.LogStatus;



public class HomePageTest extends TestBase {
	
	
	HomePage homePage;
	CardsPage cardsPage;
	LoginPage loginPage;
	public String TestCaseName = "HomePageTest";
	
	public HomePageTest() {
		super();
	}
	
	@BeforeClass
	
	public void CheckRunMode() {
		if (!POMDataUtil.TestCaseIDToRun(TestCaseName, xls)) {
			extentTest.log(LogStatus.INFO,"Skipping "+TestCaseName+" because runmode is N");
			throw new SkipException("Skipping test because runmode is N");
			
		}
	}
	
	@BeforeMethod
	public void SetUp() throws InterruptedException {
		initialization();
		loginPage=new LoginPage();
		homePage=loginPage.Login(prop.getProperty("username"), prop.getProperty("password"));
				
	}
	
	
	//Here we will write my test cases
	
	@Test(priority=0, description="Validating Company Name on Home Page")
	
	public void ValidateHomePage() {
		extentTest=extent.startTest("ValidateHomePage");
		boolean AccountName=homePage.ValidateCompanyName();
		Assert.assertTrue(AccountName, "Not on Home Page");
		
	
	}
	
	@Test(priority=1,description="Validating 'Order a Card' link on Home Page")
	
	public void ValidateOrderaCardLink() {
		extentTest=extent.startTest("ValidateOrderaCardLink");
		boolean AccountName=homePage.ValidateOrderACardLink();
		Assert.assertTrue(AccountName, "Order A Card is not displayed on Home Page");
	
	}
	
	@Test(priority=2,description="Validating 'Block card' link on Home Page")
	
	public void ValidateBlockCardLink() {
		extentTest=extent.startTest("ValidateBlockCardLink");
		boolean AccountName=homePage.ValidateBlockCard();
		Assert.assertTrue(AccountName, "Block Card Link is not displayed on Home Page");
	
	}

	@Test(priority=3,description="Validating 'Cancel Card' link on Home Page")

	public void ValidateCancelCardLink() {
		extentTest=extent.startTest("ValidateCancelCardLink");
		boolean AccountName=homePage.ValidateCancelCard();
		Assert.assertTrue(AccountName, "Cancel Card Link is not displayed on Home Page");

	}
	
	@Test(priority=4,description="Validating 'Card Activity' link on Home Page")
	
	public void ValidateCardActivityLink() {
		extentTest=extent.startTest("ValidateCardActivityLink");
		boolean AccountName=homePage.ValidateCardActivityLink();
		Assert.assertTrue(AccountName, "Card Activity Link is not displayed on Home Page");
	
	}
	
	@Test(priority=5,description="Validating 'Cards' link on Home Page")
	
	public void ValidateCardsLink() {
		extentTest=extent.startTest("ValidateCardsLink");
		boolean AccountName=homePage.ValidateCardsLink();
		Assert.assertTrue(AccountName, "Card Link is not displayed on Home Page");
	
	}
	
	@Test(priority=6,description="Validating 'Finance' link on Home Page")
	
	public void ValidateFinanceLink() {
		extentTest=extent.startTest("ValidateFinanceLink");
		boolean AccountName=homePage.ValidateFinanceLink();
		Assert.assertTrue(AccountName, "Finance Link is not displayed on Home Page");
	
	}
	
	//Validate if Successfully moved to Cards Page
	
	@Test(priority=7,description="Validating after clicking cards from Home Page, Cards page is displayed")
	
	public void HomeTest() throws InterruptedException {
		extentTest=extent.startTest("HomeTest");
		cardsPage=homePage.Home();
		if(!(cardsPage instanceof CardsPage)){
			Assert.fail("On clicking cards from home page, unsuccessful to move to Cards Page");
		}
	
	}
	
	@DataProvider

	public Object[][] getData() {
		return POMDataUtil.getTestData(xls, TestCaseName);
	}	

}
