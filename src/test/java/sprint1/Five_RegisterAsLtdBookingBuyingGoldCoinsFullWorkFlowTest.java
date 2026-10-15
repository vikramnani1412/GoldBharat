package sprint1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.goldbharath.genericutilities.ExcelFileUtility;
import com.goldbharath.genericutilities.PropertyFileUtility;
import com.goldbharath.genericutilities.WebDriverUtility;
import com.goldbharath.objectrepository.admin.BusinessInformationPage;
import com.goldbharath.objectrepository.admin.DistributorDetailsPage;
import com.goldbharath.objectrepository.admin.LogoutPage;
import com.goldbharath.objectrepository.admin.ManageDistributorPage;
import com.goldbharath.objectrepository.user.BookGoldPage;
import com.goldbharath.objectrepository.user.BuildYourGoldCoinCombinationPage;
import com.goldbharath.objectrepository.user.DashboardPage;
import com.goldbharath.objectrepository.user.GoldCoinsOrdersHistoryPage;
import com.goldbharath.objectrepository.user.KycAsLLpPage;
import com.goldbharath.objectrepository.user.KycAsLtdPage;
import com.goldbharath.objectrepository.user.LoginPage;
import com.goldbharath.objectrepository.user.RegisterPage;
import com.goldbharath.objectrepository.user.ReviewYourGoldOrderPage;
import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldCoinsPage;
import com.goldbharath.objectrepository.user.UserProfilePage;
import com.goldbharath.objectrepository.user.WelcomePage;
import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Five_RegisterAsLtdBookingBuyingGoldCoinsFullWorkFlowTest {
	
	WebDriverUtility wUtil = new WebDriverUtility();
    PropertyFileUtility pUtil = new PropertyFileUtility();
    ExcelFileUtility eUtil = new ExcelFileUtility();

    private WebDriver driver;
    private static String registeredMobileNumber;
    private static String OrderID;

    private static final String SHEET_NAME = "Sheet1";
    private static final int WAIT_TIME = 20;

    // COMMON METHOD: CREATE CHROME DRIVER
    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");

        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return webDriver;
    }

    // COMMON METHOD: WAIT FOR PAGE LOAD
    private void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    // COMMON METHOD: CLICK REGISTER BUTTON
    private void clickRegisterButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
        By registerButton = By.xpath("//button[normalize-space()='Register']");
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
    }

    // DATA PROVIDER: READ MOBILE NUMBERS FROM EXCEL
    @DataProvider(name = "mobileNumbers")
    public Object[][] mobileNumbers() throws Exception {
        List<String> mobileNumbers = new ArrayList<>();

        for (int row = 10; row <= 29; row++) {
            String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, row, 1);

            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                mobileNumber = mobileNumber.trim();
                mobileNumbers.add(mobileNumber);
                System.out.println("Excel Row " + row + " : " + mobileNumber);
            }
        }

        if (mobileNumbers.isEmpty()) {
            throw new Exception("No mobile numbers found in Excel rows 10 to 29.");
        }

        return new Object[][]{{mobileNumbers}};
    }

    // TEST 1: USER REGISTRATION
    @Test(priority = 1, dataProvider = "mobileNumbers")
    public void RegisteringToGoldbharathTest(List<String> mobileNumbers) throws Exception {
        System.out.println("\n==================================================");
        System.out.println("TEST 1 : GOLDBHARATH USER REGISTRATION");
        System.out.println("==================================================");

        String businessName = eUtil.readDataFromExcel(SHEET_NAME, 1, 1);
        String personName = eUtil.readDataFromExcel(SHEET_NAME, 2, 1);
        String email = eUtil.readDataFromExcel(SHEET_NAME, 3, 1);
        String date = eUtil.readDataFromExcel(SHEET_NAME, 5, 1);
        String pincode = eUtil.readDataFromExcel(SHEET_NAME, 6, 1);
        String address = eUtil.readDataFromExcel(SHEET_NAME, 7, 1);
        String userUrl = pUtil.readDataFromPropertyFile("userurl");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();
            clickRegisterButton();

            RegisterPage registerPage = new RegisterPage(driver);
            registeredMobileNumber = registerPage.registeringToGoldbharathApplication(
                    driver, businessName, personName, email, mobileNumbers, date, pincode, address);

            Assert.assertNotNull(registeredMobileNumber, "Registration failed - mobile number is NULL.");
            Assert.assertFalse(registeredMobileNumber.trim().isEmpty(), "Registration failed - mobile number is EMPTY.");

            System.out.println("--------------------------------------------------");
            System.out.println("REGISTRATION SUCCESSFUL");
            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
            System.out.println("--------------------------------------------------");
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // TEST 2: LOGIN + LLP KYC
    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest")
    public void loginUpdatingKYC() throws Exception {
        System.out.println("\n==================================================");
        System.out.println("TEST 2 : LOGIN + LLP KYC");
        System.out.println("==================================================");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        System.out.println("LOGIN MOBILE : " + registeredMobileNumber);

        String Name = eUtil.readDataFromExcel(SHEET_NAME, 34, 8);
        String MobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 8);
        String AadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 8);
        String PANNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 8);
        String SecondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 8);
        String GstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 8);
        String CinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 8);
        String BankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 8);
        String AcNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 8);
        String IFSCCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 8);
        String AccountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 8);
        String userUrl = pUtil.readDataFromPropertyFile("userurl");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            WelcomePage welcomePage = new WelcomePage(driver);
            welcomePage.clickOnLoginLink();

            LoginPage loginPage = new LoginPage(driver);
            String OTP = pUtil.readDataFromPropertyFile("userotp");
            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);

            waitForPageLoad();

            DashboardPage dashboardPage = new DashboardPage(driver);
            dashboardPage.clickOnUserProfileImageAndLogoutLink(driver);

            UserProfilePage userProfilePage = new UserProfilePage(driver);
            userProfilePage.clickOnKycLink();
            
            KycAsLtdPage kycPage = new KycAsLtdPage(driver);
            kycPage.LtdKyc(driver, Name, MobileNumber, AadharNumber,
                    PANNumber, SecondDirectorName, GstNumber,
                    CinNumber, BankName, AcNumber, IFSCCode, AccountHolderName);

            System.out.println("--------------------------------------------------");
            System.out.println("Ltd KYC COMPLETED SUCCESSFULLY");
            System.out.println("MOBILE : " + registeredMobileNumber);
            System.out.println("--------------------------------------------------");
        } 
        finally 
        {
            if (driver != null) driver.quit();
        }
    }

    // TEST 3: ADMIN APPROVAL + GOLD/SILVER LIMITS
    @Test(priority = 3, dependsOnMethods = "loginUpdatingKYC")
    public void loginToAdminApproveKycAndGivingGoldAndSilverLimit() throws Exception {
        System.out.println("\n==================================================");
        System.out.println("TEST 3 : ADMIN DISTRIBUTOR FLOW");
        System.out.println("==================================================");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");
        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);

        Assert.assertNotNull(goldLimit, "Gold Limit is NULL in Excel.");
        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold Limit is EMPTY in Excel.");
        Assert.assertNotNull(silverLimit, "Silver Limit is NULL in Excel.");
        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver Limit is EMPTY in Excel.");

        System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
        System.out.println("OWNER NAME        : " + ownerName);
        System.out.println("GOLD LIMIT        : " + goldLimit);
        System.out.println("SILVER LIMIT      : " + silverLimit);

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            com.goldbharath.objectrepository.admin.LoginPage adminLoginPage =
                    new com.goldbharath.objectrepository.admin.LoginPage(driver);
            adminLoginPage.loginToAdmin(username, password);
            waitForPageLoad();

            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
            wait.until(ExpectedConditions.elementToBeClickable(
                    adminDashboardPage.getAllDistributorLnk())).click();

            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
                    driver, registeredMobileNumber, ownerName);

            DistributorDetailsPage distributorDetailsPage = new DistributorDetailsPage(driver);
            distributorDetailsPage.acceptingDistributorKycDetails();

            manageDistributorPage.clickOnRecentlyRegisteredDistributorEditButton(
                    driver, registeredMobileNumber);

            BusinessInformationPage businessInformationPage = new BusinessInformationPage(driver);
            businessInformationPage.editingAndGivingSilverAndGoldLimitForLtd(silverLimit, goldLimit);

            System.out.println("\n==================================================");
            System.out.println("GOLDBHARATH ADMIN DISTRIBUTOR FLOW COMPLETED");
            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
            System.out.println("OWNER NAME        : " + ownerName);
            System.out.println("GOLD LIMIT        : " + goldLimit);
            System.out.println("SILVER LIMIT      : " + silverLimit);
            System.out.println("==================================================");

            Thread.sleep(2000);
            LogoutPage loPage = new LogoutPage(driver);
            loPage.logoutOfApplication();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // TEST 4: DISTRIBUTOR GOLD BOOKING
    @Test(priority = 4, dependsOnMethods = "loginToAdminApproveKycAndGivingGoldAndSilverLimit")
    public void distributorBookingGoldTest() throws Exception {
        System.out.println("\n==================================================");
        System.out.println("TEST 4 : DISTRIBUTOR GOLD BOOKING");
        System.out.println("==================================================");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available. Earlier tests may have failed.");
        }

        System.out.println("REGISTERED MOBILE NUMBER : " + registeredMobileNumber);

        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            WelcomePage welcomePage = new WelcomePage(driver);
            welcomePage.clickOnLoginLink();

            LoginPage loginPage = new LoginPage(driver);
            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            System.out.println("Distributor Login Successful");
            System.out.println("Mobile : " + registeredMobileNumber);

            DashboardPage dbPage = new DashboardPage(driver);
            dbPage.clickOnBookGoldBtn(driver);
            Thread.sleep(2000);

            BookGoldPage bgPage = new BookGoldPage(driver);
            bgPage.fetchingGoldLivePriceAndBookingGold(driver);

            System.out.println("\n==================================================");
            System.out.println("GOLD BOOKING COMPLETED SUCCESSFULLY");
            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
            System.out.println("==================================================");
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // TEST 5: DISTRIBUTOR GOLD BUYING
    @Test(priority = 5, dependsOnMethods = "distributorBookingGoldTest")
    public void distributorBuyingGoldTest() throws Exception {
        final String serialNo = "1";
        final String transactionImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
        final String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);

        System.out.println("\n==================================================");
        System.out.println("TEST 5 : DISTRIBUTOR GOLD BUYING");
        System.out.println("==================================================");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available. Earlier tests may have failed.");
        }

        System.out.println("REGISTERED MOBILE NUMBER : " + registeredMobileNumber);

        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");
        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            WelcomePage welcomePage = new WelcomePage(driver);
            welcomePage.clickOnLoginLink();

            LoginPage loginPage = new LoginPage(driver);
            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            System.out.println("Distributor Login Successful");
            System.out.println("Mobile : " + registeredMobileNumber);

            DashboardPage dbPage = new DashboardPage(driver);
            dbPage.clickOnBuyGoldBtn(driver);
            Thread.sleep(2000);

            whatWouldYouLikeToBuyPage wPage = new whatWouldYouLikeToBuyPage(driver);
            wPage.getGoldCoinsEle().click();
            Thread.sleep(2000);

            BuildYourGoldCoinCombinationPage bPage = new BuildYourGoldCoinCombinationPage(driver);
            bPage.increaseGoldGramsAccToWeight(driver, "1", 1);
            Thread.sleep(2000);
            bPage.increaseGoldGramsAccToWeight(driver, "2", 2);
            Thread.sleep(2000);
            bPage.increaseGoldGramsAccToWeight(driver, "5", 1);
            Thread.sleep(2000);
            bPage.increaseGoldGramsAccToWeight(driver, "10", 2);
            Thread.sleep(2000);
            bPage.increaseGoldGramsAccToWeight(driver, "20", 1);
            Thread.sleep(2000);
            bPage.increaseGoldGramsAccToWeight(driver, "50", 1);
            Thread.sleep(2000);
            bPage.increaseGoldGramsAccToWeight(driver, "100", 1);
            Thread.sleep(2000);

            bPage.getContinueBtn().click();
            Thread.sleep(2000);

            ReviewYourGoldOrderPage rPage = new ReviewYourGoldOrderPage(driver);
            rPage.takeAllDetailsAndClickProceedBtn(driver);
            Thread.sleep(2000);

//            Want to check and import this
            
            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
            gPage.GetAllOrderDetailsBasedOnSerialNo(driver, serialNo);
            Thread.sleep(2000);

            OrderID = gPage.GetOrderIdBasedOnSerialNo(driver, serialNo);
            if (OrderID == null || OrderID.trim().isEmpty()) {
                throw new Exception("Order ID was not captured from Test 5.");
            }

            OrderID = OrderID.trim();
            System.out.println("ORDER ID GENERATED : " + OrderID);

            gPage.clickOnUpdatePaymentDocumentBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            UploadTransactionDetailsForGoldCoinsPage uPage =
                    new UploadTransactionDetailsForGoldCoinsPage(driver);
            uPage.uploadingTransactionDetails(transactionImage, transactionIdNum);
            Thread.sleep(2000);

            gPage.successMsgValidation(driver, OrderID);

            System.out.println("\n==================================================");
            System.out.println("TEST 5 COMPLETED SUCCESSFULLY");
            System.out.println("ORDER ID : " + OrderID);
            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
            System.out.println("==================================================");
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // TEST 6: ADMIN CHECK PURCHASED GOLD + CONFIRM PAYMENT
    @Test(priority = 6, dependsOnMethods = "distributorBuyingGoldTest")
    public void adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest() throws Exception {
        System.out.println("\n==================================================");
        System.out.println("TEST 6 : ADMIN CHECK PURCHASED GOLD & CONFIRM PAYMENT");
        System.out.println("==================================================");

        if (OrderID == null || OrderID.trim().isEmpty()) {
            throw new Exception("Order ID is not available from Test 5. Test 5 may have failed.");
        }
        
        String DeliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
        String DaysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);

        OrderID = OrderID.trim();
        System.out.println("ORDER ID FROM TEST 5 : " + OrderID);

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            com.goldbharath.objectrepository.admin.LoginPage adminLoginPage =
                    new com.goldbharath.objectrepository.admin.LoginPage(driver);
            adminLoginPage.loginToAdmin(username, password);
            waitForPageLoad();

            System.out.println("Admin Login Successful");

            com.goldbharath.objectrepository.admin.DashboardPage dbPage =
                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
            dbPage.clickOnManageOrdersLnk();
            Thread.sleep(2000);

            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
            moPage.checkingRecentlyBuyedCoinsDetails(driver, OrderID);
            Thread.sleep(2000);

            moPage.clickOnVerifyOrderBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);
                   
            moPage.allActionsInConfirmDispatchPageInLLpPage(DeliveryMsg, DaysToDeliver);
            
            WebElement OrderStatus = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Dispatched ']"));
            
            Assert.assertTrue(OrderStatus.isDisplayed(), "Problem Occurred While Dispatching Order");
      		System.out.println("Order Dispatched Successfully");
            
            System.out.println("\n==================================================");
            System.out.println("ADMIN GOLD PAYMENT FLOW COMPLETED");
            System.out.println("ORDER ID : " + OrderID);
            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
            System.out.println("==================================================");
            
            
        } finally {
            if (driver != null) driver.quit();
        }
    }
    
    @Test(priority = 7, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest")
    public void distributorUpdatingDeliveryDetailsTest() throws Exception {

        System.out.println("\n==================================================");
        System.out.println("TEST 7 : DISTRIBUTOR UPDATING DELIVERY DETAILS");
        System.out.println("==================================================");

        String IdImage = System.getProperty("user.dir")+ "/src/test/resources/assets/Aadhaar_Card.jpeg";
        String Name = eUtil.readDataFromExcel("Admin", 6, 1);
        String PhNo = eUtil.readDataFromExcel("Admin", 7, 1);
        String IdNo = eUtil.readDataFromExcel("Admin", 8, 1);
        
        // Validate registered distributor mobile number
        Assert.assertNotNull(
                registeredMobileNumber,
                "Registered distributor mobile number is NULL."
        );

        Assert.assertFalse(
                registeredMobileNumber.trim().isEmpty(),
                "Registered distributor mobile number is EMPTY."
        );

        // Read user URL and OTP from property file
        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        // Launch browser
        driver = createChromeDriver();
        
        try {
            driver.get(userUrl);
            waitForPageLoad();

            // Click Login
            WelcomePage welcomePage = new WelcomePage(driver);
            welcomePage.clickOnLoginLink();

            // Login using previously registered distributor
            LoginPage loginPage = new LoginPage(driver);
            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);

            waitForPageLoad();

            System.out.println("Distributor Login Successful");
            System.out.println("Registered Mobile : " + registeredMobileNumber);

            Thread.sleep(2000);
            DashboardPage dbPage = new DashboardPage(driver);
            dbPage.getUserProfileImg().click();
            
            Thread.sleep(2000);
            dbPage.getCoinOrdersLnk().click();
            
            Thread.sleep(2000);
            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
            gPage.clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
            
            Thread.sleep(2000);
            
            UpdateDeliveryDetailsPage uPage = new UpdateDeliveryDetailsPage(driver);
            uPage.updatingDeliveryDetails(Name, PhNo,  IdNo, IdImage);

        } finally {
            
             if (driver != null) driver.quit();
        }
    }
	
}
