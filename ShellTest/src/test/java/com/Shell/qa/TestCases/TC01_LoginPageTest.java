package com.Shell.qa.TestCases;

import java.util.Hashtable;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.Shell.qa.Base.TestBase;
import com.Shell.qa.Pages.HomePage;
import com.Shell.qa.Pages.LoginPage;
import com.Shell.qa.Utility.POMDataUtil;
import com.relevantcodes.extentreports.LogStatus;



public class TC01_LoginPageTest extends TestBase {
	
	LoginPage loginPage;
	HomePage homePage;
	String TestCaseName = "TC01_LoginPageTest";
	

	public TC01_LoginPageTest() {
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
	public void SetUp() {
		initialization();
		loginPage=new LoginPage();
		
			
	}
	
	
	//Here we will write test cases for Login Page
	
	@Test(priority=1, description="Validating Login page is getting displayed")
	
	//Validation of Login page
	
	public void ValidateLoginPage() {
		extentTest=extent.startTest("ValidateLoginPage");
		boolean flag=loginPage.validateLoginPage();
		Assert.assertTrue(flag, "Not on Login Page");
	
	}
	
	//Validate if successfully moved to Home Page
	
	@Test(dataProvider = "getData",priority=2, description="Validating after Login, Home page is displayed")
	
	public void LoginTest(Hashtable<String, String> data) throws InterruptedException {
		extentTest=extent.startTest("LoginTest");
		//homePage=loginPage.Login(prop.getProperty("username"), prop.getProperty("password"));
		homePage=loginPage.Login(data.get("UserName"),data.get("Password"));
		if(!(homePage instanceof HomePage)){
			Assert.fail("Unsuccessful to move to Home Page");
		}
	}
	
	
	@DataProvider

	public Object[][] getData() {
		return POMDataUtil.getTestData(xls, TestCaseName);
	}
}
