package Silver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.goldbharath.genericutilities.DriverHolder;
import com.goldbharath.genericutilities.ExcelFileUtility;
import com.goldbharath.genericutilities.ListenersImplementationClassWithoutBaseClass;
import com.goldbharath.genericutilities.PropertyFileUtility;
import com.goldbharath.genericutilities.RetryAnalyzer;
import com.goldbharath.genericutilities.WebDriverUtility;
import com.goldbharath.objectrepository.admin.BusinessInformationPage;
import com.goldbharath.objectrepository.admin.DistributorDetailsPage;
import com.goldbharath.objectrepository.admin.LogoutPage;
import com.goldbharath.objectrepository.admin.ManageDistributorPage;
import com.goldbharath.objectrepository.admin.ManageOrdersPage;
import com.goldbharath.objectrepository.user.BookSilverPage;
import com.goldbharath.objectrepository.user.BuildYourSilverCoinCombinationPage;
import com.goldbharath.objectrepository.user.DashboardPage;
import com.goldbharath.objectrepository.user.SilverCoinsOrdersHistoryPage;
import com.goldbharath.objectrepository.user.KycAsLtdPage;
import com.goldbharath.objectrepository.user.LoginPage;
import com.goldbharath.objectrepository.user.RegisterPage;
import com.goldbharath.objectrepository.user.ReviewYourSilverOrderPage;
import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
import com.goldbharath.objectrepository.user.UploadTransactionDetailsForSilverCoinsPage;
import com.goldbharath.objectrepository.user.UserProfilePage;
import com.goldbharath.objectrepository.user.WelcomePage;
import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;
import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners(ListenersImplementationClassWithoutBaseClass.class)
public class Four_RegisterAsLtdBookingBuyingSilverCoinsFullWorkFlowTest {

	
    WebDriverUtility wUtil = new WebDriverUtility();
    PropertyFileUtility pUtil = new PropertyFileUtility();
    ExcelFileUtility eUtil = new ExcelFileUtility();

    private WebDriver driver;
    private static String registeredMobileNumber;
    private static String OrderID;

    private static final String SHEET_NAME = "Sheet1";
    private static final int WAIT_TIME = 20;

    // Browser Utilities

    private WebDriver createChromeDriver() {
        closeBrowser();   // never leave an old browser behind
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1920,1080",
                "--disable-notifications", "--disable-popup-blocking", "--disable-infobars");

        
        options.addArguments(
                "--disable-features=HttpsUpgrades,HttpsFirstBalancedModeAutoEnable,HttpsFirstModeV2ForEngagedSites",
                "--unsafely-treat-insecure-origin-as-secure=http://stg.goldbharat.com"
        );

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("https_first_mode_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        DriverHolder.set(webDriver);
        return webDriver;
    }

    private void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
                .until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    private void clickRegisterButton() {
        By registerButton = By.xpath("//button[normalize-space()='Register']");
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
                .until(ExpectedConditions.elementToBeClickable(registerButton)).click();
    }

    // Test Data Provider

    @DataProvider(name = "mobileNumbers")
    public Object[][] mobileNumbers() throws Exception {
        List<String> mobileNumbers = new ArrayList<>();

        for (int row = 10; row <= 29; row++) {
            String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, row, 1);

            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                mobileNumbers.add(mobileNumber.trim());
                System.out.println("Excel Row " + row + " : " + mobileNumber.trim());
            }
        }

        if (mobileNumbers.isEmpty()) {
            throw new Exception("No mobile numbers found in Excel rows 10 to 29.");
        }

        return new Object[][]{{mobileNumbers}};
    }


    //Test 1: User Registration

    @Test(priority = 1, dataProvider = "mobileNumbers")
    public void RegisteringToGoldbharathTest(List<String> mobileNumbers) throws Exception {
        System.out.println("\n========== TEST 1: USER REGISTRATION ==========");

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
            
            Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
            Assert.assertFalse(registeredMobileNumber.trim().isEmpty(), "Registered mobile number is EMPTY.");

            System.out.println("Registration successful. Mobile: " + registeredMobileNumber);
        } finally {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }

    // Test 2: User Login and LTD KYC

    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginUpdatingKycTest() throws Exception {
        System.out.println("\n========== TEST 2: LOGIN AND LTD KYC ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 8);
        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 8);
        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 8);
        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 8);
        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 8);
        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 8);
        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 8);
        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 8);
        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 8);
        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 8);
        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 8);
        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            new WelcomePage(driver).clickOnLoginLink();
            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            new DashboardPage(driver).clickOnUserProfileImageAndLogoutLink(driver);
            new UserProfilePage(driver).clickOnKycLink();

            new KycAsLtdPage(driver).LtdKyc(driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);
            
            System.out.println("KYC completed successfully. Mobile: " + registeredMobileNumber);
        } finally {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }


    @Test(priority = 3, dependsOnMethods = "loginUpdatingKycTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginToAdminRejectingDistributorKycTest() throws Exception {
        System.out.println("\n========== TEST 3: ADMIN Rejecting DISTRIBUTOR Kyc FLOW ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");
        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);

        Assert.assertNotNull(silverLimit, "Silver limit is NULL in Excel.");
        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver limit is EMPTY in Excel.");

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.LoginPage(driver)
                    .loginToAdmin(username, password);
            waitForPageLoad();

            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);

            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
                    .until(ExpectedConditions.elementToBeClickable(
                            adminDashboardPage.getAllDistributorLnk())).click();

            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
                    driver, registeredMobileNumber, ownerName);

            new DistributorDetailsPage(driver).rejectingDistributorKycDetails();

            Thread.sleep(2000);
            manageDistributorPage.clickOnDistributorNameBasedOnMobileNumber(driver, registeredMobileNumber);
            
            Thread.sleep(2000);
            new DistributorDetailsPage(driver).checkingRejectedStatus(driver);
            
            Thread.sleep(2000);
        }
        finally 
        {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }
    
    
    @Test(priority = 4, dependsOnMethods = "loginToAdminRejectingDistributorKycTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginUpdatingRejectedKycTest() throws Exception {
        System.out.println("\n========== TEST 4: Distributor LOGIN AND Updating Rejected LTD KYC ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 8);
        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 8);
        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 8);
        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 8);
        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 8);
        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 8);
        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 8);
        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 8);
        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 8);
        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 8);
        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 8);
        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            new WelcomePage(driver).clickOnLoginLink();
            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            new DashboardPage(driver).clickOnUserProfileImageAndLogoutLink(driver);
            new UserProfilePage(driver).clickOnKycLink();

            new KycAsLtdPage(driver).LtdKycAfterRejected(driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);

            System.out.println("KYC completed successfully. Mobile: " + registeredMobileNumber);
        } finally {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }
    

    // Test 3: Admin KYC Approval and Silver Limit

    @Test(priority = 5, dependsOnMethods = "loginUpdatingRejectedKycTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginToAdminApproveKycAndGivingSilverLimitTest() throws Exception {
        System.out.println("\n========== TEST 5: ADMIN accepting DISTRIBUTOR Kyc FLOW ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");
        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);
        String GoldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
        
        Assert.assertNotNull(silverLimit, "Silver limit is NULL in Excel.");
        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver limit is EMPTY in Excel.");

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.LoginPage(driver)
                    .loginToAdmin(username, password);
            waitForPageLoad();

            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);

            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
                    .until(ExpectedConditions.elementToBeClickable(
                            adminDashboardPage.getAllDistributorLnk())).click();

            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
                    driver, registeredMobileNumber, ownerName);

            new DistributorDetailsPage(driver).acceptingDistributorKycDetails();

            manageDistributorPage.clickOnRecentlyRegisteredDistributorEditButton(
                    driver, registeredMobileNumber);

            new BusinessInformationPage(driver)
                    .editingAndGivingSilverAndGoldLimitForLtd(silverLimit, GoldLimit);

            System.out.println("Admin flow completed successfully.");
            System.out.println("Mobile: " + registeredMobileNumber);
            System.out.println("Owner: " + ownerName);
            System.out.println("Silver Limit: " + silverLimit);

            Thread.sleep(2000);
            new LogoutPage(driver).logoutOfApplication();
        } finally {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }

    // Test 4: Distributor Silver Booking

    @Test(priority = 6, dependsOnMethods = "loginToAdminApproveKycAndGivingSilverLimitTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorBookingSilverTest() throws Exception {
        System.out.println("\n========== TEST 6: DISTRIBUTOR SILVER BOOKING ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            new WelcomePage(driver).clickOnLoginLink();
            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            new DashboardPage(driver).clickOnBookSilverBtn(driver);
            Thread.sleep(2000);

            new BookSilverPage(driver).fetchingSilverLivePriceAndBookingSilver(driver);

            System.out.println("Silver booking completed. Mobile: " + registeredMobileNumber);
        } finally {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }

    // Test 5: Distributor Silver Purchase and Payment Document Upload

    @Test(priority = 7, dependsOnMethods = "distributorBookingSilverTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorBuyingSilverTest() throws Exception {
        String serialNo = "1";
        String transactionImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);

        System.out.println("\n========== TEST 7: DISTRIBUTOR SILVER BUYING ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            new WelcomePage(driver).clickOnLoginLink();
            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            Thread.sleep(2000);
            
            new DashboardPage(driver).clickOnBuySilverBtn(driver);
            Thread.sleep(2000);

            new whatWouldYouLikeToBuyPage(driver).getSilverCoinsEle().click();
            Thread.sleep(2000);

            BuildYourSilverCoinCombinationPage bPage = new BuildYourSilverCoinCombinationPage(driver);
            bPage.increaseSilverGramsAccToWeight(driver, "1", 1);
            bPage.increaseSilverGramsAccToWeight(driver, "2", 2);
            bPage.increaseSilverGramsAccToWeight(driver, "5", 1);
            bPage.increaseSilverGramsAccToWeight(driver, "10", 2);
            bPage.increaseSilverGramsAccToWeight(driver, "20", 1);
            bPage.increaseSilverGramsAccToWeight(driver, "50", 1);
            bPage.increaseSilverGramsAccToWeight(driver, "100", 1);

            Thread.sleep(2000);
            bPage.getContinueBtn().click();
            Thread.sleep(2000);

            new ReviewYourSilverOrderPage(driver).takeAllDetailsAndClickProceedBtn(driver);
            Thread.sleep(2000);

            SilverCoinsOrdersHistoryPage gPage = new SilverCoinsOrdersHistoryPage(driver);
            gPage.GetAllOrderDetailsBasedOnSerialNo(driver, serialNo);
            Thread.sleep(2000);

            OrderID = gPage.GetOrderIdBasedOnSerialNo(driver, serialNo);

            if (OrderID == null || OrderID.trim().isEmpty()) {
                throw new Exception("Order ID was not captured from Test 5.");
            }

            OrderID = OrderID.trim();
            System.out.println("Method 5th Order ID generated: " + OrderID);

            gPage.clickOnUpdatePaymentDocumentBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            new UploadTransactionDetailsForSilverCoinsPage(driver)
                    .uploadingTransactionDetails(transactionImage, transactionIdNum);
            Thread.sleep(2000);

            gPage.successMsgValidation(driver, OrderID);

            System.out.println("Silver purchase and payment document upload completed. Order ID: " + OrderID);
        } finally {
            // browser is closed in closeBrowser() (@AfterMethod)
        }
    }

    // Test 6: Admin Payment Declineing

    @Test(priority = 8, dependsOnMethods = "distributorBuyingSilverTest") // 
    public void adminCheckingPurchasedQuantitySilverAndDeclinePaymentTest() throws Exception {
        System.out.println("\n========== TEST 8: ADMIN PAYMENT DECLINING ==========");

        String Reason = eUtil.readDataFromExcel("Admin", 2, 1);

        if (OrderID == null || OrderID.trim().isEmpty()) {
            throw new Exception("Order ID is not available from Test 5.");
        }

        OrderID = OrderID.trim();
        System.out.println("Order ID: " + OrderID);

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.LoginPage(driver)
                    .loginToAdmin(username, password);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
                    .clickOnManageOrdersLnk();
            Thread.sleep(2000);

            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);

            moPage.checkingRecentlyBuyedCoinsDetailsDecline(driver, OrderID, Reason);
            Thread.sleep(2000);

            try 
            {
    			Thread.sleep(1000);
    			WebElement Declined = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/a[.='Payment Declined']"));
    			if(Declined.isDisplayed())
    			{
    				System.out.println("Declined Msg Displayed");
    			}
    		}
            catch (Exception e) 
            {
            	System.out.println("Declined Msg Not Displayed");
    		}

        } finally {
             // browser is closed in closeBrowser() (@AfterMethod)
        }
    }

    //Test 9: Admin Payment Confirmation and Dispatch

    @Test(priority = 9, dependsOnMethods = "adminCheckingPurchasedQuantitySilverAndDeclinePaymentTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorReUploadingPaymentDetailsTest() throws Exception
    {
        System.out.println("\n========== TEST 9: UPDATE DELIVERY DETAILS ==========");

        String transactionImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
        

        Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
        Assert.assertFalse(registeredMobileNumber.trim().isEmpty(),
                "Registered mobile number is EMPTY.");

        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            new WelcomePage(driver).clickOnLoginLink();
            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            DashboardPage dbPage = new DashboardPage(driver);
            dbPage.getUserProfileImg().click();
            Thread.sleep(2000);

            dbPage.getCoinOrdersLnk().click();
            Thread.sleep(2000);

            wUtil.waitForPageLoad(driver);
            
//            Thread.sleep(1000);
//            driver.findElement(By.xpath("//button[@class='coin-btn silver']")).click();
            
            Thread.sleep(1000);
            new SilverCoinsOrdersHistoryPage(driver).clickOnSilverBtnWhilePaymentDeclined();
            
            Thread.sleep(1000);
            new SilverCoinsOrdersHistoryPage(driver)
                    .clickOnReUploaddPaymentDocumentBtnBasedOnOrderID(driver, OrderID);
            
            new UploadTransactionDetailsForSilverCoinsPage(driver)
            .ReUploadingTransactionDetails(driver, transactionImage, transactionIdNum);
            Thread.sleep(2000);
            System.out.println("Re Uploaded Transaction Image and Number");
            
        } finally {
             // browser is closed in closeBrowser() (@AfterMethod)
        }
    }


 // Test 6: Admin Payment Confirmation and Dispatch

    @Test(priority = 10, dependsOnMethods = "distributorReUploadingPaymentDetailsTest", retryAnalyzer = RetryAnalyzer.class)
    public void adminCheckingPurchasedQuantitySilverAndConfirmPaymentTest() throws Exception {
        System.out.println("\n========== TEST 10: ADMIN PAYMENT CONFIRMATION ==========");

        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 2);
        String daysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);

        if (OrderID == null || OrderID.trim().isEmpty()) {
            throw new Exception("Order ID is not available from Test 5.");
        }

        OrderID = OrderID.trim();
        System.out.println("Order ID: " + OrderID);

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.LoginPage(driver)
                    .loginToAdmin(username, password);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
                    .clickOnManageOrdersLnk();
            Thread.sleep(2000);

            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);

            moPage.checkingRecentlyBuyedCoinsDetailsAccepting(driver, OrderID);
            Thread.sleep(2000);

            // Retry-safe: skip dispatch actions if the order was already verified/dispatched in an earlier attempt
            boolean verified = moPage.clickOnVerifyOrderBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            if (verified) {
                moPage.allActionsInConfirmDispatchPageInLLpPage(deliveryMsg, daysToDeliver);
                Thread.sleep(2000);
            }
            
            moPage.confirmingOrderStatus(driver, OrderID);

        } finally {
             // browser is left open; the previous one is closed when the next test starts
        }
    }

    // Test 7: Distributor Delivery Details Update
    @Test(priority = 11, dependsOnMethods = "adminCheckingPurchasedQuantitySilverAndConfirmPaymentTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorUpdatingDeliveryDetailsTest() throws Exception
    {
        System.out.println("\n========== TEST 11: UPDATE DELIVERY DETAILS ==========");

        String idImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Aadhaar_Card.jpeg";
        String name = eUtil.readDataFromExcel("Admin", 6, 1);
        String phoneNumber = eUtil.readDataFromExcel("Admin", 7, 1);
        String idNumber = eUtil.readDataFromExcel("Admin", 8, 1);

        Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
        Assert.assertFalse(registeredMobileNumber.trim().isEmpty(),
                "Registered mobile number is EMPTY.");

        String userUrl = pUtil.readDataFromPropertyFile("userurl");
        String OTP = pUtil.readDataFromPropertyFile("userotp");

        driver = createChromeDriver();

        try {
            driver.get(userUrl);
            waitForPageLoad();

            new WelcomePage(driver).clickOnLoginLink();
            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
            waitForPageLoad();

            DashboardPage dbPage = new DashboardPage(driver);
            dbPage.getUserProfileImg().click();
            Thread.sleep(2000);

            openSilverOrdersPage(dbPage);
            Thread.sleep(2000);

            wUtil.waitForPageLoad(driver);
            
            new SilverCoinsOrdersHistoryPage(driver)
                    .clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            new UpdateDeliveryDetailsPage(driver)
                    .updatingDeliveryDetails(driver, name, phoneNumber, idNumber, idImage);

            System.out.println("Delivery details updated successfully. Order ID: " + OrderID);
        } finally {
             // browser is left open; the previous one is closed when the next test starts
        }
    }

    // Test 8: Admin Accepting Picker Details
    @Test(priority = 12, dependsOnMethods = "distributorUpdatingDeliveryDetailsTest", retryAnalyzer = RetryAnalyzer.class)
    public void adminAcceptingPickerDetails() throws Exception {

        System.out.println("\n========== TEST 12: ADMIN ACCEPTING PICKER DETAILS ==========");

        // Get the same Order ID used in Method 7
        Assert.assertNotNull(OrderID, "Order ID is NULL from Method 7.");
        Assert.assertFalse(OrderID.trim().isEmpty(), "Order ID is EMPTY.");

        String orderIdFromMethod7 = OrderID.trim();

        System.out.println("Order ID received from Method 7: " + orderIdFromMethod7);

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");

        driver = createChromeDriver();

        try {
            driver.get(adminUrl);
            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.LoginPage(driver)
                    .loginToAdmin(username, password);

            waitForPageLoad();

            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
                    .clickOnManageOrdersLnk();

            waitForPageLoad();

            ManageOrdersPage moPage = new ManageOrdersPage(driver);

            
            moPage.clickOnCoinsLnk(driver);

            Thread.sleep(2000);
            moPage.clickOnPickerDetailsBtnBasedOnOrderIDAndAllActionsInThatPage(
                    driver, orderIdFromMethod7);

            Thread.sleep(2000);
            System.out.println(
                    "Picker Details action completed successfully. Order ID: "
                            + orderIdFromMethod7);

        } finally {
            // browser is left open; the previous one is closed when the next test starts
        }
    }

    // Opens the SILVER coin orders list from the profile menu.
    // getCoinOrdersLnk() opens the default (gold) orders list, where a silver order ID does not exist,
    // so the Reupload / Update Delivery buttons were never found.
    private void openSilverOrdersPage(DashboardPage dbPage) throws Exception {
        List<WebElement> silverLinks = driver.findElements(By.xpath(
                "//a[contains(translate(normalize-space(.),'SILVER','silver'),'silver')"
                + " and contains(translate(normalize-space(.),'ORDER','order'),'order')]"));

        if (!silverLinks.isEmpty() && silverLinks.get(0).isDisplayed()) {
            System.out.println("Opening silver orders link: " + silverLinks.get(0).getText().trim());
            silverLinks.get(0).click();
        } else {
            System.out.println("No separate silver orders link found, using coin orders link.");
            dbPage.getCoinOrdersLnk().click();
            Thread.sleep(2000);

            // If the orders page has a Silver tab / toggle, switch to it
            List<WebElement> silverTabs = driver.findElements(By.xpath(
                    "//*[self::button or self::a or self::li or self::span]"
                    + "[normalize-space(.)='Silver' or normalize-space(.)='Silver Coins']"));
            if (!silverTabs.isEmpty() && silverTabs.get(0).isDisplayed()) {
                System.out.println("Switching to Silver tab.");
                silverTabs.get(0).click();
            }
        }

        Thread.sleep(2000);
        wUtil.waitForPageLoad(driver);

        // Fail with a clear message if this list does not contain the order
        try {
            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME)).until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//td[.='" + OrderID + "']")));
        } catch (Exception e) {
            throw new Exception("Order " + OrderID + " not found on the page opened for silver orders. "
                    + "Current URL: " + driver.getCurrentUrl());
        }
    }

    // Closes the PREVIOUS test's browser; called only when the next test opens a new one.
    // The browser used by the last test is intentionally left open.
    private void quitPreviousBrowser() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Browser close issue: " + e.getMessage());
            }
            driver = null;
        }
    }

    // Closes the browser after every test attempt (pass, fail or retry).
    // TestNG calls the listener's onTestFailure/onTestSkipped BEFORE this method,
    // so the screenshot is taken while the browser is still open.
    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Browser close issue: " + e.getMessage());
            }
            driver = null;
        }
        DriverHolder.clear();
    }

    
    
   
}