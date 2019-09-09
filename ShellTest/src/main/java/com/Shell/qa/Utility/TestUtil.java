package com.Shell.qa.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class TestUtil {
	
	public static long PageLoadTimeOut=40;
	public static long Implicit_Wait=30;
	public static WebDriver driver;

	
	
	// Take Screen shot
	public static String getScreenshot(WebDriver driver,String ScreenshotName) throws IOException {
		String Date=new SimpleDateFormat("ddMMyyyy hhmmss").format(new Date());
		TakesScreenshot ts=(TakesScreenshot)driver;
		File Source=ts.getScreenshotAs(OutputType.FILE);
		
		//after execution you could see a folder "FailedTestsScreenshot"
		//under src folder
		String Destination=System.getProperty("user.dir")+"//FailedTestsScreenshots//"+ScreenshotName+"_"+Date+".png";
		File finalDestination=new File(Destination);
		FileUtils.copyFile(Source, finalDestination);
		return Destination;
		
		
	}
	
	
}
