package com.goldbharath.genericutilities;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.goldbharath.objectrepository.user.DashboardPage;
import com.goldbharath.objectrepository.user.LoginPage;
import com.goldbharath.objectrepository.user.LogoutPage;
import com.goldbharath.objectrepository.user.UserProfilePage;

public class DistributorBaseClass extends CommonBaseClass {

    protected WebDriver driver;

    protected PropertyFileUtility pUtil = new PropertyFileUtility();
    protected WebDriverUtility wUtil = new WebDriverUtility();
    protected JavaUtility jUtil = new JavaUtility();
    protected ExcelFileUtility eUtil = new ExcelFileUtility();

    @BeforeClass(alwaysRun = true)
    public void launchBrowser() throws IOException {

        String browser = pUtil.readDataFromPropertyFile("browser1");
        String url = pUtil.readDataFromPropertyFile("userUrl");

        // Used by Extent Report
        System.setProperty("browser", browser);
        System.setProperty("baseUrl", url);

        if (browser.equalsIgnoreCase("chrome")) {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("edge")) {

            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-notifications");
            driver = new EdgeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {

            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-notifications");
            driver = new FirefoxDriver(options);

        } else {

            throw new RuntimeException("Unsupported Browser : " + browser);
        }

        // Store driver in ThreadLocal
        CommonBaseClass.setDriver(driver);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

        driver.get(url);
    }

    
    @BeforeMethod
    public void loginToDoctorApp() throws Throwable
    {
        String MOBILE_NUMBER = pUtil.readDataFromPropertyFile("usermobilenumber");
        String OTP = pUtil.readDataFromPropertyFile("userotp");
    	
        LoginPage lPage = new LoginPage(driver);
        lPage.LoginToApplication(driver, MOBILE_NUMBER, OTP);

    }
    
    @AfterMethod
    public void logoutFromDoctorApp() throws Exception
    {    	
    	try {
    		Thread.sleep(3000);
    		DashboardPage dbPage = new DashboardPage(driver);
    		dbPage.clickOnUserProfileImageAndLogoutLink(driver);
    		Thread.sleep(2000);
    		UserProfilePage upPage = new UserProfilePage(driver);
    		upPage.clickOnLogoutLink();
    		Thread.sleep(2000);
            LogoutPage lPage = new LogoutPage(driver);
            lPage.logoutOfApplication(driver);
            System.out.println("Doctor Logout Successful");
        } catch (Exception e) {
            System.out.println(
                    "Logout skipped because user is already logged out or page not available");
        }
    }
    
    
    @AfterClass(alwaysRun = true)
    public void closeBrowser() {

        if (driver != null) {
            driver.quit();
        }

        CommonBaseClass.unloadDriver();
    }
}