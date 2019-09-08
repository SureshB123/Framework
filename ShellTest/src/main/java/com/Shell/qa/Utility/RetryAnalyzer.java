package com.Shell.qa.Utility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	
	int count=0;
	int retry=1;

	public boolean retry(ITestResult result) {
		if(count<retry) {
			count++;
			return true;
		}
		return false;
	}

}
