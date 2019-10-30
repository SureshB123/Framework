package com.Shell.qa.TestCases;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.Shell.qa.Base.TestBase;
import com.Shell.qa.Pages.CardsPage;
import com.Shell.qa.Pages.HomePage;
import com.Shell.qa.Pages.LoginPage;
import com.Shell.qa.Utility.POMDataUtil;
import com.relevantcodes.extentreports.LogStatus;



public class TC02_HomePageTest extends TestBase {
	
	
	HomePage homePage;
	CardsPage cardsPage;
	LoginPage loginPage;
	String TestCaseName = "TC02_HomePageTest";
	
	public TC02_HomePageTest() {
		super();
	}
	
	@BeforeClass
	
	public void CheckRunMode() {
		if (!POMDataUtil.TestCaseIDToRun(TestCaseName, xls)) {
			extentTest.log(LogStatus.INFO,"Skipping "+TestCaseName+" because runmode is N");
			throw new SkipException("Skipping test because runmode is N");
			
		}
	}
	
	@Parameters({ "browser" })
	@BeforeMethod()
	public void SetUp(String browser) throws InterruptedException {
		initialization(browser);
		loginPage=new LoginPage();
		homePage=LoginPage.Login(prop.getProperty("username"), prop.getProperty("password"));
				
	}
	
	
	//Here we will write my tests
	
	@Test(priority=0, description="Validating Company Name on Home Page")
	
	public void ValidateHomePage() {
		extentTest=extent.startTest("ValidateHomePage");
		IsDisplayed("HomePageCompnayName");
		
	}
	
	
	@Test(dataProvider = "getData",priority=1,description="TC82953_TC82954: Verify the list of companies on Home page for logged in user")
	
	public void TC82953_TC82954(Hashtable<String, String> data){
		extentTest=extent.startTest("TC82953_TC82954");
		HomePage.TC82953_TC82954(data.get("SinglePayerMultiAccount"));
		
	
	}
	
	//Validate if Successfully moved to Cards Page
	
	@Test(priority=2,description="Validating after clicking cards from Home Page, Cards page is displayed")
	
	public void HomeTest() throws InterruptedException {
		extentTest=extent.startTest("HomeTest");
		cardsPage=HomePage.Home();
		if(!(cardsPage instanceof CardsPage)){
			Assert.fail("On clicking cards from home page, unsuccessful to move to Cards Page");
		}
	
	}
	
	@DataProvider

	public Object[][] getData() {
		return POMDataUtil.getTestData(xls, TestCaseName);
	}	

}
