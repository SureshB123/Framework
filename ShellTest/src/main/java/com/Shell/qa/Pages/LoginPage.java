package com.Shell.qa.Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.Shell.qa.Base.TestBase;
import com.Shell.qa.Utility.TestUtil;

public class LoginPage extends TestBase {
	
	//PageFactory
	
	@FindBy(id="UserName")
	WebElement userName;
	
	@FindBy(id="Password")
	WebElement password;
	
	@FindBy(xpath="//input[@type='submit']")
	WebElement LoginButton;
	
	//Initializing the page Object
	
	public LoginPage() {
		PageFactory.initElements(driver,this );
	}
	
	//Actions to be performed
	
	//Validate Login Page
	
	public boolean validateLoginPage() {
		
		return LoginButton.isDisplayed();
		
	}
	
	//Login
	
	public HomePage Login(String un,String pw) throws InterruptedException {
		
		userName.sendKeys(un);
		password.sendKeys(pw);
		LoginButton.click();
		Thread.sleep(50000);
		String URL=GetCurrentURL();
		if(URL.equalsIgnoreCase(prop.getProperty("HomePage_URL"))) {
			return new HomePage();
		}else {
			return null;
		}
	}
	
	
	

}
