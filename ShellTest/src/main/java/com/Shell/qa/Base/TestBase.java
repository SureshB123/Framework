package com.Shell.qa.Base;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import com.Shell.qa.Utility.POMDataUtil;
import com.Shell.qa.Utility.POMXls_Reader;
import com.Shell.qa.Utility.TestUtil;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestBase {
	
	//Initialize your webdriver
	public static WebDriver driver;
	
	//Initializing properties file
	public static Properties prop= null;
	
	//Initializing POMXLS_Reader
	public static POMXls_Reader xls;
	
	//Initializing Extent Report
	public static ExtentReports extent;
	public static ExtentTest extentTest;
	
	public String Excel_path=System.getProperty("user.dir")+"//src//main//java//com//Shell//qa//TestData//POMTestDataSheet.xlsx";
	
	//Constructor of Base Test
	
	public TestBase() {
		
		try{
			prop=new Properties();
			FileInputStream fis= new  FileInputStream(System.getProperty("user.dir")+"//src//main//java//com//Shell//qa//Config//config.properties");
			prop.load(fis);
			
			xls= new POMXls_Reader(Excel_path);
			
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Unable to Load Properties File");
				
			}
		
	}
	
	//Driver Initialization
	
	public static void initialization() {
		
		
		String browserName=prop.getProperty("browser");
		try{
			
			  if(browserName.equals("Mozilla")){
				  		System.setProperty("webdriver.firefox.marionette",System.getProperty("user.dir")+"\\Drivers\\geckodriver.exe");
			    		driver= new FirefoxDriver();
			  }else if(browserName.equals("chrome")){
						System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Drivers\\chromedriver.exe");
						driver= new ChromeDriver();
			  }else if(browserName.equals("ie")){
						System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"\\Drivers\\msedgedriver.exe");
						driver = new InternetExplorerDriver();
			  }
					driver.manage().window().maximize();
					driver.manage().deleteAllCookies();
					driver.manage().timeouts().pageLoadTimeout(TestUtil.PageLoadTimeOut, TimeUnit.SECONDS);
					driver.manage().timeouts().implicitlyWait(TestUtil.Implicit_Wait, TimeUnit.SECONDS);
					driver.get(prop.getProperty("url"));
			    }catch(Exception e){
			    		System.out.println("Unable to find browser and open it");
			    		e.printStackTrace();
			    }
	}
	
	public String GetCurrentURL() {
		String CurrentURL=driver.getCurrentUrl();
		return CurrentURL;
	}
	
	
	@BeforeTest
	
	public void setExtent() {
		Date d=new Date();
		//This will generate the report every time with new time stamp
		String Class=this.getClass().getSimpleName();
		String fileName=Class+"_"+d.toString().replace(":", "_").replace(" ", "_")+".html";
		//extent=new ExtentReports(System.getProperty("user.dir")+"//test-output//Shell_ExtentReport.html"+fileName, true, DisplayOrder.NEWEST_FIRST);
		extent=new ExtentReports(System.getProperty("user.dir")+"//Reports//"+fileName, true, DisplayOrder.NEWEST_FIRST);
		extent.loadConfig(new File(System.getProperty("user.dir")+"//ReportsConfig.xml"));
		extent.addSystemInfo("User Name", "Suresh Bisht");
		extent.addSystemInfo("Selenium Version", "2.53.0").addSystemInfo("Environment", "QA");
		
			
	}
	
	@AfterTest
	
	public void endReport() {
		extent.flush();
		extent.close();
	}
	
	@AfterMethod
	
	public void QuitBrowser(ITestResult result) throws IOException {
		
		//This will attach the screenshot to extent report if test got failed/skipped/passed
		
		if(result.getStatus()==ITestResult.FAILURE) {
			extentTest.log(LogStatus.FAIL, "Test Case FAILED: "+result.getName());// add screenshot in extent report
			extentTest.log(LogStatus.FAIL, "Test Case FAILED: "+result.getThrowable()); //add error/exception in extent report
			String ScreenshotPath=TestUtil.getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(ScreenshotPath)); //add screenshot in extent report
			//extentTest.log(LogStatus.FAIL, extentTest.addScreencast(ScreenshotPath)); //add video in extent report
			
		}else if(result.getStatus()==ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, "Test Case SKIPPED: "+result.getName());
		}else if(result.getStatus()==ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, "Test Case PASS: "+result.getName());
		}
		
		extent.endTest(extentTest); //ending the test
		
		QuitDriver();
	
	}

	//Quit Driver
	
	public void QuitDriver() {
		driver.quit();
	}
	
	
}
