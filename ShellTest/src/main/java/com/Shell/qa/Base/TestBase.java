package com.Shell.qa.Base;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
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
				  		System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"\\Drivers\\geckodriver.exe");
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
	
	
	// Sorting Accounts in alphabetical order (used in TC72837())
	
			public void Sorting(String Xpath, String Text) throws InterruptedException {
			ArrayList<String> obtainedList = new ArrayList<String>();
			List<WebElement> elementList = driver.findElements(By.xpath(prop.getProperty(Xpath)));
			for (WebElement we : elementList) {
				obtainedList.add(we.getText());
			}
			extentTest.log(LogStatus.INFO, "Results before applying  sorting: " + obtainedList);
			ArrayList<String> sortedList = new ArrayList<String>();
			for (String s : obtainedList) {
				sortedList.add(s);
			}
			Collections.sort(sortedList);
			extentTest.log(LogStatus.PASS, "Results after applying sorting: " + sortedList);

			if (sortedList.equals(obtainedList)) {
				extentTest.log(LogStatus.PASS, Text + " are alphabetically sorted");

			} else {
				extentTest.log(LogStatus.FAIL, Text + "are not alphabetically sorted");
				extentTest.log(LogStatus.FAIL, "Sorting Unsuccessful");

			}

		}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void Search(String Searchbox, String Text, String ClickSearchbutton) {
		try {
			extentTest.log(LogStatus.PASS, "Searching for: " + Text);
			EnterText(Searchbox, Text);
			click(ClickSearchbutton);
			Wait(Searchbox);
			if (isDisplayedValid("PricedTransaction_NoItemsMatch")) {
				extentTest.log(LogStatus.INFO, "No items match is displayed");
			} else {
				extentTest.log(LogStatus.PASS, "Results displayed for valid search input");
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.PASS, "Unable to search: " + Text);
			System.out.println("Unable to search: " + Text);
			extentTest.log(LogStatus.PASS, e.getMessage());
		}

	}

	// Average of numbers
	public double Average(String Xpath, String Xpath1, String Xpath2) {
		double average = 0;
		List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty(Xpath)));
		int Contents = Size.size();

		for (int i = 2; i < Contents + 2; i++) {
			String Text = GetTextMutipleParameters(Xpath1, i, Xpath2);
			double Value = DoubleOutofString(Text);
			average = average + Value;

		}
		average = average / Contents;
		System.out.println(average);
		return average;

	}

	// Make all the accounts UnFavorite

	public void MakeAccountsUnFavorite(String UnFavouriteAccounts) {
		String FavouriteAccount;
		String PayerNumber;
		try {
			LoginToAccount(UnFavouriteAccounts);
			// Click My account
			click("MyAccount");

			// Click My Profile
			click("MyAccount_MyProfile");

			// Click Account Access

			List<WebElement> Size = driver
					.findElements(By.xpath(prop.getProperty("UserNamg_Tabs_UserDetailsPage_Size")));
			int Count = Size.size();
			int count = 0;
			for (int k = 1; k <= Count; k++) {
				String Tabs = GetTextMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k,
						"UserNamg_Tabs_UserDetailsPage2");
				if (Tabs.equals("ACCOUNT ACCESS")) {
					count = 1;

					// Clicking Account Access tab
					clickMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k, "UserNamg_Tabs_UserDetailsPage2");
					Thread.sleep(5000);
					if (isDisplayedValid("UserMang__AccountAccess_FirstPayer")) {
						click("UserMang__AccountAccess_FirstPayerAccordian");
						Thread.sleep(3000);
						Size = driver.findElements(By.xpath(prop.getProperty(
								"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_count")));
						int Contents = Size.size();
						for (int j = 1; j <= Contents; j++) {
							String Attribute = driver.findElement(By.xpath(prop.getProperty(
									"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1")
									+ j
									+ prop.getProperty(
											"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2")))
									.getAttribute("class");

							// UnCheck favorite accounts

							if (Attribute.equals("c-button c-account-access-panel__item-favourite-button is-active")) {
								clickMutipleParameters(
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1",
										j,
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2");
							}

						}

					} else {
						extentTest.log(LogStatus.FAIL,
								"User is not able to view the information of those accounts for which access is provided");
						TakeScreenshot();
					}

					break;
				}
			}
			if (count == 0) {
				extentTest.log(LogStatus.FAIL, "'Account Access' tab is not displayed for user " + UnFavouriteAccounts);
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to unfavorite the accounts of user " + UnFavouriteAccounts);
		}

	}

	// Enabling all Permissions

	public void EnabingPermissions(String NonUUIDUserUnderAdminUser, String AdminUser) {
		try {
			LoginToAccount(AdminUser);
			click("ShellMenu_Xpath");
			// Validate 'Administration' tab is displayed on Shell Menu
			List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("ShellTabsSize")));
			int Contents = Size.size();
			int flag4 = 0;
			for (int k = 1; k <= Contents; k++) {
				String Text = GetTextMutipleParameters("ShellTabs1", k, "ShellTab2");
				// Validate if 'Administration' should be displayed
				if (Text.equalsIgnoreCase("Administration")) {
					flag4 = 1;
					extentTest.log(LogStatus.PASS, "'Adminstration' tab is there for user " + AdminUser);
					clickMutipleParameters("ShellTabs1", k, "ShellTab2");
					Thread.sleep(5000);
					// Validate User count should be greater than one on User management page
					Size = driver.findElements(By.xpath(prop.getProperty("UserMang_AccountsSize")));
					int Count = Size.size();
					if (Count > 1) {

						// Select the non-admin user having access to same payer(which user do not
						// require UUID to login)

						Size = driver.findElements(By.xpath(prop.getProperty("UserMang_AccountsSize")));
						Count = Size.size();
						int flag1 = 0;
						for (int m = 1; m <= Count; m++) {
							String UserName = GetTextMutipleParameters("UserMang_AccountsSize1", m,
									"UserMang_AccountsSize2");
							if (UserName.equalsIgnoreCase(NonUUIDUserUnderAdminUser)) {
								flag1 = 1;
								clickMutipleParameters("UserMang_AccountsSize1", m, "UserMang_AccountsSize2");
								// Validate if Permissions tab is there
								if (isDisplayedValid("UserMang_AccountAccess_Permissions_Tab")) {
									extentTest.log(LogStatus.PASS, "'Permissions' tab is there for user "
											+ NonUUIDUserUnderAdminUser + " under admin user " + AdminUser);
									TakeScreenshot();
									click("UserMang_AccountAccess_Permissions_Tab");

									click("UserMang_UserDetails_Permissions_AdministratorToggleButton");
									click("UserMang_UserDetails_Permissions_AdministratorToggleButton");

									// String
									// CheckedStatusAdministrator=driver.findElement(By.xpath(prop.getProperty("UserMang_UserDetails_Permissions_AdministratorToggleButton_GetAttribute"))).getAttribute("class");
									String CheckedStatusAdministrator = GetAttribute(
											"UserMang_UserDetails_Permissions_AdministratorToggleButton_GetAttribute",
											"class");

									// if Administrator tab is enabled
									if (CheckedStatusAdministrator.equals("c-control__input")) {
										click("UserMang_UserDetails_Permissions_AdministratorToggleButton");
										click("UserMang_UserDetails_Permissions_CardsToggleButton");
										click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
										click("UserMang_UserDetails_Permissions_ReportsToggleButton");
										click("UserMang_UserDetails_Permissions_FinanceToggleButton");
									} else {
										// if Administrator tab is disabled
										click("UserMang_UserDetails_Permissions_CardsToggleButton");
										click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
										click("UserMang_UserDetails_Permissions_ReportsToggleButton");
										click("UserMang_UserDetails_Permissions_FinanceToggleButton");
									}
									click("UserMang_UserDetails_Permissions_SavePermissions");

									if (isDisplayedValid("NewUser_UserDetails_CreateUser_GiveAccess_Button")) {
										click("NewUser_UserDetails_CreateUser_GiveAccess_Button");
									}

									// Validate the success message after Saving permissions

									if (isDisplayedValid("UserMang_Permissions_SuccessMessage")) {
										extentTest.log(LogStatus.PASS, "Success message displayed after saving permissions");
										TakeScreenshot();

										break;

									} else {
										extentTest.log(LogStatus.FAIL,
												"Success message not displayed after saving permissions");
										TakeScreenshot();
									}

									break;

								} else {
									extentTest.log(LogStatus.FAIL, "'Permissions' tab is not there for user "
											+ NonUUIDUserUnderAdminUser + " under admin user " + AdminUser);
									TakeScreenshot();

								}

								break;

							}

						}
						if (flag1 == 0) {
							extentTest.log(LogStatus.FAIL, "Unable to get " + NonUUIDUserUnderAdminUser
									+ " in UserManagement of " + AdminUser);
						}
						break;

					} else {
						extentTest.log(LogStatus.FAIL,
								"Only one user(i.e admin user) is there for user " + AdminUser + " in User management");

					}
					break;
				}
			}
			if (flag4 == 0) {
				extentTest.log(LogStatus.FAIL, "'Adminstration' tab is not there for user " + AdminUser);
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to change the permission of user " + NonUUIDUserUnderAdminUser
					+ " whose admin is " + AdminUser);
		}
	}

	// Disable given Permission

	public void DisablePermission(String NonUIDUserInUserManagement, String UserHavingMansourAsCompany,
			String Permission) {
		try {
			LoginToAccount(UserHavingMansourAsCompany);
			click("ShellMenu_Xpath");
			// Validate 'Administration' tab is displayed on Shell Menu
			List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("ShellTabsSize")));
			int Contents = Size.size();
			int flag4 = 0;
			for (int k = 1; k <= Contents; k++) {
				String Text = GetTextMutipleParameters("ShellTabs1", k, "ShellTab2");
				// Validate if 'Administration' should be displayed
				if (Text.equalsIgnoreCase("Administration")) {
					flag4 = 1;
					extentTest.log(LogStatus.PASS, "'Adminstration' tab is there for user " + UserHavingMansourAsCompany);
					clickMutipleParameters("ShellTabs1", k, "ShellTab2");
					Thread.sleep(3000);
					// Validate User count should be greater than one on User management page
					Size = driver.findElements(By.xpath(prop.getProperty("UserMang_AccountsSize")));
					int Count = Size.size();
					if (Count > 1) {

						// Select the non-admin user having access to same payer(which user do not
						// require UUID to login)

						Size = driver.findElements(By.xpath(prop.getProperty("UserMang_AccountsSize")));
						Count = Size.size();
						int flag1 = 0;
						for (int m = 1; m <= Count; m++) {
							String UserName = GetTextMutipleParameters("UserMang_AccountsSize1", m,
									"UserMang_AccountsSize2");
							if (UserName.equalsIgnoreCase(NonUIDUserInUserManagement)) {
								flag1 = 1;
								clickMutipleParameters("UserMang_AccountsSize1", m, "UserMang_AccountsSize2");
								// Validate if Permissions tab is there
								if (isDisplayedValid("UserMang_AccountAccess_Permissions_Tab")) {
									extentTest.log(LogStatus.PASS,
											"'Permissions' tab is there for user " + NonUIDUserInUserManagement
													+ " under admin user " + UserHavingMansourAsCompany);
									TakeScreenshot();
									click("UserMang_AccountAccess_Permissions_Tab");

									click("UserMang_UserDetails_Permissions_AdministratorToggleButton");
									click("UserMang_UserDetails_Permissions_AdministratorToggleButton");

									// String
									// CheckedStatusAdministrator=driver.findElement(By.xpath(prop.getProperty("UserMang_UserDetails_Permissions_AdministratorToggleButton_GetAttribute"))).getAttribute("class");
									String CheckedStatusAdministrator = GetAttribute(
											"UserMang_UserDetails_Permissions_AdministratorToggleButton_GetAttribute",
											"class");

									// if Administrator tab is enabled
									if (CheckedStatusAdministrator.equals("c-control__input")) {
										click("UserMang_UserDetails_Permissions_AdministratorToggleButton");
										if (Permission.equalsIgnoreCase("Cards")) {
											click("UserMang_UserDetails_Permissions_ReportsToggleButton");
											click("UserMang_UserDetails_Permissions_FinanceToggleButton");
										} else if (Permission.equalsIgnoreCase("Reports")) {
											click("UserMang_UserDetails_Permissions_CardsToggleButton");
											click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
											click("UserMang_UserDetails_Permissions_FinanceToggleButton");
										} else if (Permission.equalsIgnoreCase("Finance")) {
											click("UserMang_UserDetails_Permissions_CardsToggleButton");
											click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
											click("UserMang_UserDetails_Permissions_ReportsToggleButton");
										}
									} else {
										// if Administrator tab is disabled
										if (Permission.equalsIgnoreCase("Cards")) {
											click("UserMang_UserDetails_Permissions_ReportsToggleButton");
											click("UserMang_UserDetails_Permissions_FinanceToggleButton");
										} else if (Permission.equalsIgnoreCase("Reports")) {
											click("UserMang_UserDetails_Permissions_CardsToggleButton");
											click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
											click("UserMang_UserDetails_Permissions_FinanceToggleButton");
										} else if (Permission.equalsIgnoreCase("Finance")) {
											click("UserMang_UserDetails_Permissions_CardsToggleButton");
											click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
											click("UserMang_UserDetails_Permissions_ReportsToggleButton");
										}
									}
									click("UserMang_UserDetails_Permissions_SavePermissions");

									if (isDisplayedValid("NewUser_UserDetails_CreateUser_GiveAccess_Button")) {
										click("NewUser_UserDetails_CreateUser_GiveAccess_Button");
									}

									// Validate the success message after Saving permissions

									if (isDisplayedValid("UserMang_Permissions_SuccessMessage")) {
										extentTest.log(LogStatus.PASS, "Success message displayed after saving permissions");
										TakeScreenshot();

										break;

									} else {
										extentTest.log(LogStatus.FAIL,
												"Success message not displayed after saving permissions");
										TakeScreenshot();
									}

									break;

								} else {
									extentTest.log(LogStatus.FAIL,
											"'Permissions' tab is not there for user " + NonUIDUserInUserManagement
													+ " under admin user " + UserHavingMansourAsCompany);
									TakeScreenshot();

								}

								break;

							}

						}
						if (flag1 == 0) {
							extentTest.log(LogStatus.FAIL, "Unable to get " + NonUIDUserInUserManagement
									+ " in UserManagement of " + UserHavingMansourAsCompany);
						}
						break;

					} else {
						extentTest.log(LogStatus.FAIL, "Only one user(i.e admin user) is there for user "
								+ UserHavingMansourAsCompany + " in User management");

					}
					break;
				}
			}
			if (flag4 == 0) {
				extentTest.log(LogStatus.FAIL, "'Adminstration' tab is not there for user " + UserHavingMansourAsCompany);
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to change the permission of user " + NonUIDUserInUserManagement
					+ " whose admin is " + UserHavingMansourAsCompany);
		}

	}

	// Disable given Permission

	public void DisablePermissionOnPermissionsTab(String Permission) {
		try {

			click("UserMang_AccountAccess_Permissions_Tab");

			click("UserMang_UserDetails_Permissions_AdministratorToggleButton");
			click("UserMang_UserDetails_Permissions_AdministratorToggleButton");

			// String
			// CheckedStatusAdministrator=driver.findElement(By.xpath(prop.getProperty("UserMang_UserDetails_Permissions_AdministratorToggleButton_GetAttribute"))).getAttribute("class");
			String CheckedStatusAdministrator = GetAttribute(
					"UserMang_UserDetails_Permissions_AdministratorToggleButton_GetAttribute", "class");

			// if Administrator tab is enabled
			if (CheckedStatusAdministrator.equals("c-control__input")) {
				click("UserMang_UserDetails_Permissions_AdministratorToggleButton");
				if (Permission.equalsIgnoreCase("Cards")) {
					click("UserMang_UserDetails_Permissions_ReportsToggleButton");
					click("UserMang_UserDetails_Permissions_FinanceToggleButton");
				} else if (Permission.equalsIgnoreCase("Reports")) {
					click("UserMang_UserDetails_Permissions_CardsToggleButton");
					click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
					click("UserMang_UserDetails_Permissions_FinanceToggleButton");
				} else if (Permission.equalsIgnoreCase("Finance")) {
					click("UserMang_UserDetails_Permissions_CardsToggleButton");
					click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
					click("UserMang_UserDetails_Permissions_ReportsToggleButton");
				}
			} else {
				// if Administrator tab is disabled
				if (Permission.equalsIgnoreCase("Cards")) {
					click("UserMang_UserDetails_Permissions_ReportsToggleButton");
					click("UserMang_UserDetails_Permissions_FinanceToggleButton");
				} else if (Permission.equalsIgnoreCase("Reports")) {
					click("UserMang_UserDetails_Permissions_CardsToggleButton");
					click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
					click("UserMang_UserDetails_Permissions_FinanceToggleButton");
				} else if (Permission.equalsIgnoreCase("Finance")) {
					click("UserMang_UserDetails_Permissions_CardsToggleButton");
					click("UserMang_UserDetails_Permissions_ManageCardsToggleButton");
					click("UserMang_UserDetails_Permissions_ReportsToggleButton");
				}
			}
			click("UserMang_UserDetails_Permissions_SavePermissions");

			if (isDisplayedValid("NewUser_UserDetails_CreateUser_GiveAccess_Button")) {
				click("NewUser_UserDetails_CreateUser_GiveAccess_Button");
			}

			// Validate the success message after Saving permissions

			if (isDisplayedValid("UserMang_Permissions_SuccessMessage")) {
				extentTest.log(LogStatus.PASS, "Success message displayed after saving permissions");
				TakeScreenshot();

			} else {
				extentTest.log(LogStatus.FAIL, "Success message not displayed after saving permissions");
				TakeScreenshot();
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to disable the " + Permission + " permission");
		}

	}

	// Getting Text, it will return text

	public String GetText(String Xpath1) {
		WebElement Element = null;
		int timeout = 17;
		String Result = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(Xpath1))));
			Result = Element.getText();
			extentTest.log(LogStatus.INFO, "Text is : " + Result + " from Xpath: " + Xpath1);
			System.out.println("Text is : " + Result + " from Xpath: " + Xpath1);
			TakeScreenshot();
		} catch (Exception e) {
			if (e.toString().contains("stale element reference")
					|| e.toString().contains("element is not attached to the page")
					|| e.toString().contains("is not clickable") || e.toString().contains("Unable to locate element")) {
				try {
					Thread.sleep(12000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				extentTest.log(LogStatus.INFO, "Unable to get the text from Xpath: " + Xpath1 + ", trying once again");
				Result = Element.getText();
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to get Text from Xpath " + Xpath1);
				TakeScreenshot();
				extentTest.log(LogStatus.INFO, e.getMessage());
				System.out.println("Unable to get Text from Xpath " + Result);
				e.printStackTrace();

			}
		}
		return Result;

	}

	// Scroll screen till the element is found

	public void ElementLocatorOnPage(String Xpath) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Find element by link text and store in variable "Element"
		WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));

		// This will scroll the page till the element is found
		js.executeScript("arguments[0].scrollIntoView();", Element);
	}

	// Clear Text
	public void Clear(String Xpath) {
		try {
			driver.findElement(By.xpath(prop.getProperty(Xpath))).clear();
		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, e.getMessage());
		}

	}

	// Last word from the string
	public String LastWord(String Xpath) {
		String test = GetText(Xpath);
		String lastWord = test.substring(test.lastIndexOf(" ") + 1).replaceAll("[-+.^:,]", "");
		return lastWord;
	}

	// Go to URL
	public void GoToURL(String URL) {
		try {
			extentTest.log(LogStatus.INFO, "Navigating to " + prop.getProperty(URL));
			driver.navigate().to(prop.getProperty(URL));
			// Thread.sleep(2000);
			extentTest.log(LogStatus.INFO, "Navigated to " + prop.getProperty(URL));
			TakeScreenshot();
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to navigate to " + prop.getProperty(URL));
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to navigate to " + prop.getProperty(URL));
			e.printStackTrace();
		}

	}

	// RandomString
	public String RandomString() {

		char[] chars = "ABCDEFGHIJHKLMNOPQRSTUVWXYZ123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;

	}

	// Get Email from String

	public static String EmailRegex(String s) {
		String k = null;
		Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]+").matcher(s);
		while (m.find()) {
			k = m.group();
		}
		return k;
	}

	// Intersticial Page

	public void IntersticialPage(String UserMang_Intersticial_FirstAccount) throws InterruptedException {
		if ((driver.getTitle().contains("Interstitial"))) {
			List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("Interstitial_CompanySize")));
			int Count = Size.size();
			if (Count != 0) {
				// Get Company ID of the first company
				String Attribute = GetAttriMultiParameters("Interstitial_SetAsDefault1", "Interstitial_SetAsDefault2",
						1, "class");
				if (Attribute.equals("c-table__row c-table__row--body c-interstitial-results-panel__table--body-row")) {
					clickMutipleParameters("Interstitial_SetAsDefault1_Button", 1, "Interstitial_SetAsDefault2_Button");
				}
				clickMutipleParameters("Interstitial_GoToCompany1", 1, "Interstitial_GoToCompany2");
			} else {
				TakeScreenshot();
				extentTest.log(LogStatus.INFO, "No companies displayed at Intersticial Page");
			}
		}
	}

	// RandomString
	public String RandomNumbers() {

		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 1; i < 10; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;

	}

	// Click
	public static void click(String Xpath) {
		WebElement Element = null;
		int timeout = 40;
		WebDriverWait wait;
		try {
			wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Xpath))));
			ScrollToElement(Xpath);
			Element.click();

		} catch (Exception e) {
			try {
				Thread.sleep(7000);
				wait = new WebDriverWait(driver, timeout);
				Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Xpath))));
				ScrollToElement(Xpath);
				Element.click();
			} catch (InterruptedException e1) {
				Assert.fail("Unable to click " + Xpath);
				
			}

		}

	}

	// Drag N Drop
	public void DragnDrop(String Drag, String Drop) {

		// Element which needs to drag.
		WebElement From = driver.findElement(By.xpath(prop.getProperty(Drag)));

		// Element on which need to drop.
		WebElement To = driver.findElement(By.xpath(prop.getProperty(Drop)));

		// Using Action class for drag and drop.
		Actions act = new Actions(driver);

		// Dragged and dropped.
		// act.dragAndDrop(From, To).build().perform();
		// act.dragAndDropBy(From, 960, 450).build().perform();
		// act.clickAndHold(From).build().perform();
		// act.moveByOffset(960, 420).build().perform();;
		act.release().build().perform();

		// act.clickAndHold(From).build().perform();
		// act.moveToElement(To).build().perform();
		// act.moveToElement(To).moveByOffset(960, 420).build().perform();
	}

	public void clickMutipleParameters(String Xpath1, int i, String Xpath2) {
		WebElement Element = null;
		int timeout = 70;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(
					driver.findElement(By.xpath(prop.getProperty(Xpath1) + i + prop.getProperty(Xpath2)))));
			Element.click();
			Thread.sleep(1000);

		} catch (Exception e) {
			try {
				Element.click();
			} catch (Exception e1) {
				extentTest.log(LogStatus.FAIL, "Unable to click " + Xpath1 + i + Xpath2);
				Assert.fail("Unable to click " + Xpath1 + i + Xpath2);
				
			}

		}

	}

	// This peace of code is for MultiPayer
	// It will choose an account make it default account(copy the company number)
	// Then it will go to Customer Configuration, select the country, select the
	// company and it will enable all the market and customer configuration

	public void NonAgentCustomerConfiguration(String Account, String AdminAccount) throws InterruptedException {
		String CompanyID;
		String CountryPrefix;
		String Country = null;
		String Attribute;
		LoginToAccount(Account);

		click("ShellMenu_Xpath");
		click("Interstitial_ChangeCompany");
		Wait("Interstitial_SearchField");
		Thread.sleep(5000);
		List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("Interstitial_TotalCompanySize")));
		int Count = Size.size();
		if (Count != 0) {
			// Get Company ID of the first company
			CompanyID = GetTextMutipleParameters("Interstitial_TotalCompany1", 1, "Interstitial_TotalCompany2").trim();
			CountryPrefix = CompanyID.substring(0, 2);
			Attribute = GetAttriMultiParameters("Interstitial_SetAsDefault1", "Interstitial_SetAsDefault2", 1, "class");
			if (Attribute.equals("c-table__row c-table__row--body c-interstitial-results-panel__table--body-row")) {
				clickMutipleParameters("Interstitial_SetAsDefault1_Button", 1, "Interstitial_SetAsDefault2_Button");
				Thread.sleep(5000);
			}

			if (CountryPrefix.equals("DE")) {
				Country = "Germany";
			} else if (CountryPrefix.equals("CZ")) {
				Country = "Czech Republic";
			} else if (CountryPrefix.equals("SG")) {
				Country = "Singapore";
			} else if (CountryPrefix.equals("MY")) {
				Country = "Malaysia";
			} else if (CountryPrefix.equals("PL")) {
				Country = "Latvia";
			} else if (CountryPrefix.equals("GB")) {
				Country = "United Kingdom";
			}

			Wait_MultipleParameters("Interstitial_GoToCompany1", "Interstitial_GoToCompany2", 1);
			clickMutipleParameters("Interstitial_GoToCompany1", 1, "Interstitial_GoToCompany2");
			Thread.sleep(5000);
			// Customer Configuration

			LoginToAdminAccount(AdminAccount);
			Wait("MarketConfiguration_SelectMarketDropdown");
			select_Pagination("MarketConfiguration_SelectMarketDropdown", Country);
			EnterText("CustomerConfig_SearchCompany", CompanyID);
			click("CustomerConfig_SearchCompanyWindow_SearchButton");

			// Click 'Enter'

			// driver.findElement(By.xpath(prop.getProperty("CustomerConfig_SearchCompany"))).sendKeys(Keys.ENTER);

			click("CustomerConfig_SearchCompanyWindow_SearchResult_FirstRecord");

			// Get Selected Company number
			// String
			// CompanyNumber=GetText("CustomerConfig_SearchForACompany_FirstCompanyNumber");

			click("CustomerConfig_SearchCompanyWindow_SelectButton");

			Thread.sleep(3000);

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_chooseAddressDuringCardOrder")) {
				click("CustomerConfig_AfterMarketAndCompany_chooseAddressDuringCardOrder_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_orderCardsViaSpreadsheet")) {
				click("CustomerConfig_AfterMarketAndCompany_orderCardsViaSpreadsheet_Click");
			}

			/*
			 * if (!IsSelected(
			 * "CustomerConfig_AfterMarketAndCompany_multiAccountOrderViaSpreadsheet")) {
			 * click(
			 * "CustomerConfig_AfterMarketAndCompany_multiAccountOrderViaSpreadsheet_Click")
			 * ; }
			 */

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_cancelCardsViaSpreadsheet")) {
				click("CustomerConfig_AfterMarketAndCompany_cancelCardsViaSpreadsheet_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_MultiAccountcancelCardsViaSpreadsheet")) {
				click("CustomerConfig_AfterMarketAndCompany_MultiAccountcancelCardsViaSpreadsheet_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_fleetId")) {
				click("CustomerConfig_AfterMarketAndCompany_fleetId_Click");
			}

			// Validate turn ON Restriction toggle

			int flag = 0;
			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_Restrictions_EnableORDisable")) {
				click("CustomerConfig_AfterMarketAndCompany_Restrictions");

			} else {
				click("CustomerConfig_AfterMarketAndCompany_Restrictions");
				click("CustomerConfig_AfterMarketAndCompany_Restrictions");

			}

			click("CustomerConfig_AfterMarketAndCompany_CountryRestrictions");
			click("CustomerConfig_AfterMarketAndCompany_NetworkRestrictions");
			click("CustomerConfig_AfterMarketAndCompany_DiscountGroupRestrictions");
			// click("CustomerConfig_AfterMarketAndCompany_FuelStationRestrictions");
			click("CustomerConfig_AfterMarketAndCompany_RestrictionProfile");
			click("CustomerConfig_AfterMarketAndCompany_RestrictionBalances");

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_CardActivity")) {
				click("CustomerConfig_AfterMarketAndCompany_CardActivity_Click");
				click("CustomerConfig_AfterMarketAndCompany_DeclinedTrans_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_VolumeBasedPricing")) {

				click("CustomerConfig_AfterMarketAndCompany_VolumeBasedPricing_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_VolumeBasedBonus")) {
				click("CustomerConfig_AfterMarketAndCompany_VolumeBasedBonus_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_Charges")) {
				click("CustomerConfig_AfterMarketAndCompany_Charges_Click");
			}

			if (!IsSelected("CustomerConfig_AfterMarketAndCompany_FuelPriceLists")) {
				click("CustomerConfig_AfterMarketAndCompany_FuelPriceLists_Click");
			}

			if (IsSelected("CustomerConfig_AfterMarketAndCompany_Notifications")) {
				click("CustomerConfig_AfterMarketAndCompany_Notifications_Click");
				extentTest.log(LogStatus.INFO, "Notification are turned OFF");

			}

			click("CustomerConfig_AfterMarketAndCompany_Alerts");
			click("CustomerConfig_ApplyButton");
			click("CustomerConfig_ConfirmButton");
			Wait("CustomerConfig_ApplySettings_SuccessMessage");

			if (isDisplayedValid("CustomerConfig_ApplySettings_SuccessMessage")) {
				String Text = GetText("CustomerConfig_ApplySettings_SuccessMessage");
				if (Text.contains(CompanyID)) {
					flag = 1;
				} else {
					flag = 0;
					extentTest.log(LogStatus.FAIL,
							"Success message displayed, but company ID is not the same for which customer configuration is changed ");
					TakeScreenshot();
				}
			} else {
				flag = 0;
			}
			if (flag == 0) {
				extentTest.log(LogStatus.FAIL,
						"Success message not displayed after applying setting in Customer Configuration");
				TakeScreenshot();
			} else {
				extentTest.log(LogStatus.PASS, "Success message displayed after applying setting in Customer Configuration");

			}

			// Market Configuration

			click("MarketConfiguration_HeaderTab");
			Wait("MarketConfiguration_MarketLabel");
			Thread.sleep(5000);

			Wait("MarketConfiguration_SelectMarketDropdown");
			select_Pagination("MarketConfiguration_SelectMarketDropdown", Country);

			if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_RestrictionBalance")) {
				click("MarketConfiguration_MultipleCompanyAdminUser_RestrictionBalance_Click");
			}

			/*
			 * if
			 * (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_CreditBalance")) {
			 * click("MarketConfiguration_MultipleCompanyAdminUser_CreditBalance_Click"); }
			 */

			if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_VolumeRestriction")) {
				click("MarketConfiguration_MultipleCompanyAdminUser_VolumeRestriction_Click");
			}

			if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_ViewPIN")) {
				click("MarketConfiguration_MultipleCompanyAdminUser_ViewPIN_Click");
			}

			if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_EnablePIN")) {
				click("MarketConfiguration_MultipleCompanyAdminUser_EnablePIN_Click");
			}

			if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_RequireVRN")) {
				click("MarketConfiguration_MultipleCompanyAdminUser_RequireVRN_Click");
			}

			if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_TollsWarning")) {
				click("MarketConfiguration_MultipleCompanyAdminUser_TollsWarning_Click");
			}

			int Apply = 0;
			EnterText("MarketConfiguration_SigleCompanyAdminUser_Email", "Automation" + RandomString() + "@gmail.com");
			EnterText("MarketConfiguration_MultipleCompanyAdminUser_Phone", RandomNumbers());
			click("MarketConfiguration_SigleCompanyAdminUser_Apply");
			if (isDisplayedValid("MarketConfiguration_SuccessMessage")) {
				Apply = 1;
			} else {
				Apply = 0;
			}

			if (Apply == 0) {
				extentTest.log(LogStatus.FAIL,
						"Success message not displayed after applying setting in Market Configuration");
				TakeScreenshot();
			} else {
				extentTest.log(LogStatus.PASS, "Success message displayed after applying setting in Market Configuration");

			}

		} else {
			TakeScreenshot();
			extentTest.log(LogStatus.FAIL, "No Companies displayed at Intersticial Page");
		}
	}

	public String GetTextMutipleParameters(String Xpath1, int i, String Xpath2) {
		WebElement Element = null;
		int timeout = 20;
		String Result = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty(Xpath1) + i + prop.getProperty(Xpath2))));
			Result = Element.getText();
			extentTest.log(LogStatus.INFO, "Text is : " + Result);
			System.out.println("Text is : " + Result);
			TakeScreenshot();

		} catch (Exception e) {
			if (e.toString().contains("stale element reference")
					|| e.toString().contains("element is not attached to the page")
					|| e.toString().contains("is not clickable") || e.toString().contains("Unable to locate element")) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				extentTest.log(LogStatus.INFO,
						"Unable to get the text from Xpath: " + Xpath1 + i + Xpath2 + ", trying once again");
				Result = Element.getText();
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to get Text from Xpath: " + Xpath1 + i + Xpath2);
				TakeScreenshot();
				extentTest.log(LogStatus.INFO, e.getMessage());
				System.out.println("Unable to get Text from Xpath: " + Xpath1 + i + Xpath2);
				e.printStackTrace();

			}
		}
		return Result;

	}

	public String GetTextMutipleParameters_InvoicesTask(String Xpath1, int i, String Xpath2) {
		WebElement Element = null;
		int timeout = 10;
		String Result = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty(Xpath1) + i + prop.getProperty(Xpath2))));
			Result = Element.getText();
			extentTest.log(LogStatus.INFO, "Text is : " + Result);
			System.out.println("Text is : " + Result);
			TakeScreenshot();

		} catch (Exception e) {
			if (e.toString().contains("stale element reference")
					|| e.toString().contains("element is not attached to the page")
					|| e.toString().contains("is not clickable") || e.toString().contains("Unable to locate element")) {
				try {
					Thread.sleep(12000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				extentTest.log(LogStatus.INFO,
						"Unable to get the text from Xpath: " + Xpath1 + i + Xpath2 + ", trying once again");
				Result = Element.getText();
			} else {
				extentTest.log(LogStatus.INFO, "Unable to get Text from Xpath: " + Xpath1 + i + Xpath2);
				TakeScreenshot();
				extentTest.log(LogStatus.INFO, e.getMessage());
				System.out.println("Unable to get Text from Xpath: " + Xpath1 + i + Xpath2);
				e.printStackTrace();

			}
		}
		return Result;

	}

	// Get selected value of drop down

	public String SelecetdValueFromDropdown(String Xpath) {
		try {
			Select select = new Select(driver.findElement(By.xpath(prop.getProperty(Xpath))));
			WebElement option = select.getFirstSelectedOption();
			String defaultItem = option.getText();
			return defaultItem;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get text selected text  " + Xpath);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to enter Text");
			e.printStackTrace();
			return null;
		}
	}

	// EnterText

	public void EnterText(String Xpath, String Text) {
		WebElement Element = null;
		int timeout = 17;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(Xpath))));
			extentTest.log(LogStatus.INFO, "Entering " + Text + " in " + Xpath);
			ClearText(Xpath);
			Element.sendKeys(Text);
			extentTest.log(LogStatus.INFO, "Entering " + Text + " in " + Xpath);
			TakeScreenshot();

		} catch (Exception e) {
			if (e.toString().contains("stale element reference")
					|| e.toString().contains("element is not attached to the page")
					|| e.toString().contains("is not clickable") || e.toString().contains("Unable to locate element")) {
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				extentTest.log(LogStatus.INFO, "Unable to enter " + Text + " in " + Xpath + ", trying once again");
				Element.sendKeys(Text);
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to enter text in " + Xpath);
				TakeScreenshot();
				extentTest.log(LogStatus.INFO, e.getMessage());
				System.out.println("Unable to enter text in " + Xpath);
				e.printStackTrace();
			}

		}
	}

	// EnterText Multiple Parameters

	public void EnterTextMultipleParameters(String Property1, String Property2, int i, String Text) {
		try {
			extentTest.log(LogStatus.INFO, "Entering Text at " + Property1 + i + Property2);
			WebElement Element;
			WebDriverWait wait = new WebDriverWait(driver, 30);
			Element = wait.until(ExpectedConditions.visibilityOf(
					driver.findElement(By.xpath(prop.getProperty(Property1) + i + prop.getProperty(Property2)))));
			Element.sendKeys(Text);
			extentTest.log(LogStatus.INFO, "Entered Text " + Text + " at Xpath: " + Property1 + i + Property2);
			TakeScreenshot();
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to enter text: " + Text + " at Xpath: " + Property1 + i + Property2);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to enter Text: " + Text + " at Xpath: " + Property1 + i + Property2);
			e.printStackTrace();
		}
	}

	// Switch to Frame

	public void SwitchtoFrame(String Frame) {
		try {
			extentTest.log(LogStatus.INFO, "Switching to frame " + Frame);
			driver.switchTo().frame(Frame);
			extentTest.log(LogStatus.INFO, "Switched to frame " + Frame);
			TakeScreenshot();
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to switch to frame: " + Frame);
			TakeScreenshot();
			e.printStackTrace();
		}
	}

	// ClearText

	public void ClearText(String Xpath) {
		WebElement Element = null;
		int timeout = 17;
		try {
			extentTest.log(LogStatus.INFO, "Clearing Text: " + Xpath);
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(Xpath))));
			Element.clear();
			TakeScreenshot();
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to clear text from " + Xpath);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to Clear Text from " + Xpath);
			e.printStackTrace();
		}
	}

	// EnterTextData

	public void EnterTextData(String Xpath, String Text) {
		try {
			extentTest.log(LogStatus.INFO, "Entering Text at " + Xpath + "and text is " + Text);
			driver.findElement(By.xpath(prop.getProperty(Xpath))).sendKeys(Text);
			extentTest.log(LogStatus.INFO, "Entered Text " + Text);
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to enter text at  " + Xpath + "and text " + Text);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to enter Text");
			e.printStackTrace();
		}
	}

	// GetElement
	public WebElement getElement(String locatorkey) {
		WebElement e = null;
		extentTest.log(LogStatus.INFO, "Get Element " + locatorkey);
		try {
			if (locatorkey.endsWith("_Xpath")) {
				e = driver.findElement(By.xpath(prop.getProperty(locatorkey)));
			} else if (locatorkey.endsWith("_id")) {
				e = driver.findElement(By.id(prop.getProperty(locatorkey)));
			} else if (locatorkey.endsWith("_name")) {
				e = driver.findElement(By.name(prop.getProperty(locatorkey)));
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to get Element " + locatorkey);
				TakeScreenshot();
			}
		} catch (Exception ex) {
			// fail the test and report the error
			ReportFail(ex.getMessage());
			ex.printStackTrace();
			// Assert.fail("Failed the test"+ex.getMessage() );
		}
		extentTest.log(LogStatus.INFO, "Located element " + locatorkey);
		return e;
	}

	// Popup

	public void popup(String Value) {
		try {
			extentTest.log(LogStatus.INFO, "Handling Popup");
			// Switching to Alert
			Alert alert = driver.switchTo().alert();
			// Capturing alert message.
			/*
			 * String alertMessage= driver.switchTo().alert().getText(); // Displaying alert
			 * message System.out.println(alertMessage); // Accepting alert
			 * if(Value.equalsIgnoreCase("YES")) {
			 */
			alert.accept();
			/*
			 * }else if(Value.equalsIgnoreCase("NO")) { alert.dismiss();
			 */

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to handle Popup");
			TakeScreenshot();

		}
	}

	// Quit
	public void Quit() {
		driver.quit();
	}

	// GetTextAttribute--------It will get text from editable text field

	public String GetTextAttribute(String Xpath) {
		try {
			extentTest.log(LogStatus.INFO, "Getting Text for " + Xpath);
			WebElement Value = driver.findElement(By.xpath(prop.getProperty(Xpath)));
			String Text = Value.getAttribute("value");
			TakeScreenshot();
			return Text;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get text for  " + Xpath);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to enter Text");
			e.printStackTrace();
			return null;
		}
	}

	// GetTextAttribute--------It will get text from editable text field

	public String GetAttribute(String Xpath, String Value) {
		try {
			extentTest.log(LogStatus.INFO, "Getting Text for " + Xpath);
			WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));
			String Text = Element.getAttribute(Value);
			TakeScreenshot();
			return Text;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get text for  " + Xpath);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to enter Text");
			e.printStackTrace();
			return null;
		}
	}

	// GetTextAttribute--------It will get text from editable text field

	public String GetAttributeOfLoader(String Xpath, String Value) {
		try {
			extentTest.log(LogStatus.INFO, "Getting Text for " + Xpath);
			WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));
			String Text = Element.getAttribute(Value);
			TakeScreenshot();
			return Text;
		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, "Unable to get text for  " + Xpath);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to enter Text");
			e.printStackTrace();
			return null;
		}
	}

	// GetAttriMultiParameters

	public String GetAttriMultiParameters(String Xpath1, String Xpath2, int i, String Attri) {
		String Attribute = "";
		try {
			WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath1) + i + prop.getProperty(Xpath2)));
			Attribute = Element.getAttribute(Attri);
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get the attribute from " + Xpath1 + i + Xpath2);
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to get the attribute from " + Xpath1 + i + Xpath2);
			e.printStackTrace();
			return null;
		}
		return Attribute;
	}

	// ################################################ Validations
	// ################################################//

	// VerifyTitle
	public boolean VerifyTitle() {

		return false;
	}

	// VerifyText
	public boolean VerifyText(String Expected, String ActuaText) {

		String ExpectedResult = getElement(Expected).getText().trim();
		String ActualResult = prop.getProperty(ActuaText);
		if (ExpectedResult.equals(ActualResult)) {
			extentTest.log(LogStatus.INFO, Expected + " and " + ActuaText + "are equal");
			return true;
		} else {
			extentTest.log(LogStatus.FAIL, Expected + " and " + ActuaText + "are not equal");
			TakeScreenshot();
			return false;
		}
	}

	public void GetTextandDisplay(String Xpath, String Value) {
		try {
			extentTest.log(LogStatus.INFO, "Getting " + Xpath + " and  it's " + Value);
			String Text1 = driver.findElement(By.xpath(prop.getProperty(Xpath))).getText();
			String Text2 = driver.findElement(By.xpath(prop.getProperty(Value))).getText();
			extentTest.log(LogStatus.INFO, Text1 + " is " + Text2);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to find " + Xpath + " and it's value " + Value);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to find " + Xpath + " and it's value " + Value);
			e.printStackTrace();
			Assert.fail();
		}

	}

	// isElementPresent
	public boolean isElementPresent(String locatorkey) {
		List<WebElement> elementList = null;
		extentTest.log(LogStatus.INFO, "Validating Element " + locatorkey + " present ");
		if (locatorkey.endsWith("_Xpath")) {
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorkey)));
		} else if (locatorkey.endsWith("_id")) {
			elementList = driver.findElements(By.id(prop.getProperty(locatorkey)));
		} else if (locatorkey.endsWith("_name")) {
			elementList = driver.findElements(By.name(prop.getProperty(locatorkey)));
		} else {
			extentTest.log(LogStatus.FAIL, "Element: " + locatorkey + " not found");
			TakeScreenshot();
			// ReportFail("Locator not found"+ locatorkey );
			Assert.fail("Locator not found" + locatorkey);
		}
		extentTest.log(LogStatus.INFO, "Element " + locatorkey + " found");
		if (elementList.size() == 0)
			return false;
		else
			return true;
	}

	// IsDisplayed

	public boolean isDisplayed(String locatorkey) {
		try {
			driver.findElement(By.xpath(prop.getProperty(locatorkey))).isDisplayed();
			extentTest.log(LogStatus.PASS, locatorkey + " is displayed");
			System.out.println(locatorkey + " is displayed");
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, locatorkey + " is not displayed");
			// TakeScreenshot();
			e.printStackTrace();
			return false;
		}

	}

	// IsSelected

	public boolean IsSelected(String locatorkey) {
		try {

			if (driver.findElement(By.xpath(prop.getProperty(locatorkey))).isSelected()) {
				extentTest.log(LogStatus.INFO, locatorkey + " is Selected");
				TakeScreenshot();
				return true;
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, "Unable to locate this element " + locatorkey);
			TakeScreenshot();
			e.printStackTrace();
		}
		return false;

	}

	// Is Enabled
	public boolean isEnabled(String Xpath) {
		WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));
		if (Element.isEnabled()) {
			extentTest.log(LogStatus.PASS, Xpath + " is Enabled");
			TakeScreenshot();
			return true;
		} else {
			extentTest.log(LogStatus.PASS, Xpath + " is Disabled");
			TakeScreenshot();
			return false;
		}

	}

	// Take Screenshot
	public static void TakeScreenshot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "//screenshots//" + screenshotFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// This will put the screenshot in report
		extentTest.log(LogStatus.INFO, "Screenshot"
				+ extentTest.addScreenCapture(System.getProperty("user.dir") + "//screenshots//" + screenshotFile));

	}

	// IsDisplayedValid(For Validation- Irrespective of PASS/FAIL)

	public boolean isDisplayedValid(String locatorkey) {
		WebElement Element = null;
		int timeout = 5;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(locatorkey))));
			extentTest.log(LogStatus.INFO, locatorkey + " is displayed");
			TakeScreenshot();
			System.out.println(locatorkey + " is displayed");
			return true;
		} catch (Exception e) {

			extentTest.log(LogStatus.INFO, locatorkey + " is not displayed");
			System.out.println(locatorkey + " is not displayed");
			TakeScreenshot();
			e.printStackTrace();
			return false;
		}

	}

	// IsDisplayedCount(For Validation- Irrespective of PASS/FAIL)

	public boolean isDisplayedCount(String locatorkey) {
		try {
			driver.findElement(By.xpath(prop.getProperty(locatorkey))).isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	// Count

	public void Count(String locatorkey) {

		for (int i = 1; i <= 150; i++) {
			if (isDisplayedCount(locatorkey) == true) {
				i++;
			} else {
				break;
			}
		}

	}

	// IsDisplayedValid(For Validation- Irrespective of PASS/FAIL for Long
	// Xpaths)

	public boolean isDisplayedValidation(String locatorkey1, String locatorkey2, int i) {
		WebElement Element = null;
		int timeout = 5;

		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath(prop.getProperty(locatorkey1) + i + prop.getProperty(locatorkey2))));
			extentTest.log(LogStatus.INFO, locatorkey1 + i + locatorkey2 + " is displayed");
			return true;

		} catch (Exception e) {
			extentTest.log(LogStatus.PASS, locatorkey1 + i + locatorkey2 + " is not displayed");
			return false;

		}

	}

	// Select from drop down

	public String select(String Locatorkey, int Index) {
		String ValueSelected = "";
		try {
			extentTest.log(LogStatus.INFO, "Clicking element " + Locatorkey + " and selecting " + Index);
			Select dropdown = new Select(driver.findElement(By.xpath(prop.getProperty(Locatorkey))));
			List<WebElement> elementCount = dropdown.getOptions();
			int iSize = elementCount.size();
			if (iSize == 0) {
				extentTest.log(LogStatus.INFO, "No options in the dropdown to select");
			} else {
				dropdown.selectByIndex(Index);
				Wait(Locatorkey);
				extentTest.log(LogStatus.INFO, "Clicked " + Locatorkey + " and selected " + Index + " value");
				ValueSelected = SelectedValueFromDropDown(Locatorkey);
				System.out.println("Selected Value is: " + ValueSelected);
				TakeScreenshot();

			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to click " + Locatorkey + " and Select " + Locatorkey);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to click " + Locatorkey + " and Select " + Locatorkey);

		}

		return ValueSelected.trim();
	}

	// Select from drop down (Pagination)

	public int select_Pagination(String Locatorkey, String Text) {
		String TextSelected = "";
		WebElement Element = null;
		int timeout = 8;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(Locatorkey))));
			extentTest.log(LogStatus.INFO, "Clicking element " + Locatorkey + " and selecting " + Text);
			Select dropdown = new Select(Element);
			List<WebElement> elementCount = dropdown.getOptions();
			int iSize = elementCount.size();
			dropdown.selectByVisibleText(Text);
			Thread.sleep(3000);
			TextSelected = SelectedValueFromDropDown(Locatorkey);
			System.out.println("Selected Value is: " + TextSelected);
			extentTest.log(LogStatus.INFO, "Clicked " + Locatorkey + " and selected " + TextSelected);
			TakeScreenshot();
			return iSize;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to click " + Locatorkey + " and Select " + Locatorkey);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to click " + Locatorkey + " and Select " + Locatorkey);
			return 0;

		}

	}

	// Get Size of Drop down

	public int DropDownSize(String Locatorkey) {
		int iSize = 0;
		WebElement Element = null;
		int timeout = 8;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(Locatorkey))));
			Select dropdown = new Select(Element);
			List<WebElement> elementCount = dropdown.getOptions();
			iSize = elementCount.size();
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get the size for  drop down" + Locatorkey);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to get the size for  drop down" + Locatorkey);

		}
		return iSize;

	}

	// Language Change

	public void LanguageChange(String Account) throws InterruptedException {
		LoginToAccount(Account);
		Wait("MyAccount");
		click("MyAccount");
		Thread.sleep(5000);
		Wait("MyAccount_MyProfile");
		click("MyAccount_MyProfile");
		Thread.sleep(10000);
		Wait("SiteSetting_Xpath");
		click("SiteSetting_Xpath");

		String SelectedLanguage = SelecetdValueFromDropdown("SiteSettings_LanguageDropdown");
		System.out.println(SelectedLanguage);
		if (!SelectedLanguage.equalsIgnoreCase("English - United States")) {

			select_Pagination("SiteSettings_LanguageDropdown", "English - United States");
			select_Pagination("SiteSettings_DatFormatDropdown", "MM/DD/YY");
			click("SiteSettings_Monday");
			click("SiteSettings_Sunday");
			click("SiteSettings_SaveSettings");

		} else {
			select_Pagination("SiteSettings_DatFormatDropdown", "MM/DD/YY");
			click("SiteSettings_Monday");
			click("SiteSettings_Sunday");
			click("SiteSettings_SaveSettings");
		}

	}

	// Select from drop down count

	public int DropDownCount(String Locatorkey) {
		try {
			extentTest.log(LogStatus.INFO, "Clicking element " + Locatorkey + " and getting drop down counts");
			Select dropdown = new Select(driver.findElement(By.xpath(prop.getProperty(Locatorkey))));
			Thread.sleep(2000);
			List<WebElement> elementCount = dropdown.getOptions();
			int iSize = elementCount.size();
			for (int i = 1; i < iSize; i++) {
				String sValue = elementCount.get(i).getText();
			}
			return iSize;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get the Drpdown count for Locator key: " + Locatorkey);
			TakeScreenshot();
			extentTest.log(LogStatus.INFO, e.getMessage());
			System.out.println("Unable to get the Drpdown count for Locator key: " + Locatorkey);
			return 0;

		}

	}

	// ######################################## Reporting
	// ################################################//

	public void ReportPass(String msg) {
		extentTest.log(LogStatus.PASS, msg);
	}

	public void ReportFail(String msg) {
		extentTest.log(LogStatus.FAIL, msg);
		TakeScreenshot();
		Assert.fail();

	}

	// ######################################## Wait
	// ################################################//

	public void waitForPageToLoad() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String state = (String) js.executeScript("return document.readtstate");

		while (!state.equals("complete")) {
			try {
				Thread.sleep(2000);
				state = (String) js.executeScript("return document.readtstate");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// Wait
	public static void Wait(String Locator) {
		WebElement Element = null;
		int timeout = 30;
		try {
			Thread.sleep(2000);
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Locator))));
			extentTest.log(LogStatus.INFO, Locator + " is displayed");
			System.out.println(Locator + " is displayed");
		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, Locator + " is not displayed");
			System.out.println(Locator + " is not displayed");

		}
	}

	// Wait For
	public static void WaitFor(String Locator, int Sec) {
		WebElement Element = null;
		int timeout = Sec;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Locator))));
			extentTest.log(LogStatus.INFO, Locator + " is displayed");
			System.out.println(Locator + " is displayed");
		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, Locator + " is not displayed");
			System.out.println(Locator + " is not displayed");

		}
	}

	// Wait
	public void Wait_MultipleParameters(String Locator1, String Locator2, int i) {
		WebElement Element = null;
		int timeout = 20;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty(Locator1) + i + prop.getProperty(Locator2))));
			extentTest.log(LogStatus.INFO, Locator1 + i + Locator2 + " is displayed");

		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, Locator1 + i + Locator2 + " is not displayed");

		}
	}

	// Click and Wait
	public void ClickandWaitForCondition(String TobeClicked, String WillBeDisplayed) {

		int count = 5;
		for (int i = 0; i < count; i++) {
			getElement(TobeClicked).click();
			if (isElementPresent(WillBeDisplayed))
				break;

		}
	}

	// Refresh

	public void Refresh() {
		driver.navigate().refresh();

	}

	// EnterTextAndValidate

	/*
	 * public void EnterTextAndValidate(String Test, String Xpath, String Text) {
	 * try { int StrLengthFromExcel = Text.length();
	 * test.log(LogStatus.INFO,"Entering Text at " + Xpath + ", and text is " + Text
	 * + " whose length is: " + StrLengthFromExcel);
	 * driver.findElement(By.xpath(prop.getProperty(Xpath))).sendKeys(Text);
	 * WebElement EnteredTextPath =
	 * driver.findElement(By.xpath(prop.getProperty(Xpath))); String EnteredText =
	 * EnteredTextPath.getAttribute("value"); int StrLength = EnteredText.length();
	 * test.log(LogStatus.INFO, "Entered Text is : " + EnteredText +
	 * " whose length is: " + StrLength); if
	 * (isDisplayedValid("NameOnCard_ErrorMsg1") ||
	 * isDisplayedValid("NameOnCard_ErrorMsg2")) { test.log(LogStatus.PASS, Test +
	 * " is throwing proper error message for invalid input"); } else {
	 * test.log(LogStatus.FAIL, Test +
	 * " is not throwing proper error message for invalid input"); }
	 * 
	 * } catch (Exception e) { test.log(LogStatus.FAIL, "Unable to enter text at " +
	 * Xpath + "and text " + Text); TakeScreenshot(); test.log(LogStatus.INFO,
	 * e.getMessage()); System.out.println("Unable to enter text at  " + Xpath +
	 * "and text " + Text); e.printStackTrace(); } }
	 */

	public void CSVDownload(String Date, String Click) throws ParseException, InterruptedException {

		try {
			extentTest.log(LogStatus.PASS, "Getting: " + Date);
			click("CSV_DownloadButton");
			Thread.sleep(1000);
			click(Click);
			Thread.sleep(1000);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date DatetoBeSelected = sdf.parse(Date);

			// Selecting Month from drop down
			try {
				sdf = new SimpleDateFormat("MMMM");
				String monthToBeSelected = sdf.format(DatetoBeSelected).trim();
				extentTest.log(LogStatus.INFO, "Selecting Month: " + monthToBeSelected);
				System.out.println(sdf.format(DatetoBeSelected));
				Select Month = new Select(driver.findElement(By.xpath(prop.getProperty("MonthDisplayed"))));
				Month.selectByVisibleText(monthToBeSelected);
				extentTest.log(LogStatus.INFO, "Selected Month: " + monthToBeSelected);
			} catch (Exception e) {
				extentTest.log(LogStatus.FAIL, "Unable to Select Month");
				extentTest.log(LogStatus.INFO, e.getMessage());
			}

			// Selecting Year from drop down
			try {
				sdf = new SimpleDateFormat("yyyy");
				String yeartoBeSelected = sdf.format(DatetoBeSelected).trim();
				extentTest.log(LogStatus.INFO, "Selecting Year: " + yeartoBeSelected);
				System.out.println(sdf.format(DatetoBeSelected));
				Select Year = new Select(driver.findElement(By.xpath(prop.getProperty("YearDisplayed"))));
				Year.selectByVisibleText(yeartoBeSelected);
				extentTest.log(LogStatus.INFO, "Selected Year: " + yeartoBeSelected);
			} catch (Exception e) {
				extentTest.log(LogStatus.FAIL, "Unable to Select Year");
				extentTest.log(LogStatus.INFO, e.getMessage());
			}

			// Selecting Date
			try {
				sdf = new SimpleDateFormat("dd");
				String DateToBeSelected = sdf.format(DatetoBeSelected).trim();
				extentTest.log(LogStatus.INFO, "Selecting Date: " + DateToBeSelected);
				System.out.println(sdf.format(DatetoBeSelected));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("window.scrollBy(0,250)", "");
				driver.findElement(By.xpath(prop.getProperty("Date1") + DateToBeSelected + prop.getProperty("Date2")))
						.click();
				extentTest.log(LogStatus.INFO, "Selected Date: " + DateToBeSelected);
			} catch (Exception e) {
				extentTest.log(LogStatus.FAIL, "Unable to Select Date");
				extentTest.log(LogStatus.INFO, e.getMessage());

			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to insert: " + Date);
		}

	}

	// Scroll to element

	public static void ScrollToElement(String Xpath) {
		int timeout = 30;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Find element by link text and store in variable "Element"
			// WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			WebElement Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Xpath))));
			// This will scroll the page till the element is found
			js.executeScript("arguments[0].scrollIntoView();", Element);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to scroll to element " + Xpath);

		}

	}

	// Scroll to element

	public static void ScrollToElementMultipleParameters(String Xpath1, int i, String Xpath2) {
		int timeout = 30;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Find element by link text and store in variable "Element"
			// WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			WebElement Element = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(prop.getProperty(Xpath1) + i + prop.getProperty(Xpath2))));
			// This will scroll the page till the element is found
			js.executeScript("arguments[0].scrollIntoView();", Element);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to scroll to element " + Xpath1 + i + Xpath2);

		}

	}

	// Color validation
	public String Color(String Xpath) throws Exception {

		String color = driver.findElement(By.xpath(prop.getProperty(Xpath))).getCssValue("color");
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");

		int hexValue1 = Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2 = Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3 = Integer.parseInt(hexValue[2]);

		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);

		return actualColor;
	}

	// Get Integer Of Text
	public int GetIntegerOfText(String Text) {

		String str = Text;
		String numberOnly = str.replaceAll("[^0-9]", "");
		int result = Integer.parseInt(numberOnly);
		return result;
	}

	// GetURL
	public String GetURL() {
		try {
			String GetURL = driver.getCurrentUrl();
			extentTest.log(LogStatus.PASS, "URL: " + GetURL);
			TakeScreenshot();
			return GetURL;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get URL");
			TakeScreenshot();
			return null;
		}
	}

	// Get Drop down Values

	public String DropDownVlues(String Xpath) {
		WebElement dropdown = driver.findElement(By.xpath(prop.getProperty(Xpath)));
		Select select = new Select(dropdown);
		List<WebElement> options = select.getOptions();
		for (WebElement item : options) {

			String Options = item.getText();
			return Options;
		}
		return null;
	}

	// Get Selected value from drop down

	public String SelectedValueFromDropDown(String Xpath) {
		try {
			Select archiveList = new Select(driver.findElement(By.xpath(prop.getProperty(Xpath))));
			String selectedValue = archiveList.getFirstSelectedOption().getText();
			return selectedValue;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get the selected value from drop down");
			return null;
		}

	}

	// Sort Alphabetically

	public void SortAlphabetically(String Xpath) {
		try {
			ArrayList<String> obtainedList = new ArrayList<String>();
			List<WebElement> elementList = driver.findElements(By.xpath(prop.getProperty(Xpath)));
			for (WebElement we : elementList) {
				// obtainedList.add(we.getText());
				obtainedList.add(we.getAttribute("innerText").trim());

			}
			System.out.println(obtainedList);
			extentTest.log(LogStatus.INFO, "Obtained List : " + obtainedList);
			ArrayList<String> sortedList = new ArrayList<String>();
			for (String s : obtainedList) {
				sortedList.add(s);
			}
			Collections.sort(sortedList);

			System.out.println(sortedList);
			extentTest.log(LogStatus.INFO, "Sorted List : " + sortedList);
			// Assert.assertTrue(sortedList.equals(obtainedList));
			if (sortedList.equals(obtainedList)) {
				extentTest.log(LogStatus.PASS, "Sorted Alphabetically");
			} else {
				extentTest.log(LogStatus.FAIL, "Not Sorted Alphabetically");
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to Sort Alphabetically");

		}

	}

	// Login to Admin account
	public void LoginToAdminAccount(String AccountWithSetasDefault) throws InterruptedException {
		GoToURL("URL_Admin");
		Thread.sleep(2000);
		String URL = driver.getCurrentUrl();
		if (URL.contains("scolnextgenpprd")) {
			Wait("UserName_Xpath");
			EnterText("UserName_Xpath", AccountWithSetasDefault);
			EnterText("Password_Xpath", prop.getProperty("Password_WithOutJanrain"));
			click("LoginButton");
		} else {
			EnterText("UserName_Xpath", AccountWithSetasDefault);
			EnterText("Password_Xpath", prop.getProperty("Password_WithOutJanrain"));
			click("LoginButton");
		}

		WaitFor("CustomerConfiguration_Button", 8);
		click("CustomerConfiguration_Button");
		WaitFor("MarketConfiguration_SelectMarketDropdown", 8);

	}

	public void CustomerConfigEnableAll_Generic(String User, String Admin) throws InterruptedException {

		String CompanyNumber = null;
		String CountryPrefix = null;
		String CountryName = null;
		LoginToAccount(User);

		click("MyAccount");
		click("MyAccount_MyProfile");
		Thread.sleep(3000);

		List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("UserNamg_Tabs_UserDetailsPage_Size")));
		int Count = Size.size();
		int count = 0;
		for (int m = 1; m <= Count; m++) {
			String Tabs = GetTextMutipleParameters("UserNamg_Tabs_UserDetailsPage1", m,
					"UserNamg_Tabs_UserDetailsPage2");
			if (Tabs.equals("ACCOUNT ACCESS")) {

				count = 1;
				clickMutipleParameters("UserNamg_Tabs_UserDetailsPage1", m, "UserNamg_Tabs_UserDetailsPage2");
				WaitFor("UserMang_AccountAccess_FirstPayerName", 10);
				String CompanyName = GetText("UserMang_AccountAccess_FirstPayerName");

				String CompanyNumberEdit = GetText("UserMang_AccountAccess_FirstPayerNumber").trim();
				CompanyNumber = CompanyNumberEdit.substring(CompanyNumberEdit.length() - 10);
				CountryPrefix = CompanyNumber.substring(0, 2);

				if (CountryPrefix.equals("DE")) {
					CountryName = "Germany";
				} else if (CountryPrefix.equals("CZ")) {
					CountryName = "Czech Republic";
				} else if (CountryPrefix.equals("SG")) {
					CountryName = "Singapore";
				} else if (CountryPrefix.equals("MY")) {
					CountryName = "Malaysia";
				} else if (CountryPrefix.equals("PL")) {
					CountryName = "Latvia";
				}

				LoginToAdminAccount(Admin);
				Wait("MarketConfiguration_SelectMarketDropdown");
				select_Pagination("MarketConfiguration_SelectMarketDropdown", CountryName);
				EnterText("CustomerConfig_SearchCompany", CompanyNumber);

				// click("CustomerConfig_SearchCompanyWindow_SearchButton");

				// Click 'Enter'
				driver.findElement(By.xpath(prop.getProperty("CustomerConfig_SearchCompany"))).sendKeys(Keys.ENTER);

				if (isDisplayedValid("CustomerConfig_NoMatchingCompanies")) {
					int k = 1;
					while (k <= 20) {
						EnterText("CustomerConfig_SearchCompany", CompanyName);
						click("CustomerConfig_SearchCompanyWindow_SearchButton");
						Thread.sleep(2000);
						List<WebElement> CompanySize = driver
								.findElements(By.xpath(prop.getProperty("CustomerConfig_Company_Size")));
						int CompanyCount = CompanySize.size();
						int flag1 = 0;
						for (int i = 1; i <= CompanyCount; i++) {
							String SearchResultCompanyName = GetTextMutipleParameters("CustomerConfig_Company1", i,
									"CustomerConfig_Company2");
							if (CompanyName.equals(SearchResultCompanyName)) {
								clickMutipleParameters("CustomerConfig_Button_Company1", i,
										"CustomerConfig_Button_Company2");
								flag1 = 1;
								break;
							} else if (SearchResultCompanyName.equalsIgnoreCase("CR6238")) {
								clickMutipleParameters("CustomerConfig_Button_Company1", i,
										"CustomerConfig_Button_Company2");
								flag1 = 1;
								break;
							}

						}
						if (flag1 == 0) {
							if (isDisplayedValid("CustomerConfig_LoadMoreResults")) {
								click("CustomerConfig_LoadMoreResults");
							} else {
								extentTest.log(LogStatus.FAIL,
										"Company: " + CompanyName + " not found in Customer Configuration");
								break;
							}
						}

						if (flag1 == 1) {
							break;
						}
					}
				} else {
					Wait("CustomerConfig_SearchCompanyWindow_SearchResult_FirstRecord");
					click("CustomerConfig_SearchCompanyWindow_SearchResult_FirstRecord");
				}

				// Get Selected Company number
				// String
				// CompanyNumber=GetText("CustomerConfig_SearchForACompany_FirstCompanyNumber");

				click("CustomerConfig_SearchCompanyWindow_SelectButton");

				Thread.sleep(3000);

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_chooseAddressDuringCardOrder")) {
					click("CustomerConfig_AfterMarketAndCompany_chooseAddressDuringCardOrder_Click");
				}

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_orderCardsViaSpreadsheet")) {
					click("CustomerConfig_AfterMarketAndCompany_orderCardsViaSpreadsheet_Click");
				}

				/*
				 * if (!IsSelected(
				 * "CustomerConfig_AfterMarketAndCompany_multiAccountOrderViaSpreadsheet")) {
				 * click(
				 * "CustomerConfig_AfterMarketAndCompany_multiAccountOrderViaSpreadsheet_Click")
				 * ; }
				 */

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_cancelCardsViaSpreadsheet")) {
					click("CustomerConfig_AfterMarketAndCompany_cancelCardsViaSpreadsheet_Click");
				}

				/*
				 * if (!IsSelected(
				 * "CustomerConfig_AfterMarketAndCompany_MultiAccountcancelCardsViaSpreadsheet")
				 * ) { click(
				 * "CustomerConfig_AfterMarketAndCompany_MultiAccountcancelCardsViaSpreadsheet_Click"
				 * ); }
				 */

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_fleetId")) {
					click("CustomerConfig_AfterMarketAndCompany_fleetId_Click");
				}

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_LifetimeCards")) {
					click("CustomerConfig_AfterMarketAndCompany_LifetimeCards_Click");
				}
				
				// Validate turn ON Restriction toggle

				int flag = 0;
				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_Restrictions_EnableORDisable")) {
					click("CustomerConfig_AfterMarketAndCompany_Restrictions");

				} else {
					click("CustomerConfig_AfterMarketAndCompany_Restrictions");
					click("CustomerConfig_AfterMarketAndCompany_Restrictions");

				}

				click("CustomerConfig_AfterMarketAndCompany_CountryRestrictions");
				click("CustomerConfig_AfterMarketAndCompany_NetworkRestrictions");
				click("CustomerConfig_AfterMarketAndCompany_DiscountGroupRestrictions");
				// click("CustomerConfig_AfterMarketAndCompany_FuelStationRestrictions");
				click("CustomerConfig_AfterMarketAndCompany_RestrictionProfile");
				click("CustomerConfig_AfterMarketAndCompany_RestrictionBalances");

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_CardActivity")) {
					click("CustomerConfig_AfterMarketAndCompany_CardActivity_Click");
					click("CustomerConfig_AfterMarketAndCompany_DeclinedTrans_Click");
				}

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_VolumeBasedPricing")) {

					click("CustomerConfig_AfterMarketAndCompany_VolumeBasedPricing_Click");
				}

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_VolumeBasedBonus")) {
					click("CustomerConfig_AfterMarketAndCompany_VolumeBasedBonus_Click");
				}

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_Charges")) {
					click("CustomerConfig_AfterMarketAndCompany_Charges_Click");
				}

				if (!IsSelected("CustomerConfig_AfterMarketAndCompany_FuelPriceLists")) {
					click("CustomerConfig_AfterMarketAndCompany_FuelPriceLists_Click");
				}

				if (IsSelected("CustomerConfig_AfterMarketAndCompany_Notifications")) {
					click("CustomerConfig_AfterMarketAndCompany_Notifications_Click");
					extentTest.log(LogStatus.INFO, "Notification are turned OFF");

				}

				click("CustomerConfig_AfterMarketAndCompany_Alerts");
				click("CustomerConfig_ApplyButton");
				click("CustomerConfig_ConfirmButton");

				Wait("CustomerConfig_ApplySettings_SuccessMessage");
				if (isDisplayedValid("CustomerConfig_ApplySettings_SuccessMessage")) {
					flag = 1;
				} else {
					flag = 0;
				}
				if (flag == 0) {
					extentTest.log(LogStatus.FAIL,
							"Success message not displayed after applying setting in Customer Configuration");
					TakeScreenshot();
				} else {
					extentTest.log(LogStatus.PASS,
							"Success message displayed after applying setting in Customer Configuration");
					TakeScreenshot();

				}

				// Market Configuration

				click("MarketConfiguration_HeaderTab");
				Wait("MarketConfiguration_MarketLabel");
				Thread.sleep(5000);

				select_Pagination("MarketConfiguration_SelectMarketDropdown", CountryName);

				if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_RestrictionBalance")) {
					click("MarketConfiguration_MultipleCompanyAdminUser_RestrictionBalance_Click");
				}

				/*
				 * if
				 * (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_CreditBalance")) {
				 * click("MarketConfiguration_MultipleCompanyAdminUser_CreditBalance_Click"); }
				 */

				if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_VolumeRestriction")) {
					click("MarketConfiguration_MultipleCompanyAdminUser_VolumeRestriction_Click");
				}

				if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_ViewPIN")) {
					click("MarketConfiguration_MultipleCompanyAdminUser_ViewPIN_Click");
				}

				if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_EnablePIN")) {
					click("MarketConfiguration_MultipleCompanyAdminUser_EnablePIN_Click");
				}

				if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_RequireVRN")) {
					click("MarketConfiguration_MultipleCompanyAdminUser_RequireVRN_Click");
				}

				if (!IsSelected("MarketConfiguration_MultipleCompanyAdminUser_TollsWarning")) {
					click("MarketConfiguration_MultipleCompanyAdminUser_TollsWarning_Click");
				}

				int Apply = 0;
				EnterText("MarketConfiguration_SigleCompanyAdminUser_Email",
						"Automation" + RandomString() + "@gmail.com");
				EnterText("MarketConfiguration_MultipleCompanyAdminUser_Phone", RandomNumbers());
				click("MarketConfiguration_SigleCompanyAdminUser_Apply");
				if (isDisplayedValid("MarketConfiguration_SuccessMessage")) {
					Apply = 1;
				} else {
					Apply = 0;
				}

				if (Apply == 0) {
					extentTest.log(LogStatus.FAIL,
							"Success message not displayed after applying setting in Market Configuration");
					TakeScreenshot();
				} else {
					extentTest.log(LogStatus.PASS,
							"Success message displayed after applying setting in Market Configuration");

				}
				break;
			}
		}
		if (count == 0) {
			extentTest.log(LogStatus.FAIL, "'Account Access' tab is not displayed for user " + User);
		}
	}

	// Login to account

	public void LoginToAccount(String AccountWithSetasDefault) throws InterruptedException {
		GoToURL("URL");
		String URL = driver.getCurrentUrl();
		if (URL.contains("scolnextgenpprd")) {
			EnterText("Janrein_SignIn_EmailID", AccountWithSetasDefault);
			EnterText("Janrein_Password", prop.getProperty("Password_WithJanrain"));
			click("Janrein_SignInButton");
		} else {
			EnterText("UserName_Xpath", AccountWithSetasDefault);
			EnterText("Password_Xpath", prop.getProperty("Password_WithOutJanrain"));
			Thread.sleep(2000);
			click("LoginButton");
			Thread.sleep(10000);
		}

		IntersticialPage("UserMang_Intersticial_FirstAccount");
	}

	
		// Login to SIT2

		public void LoginToSIT2(String AccountWithSetasDefault) throws InterruptedException {
			GoToURL("SIT2_URL");
			String URL = driver.getCurrentUrl();
			if (URL.contains("scolnextgenpprd")) {
				EnterText("Janrein_SignIn_EmailID", AccountWithSetasDefault);
				EnterText("Janrein_Password", prop.getProperty("Password_WithJanrain"));
				click("Janrein_SignInButton");
			} else {
				EnterText("UserName_Xpath", AccountWithSetasDefault);
				EnterText("Password_Xpath", prop.getProperty("Password_WithOutJanrain"));
				click("LoginButton");
			}

			IntersticialPage("UserMang_Intersticial_FirstAccount");
		}
	
	
	// Get Title

	public String GetTitle() {

		String Title = "";
		try {
			Title = driver.getTitle();

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get title of the page");
			TakeScreenshot();
			extentTest.log(LogStatus.FAIL, e.getMessage());
		}

		return Title;

	}

	// Login to account(For Janrain flow scenarios)

	public void LoginToJanrainAccount(String AccountWithSetasDefault) throws InterruptedException {
		GoToURL("URL_Janrain");
		EnterText("Janrein_SignIn_EmailID", AccountWithSetasDefault);
		EnterText("Janrein_Password", prop.getProperty("Password_Janrain"));
		click("Janrein_SignInButton");
		IntersticialPage("UserMang_Intersticial_FirstAccount");
	}

	// Logout from account

	public void LogoutFromAccount() throws InterruptedException {
		try {
			// Before Logging into Preprod, log out from current account
			click("MyAccount");
			Wait("Account_SignOut");
			// click("Account_SignOut");
			driver.findElement(By.xpath(prop.getProperty("Account_SignOut"))).click();
			Thread.sleep(5000);
			driver.switchTo().alert().accept();
			Wait("Janrein_SignIn_EmailID");
			Refresh();
			Wait("Janrein_SignIn_EmailID");

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to Log out from account");
			extentTest.log(LogStatus.FAIL, e.getMessage());
		}
	}

	// Logout from account

	public void LogOutFromAdminAccount() throws InterruptedException {
		try {
			// Before Logging into Preprod, log out from current account
			click("Admin_SignOut");
			Refresh();

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to Log out from account");
			extentTest.log(LogStatus.FAIL, e.getMessage());
		}
	}

	// Get Selected value from drop down

	public boolean isChecked(String Xpath) {
		try {
			if (driver.findElement(By.xpath(prop.getProperty(Xpath))).isSelected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get the selected value");
			return false;
		}

	}

	// Get Selected value from drop down

	public boolean FontWight_Bold(String Xpath) {
		String Value = "";
		try {
			Value = GetText(Xpath);
			String fontWeight = driver.findElement(By.xpath(prop.getProperty(Xpath))).getCssValue("font-weight");

			boolean isBold = "bold".equals(fontWeight) || "bolder".equals(fontWeight)
					|| Integer.parseInt(fontWeight) > 299;
					extentTest.log(LogStatus.PASS, "Displayed text " + "'" + Value + "'" + " is bold");
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Displayed text " + "'" + Value + "'" + " is not bold");
			return false;
		}

	}

	// Return Integer out of a string

	public int IntegerOutofString(String Value) {
		try {
			String str = Value;
			String numberOnly = str.replaceAll("[^0-9]", "");
			int result = Integer.parseInt(numberOnly);
			return result;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get integer out of string");
			return 0;

		}

	}

	// Is Valid Date Format

	public static boolean isValidFormat(String format, String value) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
			if (!value.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date != null;
	}

	// Return Double out of a string

	public double DoubleOutofString(String str) {
		double value = 0.0;
		try {
			// String str =GetTextMutipleParameters(Xpath1, i, Xpath2);
			value = Double.parseDouble(str.replaceAll("[^0-9\\.]+", ""));
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to get double from string");

		}
		return value;
	}

	// Color Coding (Color change after mouse houvering)

	public String ColorCoding_MouseHouver(String Locator) {
		String bgColor = "";
		try {
			WebElement ServicesCharges = driver.findElement(By.xpath(prop.getProperty(Locator)));

			Actions builder = new Actions(driver);
			Action mouseOverHome = builder.moveToElement(ServicesCharges).build();

			bgColor = ServicesCharges.getCssValue("background-color");
			System.out.println("Before hover: " + bgColor);

			mouseOverHome.perform();

			bgColor = ServicesCharges.getCssValue("background-color");
			System.out.println("After hover: " + bgColor);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to check color change functionallity after mouse houvering");
		}
		return bgColor;

	}

	// Navigate back

	public void NavigateBack() {
		try {
			driver.navigate().back();

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to navigate back");
		}

	}

	// isAccessable
	public void isAccessable(String Xpath) {
		WebElement Element = null;
		int timeout = 17;
		try {
			/*
			 * Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30,
			 * TimeUnit.SECONDS).pollingEvery(2,
			 * TimeUnit.SECONDS).ignoring(NoSuchElementException.class); Element =
			 * wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(prop.
			 * getProperty(Xpath)))));
			 */
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Xpath))));
			extentTest.log(LogStatus.INFO, "Clicking " + Xpath);
			ScrollToElement(Xpath);
			Element.click();
			Thread.sleep(1500);
			extentTest.log(LogStatus.INFO, "Clicked " + Xpath);
			extentTest.log(LogStatus.PASS,
					"Restriction Balances is enable in the customer configuration and toggle turned ON.");
			TakeScreenshot();

		} catch (Exception e) {

			extentTest.log(LogStatus.FAIL, "Unable to click " + Xpath);
			extentTest.log(LogStatus.FAIL,
					"Restriction Balances is disable in the customer configuration and unable to turn toggle ON.");
			TakeScreenshot();

		}
	}

	// isNotAccessable
	public void isNotAccessable(String Xpath) {
		WebElement Element = null;
		int timeout = 17;
		try {
			/*
			 * Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30,
			 * TimeUnit.SECONDS).pollingEvery(2,
			 * TimeUnit.SECONDS).ignoring(NoSuchElementException.class); Element =
			 * wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(prop.
			 * getProperty(Xpath)))));
			 */
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Xpath))));
			extentTest.log(LogStatus.INFO, "Clicking " + Xpath);
			ScrollToElement(Xpath);
			Element.click();
			Thread.sleep(1500);
			extentTest.log(LogStatus.INFO, "Clicked " + Xpath);
			extentTest.log(LogStatus.FAIL, "Restriction Balances is not disable in the customer configuration.");
			TakeScreenshot();

		} catch (Exception e) {

			extentTest.log(LogStatus.PASS, "Unable to click " + Xpath);
			extentTest.log(LogStatus.PASS, "Restriction Balances is disable in the customer configuration.");
			TakeScreenshot();

		}
	}

	// isClickable

	public void isClickable(String Xpath) {
		WebElement Element = null;
		int timeout = 17;
		try {
			/*
			 * Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30,
			 * TimeUnit.SECONDS).pollingEvery(2,
			 * TimeUnit.SECONDS).ignoring(NoSuchElementException.class); Element =
			 * wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(prop.
			 * getProperty(Xpath)))));
			 */
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(Xpath))));
			extentTest.log(LogStatus.INFO, "Clicking " + Xpath);
			ScrollToElement(Xpath);
			Element.click();
			Thread.sleep(1500);
			extentTest.log(LogStatus.INFO, "Clicked " + Xpath);
			TakeScreenshot();

		} catch (Exception e) {

			extentTest.log(LogStatus.FAIL, "Unable to click " + Xpath);
			TakeScreenshot();

		}
	}

	// IsDisabled
	public boolean isDisabled(String Xpath) {
		WebElement Element = driver.findElement(By.xpath(prop.getProperty(Xpath)));
		if (Element.isEnabled()) {
			extentTest.log(LogStatus.INFO, Xpath + " is not Disable");
			TakeScreenshot();
			return true;
		} else {
			extentTest.log(LogStatus.INFO, Xpath + " is Disable");
			TakeScreenshot();
			return false;
		}

	}

	/*
	 * // Is Enabled public boolean isEnabled(String Xpath) { WebElement Element =
	 * driver.findElement(By.xpath(prop.getProperty(Xpath))); if
	 * (Element.isEnabled()) { test.log(LogStatus.INFO, Xpath + " is Enabled");
	 * TakeScreenshot(); return true; } else { test.log(LogStatus.INFO, Xpath +
	 * " is Disabled"); TakeScreenshot(); return false; }
	 * 
	 * }
	 */
	// RandomChar
	public String RandomChar() {

		char[] chars = "ABCDEFGHIJHKLMNOPQRSTUVWXYZ".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 1; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;

	}

	// IsSorted : This method will return false if you list is not sorted

	public boolean isSorted(List<String> list) {
		boolean sorted = true;
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1).compareTo(list.get(i)) > 0)
				sorted = false;
		}

		return sorted;
	}

	// File download PDF/ZIP

	public boolean isFileDownloaded_PDF_ZIP_CSV(String dirPath, String pdf, String zip, String csv) {
		boolean flag = false;
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			flag = false;
			extentTest.log(LogStatus.INFO, "No files exists in download folder");
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(pdf) || files[i].getName().contains(zip)
					|| files[i].getName().contains(csv)) {
				extentTest.log(LogStatus.PASS, " FILE downloaded successfully");
				flag = true;
			}
		}
		return flag;
	}

	// Language Change and make accounts unfavourite Default Company

	public void LanguageChangeAndMakeAccountsUnFavorite_DefaultCompany(String Account) throws InterruptedException {

		try {
			LoginToAccount(Account);
			click("MyAccount");
			click("MyAccount_MyProfile");
			click("SiteSetting_Xpath");
			// select("SiteSettings_LanguageDropdown", Index)
			String SelectedLanguage = SelecetdValueFromDropdown("SiteSettings_LanguageDropdown");
			System.out.println(SelectedLanguage);
			if (!SelectedLanguage.equalsIgnoreCase("English - United States")) {
				// select("SiteSettings_LanguageDropdown",0);
				select_Pagination("SiteSettings_LanguageDropdown", "English - United States");
				select_Pagination("SiteSettings_DatFormatDropdown", "MM/DD/YY");
				click("SiteSettings_Monday");
				click("SiteSettings_Sunday");
				click("SiteSettings_SaveSettings");

			} else {
				select_Pagination("SiteSettings_DatFormatDropdown", "MM/DD/YY");
				click("SiteSettings_Monday");
				click("SiteSettings_Sunday");
				click("SiteSettings_SaveSettings");
			}

			// Click Account Access

			List<WebElement> Size = driver
					.findElements(By.xpath(prop.getProperty("UserNamg_Tabs_UserDetailsPage_Size")));
			int Count = Size.size();
			int count = 0;
			for (int k = 1; k <= Count; k++) {
				String Tabs = GetTextMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k,
						"UserNamg_Tabs_UserDetailsPage2");
				if (Tabs.equals("ACCOUNT ACCESS")) {
					count = 1;
					Thread.sleep(2000);

					// Clicking Account Access tab
					clickMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k, "UserNamg_Tabs_UserDetailsPage2");
					Thread.sleep(5000);
					if (isDisplayedValid("UserMang_AccountAccess_FirstPayerAccordion_SinglePayerUser")) {
						click("UserMang_AccountAccess_FirstPayerAccordion_SinglePayerUser");
						Thread.sleep(10000);
						Wait("UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Size");
						Size = driver.findElements(By.xpath(prop.getProperty(
								"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Size")));
						int Contents = Size.size();
						for (int j = 1; j <= Contents; j++) {
							String Attribute = driver.findElement(By.xpath(prop.getProperty(
									"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1")
									+ j
									+ prop.getProperty(
											"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2")))
									.getAttribute("class");

							Attribute = Attribute.replaceAll("\\s", "");

							// UnCheck favorite accounts

							if (Attribute.equals("c-buttonc-account-access-panel__item-favourite-buttonis-active")) {
								ScrollToElementMultipleParameters(
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1",
										j,
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2");
								clickMutipleParameters(
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1",
										j,
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2");
							}
						}

					}
					break;
				}
			}
			if (count == 0) {
				extentTest.log(LogStatus.FAIL, "'Account Access' tab is not displayed for user " + Account);
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to unfavorite the accounts of user " + Account);
		}
	}

	// Language Change & Make Accounts Favorite DefaultCompany

	public void LanguageChangeAndMakeAccountsFavorite_DefaultCompany(String Account) throws InterruptedException {
		try {
			LoginToAccount(Account);
			click("MyAccount");
			click("MyAccount_MyProfile");
			click("SiteSetting_Xpath");
			// select("SiteSettings_LanguageDropdown", Index)
			String SelectedLanguage = SelecetdValueFromDropdown("SiteSettings_LanguageDropdown");
			System.out.println(SelectedLanguage);
			if (!SelectedLanguage.equalsIgnoreCase("English - United States")) {
				// select("SiteSettings_LanguageDropdown",0);
				select_Pagination("SiteSettings_LanguageDropdown", "English - United States");
				select_Pagination("SiteSettings_DatFormatDropdown", "MM/DD/YY");
				click("SiteSettings_Monday");
				click("SiteSettings_Sunday");
				click("SiteSettings_SaveSettings");

			} else {
				select_Pagination("SiteSettings_DatFormatDropdown", "MM/DD/YY");
				click("SiteSettings_Monday");
				click("SiteSettings_Sunday");
				click("SiteSettings_SaveSettings");
			}

			// Click Account Access

			List<WebElement> Size = driver
					.findElements(By.xpath(prop.getProperty("UserNamg_Tabs_UserDetailsPage_Size")));
			int Count = Size.size();
			int count = 0;
			for (int k = 1; k <= Count; k++) {
				String Tabs = GetTextMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k,
						"UserNamg_Tabs_UserDetailsPage2");
				if (Tabs.equals("ACCOUNT ACCESS")) {
					count = 1;

					// Clicking Account Access tab
					clickMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k, "UserNamg_Tabs_UserDetailsPage2");
					Thread.sleep(5000);
					if (isDisplayedValid("UserMang_AccountAccess_FirstPayerAccordion_SinglePayerUser")) {
						click("UserMang_AccountAccess_FirstPayerAccordion_SinglePayerUser");
						Thread.sleep(5000);
						Wait("UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Size");
						Size = driver.findElements(By.xpath(prop.getProperty(
								"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Size")));
						int Contents = Size.size();
						for (int j = 1; j <= Contents; j++) {
							String Attribute = driver.findElement(By.xpath(prop.getProperty(
									"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1")
									+ j
									+ prop.getProperty(
											"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2")))
									.getAttribute("class");

							Attribute = Attribute.replaceAll("\\s", "");
							// Check unfavorite accounts

							if (!(Attribute.equals("c-buttonc-account-access-panel__item-favourite-buttonis-active"))) {
								clickMutipleParameters(
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1",
										j,
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2");
								Thread.sleep(5000);
							}
						}
					}

					break;
				}
			}
			if (count == 0) {
				extentTest.log(LogStatus.FAIL, "'Account Access' tab is not displayed for user ");
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to unfavorite the accounts of user " + Account);
		}

	}

	// Make all the accounts Favorite for default company

	public void MakeAccountsFavorite_DefaultCompany(String UnFavouriteAccounts) {
		try {
			LoginToAccount(UnFavouriteAccounts);
			// Click My account
			click("MyAccount");
			// Click My Profile
			click("MyAccount_MyProfile");
			Thread.sleep(10000);
			// Click Account Access

			List<WebElement> Size = driver
					.findElements(By.xpath(prop.getProperty("UserNamg_Tabs_UserDetailsPage_Size")));
			int Count = Size.size();
			int count = 0;
			for (int k = 1; k <= Count; k++) {
				String Tabs = GetTextMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k,
						"UserNamg_Tabs_UserDetailsPage2");
				if (Tabs.equals("ACCOUNT ACCESS")) {
					count = 1;

					// Clicking Account Access tab
					clickMutipleParameters("UserNamg_Tabs_UserDetailsPage1", k, "UserNamg_Tabs_UserDetailsPage2");
					Thread.sleep(5000);
					Wait("UserMang_AccountAccess_FirstPayer_MultiPayerUser");
					if (isDisplayedValid("UserMang_AccountAccess_FirstPayer_MultiPayerUser")) {
						click("UserMang_AccountAccess_FirstPayerAccordion_MultiPayerUser");
						Thread.sleep(5000);
						Wait("UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Size");
						Size = driver.findElements(By.xpath(prop.getProperty(
								"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Size")));
						int Contents = Size.size();
						for (int j = 1; j <= Contents; j++) {
							String Attribute = driver.findElement(By.xpath(prop.getProperty(
									"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1")
									+ j
									+ prop.getProperty(
											"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2")))
									.getAttribute("class");

							// Check unfavorite accounts

							if (!(Attribute
									.equals("c-button c-account-access-panel__item-favourite-button is-active"))) {
								clickMutipleParameters(
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute1",
										j,
										"UserMang_AccountAccess_MyCompaniesIn_FirstPayer_FirstAccount_Star_Attribute2");
								Thread.sleep(5000);
							}
						}
					} else {
						extentTest.log(LogStatus.FAIL,
								"User is not able to view the information of those accounts for which access is provided");
						TakeScreenshot();
					}

					break;
				}
			}
			if (count == 0) {
				extentTest.log(LogStatus.FAIL, "'Account Access' tab is not displayed for user " + UnFavouriteAccounts);
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to unfavorite the accounts of user " + UnFavouriteAccounts);
		}

	}

	public void CreateNewUserAndLogin(String Username, String ShellAdmin) throws InterruptedException {
		click("ShellMenu_Xpath");
		// Validation 'Administration' tab is displayed under shell Menu
		int Administration = 0;
		List<WebElement> Size = driver.findElements(By.xpath(prop.getProperty("ShellTabsSize")));
		int Contents = Size.size();
		for (int i = 1; i <= Contents; i++) {
			String Text = GetTextMutipleParameters("ShellTabs1", i, "ShellTab2");
			// Validate if 'Administration' tab should be displayed
			if (Text.equalsIgnoreCase("Administration")) {
				Administration = 1;
				// Clicking 'Administration' tab
				clickMutipleParameters("ShellTabs1", i, "ShellTab2");
				Thread.sleep(3000);

				click("AddUserButton");

				// Validate if New user Text is displayed

				if (isDisplayedValid("NewUser_Text")) {

					EnterText("NewUser_UserDetails_FirstName", "ShellAutomation " + RandomString());
					EnterText("NewUser_UserDetails_LastName", "Automation " + RandomString());
					EnterText("NewUser_UserDetails_Email", "Automation" + RandomString() + "@gmail.com");
					String Email = GetAttribute("NewUser_UserDetails_Email", "value");

					String AdminstratorAttribute = GetAttribute("NewUser_UserDetails_AdministratorTab_Attribute",
							"class");

					click("NewUser_UserDetails_AdministratorTab_Click");

					// Validate user should get Create user option and access account page should
					// not get displayed

					if (isDisplayedValid("NewUser_UserDetails_CreateUser_Button")) {
						extentTest.log(LogStatus.PASS,
								"User get 'Create user' button and access account page should not get displayed for Multi payer , Single country , Single account  "
										+ Username);
						TakeScreenshot();
						click("NewUser_UserDetails_CreateUser_Button");
					} else {
						extentTest.log(LogStatus.FAIL,
								"User do not get 'Create user' button and access account page should not get displayed for Multi payer , Single country , Single account  "
										+ Username);
						TakeScreenshot();
					}

					if (isDisplayedValid("AdminAccessPopUp")) {
						extentTest.log(LogStatus.PASS, "'Give administrator access?' Pop-up displayed");
						TakeScreenshot();
						click("NewUser_UserDetails_CreateUser_GiveAccess_Button");
					} else {
						extentTest.log(LogStatus.FAIL, "'Give administrator access?' Pop-up not displayed");
						TakeScreenshot();
					}

					LoginToAdminAccount(ShellAdmin);
					click("ConfigSIT_UserManagementTab");
					EnterText("ConfigSIT_UserManagementTabEmail", Email);
					click("ConfigSIT_UserManagementTabEmailSearchButton");
					String UUID = GetText("ConfigSIT_UserManagementTab_UUID");
					extentTest.log(LogStatus.INFO, "UUID: " + UUID);
					LoginToAccount(UUID);
					if (isDisplayedValid("Cokies_PopUp")) {
						extentTest.log(LogStatus.INFO, "Cookies Pop-up displayed");
						click("CookiesPopUp_AgreeAndProceedButton");

					} else {
						extentTest.log(LogStatus.INFO, "Cookies Pop-up not displayed");
						TakeScreenshot();
					}

				} else {
					extentTest.log(LogStatus.FAIL, "Unable to navigate to 'Add User' page for user " + Username);
					TakeScreenshot();
				}

				break;
			}
		}
		if (Administration == 0) {
			extentTest.log(LogStatus.FAIL, "Administration tab is not there for user " + Username + " under shell menu");
		}

	}

	// Delete Files from folder

	public void DeleteFiles(String FolderPath) {
		File directory = new File(FolderPath);

		// Get all files in directory
		File[] files = directory.listFiles();
		for (File file : files) {
			// Delete each file
			if (!file.delete()) {
				// Failed to delete file
				extentTest.log(LogStatus.FAIL, "Failed to delete " + file);
			}

		}

	}

	// Restriction Profile

	// Add Cards to Profile
	public void AddCardToProfile() {
		try {
			String VehicleSize = "";
			GoToURL("Restrictions_URL");
			Wait("Restrictions_CreateNewProfile");
			click("Restrictions_CreateNewProfile");
			Thread.sleep(5000);
			Wait("Restrictions_ProfileName");
			EnterText("Restrictions_ProfileName", "Shell" + RandomString());
			EnterText("Restrictions_ProfileDesc", "Test Profile");

			String PurchaseCategory = GetText("CreateNewRestrictionPrfile_LastPurchaseCategory");

			if (isDisplayedValid("CreateNewRestrictionPrfile_FirstVehicleSize")) {
				VehicleSize = GetText("CreateNewRestrictionPrfile_LastVehicleSize");
			}
			ClickPurchaseCategoryAndVehicleType(VehicleSize, PurchaseCategory);

			// Day & time restrictions section
			click("RestrictProfiles_CreateNewProfile_Restrictions_DayNTimeRestrEditButton");
			try {
				List<WebElement> Records = driver.findElements(By.xpath(
						prop.getProperty("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton")));
				int RecordsSize = Records.size();
				System.out.println(RecordsSize);
				for (int i = 1; i <= RecordsSize - 2; i++) {
					// driver.findElement(By.xpath(prop.getProperty("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton1")+i+prop.getProperty("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton2"))).click();
					clickMutipleParameters("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton1", i,
							"Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton2");
					TakeScreenshot();
				}
			} catch (Exception e) {
				extentTest.log(LogStatus.FAIL, "Unable to click Days of the week in Day and Time restrictions");
				e.printStackTrace();
			}
			click("Restriction_CreateNewProfile_SetRestrictions_SaveChangesButton");
			String RestrictionProfile = GetText("Restrictions_FirstProfile").trim();
			click("RestrictProfiles_NoProfilesApplied");
			click("RestrictProfiles_NoProfilesAppliedPage_Filter");
			click("RestrictProfiles_NoProfApplPage_Filter_Active");
			click("RestrictProfiles_NoProfApplPage_Filter_Apply");
			select_Pagination("RestrictProfiles_NoProfApplPage_PaginationFilter", "100");
			Thread.sleep(10000);

			// Clicking the check box of Card having desired purchase category
			List<WebElement> ActiveRecordsCheckBoxes = driver.findElements(
					By.xpath(prop.getProperty("RestrictProfiles_NoProfApplPage_NoVehicleType_PurchaseCategoryCount")));
			int ActiveRecordsSize = ActiveRecordsCheckBoxes.size();
			for (int i = 1; i <= 1; i++) {
				int k = 0;
				int l = 0;

				while (k != 3) {
					for (int j = 1; j <= ActiveRecordsSize; j++) {
						String PurchaseCateg = GetTextMutipleParameters(
								"RestrictProfiles_NoProfApplPage_NoVehicleType_PurchaseCategoryCount1", j,
								"RestrictProfiles_NoProfApplPage_NoVehicleType_PurchaseCategoryCount2");
						String VehicleCateg = GetTextMutipleParameters(
								"RestrictProfiles_NoProfApplPage_VehiclesizeCount1", j,
								"RestrictProfiles_NoProfApplPage_VehiclesizeCount2").trim();
						System.out.println("J: " + j);

						if (PurchaseCateg.trim().contains(PurchaseCategory.trim())
								&& VehicleCateg.trim().contains(VehicleSize.trim())) {
							JavascriptExecutor js = (JavascriptExecutor) driver;
							// Find element by link text and store in variable "Element"
							WebElement Element = driver.findElement(By.xpath(prop
									.getProperty("RestrictProfiles_NoProfApplPage_TotalActRecords_Chckboxes1") + j
									+ prop.getProperty("RestrictProfiles_NoProfApplPage_TotalActRecords_Chckboxes2")));
							// This will scroll the page till the element is found
							js.executeScript("arguments[0].scrollIntoView();", Element);
							clickMutipleParameters("RestrictProfiles_NoProfApplPage_TotalActRecords_Chckboxes1", j,
									"RestrictProfiles_NoProfApplPage_TotalActRecords_Chckboxes2");
							k++;

							if (k == 3) {
								break;
							}
						}

						if (j == 100 && k < 3) {
							k = 0;
							click("NextPage");
							Thread.sleep(10000);
						}
						if (k == 3)
							break;
					}
				}
				break;
			}
			click("RestrictProfiles_NoProfApplPage_TotalActRecords_MoveToProfile");
			Thread.sleep(5000);
			select_Pagination("Restrict_GetResProf_MeatBall_MoveCardsToProf_ChooseResProf", RestrictionProfile);
			Thread.sleep(2500);
			click("RestrictProfiles_NoProfApplPage_MoveCardsToProf_MoveCards");

			Thread.sleep(4000);
			if (isDisplayedValid("Restrict_AddCrdToProf_CrdAddedSuccessMsg")) {
				extentTest.log(LogStatus.PASS, "Selected cards moved successfully to profile " + RestrictionProfile);
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to move cards to profile " + RestrictionProfile);
			}

			// click("Restrict_GoBackToRestrictionsProfile");

		} catch (Exception e) {

			extentTest.log(LogStatus.FAIL, "Unable to move cards to profile");
		}
	}

	// Add Cards to Profile Without Vehicle Type

	public void AddCardToProfileWithoutVehicleType() {
		try {
			GoToURL("Restrictions_URL");
			click("Restrictions_CreateNewProfile");
			Wait("Restrictions_ProfileName");
			EnterText("Restrictions_ProfileName", "Shell" + RandomString());
			EnterText("Restrictions_ProfileDesc", "Test Profile");
			String PurchaseCategory = GetText("CreateNewRestrictionPrfile_FirstPurchaseCategory");
			int flag = 0;

			if (isDisplayedValid("CreateNewRestrictionPrfile_FirstVehicleSize")) {
				String VehicleSize = GetText("CreateNewRestrictionPrfile_FirstVehicleSize");
			}

			List<WebElement> PurchaseCategoryList = driver
					.findElements(By.xpath(prop.getProperty("Restrictions_PurchaseCategoryList")));
			int PurchaseCategorySize = PurchaseCategoryList.size();
			for (int i = 1; i <= PurchaseCategorySize; i++) {
				String PurchCategory = GetTextMutipleParameters("Restrictions_PurchaseCategoryList1", i,
						"Restrictions_PurchaseCategoryList2").trim();
				if (PurchCategory.trim().equals(PurchaseCategory.trim())) {
					clickMutipleParameters("Restrictions_PurchaseCategoryList1", i,
							"Restrictions_PurchaseCategoryList2");
					flag = 1;
				}

				if (flag == 1)
					break;
			}

			click("Restrictions_SetRestrictions");
			Thread.sleep(2000);

			// Day & time restrictions section
			click("RestrictProfiles_CreateNewProfile_Restrictions_DayNTimeRestrEditButton");
			try {
				List<WebElement> Records = driver.findElements(By.xpath(
						prop.getProperty("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton")));
				int RecordsSize = Records.size();
				System.out.println(RecordsSize);
				for (int i = 1; i <= RecordsSize - 2; i++) {
					// driver.findElement(By.xpath(prop.getProperty("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton1")+i+prop.getProperty("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton2"))).click();
					clickMutipleParameters("Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton1", i,
							"Restriction_CreateNewProfile_SetRestrictions_DayNTimeRestrEditButton2");
					TakeScreenshot();
				}
			} catch (Exception e) {
				extentTest.log(LogStatus.FAIL, "Unable to click Days of the week in Day and Time restrictions");
				e.printStackTrace();
			}
			click("Restriction_CreateNewProfile_SetRestrictions_SaveChangesButton");
			String RestrictionProfile = GetText("Restrictions_FirstProfile").trim();
			click("RestrictProfiles_NoProfilesApplied");
			click("RestrictProfiles_NoProfilesAppliedPage_Filter");
			click("RestrictProfiles_NoProfApplPage_Filter_Active");
			click("RestrictProfiles_NoProfApplPage_Filter_Apply");
			select_Pagination("RestrictProfiles_NoProfApplPage_PaginationFilter", "50");
			Thread.sleep(15000);

			// Clicking the check box of Card having desired purchase category
			List<WebElement> ActiveRecordsCheckBoxes = driver
					.findElements(By.xpath(prop.getProperty("Restrictions_CheckBoxes")));
			int ActiveRecordsSize = ActiveRecordsCheckBoxes.size();
			for (int i = 1; i <= 1; i++) {
				int k = 0;

				while (k != 3) {
					for (int j = 2; j <= ActiveRecordsSize; j++) {
						String PurchaseCateg = GetTextMutipleParameters("RestrictProfiles_NoProfApplPage_PurchCat1", j,
								"RestrictProfiles_NoProfApplPage_PurchCat2").trim();
						// String
						// VehicleCateg=GetTextMutipleParameters("RestrictProfiles_NoProfApplPage_Vehiclesize1",
						// j, "RestrictProfiles_NoProfApplPage_Vehiclesize2").trim();
						System.out.println("J: " + j);

						if (PurchaseCateg.trim().contains(PurchaseCategory.trim())) {
							JavascriptExecutor js = (JavascriptExecutor) driver;
							// Find element by link text and store in variable "Element"
							WebElement Element = driver
									.findElement(By.xpath(prop.getProperty("Restrictions_CheckBoxes1") + j
											+ prop.getProperty("Restrictions_CheckBoxes2")));
							// This will scroll the page till the element is found
							js.executeScript("arguments[0].scrollIntoView();", Element);
							clickMutipleParameters("Restrictions_CheckBoxes1", j, "Restrictions_CheckBoxes2");
							k++;

							if (k == 3) {
								break;
							}
						}

						if (j == 100 && k < 3) {
							k = 0;
							click("NextPage");
							Thread.sleep(15000);
						}
						if (k == 3)
							break;
					}
				}
				break;
			}
			click("RestrictProfiles_NoProfApplPage_TotalActRecords_MoveToProfile");
			Thread.sleep(2500);
			select_Pagination("Restrict_GetResProf_MeatBall_MoveCardsToProf_ChooseResProf", RestrictionProfile);
			Thread.sleep(2500);
			click("RestrictProfiles_NoProfApplPage_MoveCardsToProf_MoveCards");

			Thread.sleep(4000);
			if (isDisplayedValid("Restrict_AddCrdToProf_CrdAddedSuccessMsg")) {
				extentTest.log(LogStatus.PASS, "Selected cards moved successfully to profile " + RestrictionProfile);
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to move cards to profile " + RestrictionProfile);
			}
		} catch (Exception e) {

			extentTest.log(LogStatus.FAIL, "Unable to move cards to profile");
		}
	}

	// Click VehicleType and Purchase Category

	public void ClickPurchaseCategoryAndVehicleType(String VehicleSize, String PurchaseCategory) {
		List<WebElement> ProfileSettingsCategories = driver
				.findElements(By.xpath(prop.getProperty("Restrict_CreateNewProfile_ProfileSettings_Categories")));
		int ProfileSettingsCategoriesSize = ProfileSettingsCategories.size();
		System.out.println("Record Size" + ProfileSettingsCategoriesSize);
		try {
			if (ProfileSettingsCategoriesSize == 2) {

				// Select a Vehicle type(displayed)
				if (isDisplayedValid("Restrictions_SelectAVehicleType_Text")) {
					isDisplayed("Restrictions_SmallerVehicles");
					isDisplayed("Restrictions_LargerVehicles");
				} else {
					extentTest.log(LogStatus.PASS, "Select a Vehicle type is not applicable for user");
				}

				// Select a Purchase Category(displayed)
				if (isDisplayedValid("Restrictions_SelectAPurchaseCategory_Text")) {

				} else {
					extentTest.log(LogStatus.INFO, "Select a purchase category is not applicable for user");
				}

				// Vehicle Type (click)
				ClickVehicleType(VehicleSize);

				// Purchase Category (click)
				PurchaseCategory(PurchaseCategory);

				click("Restrictions_SetRestrictions");
				Thread.sleep(2000);

			} else {

				// Purchase Category (click)
				PurchaseCategoryWithoutVehicleType(PurchaseCategory);
				click("Restrictions_SetRestrictions");

			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to execute 'Verify by creating new profile'");
		}

	}

	// Create new profile(Vehicle Type)
	public void ClickVehicleType(String VehicleTypeFromXcel) {

		try {
			if (isDisplayedValid("Restrictions_SelectAVehicleType_Text")) {
				List<WebElement> VehicleType = driver
						.findElements(By.xpath(prop.getProperty("Restrictions_VehicleType")));
				int VehicleTypeFuelSize = VehicleType.size();
				for (int i = 1; i <= VehicleTypeFuelSize; i++) {
					String VehclType = driver.findElement(By.xpath(
							prop.getProperty("Restrictions_VehType1") + i + prop.getProperty("Restrictions_VehType2")))
							.getText().trim();
					// Thread.sleep(1000);
					if (VehclType.equals(VehicleTypeFromXcel.trim())) {
						driver.findElement(By.xpath(prop.getProperty("Restrictions_VehType1") + i
								+ prop.getProperty("Restrictions_VehType2"))).click();
					}
				}
			} else {
				extentTest.log(LogStatus.PASS, "Select a Vehicle type is not applicable for user");
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to click Vehicle type");
		}
	}

	// Create new profile(Purchase Category)
	public void PurchaseCategory(String PurchaseCategoryFromXcel) {

		try {
			if (isDisplayedValid("Restrictions_SelectAPurchaseCategory_Text")) {
				List<WebElement> PurchaseCategory = driver
						.findElements(By.xpath(prop.getProperty("Restrictions_PurchaseCategory")));
				int PurchaseCategorySize = PurchaseCategory.size();
				for (int i = 1; i <= PurchaseCategorySize; i++) {
					String PurchCategory = driver.findElement(By.xpath(prop.getProperty("Restrictions_PurchCategory1")
							+ i + prop.getProperty("Restrictions_PurchCategory2"))).getText().trim();
					if (PurchCategory.equals(PurchaseCategoryFromXcel.trim())) {
						driver.findElement(By.xpath(prop.getProperty("Restrictions_PurchCategory1") + i
								+ prop.getProperty("Restrictions_PurchCategory2"))).click();

					}
				}
			} else {
				extentTest.log(LogStatus.PASS, "Select Purchase Category is not applicable for user");
			}
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to click Purchase Category");
		}
	}

	// Create new profile(Purchase Category)
	public void PurchaseCategoryWithoutVehicleType(String PurchaseCategoryFromXcel) {

		try {
			int flag = 0;
			List<WebElement> PurchaseCategory = driver
					.findElements(By.xpath(prop.getProperty("Restrictions_WithoutVehicles_PurchaseCategory")));
			int PurchaseCategorySize = PurchaseCategory.size();
			for (int i = 1; i <= PurchaseCategorySize; i++) {
				String PurchCategory = driver
						.findElement(By.xpath(prop.getProperty("Restrictions_WithoutVehicles_PurchCategory1") + i
								+ prop.getProperty("Restrictions_WithoutVehicles_PurchCategory2")))
						.getText().trim();
				// Thread.sleep(1000);
				if (PurchCategory.equals(PurchaseCategoryFromXcel.trim())) {
					driver.findElement(By.xpath(prop.getProperty("Restrictions_WithoutVehicles_PurchCategory1") + i
							+ prop.getProperty("Restrictions_WithoutVehicles_PurchCategory2"))).click();
					flag = 1;
				}
				if (flag == 1)
					break;
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Unable to click Purchase Category");
		}
	}

	public void FindBrokenLinks(String URL) throws MalformedURLException, IOException {

		GoToURL(URL);
		
		// Get list of all broken link and images
		
		List <WebElement> LinksList =driver.findElements(By.tagName("a"));
		LinksList.addAll(driver.findElements(By.tagName("img")));
		extentTest.log(LogStatus.INFO, "Total Number of Links on page: "+ LinksList.size());
		System.out.println("Total Number of Links on page: "+ LinksList.size());
		
		List <WebElement> ActiveLinksList = new ArrayList<WebElement>();
		List <WebElement> DeadLinksList = new ArrayList<WebElement>();
		
		
		for(int i=0;i<LinksList.size();i++) {
			
			System.out.println("Link : " + LinksList.get(i).getAttribute("href"));
			if(LinksList.get(i).getAttribute("href")!=null && (!LinksList.get(i).getAttribute("href").contains("javascript")) && (!LinksList.get(i).getAttribute("href").contains("mailto:"))) {
				
				ActiveLinksList.add(LinksList.get(i));
				
			}
		}
		
		System.out.println("Total Number of Active Links on page: "+ ActiveLinksList.size());
		extentTest.log(LogStatus.INFO, "Total Number of Active Links on page: "+ ActiveLinksList.size());
		
		for(int j=0;j<ActiveLinksList.size();j++) {
			
			HttpURLConnection connection =(HttpURLConnection)new URL(ActiveLinksList.get(j).getAttribute("href")).openConnection();
			connection.connect();
			String response=connection.getResponseMessage();
			connection.disconnect();
			System.out.println("Active Link: "+ ActiveLinksList.get(j).getAttribute("href")+ "---->"+response);
			extentTest.log(LogStatus.INFO, "Active Link: "+ ActiveLinksList.get(j).getAttribute("href")+ "---->"+response);
			
			if(!(response.equalsIgnoreCase("OK") || (response.equalsIgnoreCase("Found")))) {
				
				extentTest.log(LogStatus.FAIL, "Check Link: "+ ActiveLinksList.get(j).getAttribute("href")+ "---->"+response);
					
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
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
