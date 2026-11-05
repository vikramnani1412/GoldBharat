package Goldbharat;

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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.goldbharath.genericutilities.ExcelFileUtility;
import com.goldbharath.genericutilities.PropertyFileUtility;
import com.goldbharath.genericutilities.RetryAnalyzer;
import com.goldbharath.genericutilities.WebDriverUtility;
import com.goldbharath.objectrepository.admin.BusinessInformationPage;
import com.goldbharath.objectrepository.admin.DistributorDetailsPage;
import com.goldbharath.objectrepository.admin.LogoutPage;
import com.goldbharath.objectrepository.admin.ManageDistributorPage;
import com.goldbharath.objectrepository.admin.ManageOrdersPage;
import com.goldbharath.objectrepository.user.BookGoldPage;
import com.goldbharath.objectrepository.user.BuildYourGoldCoinCombinationPage;
import com.goldbharath.objectrepository.user.DashboardPage;
import com.goldbharath.objectrepository.user.GoldCoinsOrdersHistoryPage;
import com.goldbharath.objectrepository.user.KycAsPvtLtdPage;
import com.goldbharath.objectrepository.user.LoginPage;
import com.goldbharath.objectrepository.user.RegisterPage;
import com.goldbharath.objectrepository.user.ReviewYourGoldOrderPage;
import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldCoinsPage;
import com.goldbharath.objectrepository.user.UserProfilePage;
import com.goldbharath.objectrepository.user.WelcomePage;
import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;

import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners(com.goldbharath.genericutilities.ListenersImplementationClass.class)
public class Two_RegisterAsPvtLtdBookingBuyingGoldCoinsFullWorkFlowTest {

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
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1920,1080",
                "--disable-notifications", "--disable-popup-blocking", "--disable-infobars");

        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

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

    @Test(priority = 1, dataProvider = "mobileNumbers", retryAnalyzer = RetryAnalyzer.class)
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
            if (driver != null) driver.quit();
        }
    }

    // Test 2: User Login and Pvt Ltd KYC

    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginUpdatingKycTest() throws Exception {
        System.out.println("\n========== TEST 2: LOGIN AND PVT LTD KYC ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 11);
        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 11);
        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 11);
        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 11);
        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 11);
        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 11);
        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 11);
        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 11);
        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 11);
        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 11);
        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 11);
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

            new KycAsPvtLtdPage(driver).PvtLtdKyc(
                    driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);

            System.out.println("Pvt Ltd KYC completed successfully. Mobile: " + registeredMobileNumber);
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // Test 3: Admin Rejecting Distributor KYC

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
        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);

        Assert.assertNotNull(goldLimit, "Gold limit is NULL in Excel.");
        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold limit is EMPTY in Excel.");
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
            if (driver != null) driver.quit();
        }
    }

    // Test 4: Distributor Re-submitting Rejected KYC

    @Test(priority = 4, dependsOnMethods = "loginToAdminRejectingDistributorKycTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginUpdatingRejectedKycTest() throws Exception {
        System.out.println("\n========== TEST 4: Distributor LOGIN AND Updating Rejected PVT LTD KYC ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 11);
        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 11);
        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 11);
        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 11);
        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 11);
        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 11);
        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 11);
        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 11);
        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 11);
        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 11);
        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 11);
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

            
            new KycAsPvtLtdPage(driver).PvtLtdKycAfterRejected(driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, accountHolderName, ifscCode, accountHolderName);
            
//            new KycAsPvtLtdPage(driver).PvtLtdKycAfterRejecting(
//                    driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);

            System.out.println("Pvt Ltd KYC re-submitted successfully after rejection. Mobile: " + registeredMobileNumber);
        } finally {
            if (driver != null) driver.quit();
        }
    }


    // Test 3: Admin KYC Approval and Gold/Silver Limits

    @Test(priority = 5, dependsOnMethods = "loginUpdatingRejectedKycTest", retryAnalyzer = RetryAnalyzer.class)
    public void loginToAdminApproveKycAndGivingGoldAndSilverLimitTest() throws Exception {
        System.out.println("\n========== TEST 5: ADMIN accepting DISTRIBUTOR Kyc FLOW ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
        String username = pUtil.readDataFromPropertyFile("adminusername");
        String password = pUtil.readDataFromPropertyFile("adminpassword");
        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);

        Assert.assertNotNull(goldLimit, "Gold limit is NULL in Excel.");
        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold limit is EMPTY in Excel.");
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
                    .editingAndGivingSilverAndGoldLimitForLtd(silverLimit, goldLimit);

            System.out.println("Admin flow completed successfully.");
            System.out.println("Mobile: " + registeredMobileNumber);
            System.out.println("Owner: " + ownerName);
            System.out.println("Gold Limit: " + goldLimit);
            System.out.println("Silver Limit: " + silverLimit);

            Thread.sleep(2000);
            new LogoutPage(driver).logoutOfApplication();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // Test 4: Distributor Gold Booking

    @Test(priority = 6, dependsOnMethods = "loginToAdminApproveKycAndGivingGoldAndSilverLimitTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorBookingGoldTest() throws Exception {
        System.out.println("\n========== TEST 6: DISTRIBUTOR GOLD BOOKING ==========");

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

            new DashboardPage(driver).clickOnBookGoldBtn(driver);
            Thread.sleep(2000);

            new BookGoldPage(driver).fetchingGoldLivePriceAndBookingGold(driver);

            System.out.println("Gold booking completed. Mobile: " + registeredMobileNumber);
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // Test 5: Distributor Gold Purchase and Payment Document Upload

    @Test(priority = 7, dependsOnMethods = "distributorBookingGoldTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorBuyingGoldTest() throws Exception {
        String serialNo = "1";
        String transactionImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);

        System.out.println("\n========== TEST 7: DISTRIBUTOR GOLD BUYING ==========");

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
            
            new DashboardPage(driver).clickOnBuyGoldBtn(driver);
            Thread.sleep(2000);

            new whatWouldYouLikeToBuyPage(driver).getGoldCoinsEle().click();
            Thread.sleep(2000);

            BuildYourGoldCoinCombinationPage bPage = new BuildYourGoldCoinCombinationPage(driver);
            bPage.increaseGoldGramsAccToWeight(driver, "1", 1);
            bPage.increaseGoldGramsAccToWeight(driver, "2", 2);
            bPage.increaseGoldGramsAccToWeight(driver, "5", 1);
            bPage.increaseGoldGramsAccToWeight(driver, "10", 2);
            bPage.increaseGoldGramsAccToWeight(driver, "20", 1);
            bPage.increaseGoldGramsAccToWeight(driver, "50", 1);
            bPage.increaseGoldGramsAccToWeight(driver, "100", 1);

            Thread.sleep(2000);
            bPage.getContinueBtn().click();
            Thread.sleep(2000);

            new ReviewYourGoldOrderPage(driver).takeAllDetailsAndClickProceedBtn(driver);
            Thread.sleep(2000);

            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
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

            new UploadTransactionDetailsForGoldCoinsPage(driver)
                    .uploadingTransactionDetails(transactionImage, transactionIdNum);
            Thread.sleep(2000);

            gPage.successMsgValidation(driver, OrderID);

            System.out.println("Gold purchase and payment document upload completed. Order ID: " + OrderID);
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // Test 6: Admin Payment Declineing

    @Test(priority = 8, dependsOnMethods = "distributorBuyingGoldTest", retryAnalyzer = RetryAnalyzer.class)
    public void adminCheckingPurchasedQuantityGoldAndDeclinePaymentTest() throws Exception {
        System.out.println("\n========== TEST 8: ADMIN PAYMENT CONFIRMATION ==========");

        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
        String Reason = eUtil.readDataFromExcel("Admin", 2, 1);
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
             if (driver != null) driver.quit();
        }
    }

    //Test 6: Admin Payment Confirmation and Dispatch

    @Test(priority = 9, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndDeclinePaymentTest", retryAnalyzer = RetryAnalyzer.class)
    public void distributorReUploadingPaymentDetailsTest() throws Exception
    {
        System.out.println("\n========== TEST 9: UPDATE DELIVERY DETAILS ==========");

        String transactionImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
        
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

            dbPage.getCoinOrdersLnk().click();
            Thread.sleep(2000);

            wUtil.waitForPageLoad(driver);
            
            new GoldCoinsOrdersHistoryPage(driver)
                    .clickOnReUploadPaymentDocumentBtnBasedOnOrderID(driver, OrderID);
            
            new UploadTransactionDetailsForGoldCoinsPage(driver)
            .ReUploadingTransactionDetails(driver, transactionImage, transactionIdNum);
            Thread.sleep(2000);
            System.out.println("Re Uploaded Transaction Image and Number");
            
        } finally {
             if (driver != null) driver.quit();
        }
    }


    // Test 6: Admin Payment Confirmation and Dispatch

    @Test(priority = 10, dependsOnMethods = "distributorReUploadingPaymentDetailsTest", retryAnalyzer = RetryAnalyzer.class)
    public void adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest() throws Exception {
        System.out.println("\n========== TEST 10: ADMIN PAYMENT CONFIRMATION ==========");

        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
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
             if (driver != null) driver.quit();
        }
    }

    // Test 7: Distributor Delivery Details Update
    @Test(priority = 11, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest", retryAnalyzer = RetryAnalyzer.class)
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

            dbPage.getCoinOrdersLnk().click();
            Thread.sleep(2000);

            wUtil.waitForPageLoad(driver);
            
            new GoldCoinsOrdersHistoryPage(driver)
                    .clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            new UpdateDeliveryDetailsPage(driver)
                    .updatingDeliveryDetails(driver, name, phoneNumber, idNumber, idImage);

            System.out.println("Delivery details updated successfully. Order ID: " + OrderID);
        } finally {
             if (driver != null) driver.quit();
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

            System.out.println(
                    "Picker Details action completed successfully. Order ID: "
                            + orderIdFromMethod7);

        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }
    
    
}























//package Goldbharat;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.goldbharath.genericutilities.ExcelFileUtility;
//import com.goldbharath.genericutilities.PropertyFileUtility;
//import com.goldbharath.genericutilities.WebDriverUtility;
//import com.goldbharath.objectrepository.admin.BusinessInformationPage;
//import com.goldbharath.objectrepository.admin.DistributorDetailsPage;
//import com.goldbharath.objectrepository.admin.LogoutPage;
//import com.goldbharath.objectrepository.admin.ManageDistributorPage;
//import com.goldbharath.objectrepository.admin.ManageOrdersPage;
//import com.goldbharath.objectrepository.user.BookGoldPage;
//import com.goldbharath.objectrepository.user.BuildYourGoldCoinCombinationPage;
//import com.goldbharath.objectrepository.user.DashboardPage;
//import com.goldbharath.objectrepository.user.GoldCoinsOrdersHistoryPage;
//import com.goldbharath.objectrepository.user.KycAsPvtLtdPage;
//import com.goldbharath.objectrepository.user.LoginPage;
//import com.goldbharath.objectrepository.user.RegisterPage;
//import com.goldbharath.objectrepository.user.ReviewYourGoldOrderPage;
//import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
//import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldCoinsPage;
//import com.goldbharath.objectrepository.user.UserProfilePage;
//import com.goldbharath.objectrepository.user.WelcomePage;
//import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//
//@Listeners(com.goldbharath.genericutilities.ListenersImplementationClass.class)
//public class Two_RegisterAsPvtLtdBookingBuyingGoldCoinsFullWorkFlowTest {
//
//    WebDriverUtility wUtil = new WebDriverUtility();
//    PropertyFileUtility pUtil = new PropertyFileUtility();
//    ExcelFileUtility eUtil = new ExcelFileUtility();
//
//    private WebDriver driver;
//    private static String registeredMobileNumber;
//    private static String OrderID;
//
//    private static final String SHEET_NAME = "Sheet1";
//    private static final int WAIT_TIME = 20;
//
//    // Browser Utilities
//
//    private WebDriver createChromeDriver() {
//        WebDriverManager.chromedriver().setup();
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-gpu", "--window-size=1920,1080",
//                "--disable-notifications", "--disable-popup-blocking", "--disable-infobars");
//
//        WebDriver webDriver = new ChromeDriver(options);
//        webDriver.manage().window().maximize();
//        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//
//        return webDriver;
//    }
//
//    private void waitForPageLoad() {
//        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
//                .until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
//                        .executeScript("return document.readyState").equals("complete"));
//    }
//
//    private void clickRegisterButton() {
//        By registerButton = By.xpath("//button[normalize-space()='Register']");
//        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
//                .until(ExpectedConditions.elementToBeClickable(registerButton)).click();
//    }
//
//    // Test Data Provider
//
//    @DataProvider(name = "mobileNumbers")
//    public Object[][] mobileNumbers() throws Exception {
//        List<String> mobileNumbers = new ArrayList<>();
//
//        for (int row = 10; row <= 29; row++) {
//            String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, row, 1);
//
//            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
//                mobileNumbers.add(mobileNumber.trim());
//                System.out.println("Excel Row " + row + " : " + mobileNumber.trim());
//            }
//        }
//
//        if (mobileNumbers.isEmpty()) {
//            throw new Exception("No mobile numbers found in Excel rows 10 to 29.");
//        }
//
//        return new Object[][]{{mobileNumbers}};
//    }
//    
//  //Test 1: User Registration
//
//    @Test(priority = 1, dataProvider = "mobileNumbers")
//    public void RegisteringToGoldbharathTest(List<String> mobileNumbers) throws Exception {
//        System.out.println("\n========== TEST 1: USER REGISTRATION ==========");
//
//        String businessName = eUtil.readDataFromExcel(SHEET_NAME, 1, 1);
//        String personName = eUtil.readDataFromExcel(SHEET_NAME, 2, 1);
//        String email = eUtil.readDataFromExcel(SHEET_NAME, 3, 1);
//        String date = eUtil.readDataFromExcel(SHEET_NAME, 5, 1);
//        String pincode = eUtil.readDataFromExcel(SHEET_NAME, 6, 1);
//        String address = eUtil.readDataFromExcel(SHEET_NAME, 7, 1);
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//            clickRegisterButton();
//
//            RegisterPage registerPage = new RegisterPage(driver);
//            registeredMobileNumber = registerPage.registeringToGoldbharathApplication(
//                    driver, businessName, personName, email, mobileNumbers, date, pincode, address);
//
//            Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
//            Assert.assertFalse(registeredMobileNumber.trim().isEmpty(), "Registered mobile number is EMPTY.");
//
//            System.out.println("Registration successful. Mobile: " + registeredMobileNumber);
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 2: User Login and Pvt Ltd KYC
//
//    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest")
//    public void loginUpdatingKycTest() throws Exception {
//        System.out.println("\n========== TEST 2: LOGIN AND PVT LTD KYC ==========");
//
//        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//            throw new Exception("Registered mobile number is not available.");
//        }
//
//        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 11);
//        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 11);
//        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 11);
//        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 11);
//        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 11);
//        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 11);
//        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 11);
//        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 11);
//        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 11);
//        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 11);
//        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 11);
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//        String OTP = pUtil.readDataFromPropertyFile("userotp");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//
//            new WelcomePage(driver).clickOnLoginLink();
//            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
//            waitForPageLoad();
//
//            new DashboardPage(driver).clickOnUserProfileImageAndLogoutLink(driver);
//            new UserProfilePage(driver).clickOnKycLink();
//
//            new KycAsPvtLtdPage(driver).PvtLtdKyc(
//                    driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);
//
//            System.out.println("Pvt Ltd KYC completed successfully. Mobile: " + registeredMobileNumber);
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 3: Admin Rejecting Distributor KYC
//
//    @Test(priority = 3, dependsOnMethods = "loginUpdatingKycTest")
//    public void loginToAdminRejectingDistributorKycTest() throws Exception {
//        System.out.println("\n========== TEST 3: ADMIN Rejecting DISTRIBUTOR Kyc FLOW ==========");
//
//        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//            throw new Exception("Registered mobile number is not available.");
//        }
//
//        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//        String username = pUtil.readDataFromPropertyFile("adminusername");
//        String password = pUtil.readDataFromPropertyFile("adminpassword");
//        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
//        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
//        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);
//
//        Assert.assertNotNull(goldLimit, "Gold limit is NULL in Excel.");
//        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold limit is EMPTY in Excel.");
//        Assert.assertNotNull(silverLimit, "Silver limit is NULL in Excel.");
//        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver limit is EMPTY in Excel.");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(adminUrl);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.LoginPage(driver)
//                    .loginToAdmin(username, password);
//            waitForPageLoad();
//
//            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
//                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
//
//            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
//                    .until(ExpectedConditions.elementToBeClickable(
//                            adminDashboardPage.getAllDistributorLnk())).click();
//
//            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
//            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
//                    driver, registeredMobileNumber, ownerName);
//
//            new DistributorDetailsPage(driver).rejectingDistributorKycDetails();
//
//            Thread.sleep(2000);
//            manageDistributorPage.clickOnDistributorNameBasedOnMobileNumber(driver, registeredMobileNumber);
//            
//            Thread.sleep(2000);
//            new DistributorDetailsPage(driver).checkingRejectedStatus(driver);
//            
//            Thread.sleep(2000);
//        }
//        finally 
//        {
//            if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 4: Distributor Re-submitting Rejected KYC
//
//    @Test(priority = 4, dependsOnMethods = "loginToAdminRejectingDistributorKycTest")
//    public void loginUpdatingRejectedKycTest() throws Exception {
//        System.out.println("\n========== TEST 4: Distributor LOGIN AND Updating Rejected PVT LTD KYC ==========");
//
//        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//            throw new Exception("Registered mobile number is not available.");
//        }
//
//        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 11);
//        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 11);
//        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 11);
//        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 11);
//        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 11);
//        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 11);
//        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 11);
//        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 11);
//        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 11);
//        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 11);
//        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 11);
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//        String OTP = pUtil.readDataFromPropertyFile("userotp");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//
//            new WelcomePage(driver).clickOnLoginLink();
//            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
//            waitForPageLoad();
//
//            new DashboardPage(driver).clickOnUserProfileImageAndLogoutLink(driver);
//            new UserProfilePage(driver).clickOnKycLink();
//
//            
//            new KycAsPvtLtdPage(driver).PvtLtdKycAfterRejected(driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, accountHolderName, ifscCode, accountHolderName);
//            
////            new KycAsPvtLtdPage(driver).PvtLtdKycAfterRejecting(
////                    driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);
//
//            System.out.println("Pvt Ltd KYC re-submitted successfully after rejection. Mobile: " + registeredMobileNumber);
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }
//
//
//    // Test 3: Admin KYC Approval and Gold/Silver Limits
//
//    @Test(priority = 5, dependsOnMethods = "loginUpdatingRejectedKycTest")
//    public void loginToAdminApproveKycAndGivingGoldAndSilverLimitTest() throws Exception {
//        System.out.println("\n========== TEST 5: ADMIN accepting DISTRIBUTOR Kyc FLOW ==========");
//
//        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//            throw new Exception("Registered mobile number is not available.");
//        }
//
//        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//        String username = pUtil.readDataFromPropertyFile("adminusername");
//        String password = pUtil.readDataFromPropertyFile("adminpassword");
//        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
//        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
//        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);
//
//        Assert.assertNotNull(goldLimit, "Gold limit is NULL in Excel.");
//        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold limit is EMPTY in Excel.");
//        Assert.assertNotNull(silverLimit, "Silver limit is NULL in Excel.");
//        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver limit is EMPTY in Excel.");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(adminUrl);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.LoginPage(driver)
//                    .loginToAdmin(username, password);
//            waitForPageLoad();
//
//            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
//                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
//
//            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
//                    .until(ExpectedConditions.elementToBeClickable(
//                            adminDashboardPage.getAllDistributorLnk())).click();
//
//            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
//            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
//                    driver, registeredMobileNumber, ownerName);
//
//            new DistributorDetailsPage(driver).acceptingDistributorKycDetails();
//
//            manageDistributorPage.clickOnRecentlyRegisteredDistributorEditButton(
//                    driver, registeredMobileNumber);
//
//            new BusinessInformationPage(driver)
//                    .editingAndGivingSilverAndGoldLimitForLtd(silverLimit, goldLimit);
//
//            System.out.println("Admin flow completed successfully.");
//            System.out.println("Mobile: " + registeredMobileNumber);
//            System.out.println("Owner: " + ownerName);
//            System.out.println("Gold Limit: " + goldLimit);
//            System.out.println("Silver Limit: " + silverLimit);
//
//            Thread.sleep(2000);
//            new LogoutPage(driver).logoutOfApplication();
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 4: Distributor Gold Booking
//
//    @Test(priority = 6, dependsOnMethods = "loginToAdminApproveKycAndGivingGoldAndSilverLimitTest")
//    public void distributorBookingGoldTest() throws Exception {
//        System.out.println("\n========== TEST 6: DISTRIBUTOR GOLD BOOKING ==========");
//
//        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//            throw new Exception("Registered mobile number is not available.");
//        }
//
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//        String OTP = pUtil.readDataFromPropertyFile("userotp");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//
//            new WelcomePage(driver).clickOnLoginLink();
//            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
//            waitForPageLoad();
//
//            new DashboardPage(driver).clickOnBookGoldBtn(driver);
//            Thread.sleep(2000);
//
//            new BookGoldPage(driver).fetchingGoldLivePriceAndBookingGold(driver);
//
//            System.out.println("Gold booking completed. Mobile: " + registeredMobileNumber);
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 5: Distributor Gold Purchase and Payment Document Upload
//
//    @Test(priority = 7, dependsOnMethods = "distributorBookingGoldTest")
//    public void distributorBuyingGoldTest() throws Exception {
//        String serialNo = "1";
//        String transactionImage = System.getProperty("user.dir")
//                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
//        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
//
//        System.out.println("\n========== TEST 7: DISTRIBUTOR GOLD BUYING ==========");
//
//        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//            throw new Exception("Registered mobile number is not available.");
//        }
//
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//        String OTP = pUtil.readDataFromPropertyFile("userotp");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//
//            new WelcomePage(driver).clickOnLoginLink();
//            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
//            waitForPageLoad();
//
//            Thread.sleep(2000);
//            
//            new DashboardPage(driver).clickOnBuyGoldBtn(driver);
//            Thread.sleep(2000);
//
//            new whatWouldYouLikeToBuyPage(driver).getGoldCoinsEle().click();
//            Thread.sleep(2000);
//
//            BuildYourGoldCoinCombinationPage bPage = new BuildYourGoldCoinCombinationPage(driver);
//            bPage.increaseGoldGramsAccToWeight(driver, "1", 1);
//            bPage.increaseGoldGramsAccToWeight(driver, "2", 2);
//            bPage.increaseGoldGramsAccToWeight(driver, "5", 1);
//            bPage.increaseGoldGramsAccToWeight(driver, "10", 2);
//            bPage.increaseGoldGramsAccToWeight(driver, "20", 1);
//            bPage.increaseGoldGramsAccToWeight(driver, "50", 1);
//            bPage.increaseGoldGramsAccToWeight(driver, "100", 1);
//
//            Thread.sleep(2000);
//            bPage.getContinueBtn().click();
//            Thread.sleep(2000);
//
//            new ReviewYourGoldOrderPage(driver).takeAllDetailsAndClickProceedBtn(driver);
//            Thread.sleep(2000);
//
//            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
//            gPage.GetAllOrderDetailsBasedOnSerialNo(driver, serialNo);
//            Thread.sleep(2000);
//
//            OrderID = gPage.GetOrderIdBasedOnSerialNo(driver, serialNo);
//
//            if (OrderID == null || OrderID.trim().isEmpty()) {
//                throw new Exception("Order ID was not captured from Test 5.");
//            }
//
//            OrderID = OrderID.trim();
//            System.out.println("Method 5th Order ID generated: " + OrderID);
//
//            gPage.clickOnUpdatePaymentDocumentBtnBasedOnOrderID(driver, OrderID);
//            Thread.sleep(2000);
//
//            new UploadTransactionDetailsForGoldCoinsPage(driver)
//                    .uploadingTransactionDetails(transactionImage, transactionIdNum);
//            Thread.sleep(2000);
//
//            gPage.successMsgValidation(driver, OrderID);
//
//            System.out.println("Gold purchase and payment document upload completed. Order ID: " + OrderID);
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 6: Admin Payment Declineing
//
//    @Test(priority = 8, dependsOnMethods = "distributorBuyingGoldTest")
//    public void adminCheckingPurchasedQuantityGoldAndDeclinePaymentTest() throws Exception {
//        System.out.println("\n========== TEST 8: ADMIN PAYMENT CONFIRMATION ==========");
//
//        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
//        String Reason = eUtil.readDataFromExcel("Admin", 2, 1);
//        String daysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);
//
//        if (OrderID == null || OrderID.trim().isEmpty()) {
//            throw new Exception("Order ID is not available from Test 5.");
//        }
//
//        OrderID = OrderID.trim();
//        System.out.println("Order ID: " + OrderID);
//
//        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//        String username = pUtil.readDataFromPropertyFile("adminusername");
//        String password = pUtil.readDataFromPropertyFile("adminpassword");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(adminUrl);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.LoginPage(driver)
//                    .loginToAdmin(username, password);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
//                    .clickOnManageOrdersLnk();
//            Thread.sleep(2000);
//
//            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
//                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
//
//            moPage.checkingRecentlyBuyedCoinsDetailsDecline(driver, OrderID, Reason);
//            Thread.sleep(2000);
//
//            try 
//            {
//    			Thread.sleep(1000);
//    			WebElement Declined = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/a[.='Payment Declined']"));
//    			if(Declined.isDisplayed())
//    			{
//    				System.out.println("Declined Msg Displayed");
//    			}
//    		}
//            catch (Exception e) 
//            {
//            	System.out.println("Declined Msg Not Displayed");
//    		}
//
//        } finally {
//             if (driver != null) driver.quit();
//        }
//    }
//
//    //Test 6: Admin Payment Confirmation and Dispatch
//
//    @Test(priority = 9, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndDeclinePaymentTest")
//    public void distributorReUploadingPaymentDetailsTest() throws Exception
//    {
//        System.out.println("\n========== TEST 9: UPDATE DELIVERY DETAILS ==========");
//
//        String transactionImage = System.getProperty("user.dir")
//                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
//        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
//        
//        String idImage = System.getProperty("user.dir")
//                + "/src/test/resources/assets/Aadhaar_Card.jpeg";
//        String name = eUtil.readDataFromExcel("Admin", 6, 1);
//        String phoneNumber = eUtil.readDataFromExcel("Admin", 7, 1);
//        String idNumber = eUtil.readDataFromExcel("Admin", 8, 1);
//
//        Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
//        Assert.assertFalse(registeredMobileNumber.trim().isEmpty(),
//                "Registered mobile number is EMPTY.");
//
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//        String OTP = pUtil.readDataFromPropertyFile("userotp");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//
//            new WelcomePage(driver).clickOnLoginLink();
//            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
//            waitForPageLoad();
//
//            DashboardPage dbPage = new DashboardPage(driver);
//            dbPage.getUserProfileImg().click();
//            Thread.sleep(2000);
//
//            dbPage.getCoinOrdersLnk().click();
//            Thread.sleep(2000);
//
//            wUtil.waitForPageLoad(driver);
//            
//            new GoldCoinsOrdersHistoryPage(driver)
//                    .clickOnReUploadPaymentDocumentBtnBasedOnOrderID(driver, OrderID);
//            
//            new UploadTransactionDetailsForGoldCoinsPage(driver)
//            .ReUploadingTransactionDetails(driver, transactionImage, transactionIdNum);
//            Thread.sleep(2000);
//            System.out.println("Re Uploaded Transaction Image and Number");
//            
//        } finally {
//             if (driver != null) driver.quit();
//        }
//    }
//
//
//    // Test 6: Admin Payment Confirmation and Dispatch
//
//    @Test(priority = 10, dependsOnMethods = "distributorReUploadingPaymentDetailsTest")
//    public void adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest() throws Exception {
//        System.out.println("\n========== TEST 10: ADMIN PAYMENT CONFIRMATION ==========");
//
//        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
//        String daysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);
//
//        if (OrderID == null || OrderID.trim().isEmpty()) {
//            throw new Exception("Order ID is not available from Test 5.");
//        }
//
//        OrderID = OrderID.trim();
//        System.out.println("Order ID: " + OrderID);
//
//        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//        String username = pUtil.readDataFromPropertyFile("adminusername");
//        String password = pUtil.readDataFromPropertyFile("adminpassword");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(adminUrl);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.LoginPage(driver)
//                    .loginToAdmin(username, password);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
//                    .clickOnManageOrdersLnk();
//            Thread.sleep(2000);
//
//            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
//                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
//
//            moPage.checkingRecentlyBuyedCoinsDetailsAccepting(driver, OrderID);
//            Thread.sleep(2000);
//
//            // Retry-safe: skip dispatch actions if the order was already verified/dispatched in an earlier attempt
//            boolean verified = moPage.clickOnVerifyOrderBtnBasedOnOrderID(driver, OrderID);
//            Thread.sleep(2000);
//
//            if (verified) {
//                moPage.allActionsInConfirmDispatchPageInLLpPage(deliveryMsg, daysToDeliver);
//                Thread.sleep(2000);
//            }
//            
//            moPage.confirmingOrderStatus(driver, OrderID);
//
//        } finally {
//             if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 7: Distributor Delivery Details Update
//    @Test(priority = 11, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest")
//    public void distributorUpdatingDeliveryDetailsTest() throws Exception
//    {
//        System.out.println("\n========== TEST 11: UPDATE DELIVERY DETAILS ==========");
//
//        String idImage = System.getProperty("user.dir")
//                + "/src/test/resources/assets/Aadhaar_Card.jpeg";
//        String name = eUtil.readDataFromExcel("Admin", 6, 1);
//        String phoneNumber = eUtil.readDataFromExcel("Admin", 7, 1);
//        String idNumber = eUtil.readDataFromExcel("Admin", 8, 1);
//
//        Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
//        Assert.assertFalse(registeredMobileNumber.trim().isEmpty(),
//                "Registered mobile number is EMPTY.");
//
//        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//        String OTP = pUtil.readDataFromPropertyFile("userotp");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(userUrl);
//            waitForPageLoad();
//
//            new WelcomePage(driver).clickOnLoginLink();
//            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
//            waitForPageLoad();
//
//            DashboardPage dbPage = new DashboardPage(driver);
//            dbPage.getUserProfileImg().click();
//            Thread.sleep(2000);
//
//            dbPage.getCoinOrdersLnk().click();
//            Thread.sleep(2000);
//
//            wUtil.waitForPageLoad(driver);
//            
//            new GoldCoinsOrdersHistoryPage(driver)
//                    .clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
//            Thread.sleep(2000);
//
//            new UpdateDeliveryDetailsPage(driver)
//                    .updatingDeliveryDetails(driver, name, phoneNumber, idNumber, idImage);
//
//            System.out.println("Delivery details updated successfully. Order ID: " + OrderID);
//        } finally {
//             if (driver != null) driver.quit();
//        }
//    }
//
//    // Test 8: Admin Accepting Picker Details
//    @Test(priority = 12, dependsOnMethods = "distributorUpdatingDeliveryDetailsTest")
//    public void adminAcceptingPickerDetails() throws Exception {
//
//        System.out.println("\n========== TEST 12: ADMIN ACCEPTING PICKER DETAILS ==========");
//
//        // Get the same Order ID used in Method 7
//        Assert.assertNotNull(OrderID, "Order ID is NULL from Method 7.");
//        Assert.assertFalse(OrderID.trim().isEmpty(), "Order ID is EMPTY.");
//
//        String orderIdFromMethod7 = OrderID.trim();
//
//        System.out.println("Order ID received from Method 7: " + orderIdFromMethod7);
//
//        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//        String username = pUtil.readDataFromPropertyFile("adminusername");
//        String password = pUtil.readDataFromPropertyFile("adminpassword");
//
//        driver = createChromeDriver();
//
//        try {
//            driver.get(adminUrl);
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.LoginPage(driver)
//                    .loginToAdmin(username, password);
//
//            waitForPageLoad();
//
//            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
//                    .clickOnManageOrdersLnk();
//
//            waitForPageLoad();
//
//            ManageOrdersPage moPage = new ManageOrdersPage(driver);
//
//            
//            moPage.clickOnCoinsLnk(driver);
//
//            Thread.sleep(2000);
//            moPage.clickOnPickerDetailsBtnBasedOnOrderIDAndAllActionsInThatPage(
//                    driver, orderIdFromMethod7);
//
//            System.out.println(
//                    "Picker Details action completed successfully. Order ID: "
//                            + orderIdFromMethod7);
//
//        } finally {
//            if (driver != null) {
//                driver.quit();
//                driver = null;
//            }
//        }
//    }
//    
//    
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package Goldbharat;
////
////import java.time.Duration;
////import java.util.ArrayList;
////import java.util.List;
////import org.openqa.selenium.By;
////import org.openqa.selenium.WebDriver;
////import org.openqa.selenium.WebElement;
////import org.openqa.selenium.chrome.ChromeDriver;
////import org.openqa.selenium.chrome.ChromeOptions;
////import org.openqa.selenium.support.ui.ExpectedConditions;
////import org.openqa.selenium.support.ui.WebDriverWait;
////import org.testng.Assert;
////import org.testng.annotations.DataProvider;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////import com.goldbharath.genericutilities.ExcelFileUtility;
////import com.goldbharath.genericutilities.PropertyFileUtility;
////import com.goldbharath.genericutilities.WebDriverUtility;
////import com.goldbharath.objectrepository.admin.BusinessInformationPage;
////import com.goldbharath.objectrepository.admin.DistributorDetailsPage;
////import com.goldbharath.objectrepository.admin.LogoutPage;
////import com.goldbharath.objectrepository.admin.ManageDistributorPage;
////import com.goldbharath.objectrepository.admin.ManageOrdersPage;
////import com.goldbharath.objectrepository.user.BookGoldPage;
////import com.goldbharath.objectrepository.user.BuildYourGoldCoinCombinationPage;
////import com.goldbharath.objectrepository.user.DashboardPage;
////import com.goldbharath.objectrepository.user.GoldCoinsOrdersHistoryPage;
////import com.goldbharath.objectrepository.user.KycAsPvtLtdPage;
////import com.goldbharath.objectrepository.user.LoginPage;
////import com.goldbharath.objectrepository.user.RegisterPage;
////import com.goldbharath.objectrepository.user.ReviewYourGoldOrderPage;
////import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
////import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldCoinsPage;
////import com.goldbharath.objectrepository.user.UserProfilePage;
////import com.goldbharath.objectrepository.user.WelcomePage;
////import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;
////
////import io.github.bonigarcia.wdm.WebDriverManager;
////
////@Listeners(com.goldbharath.genericutilities.ListenersImplementationClass.class)
////public class Two_RegisterAsPvtLtdBookingBuyingGoldCoinsFullWorkFlowTest {
////
////    WebDriverUtility wUtil = new WebDriverUtility();
////    PropertyFileUtility pUtil = new PropertyFileUtility();
////    ExcelFileUtility eUtil = new ExcelFileUtility();
////
////    private WebDriver driver;
////    private static String registeredMobileNumber;
////    private static String OrderID;
////
////    private static final String SHEET_NAME = "Sheet1";
////    private static final int WAIT_TIME = 20;
////
////    // Browser Utilities
////
////    private WebDriver createChromeDriver() {
////        WebDriverManager.chromedriver().setup();
////
////        ChromeOptions options = new ChromeOptions();
////        options.addArguments("--disable-gpu", "--window-size=1920,1080",
////                "--disable-notifications", "--disable-popup-blocking", "--disable-infobars");
////
////        WebDriver webDriver = new ChromeDriver(options);
////        webDriver.manage().window().maximize();
////        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
////
////        return webDriver;
////    }
////
////    private void waitForPageLoad() {
////        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
////                .until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
////                        .executeScript("return document.readyState").equals("complete"));
////    }
////
////    private void clickRegisterButton() {
////        By registerButton = By.xpath("//button[normalize-space()='Register']");
////        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
////                .until(ExpectedConditions.elementToBeClickable(registerButton)).click();
////    }
////
////    // Test Data Provider
////
////    @DataProvider(name = "mobileNumbers")
////    public Object[][] mobileNumbers() throws Exception {
////        List<String> mobileNumbers = new ArrayList<>();
////
////        for (int row = 10; row <= 29; row++) {
////            String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, row, 1);
////
////            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
////                mobileNumbers.add(mobileNumber.trim());
////                System.out.println("Excel Row " + row + " : " + mobileNumber.trim());
////            }
////        }
////
////        if (mobileNumbers.isEmpty()) {
////            throw new Exception("No mobile numbers found in Excel rows 10 to 29.");
////        }
////
////        return new Object[][]{{mobileNumbers}};
////    }
////    
////  //Test 1: User Registration
////
////    @Test(priority = 1, dataProvider = "mobileNumbers")
////    public void RegisteringToGoldbharathTest(List<String> mobileNumbers) throws Exception {
////        System.out.println("\n========== TEST 1: USER REGISTRATION ==========");
////
////        String businessName = eUtil.readDataFromExcel(SHEET_NAME, 1, 1);
////        String personName = eUtil.readDataFromExcel(SHEET_NAME, 2, 1);
////        String email = eUtil.readDataFromExcel(SHEET_NAME, 3, 1);
////        String date = eUtil.readDataFromExcel(SHEET_NAME, 5, 1);
////        String pincode = eUtil.readDataFromExcel(SHEET_NAME, 6, 1);
////        String address = eUtil.readDataFromExcel(SHEET_NAME, 7, 1);
////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(userUrl);
////            waitForPageLoad();
////            clickRegisterButton();
////
////            RegisterPage registerPage = new RegisterPage(driver);
////            registeredMobileNumber = registerPage.registeringToGoldbharathApplication(
////                    driver, businessName, personName, email, mobileNumbers, date, pincode, address);
////
////            Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
////            Assert.assertFalse(registeredMobileNumber.trim().isEmpty(), "Registered mobile number is EMPTY.");
////
////            System.out.println("Registration successful. Mobile: " + registeredMobileNumber);
////        } finally {
////            if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 2: User Login and Pvt Ltd KYC
////
////    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest")
////    public void loginUpdatingKYC() throws Exception {
////        System.out.println("\n========== TEST 2: LOGIN AND PVT LTD KYC ==========");
////
////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
////            throw new Exception("Registered mobile number is not available.");
////        }
////
////        String name = eUtil.readDataFromExcel(SHEET_NAME, 34, 11);
////        String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 11);
////        String aadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 11);
////        String panNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 11);
////        String secondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 11);
////        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 11);
////        String cinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 11);
////        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 11);
////        String acNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 11);
////        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 11);
////        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 11);
////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
////        String OTP = pUtil.readDataFromPropertyFile("userotp");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(userUrl);
////            waitForPageLoad();
////
////            new WelcomePage(driver).clickOnLoginLink();
////            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
////            waitForPageLoad();
////
////            new DashboardPage(driver).clickOnUserProfileImageAndLogoutLink(driver);
////            new UserProfilePage(driver).clickOnKycLink();
////
////            new KycAsPvtLtdPage(driver).PvtLtdKyc(
////                    driver, name, mobileNumber, aadharNumber, panNumber, secondDirectorName, gstNumber, cinNumber, bankName, acNumber, ifscCode, accountHolderName);
////
////            System.out.println("Pvt Ltd KYC completed successfully. Mobile: " + registeredMobileNumber);
////        } finally {
////            if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 3: Admin KYC Approval and Gold/Silver Limits
////
////    @Test(priority = 3, dependsOnMethods = "loginUpdatingKYC")
////    public void loginToAdminApproveKycAndGivingGoldAndSilverLimit() throws Exception {
////        System.out.println("\n========== TEST 3: ADMIN DISTRIBUTOR FLOW ==========");
////
////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
////            throw new Exception("Registered mobile number is not available.");
////        }
////
////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
////        String username = pUtil.readDataFromPropertyFile("adminusername");
////        String password = pUtil.readDataFromPropertyFile("adminpassword");
////        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
////        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
////        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);
////
////        Assert.assertNotNull(goldLimit, "Gold limit is NULL in Excel.");
////        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold limit is EMPTY in Excel.");
////        Assert.assertNotNull(silverLimit, "Silver limit is NULL in Excel.");
////        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver limit is EMPTY in Excel.");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(adminUrl);
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.LoginPage(driver)
////                    .loginToAdmin(username, password);
////            waitForPageLoad();
////
////            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
////                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
////
////            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME))
////                    .until(ExpectedConditions.elementToBeClickable(
////                            adminDashboardPage.getAllDistributorLnk())).click();
////
////            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
////            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
////                    driver, registeredMobileNumber, ownerName);
////
////            new DistributorDetailsPage(driver).acceptingDistributorKycDetails();
////
////            manageDistributorPage.clickOnRecentlyRegisteredDistributorEditButton(
////                    driver, registeredMobileNumber);
////
////            new BusinessInformationPage(driver)
////                    .editingAndGivingSilverAndGoldLimitForLtd(silverLimit, goldLimit);
////
////            System.out.println("Admin flow completed successfully.");
////            System.out.println("Mobile: " + registeredMobileNumber);
////            System.out.println("Owner: " + ownerName);
////            System.out.println("Gold Limit: " + goldLimit);
////            System.out.println("Silver Limit: " + silverLimit);
////
////            Thread.sleep(2000);
////            new LogoutPage(driver).logoutOfApplication();
////        } finally {
////            if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 4: Distributor Gold Booking
////
////    @Test(priority = 4, dependsOnMethods = "loginToAdminApproveKycAndGivingGoldAndSilverLimit")
////    public void distributorBookingGoldTest() throws Exception {
////        System.out.println("\n========== TEST 4: DISTRIBUTOR GOLD BOOKING ==========");
////
////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
////            throw new Exception("Registered mobile number is not available.");
////        }
////
////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
////        String OTP = pUtil.readDataFromPropertyFile("userotp");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(userUrl);
////            waitForPageLoad();
////
////            new WelcomePage(driver).clickOnLoginLink();
////            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
////            waitForPageLoad();
////
////            new DashboardPage(driver).clickOnBookGoldBtn(driver);
////            Thread.sleep(2000);
////
////            new BookGoldPage(driver).fetchingGoldLivePriceAndBookingGold(driver);
////
////            System.out.println("Gold booking completed. Mobile: " + registeredMobileNumber);
////        } finally {
////            if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 5: Distributor Gold Purchase and Payment Document Upload
////
////    @Test(priority = 5, dependsOnMethods = "distributorBookingGoldTest")
////    public void distributorBuyingGoldTest() throws Exception {
////        String serialNo = "1";
////        String transactionImage = System.getProperty("user.dir")
////                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
////        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
////
////        System.out.println("\n========== TEST 5: DISTRIBUTOR GOLD BUYING ==========");
////
////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
////            throw new Exception("Registered mobile number is not available.");
////        }
////
////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
////        String OTP = pUtil.readDataFromPropertyFile("userotp");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(userUrl);
////            waitForPageLoad();
////
////            new WelcomePage(driver).clickOnLoginLink();
////            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
////            waitForPageLoad();
////
////            Thread.sleep(2000);
////            
////            new DashboardPage(driver).clickOnBuyGoldBtn(driver);
////            Thread.sleep(2000);
////
////            new whatWouldYouLikeToBuyPage(driver).getGoldCoinsEle().click();
////            Thread.sleep(2000);
////
////            BuildYourGoldCoinCombinationPage bPage = new BuildYourGoldCoinCombinationPage(driver);
////            bPage.increaseGoldGramsAccToWeight(driver, "1", 1);
////            bPage.increaseGoldGramsAccToWeight(driver, "2", 2);
////            bPage.increaseGoldGramsAccToWeight(driver, "5", 1);
////            bPage.increaseGoldGramsAccToWeight(driver, "10", 2);
////            bPage.increaseGoldGramsAccToWeight(driver, "20", 1);
////            bPage.increaseGoldGramsAccToWeight(driver, "50", 1);
////            bPage.increaseGoldGramsAccToWeight(driver, "100", 1);
////
////            Thread.sleep(2000);
////            bPage.getContinueBtn().click();
////            Thread.sleep(2000);
////
////            new ReviewYourGoldOrderPage(driver).takeAllDetailsAndClickProceedBtn(driver);
////            Thread.sleep(2000);
////
////            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
////            gPage.GetAllOrderDetailsBasedOnSerialNo(driver, serialNo);
////            Thread.sleep(2000);
////
////            OrderID = gPage.GetOrderIdBasedOnSerialNo(driver, serialNo);
////
////            if (OrderID == null || OrderID.trim().isEmpty()) {
////                throw new Exception("Order ID was not captured from Test 5.");
////            }
////
////            OrderID = OrderID.trim();
////            System.out.println("Method 5th Order ID generated: " + OrderID);
////
////            gPage.clickOnUpdatePaymentDocumentBtnBasedOnOrderID(driver, OrderID);
////            Thread.sleep(2000);
////
////            new UploadTransactionDetailsForGoldCoinsPage(driver)
////                    .uploadingTransactionDetails(transactionImage, transactionIdNum);
////            Thread.sleep(2000);
////
////            gPage.successMsgValidation(driver, OrderID);
////
////            System.out.println("Gold purchase and payment document upload completed. Order ID: " + OrderID);
////        } finally {
////            if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 6: Admin Payment Declineing
////
////    @Test(priority = 6, dependsOnMethods = "distributorBuyingGoldTest")
////    public void adminCheckingPurchasedQuantityGoldAndDeclinePaymentTest() throws Exception {
////        System.out.println("\n========== TEST 6: ADMIN PAYMENT CONFIRMATION ==========");
////
////        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
////        String Reason = eUtil.readDataFromExcel("Admin", 2, 1);
////        String daysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);
////
////        if (OrderID == null || OrderID.trim().isEmpty()) {
////            throw new Exception("Order ID is not available from Test 5.");
////        }
////
////        OrderID = OrderID.trim();
////        System.out.println("Order ID: " + OrderID);
////
////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
////        String username = pUtil.readDataFromPropertyFile("adminusername");
////        String password = pUtil.readDataFromPropertyFile("adminpassword");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(adminUrl);
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.LoginPage(driver)
////                    .loginToAdmin(username, password);
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
////                    .clickOnManageOrdersLnk();
////            Thread.sleep(2000);
////
////            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
////                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
////
////            moPage.checkingRecentlyBuyedCoinsDetailsDecline(driver, OrderID, Reason);
////            Thread.sleep(2000);
////
////            try 
////            {
////    			Thread.sleep(1000);
////    			WebElement Declined = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/a[.='Payment Declined']"));
////    			if(Declined.isDisplayed())
////    			{
////    				System.out.println("Declined Msg Displayed");
////    			}
////    		}
////            catch (Exception e) 
////            {
////            	System.out.println("Declined Msg Not Displayed");
////    		}
////
////        } finally {
////             if (driver != null) driver.quit();
////        }
////    }
////
////    //Test 6: Admin Payment Confirmation and Dispatch
////
////    @Test(priority = 7, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndDeclinePaymentTest")
////    public void distributorReUploadingPaymentDetailsTest() throws Exception
////    {
////        System.out.println("\n========== TEST 7: UPDATE DELIVERY DETAILS ==========");
////
////        String transactionImage = System.getProperty("user.dir")
////                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
////        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
////        
////        String idImage = System.getProperty("user.dir")
////                + "/src/test/resources/assets/Aadhaar_Card.jpeg";
////        String name = eUtil.readDataFromExcel("Admin", 6, 1);
////        String phoneNumber = eUtil.readDataFromExcel("Admin", 7, 1);
////        String idNumber = eUtil.readDataFromExcel("Admin", 8, 1);
////
////        Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
////        Assert.assertFalse(registeredMobileNumber.trim().isEmpty(),
////                "Registered mobile number is EMPTY.");
////
////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
////        String OTP = pUtil.readDataFromPropertyFile("userotp");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(userUrl);
////            waitForPageLoad();
////
////            new WelcomePage(driver).clickOnLoginLink();
////            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
////            waitForPageLoad();
////
////            DashboardPage dbPage = new DashboardPage(driver);
////            dbPage.getUserProfileImg().click();
////            Thread.sleep(2000);
////
////            dbPage.getCoinOrdersLnk().click();
////            Thread.sleep(2000);
////
////            wUtil.waitForPageLoad(driver);
////            
////            new GoldCoinsOrdersHistoryPage(driver)
////                    .clickOnReUploadPaymentDocumentBtnBasedOnOrderID(driver, OrderID);
////            
////            new UploadTransactionDetailsForGoldCoinsPage(driver)
////            .ReUploadingTransactionDetails(driver, transactionImage, transactionIdNum);
////            Thread.sleep(2000);
////            System.out.println("Re Uploaded Transaction Image and Number");
////            
////        } finally {
////             if (driver != null) driver.quit();
////        }
////    }
////
////
////    // Test 6: Admin Payment Confirmation and Dispatch
////
////    @Test(priority = 8, dependsOnMethods = "distributorReUploadingPaymentDetailsTest")
////    public void adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest() throws Exception {
////        System.out.println("\n========== TEST 6: ADMIN PAYMENT CONFIRMATION ==========");
////
////        String deliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
////        String daysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);
////
////        if (OrderID == null || OrderID.trim().isEmpty()) {
////            throw new Exception("Order ID is not available from Test 5.");
////        }
////
////        OrderID = OrderID.trim();
////        System.out.println("Order ID: " + OrderID);
////
////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
////        String username = pUtil.readDataFromPropertyFile("adminusername");
////        String password = pUtil.readDataFromPropertyFile("adminpassword");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(adminUrl);
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.LoginPage(driver)
////                    .loginToAdmin(username, password);
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
////                    .clickOnManageOrdersLnk();
////            Thread.sleep(2000);
////
////            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
////                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
////
////            moPage.checkingRecentlyBuyedCoinsDetailsAccepting(driver, OrderID);
////            Thread.sleep(2000);
////
////            // Retry-safe: skip dispatch actions if the order was already verified/dispatched in an earlier attempt
////            boolean verified = moPage.clickOnVerifyOrderBtnBasedOnOrderID(driver, OrderID);
////            Thread.sleep(2000);
////
////            if (verified) {
////                moPage.allActionsInConfirmDispatchPageInLLpPage(deliveryMsg, daysToDeliver);
////                Thread.sleep(2000);
////            }
////            
////            moPage.confirmingOrderStatus(driver, OrderID);
////
////        } finally {
////             if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 7: Distributor Delivery Details Update
////    @Test(priority = 9, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest")
////    public void distributorUpdatingDeliveryDetailsTest() throws Exception
////    {
////        System.out.println("\n========== TEST 7: UPDATE DELIVERY DETAILS ==========");
////
////        String idImage = System.getProperty("user.dir")
////                + "/src/test/resources/assets/Aadhaar_Card.jpeg";
////        String name = eUtil.readDataFromExcel("Admin", 6, 1);
////        String phoneNumber = eUtil.readDataFromExcel("Admin", 7, 1);
////        String idNumber = eUtil.readDataFromExcel("Admin", 8, 1);
////
////        Assert.assertNotNull(registeredMobileNumber, "Registered mobile number is NULL.");
////        Assert.assertFalse(registeredMobileNumber.trim().isEmpty(),
////                "Registered mobile number is EMPTY.");
////
////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
////        String OTP = pUtil.readDataFromPropertyFile("userotp");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(userUrl);
////            waitForPageLoad();
////
////            new WelcomePage(driver).clickOnLoginLink();
////            new LoginPage(driver).LoginToApplication(driver, registeredMobileNumber, OTP);
////            waitForPageLoad();
////
////            DashboardPage dbPage = new DashboardPage(driver);
////            dbPage.getUserProfileImg().click();
////            Thread.sleep(2000);
////
////            dbPage.getCoinOrdersLnk().click();
////            Thread.sleep(2000);
////
////            wUtil.waitForPageLoad(driver);
////            
////            new GoldCoinsOrdersHistoryPage(driver)
////                    .clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
////            Thread.sleep(2000);
////
////            new UpdateDeliveryDetailsPage(driver)
////                    .updatingDeliveryDetails(driver, name, phoneNumber, idNumber, idImage);
////
////            System.out.println("Delivery details updated successfully. Order ID: " + OrderID);
////        } finally {
////             if (driver != null) driver.quit();
////        }
////    }
////
////    // Test 8: Admin Accepting Picker Details
////    @Test(priority = 10, dependsOnMethods = "distributorUpdatingDeliveryDetailsTest")
////    public void adminAcceptingPickerDetails() throws Exception {
////
////        System.out.println("\n========== TEST 8: ADMIN ACCEPTING PICKER DETAILS ==========");
////
////        // Get the same Order ID used in Method 7
////        Assert.assertNotNull(OrderID, "Order ID is NULL from Method 7.");
////        Assert.assertFalse(OrderID.trim().isEmpty(), "Order ID is EMPTY.");
////
////        String orderIdFromMethod7 = OrderID.trim();
////
////        System.out.println("Order ID received from Method 7: " + orderIdFromMethod7);
////
////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
////        String username = pUtil.readDataFromPropertyFile("adminusername");
////        String password = pUtil.readDataFromPropertyFile("adminpassword");
////
////        driver = createChromeDriver();
////
////        try {
////            driver.get(adminUrl);
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.LoginPage(driver)
////                    .loginToAdmin(username, password);
////
////            waitForPageLoad();
////
////            new com.goldbharath.objectrepository.admin.DashboardPage(driver)
////                    .clickOnManageOrdersLnk();
////
////            waitForPageLoad();
////
////            ManageOrdersPage moPage = new ManageOrdersPage(driver);
////
////            
////            moPage.clickOnCoinsLnk(driver);
////
////            Thread.sleep(2000);
////            moPage.clickOnPickerDetailsBtnBasedOnOrderIDAndAllActionsInThatPage(
////                    driver, orderIdFromMethod7);
////
////            System.out.println(
////                    "Picker Details action completed successfully. Order ID: "
////                            + orderIdFromMethod7);
////
////        } finally {
////            if (driver != null) {
////                driver.quit();
////                driver = null;
////            }
////        }
////    }
////    
////    
////}
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
//////package Goldbharat;
//////
//////import java.time.Duration;
//////import java.util.ArrayList;
//////import java.util.List;
//////
//////import org.openqa.selenium.By;
//////import org.openqa.selenium.WebDriver;
//////import org.openqa.selenium.WebElement;
//////import org.openqa.selenium.chrome.ChromeDriver;
//////import org.openqa.selenium.chrome.ChromeOptions;
//////import org.openqa.selenium.support.ui.ExpectedConditions;
//////import org.openqa.selenium.support.ui.WebDriverWait;
//////import org.testng.Assert;
//////import org.testng.annotations.DataProvider;
//////import org.testng.annotations.Listeners;
//////import org.testng.annotations.Test;
//////
//////import com.goldbharath.genericutilities.ExcelFileUtility;
//////import com.goldbharath.genericutilities.PropertyFileUtility;
//////import com.goldbharath.genericutilities.WebDriverUtility;
//////import com.goldbharath.objectrepository.admin.BusinessInformationPage;
//////import com.goldbharath.objectrepository.admin.DistributorDetailsPage;
//////import com.goldbharath.objectrepository.admin.LogoutPage;
//////import com.goldbharath.objectrepository.admin.ManageDistributorPage;
//////import com.goldbharath.objectrepository.admin.ManageOrdersPage;
//////import com.goldbharath.objectrepository.user.BookGoldPage;
//////import com.goldbharath.objectrepository.user.BuildYourGoldCoinCombinationPage;
//////import com.goldbharath.objectrepository.user.DashboardPage;
//////import com.goldbharath.objectrepository.user.GoldCoinsOrdersHistoryPage;
//////import com.goldbharath.objectrepository.user.KycAsLLpPage;
//////import com.goldbharath.objectrepository.user.KycAsLtdPage;
//////import com.goldbharath.objectrepository.user.KycAsPvtLtdPage;
//////import com.goldbharath.objectrepository.user.LoginPage;
//////import com.goldbharath.objectrepository.user.RegisterPage;
//////import com.goldbharath.objectrepository.user.ReviewYourGoldOrderPage;
//////import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
//////import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldCoinsPage;
//////import com.goldbharath.objectrepository.user.UserProfilePage;
//////import com.goldbharath.objectrepository.user.WelcomePage;
//////import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;
//////
//////import io.github.bonigarcia.wdm.WebDriverManager;
//////
//////@Listeners(com.goldbharath.genericutilities.ListenersImplementationClass.class)
//////public class Five_RegisterAsPvtLtdBookingBuyingGoldCoinsFullWorkFlowTest {
//////	
//////	WebDriverUtility wUtil = new WebDriverUtility();
//////    PropertyFileUtility pUtil = new PropertyFileUtility();
//////    ExcelFileUtility eUtil = new ExcelFileUtility();
//////
//////    private WebDriver driver;
//////    private static String registeredMobileNumber;
//////    private static String OrderID;
//////
//////    private static final String SHEET_NAME = "Sheet1";
//////    private static final int WAIT_TIME = 20;
//////
//////    // COMMON METHOD: CREATE CHROME DRIVER
//////    private WebDriver createChromeDriver() {
//////        WebDriverManager.chromedriver().setup();
//////
//////        ChromeOptions options = new ChromeOptions();
//////        options.addArguments("--disable-gpu");
//////        options.addArguments("--window-size=1920,1080");
//////        options.addArguments("--disable-notifications");
//////        options.addArguments("--disable-popup-blocking");
//////        options.addArguments("--disable-infobars");
//////
//////        WebDriver webDriver = new ChromeDriver(options);
//////        webDriver.manage().window().maximize();
//////        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//////
//////        return webDriver;
//////    }
//////
//////    // COMMON METHOD: WAIT FOR PAGE LOAD
//////    private void waitForPageLoad() {
//////        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
//////        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
//////                .executeScript("return document.readyState").equals("complete"));
//////    }
//////
//////    // COMMON METHOD: CLICK REGISTER BUTTON
//////    private void clickRegisterButton() {
//////        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
//////        By registerButton = By.xpath("//button[normalize-space()='Register']");
//////        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
//////    }
//////
//////    // DATA PROVIDER: READ MOBILE NUMBERS FROM EXCEL
//////    @DataProvider(name = "mobileNumbers")
//////    public Object[][] mobileNumbers() throws Exception {
//////        List<String> mobileNumbers = new ArrayList<>();
//////
//////        for (int row = 10; row <= 29; row++) {
//////            String mobileNumber = eUtil.readDataFromExcel(SHEET_NAME, row, 1);
//////
//////            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
//////                mobileNumber = mobileNumber.trim();
//////                mobileNumbers.add(mobileNumber);
//////                System.out.println("Excel Row " + row + " : " + mobileNumber);
//////            }
//////        }
//////
//////        if (mobileNumbers.isEmpty()) {
//////            throw new Exception("No mobile numbers found in Excel rows 10 to 29.");
//////        }
//////
//////        return new Object[][]{{mobileNumbers}};
//////    }
//////
//////    // TEST 1: USER REGISTRATION
//////    @Test(priority = 1, dataProvider = "mobileNumbers")
//////    public void RegisteringToGoldbharathTest(List<String> mobileNumbers) throws Exception {
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 1 : GOLDBHARATH USER REGISTRATION");
//////        System.out.println("==================================================");
//////
//////        String businessName = eUtil.readDataFromExcel(SHEET_NAME, 1, 1);
//////        String personName = eUtil.readDataFromExcel(SHEET_NAME, 2, 1);
//////        String email = eUtil.readDataFromExcel(SHEET_NAME, 3, 1);
//////        String date = eUtil.readDataFromExcel(SHEET_NAME, 5, 1);
//////        String pincode = eUtil.readDataFromExcel(SHEET_NAME, 6, 1);
//////        String address = eUtil.readDataFromExcel(SHEET_NAME, 7, 1);
//////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//////
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(userUrl);
//////            waitForPageLoad();
//////            clickRegisterButton();
//////
//////            RegisterPage registerPage = new RegisterPage(driver);
//////            registeredMobileNumber = registerPage.registeringToGoldbharathApplication(
//////                    driver, businessName, personName, email, mobileNumbers, date, pincode, address);
//////
//////            Assert.assertNotNull(registeredMobileNumber, "Registration failed - mobile number is NULL.");
//////            Assert.assertFalse(registeredMobileNumber.trim().isEmpty(), "Registration failed - mobile number is EMPTY.");
//////
//////            System.out.println("--------------------------------------------------");
//////            System.out.println("REGISTRATION SUCCESSFUL");
//////            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////            System.out.println("--------------------------------------------------");
//////        } finally {
//////            if (driver != null) driver.quit();
//////        }
//////    }
//////
//////    // TEST 2: LOGIN + Pvt Ltd KYC
//////    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest")
//////    public void loginUpdatingKYC() throws Exception {
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 2 : LOGIN + LLP KYC");
//////        System.out.println("==================================================");
//////
//////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//////            throw new Exception("Registered mobile number is not available.");
//////        }
//////
//////        System.out.println("LOGIN MOBILE : " + registeredMobileNumber);
//////
//////        String Name = eUtil.readDataFromExcel(SHEET_NAME, 34, 11);
//////        String MobileNumber = eUtil.readDataFromExcel(SHEET_NAME, 35, 11);
//////        String AadharNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 11);
//////        String PANNumber = eUtil.readDataFromExcel(SHEET_NAME, 37, 11);
//////        String SecondDirectorName = eUtil.readDataFromExcel(SHEET_NAME, 38, 11);
//////        String GstNumber = eUtil.readDataFromExcel(SHEET_NAME, 39, 11);
//////        String CinNumber = eUtil.readDataFromExcel(SHEET_NAME, 40, 11);
//////        String BankName = eUtil.readDataFromExcel(SHEET_NAME, 41, 11);
//////        String AcNumber = eUtil.readDataFromExcel(SHEET_NAME, 42, 11);
//////        String IFSCCode = eUtil.readDataFromExcel(SHEET_NAME, 43, 11);
//////        String AccountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 44, 11);
//////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//////
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(userUrl);
//////            waitForPageLoad();
//////
//////            WelcomePage welcomePage = new WelcomePage(driver);
//////            welcomePage.clickOnLoginLink();
//////
//////            LoginPage loginPage = new LoginPage(driver);
//////            String OTP = pUtil.readDataFromPropertyFile("userotp");
//////            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);
//////
//////            waitForPageLoad();
//////
//////            DashboardPage dashboardPage = new DashboardPage(driver);
//////            dashboardPage.clickOnUserProfileImageAndLogoutLink(driver);
//////
//////            UserProfilePage userProfilePage = new UserProfilePage(driver);
//////            userProfilePage.clickOnKycLink();
//////            
//////            KycAsPvtLtdPage kycPage = new KycAsPvtLtdPage(driver);
//////            kycPage.PvtLtdKyc(driver, Name, MobileNumber, AadharNumber, PANNumber, SecondDirectorName, GstNumber, CinNumber, BankName, AcNumber, IFSCCode, AccountHolderName);
//////            
//////
//////            System.out.println("--------------------------------------------------");
//////            System.out.println("Ltd KYC COMPLETED SUCCESSFULLY");
//////            System.out.println("MOBILE : " + registeredMobileNumber);
//////            System.out.println("--------------------------------------------------");
//////        } 
//////        finally 
//////        {
//////            if (driver != null) driver.quit();
//////        }
//////    }
//////
//////    // TEST 3: ADMIN APPROVAL + GOLD/SILVER LIMITS
//////    @Test(priority = 3, dependsOnMethods = "loginUpdatingKYC")
//////    public void loginToAdminApproveKycAndGivingGoldAndSilverLimit() throws Exception {
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 3 : ADMIN DISTRIBUTOR FLOW");
//////        System.out.println("==================================================");
//////
//////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//////            throw new Exception("Registered mobile number is not available.");
//////        }
//////
//////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//////        String username = pUtil.readDataFromPropertyFile("adminusername");
//////        String password = pUtil.readDataFromPropertyFile("adminpassword");
//////        String ownerName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
//////        String goldLimit = eUtil.readDataFromExcel(SHEET_NAME, 48, 1);
//////        String silverLimit = eUtil.readDataFromExcel(SHEET_NAME, 49, 1);
//////
//////        Assert.assertNotNull(goldLimit, "Gold Limit is NULL in Excel.");
//////        Assert.assertFalse(goldLimit.trim().isEmpty(), "Gold Limit is EMPTY in Excel.");
//////        Assert.assertNotNull(silverLimit, "Silver Limit is NULL in Excel.");
//////        Assert.assertFalse(silverLimit.trim().isEmpty(), "Silver Limit is EMPTY in Excel.");
//////
//////        System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////        System.out.println("OWNER NAME        : " + ownerName);
//////        System.out.println("GOLD LIMIT        : " + goldLimit);
//////        System.out.println("SILVER LIMIT      : " + silverLimit);
//////
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(adminUrl);
//////            waitForPageLoad();
//////
//////            com.goldbharath.objectrepository.admin.LoginPage adminLoginPage =
//////                    new com.goldbharath.objectrepository.admin.LoginPage(driver);
//////            adminLoginPage.loginToAdmin(username, password);
//////            waitForPageLoad();
//////
//////            com.goldbharath.objectrepository.admin.DashboardPage adminDashboardPage =
//////                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
//////
//////            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
//////            wait.until(ExpectedConditions.elementToBeClickable(
//////                    adminDashboardPage.getAllDistributorLnk())).click();
//////
//////            ManageDistributorPage manageDistributorPage = new ManageDistributorPage(driver);
//////            manageDistributorPage.clickOnRecentlyRegisteredAndKycCompletedDistributor(
//////                    driver, registeredMobileNumber, ownerName);
//////
//////            DistributorDetailsPage distributorDetailsPage = new DistributorDetailsPage(driver);
//////            distributorDetailsPage.acceptingDistributorKycDetails();
//////
//////            manageDistributorPage.clickOnRecentlyRegisteredDistributorEditButton(
//////                    driver, registeredMobileNumber);
//////
//////            BusinessInformationPage businessInformationPage = new BusinessInformationPage(driver);
//////            businessInformationPage.editingAndGivingSilverAndGoldLimitForLtd(silverLimit, goldLimit);
//////
//////            System.out.println("\n==================================================");
//////            System.out.println("GOLDBHARATH ADMIN DISTRIBUTOR FLOW COMPLETED");
//////            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////            System.out.println("OWNER NAME        : " + ownerName);
//////            System.out.println("GOLD LIMIT        : " + goldLimit);
//////            System.out.println("SILVER LIMIT      : " + silverLimit);
//////            System.out.println("==================================================");
//////
//////            Thread.sleep(2000);
//////            LogoutPage loPage = new LogoutPage(driver);
//////            loPage.logoutOfApplication();
//////        } finally {
//////            if (driver != null) driver.quit();
//////        }
//////    }
//////
//////    // TEST 4: DISTRIBUTOR GOLD BOOKING
//////    @Test(priority = 4, dependsOnMethods = "loginToAdminApproveKycAndGivingGoldAndSilverLimit")
//////    public void distributorBookingGoldTest() throws Exception {
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 4 : DISTRIBUTOR GOLD BOOKING");
//////        System.out.println("==================================================");
//////
//////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//////            throw new Exception("Registered mobile number is not available. Earlier tests may have failed.");
//////        }
//////
//////        System.out.println("REGISTERED MOBILE NUMBER : " + registeredMobileNumber);
//////
//////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//////        String OTP = pUtil.readDataFromPropertyFile("userotp");
//////
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(userUrl);
//////            waitForPageLoad();
//////
//////            WelcomePage welcomePage = new WelcomePage(driver);
//////            welcomePage.clickOnLoginLink();
//////
//////            LoginPage loginPage = new LoginPage(driver);
//////            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);
//////            waitForPageLoad();
//////
//////            System.out.println("Distributor Login Successful");
//////            System.out.println("Mobile : " + registeredMobileNumber);
//////
//////            DashboardPage dbPage = new DashboardPage(driver);
//////            dbPage.clickOnBookGoldBtn(driver);
//////            Thread.sleep(2000);
//////
//////            BookGoldPage bgPage = new BookGoldPage(driver);
//////            bgPage.fetchingGoldLivePriceAndBookingGold(driver);
//////
//////            System.out.println("\n==================================================");
//////            System.out.println("GOLD BOOKING COMPLETED SUCCESSFULLY");
//////            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////            System.out.println("==================================================");
//////        } finally {
//////            if (driver != null) driver.quit();
//////        }
//////    }
//////
//////    // TEST 5: DISTRIBUTOR GOLD BUYING
//////    @Test(priority = 5, dependsOnMethods = "distributorBookingGoldTest")
//////    public void distributorBuyingGoldTest() throws Exception {
//////        final String serialNo = "1";
//////        final String transactionImage = System.getProperty("user.dir")
//////                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
//////        final String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
//////
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 5 : DISTRIBUTOR GOLD BUYING");
//////        System.out.println("==================================================");
//////
//////        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
//////            throw new Exception("Registered mobile number is not available. Earlier tests may have failed.");
//////        }
//////
//////        System.out.println("REGISTERED MOBILE NUMBER : " + registeredMobileNumber);
//////
//////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//////        String OTP = pUtil.readDataFromPropertyFile("userotp");
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(userUrl);
//////            waitForPageLoad();
//////
//////            WelcomePage welcomePage = new WelcomePage(driver);
//////            welcomePage.clickOnLoginLink();
//////
//////            LoginPage loginPage = new LoginPage(driver);
//////            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);
//////            waitForPageLoad();
//////
//////            System.out.println("Distributor Login Successful");
//////            System.out.println("Mobile : " + registeredMobileNumber);
//////
//////            DashboardPage dbPage = new DashboardPage(driver);
//////            dbPage.clickOnBuyGoldBtn(driver);
//////            Thread.sleep(2000);
//////
//////            whatWouldYouLikeToBuyPage wPage = new whatWouldYouLikeToBuyPage(driver);
//////            wPage.getGoldCoinsEle().click();
//////            Thread.sleep(2000);
//////
//////            BuildYourGoldCoinCombinationPage bPage = new BuildYourGoldCoinCombinationPage(driver);
//////            bPage.increaseGoldGramsAccToWeight(driver, "1", 1);
//////            Thread.sleep(2000);
//////            bPage.increaseGoldGramsAccToWeight(driver, "2", 2);
//////            Thread.sleep(2000);
//////            bPage.increaseGoldGramsAccToWeight(driver, "5", 1);
//////            Thread.sleep(2000);
//////            bPage.increaseGoldGramsAccToWeight(driver, "10", 2);
//////            Thread.sleep(2000);
//////            bPage.increaseGoldGramsAccToWeight(driver, "20", 1);
//////            Thread.sleep(2000);
//////            bPage.increaseGoldGramsAccToWeight(driver, "50", 1);
//////            Thread.sleep(2000);
//////            bPage.increaseGoldGramsAccToWeight(driver, "100", 1);
//////            Thread.sleep(2000);
//////
//////            bPage.getContinueBtn().click();
//////            Thread.sleep(2000);
//////
//////            ReviewYourGoldOrderPage rPage = new ReviewYourGoldOrderPage(driver);
//////            rPage.takeAllDetailsAndClickProceedBtn(driver);
//////            Thread.sleep(2000);
//////
////////            Want to check and import this
//////            
//////            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
//////            gPage.GetAllOrderDetailsBasedOnSerialNo(driver, serialNo);
//////            Thread.sleep(2000);
//////
//////            OrderID = gPage.GetOrderIdBasedOnSerialNo(driver, serialNo);
//////            if (OrderID == null || OrderID.trim().isEmpty()) {
//////                throw new Exception("Order ID was not captured from Test 5.");
//////            }
//////
//////            OrderID = OrderID.trim();
//////            System.out.println("ORDER ID GENERATED : " + OrderID);
//////
//////            gPage.clickOnUpdatePaymentDocumentBtnBasedOnOrderID(driver, OrderID);
//////            Thread.sleep(2000);
//////
//////            UploadTransactionDetailsForGoldCoinsPage uPage =
//////                    new UploadTransactionDetailsForGoldCoinsPage(driver);
//////            uPage.uploadingTransactionDetails(transactionImage, transactionIdNum);
//////            Thread.sleep(2000);
//////
//////            gPage.successMsgValidation(driver, OrderID);
//////
//////            System.out.println("\n==================================================");
//////            System.out.println("TEST 5 COMPLETED SUCCESSFULLY");
//////            System.out.println("ORDER ID : " + OrderID);
//////            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////            System.out.println("==================================================");
//////        } finally {
//////            if (driver != null) driver.quit();
//////        }
//////    }
//////
//////    // TEST 6: ADMIN CHECK PURCHASED GOLD + CONFIRM PAYMENT
//////    @Test(priority = 6, dependsOnMethods = "distributorBuyingGoldTest")
//////    public void adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest() throws Exception {
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 6 : ADMIN CHECK PURCHASED GOLD & CONFIRM PAYMENT");
//////        System.out.println("==================================================");
//////
//////        if (OrderID == null || OrderID.trim().isEmpty()) {
//////            throw new Exception("Order ID is not available from Test 5. Test 5 may have failed.");
//////        }
//////        
//////        String DeliveryMsg = eUtil.readDataFromExcel("Admin", 2, 1);
//////        String DaysToDeliver = wUtil.generateNextDaysDateAndTimeBasedOnIndex(3);
//////
//////        OrderID = OrderID.trim();
//////        System.out.println("ORDER ID FROM TEST 5 : " + OrderID);
//////
//////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//////        String username = pUtil.readDataFromPropertyFile("adminusername");
//////        String password = pUtil.readDataFromPropertyFile("adminpassword");
//////
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(adminUrl);
//////            waitForPageLoad();
//////
//////            com.goldbharath.objectrepository.admin.LoginPage adminLoginPage =
//////                    new com.goldbharath.objectrepository.admin.LoginPage(driver);
//////            adminLoginPage.loginToAdmin(username, password);
//////            waitForPageLoad();
//////
//////            System.out.println("Admin Login Successful");
//////
//////            com.goldbharath.objectrepository.admin.DashboardPage dbPage =
//////                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
//////            dbPage.clickOnManageOrdersLnk();
//////            Thread.sleep(2000);
//////
//////            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage =
//////                    new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
//////            moPage.checkingRecentlyBuyedCoinsDetails(driver, OrderID);
//////            Thread.sleep(2000);
//////
//////            moPage.clickOnVerifyOrderBtnBasedOnOrderID(driver, OrderID);
//////            Thread.sleep(2000);
//////                   
//////            moPage.allActionsInConfirmDispatchPageInLLpPage(DeliveryMsg, DaysToDeliver);
//////            
//////            WebElement OrderStatus = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Dispatched ']"));
//////            
//////            Assert.assertTrue(OrderStatus.isDisplayed(), "Problem Occurred While Dispatching Order");
//////      		System.out.println("Order Dispatched Successfully");
//////            
//////            System.out.println("\n==================================================");
//////            System.out.println("ADMIN GOLD PAYMENT FLOW COMPLETED");
//////            System.out.println("ORDER ID : " + OrderID);
//////            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////            System.out.println("==================================================");
//////            
//////            
//////        } finally {
//////            if (driver != null) driver.quit();
//////        }
//////    }
//////    
//////    @Test(priority = 7, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest")
//////    public void distributorUpdatingDeliveryDetailsTest() throws Exception {
//////
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 7 : DISTRIBUTOR UPDATING DELIVERY DETAILS");
//////        System.out.println("==================================================");
//////
//////        String IdImage = System.getProperty("user.dir")+ "/src/test/resources/assets/Aadhaar_Card.jpeg";
//////        String Name = eUtil.readDataFromExcel("Admin", 6, 1);
//////        String PhNo = eUtil.readDataFromExcel("Admin", 7, 1);
//////        String IdNo = eUtil.readDataFromExcel("Admin", 8, 1);
//////        
//////        // Validate registered distributor mobile number
//////        Assert.assertNotNull(
//////                registeredMobileNumber,
//////                "Registered distributor mobile number is NULL."
//////        );
//////
//////        Assert.assertFalse(
//////                registeredMobileNumber.trim().isEmpty(),
//////                "Registered distributor mobile number is EMPTY."
//////        );
//////
//////        // Read user URL and OTP from property file
//////        String userUrl = pUtil.readDataFromPropertyFile("userurl");
//////        String OTP = pUtil.readDataFromPropertyFile("userotp");
//////
//////        // Launch browser
//////        driver = createChromeDriver();
//////        
//////        try {
//////            driver.get(userUrl);
//////            waitForPageLoad();
//////
//////            // Click Login
//////            WelcomePage welcomePage = new WelcomePage(driver);
//////            welcomePage.clickOnLoginLink();
//////
//////            // Login using previously registered distributor
//////            LoginPage loginPage = new LoginPage(driver);
//////            loginPage.LoginToApplication(driver, registeredMobileNumber, OTP);
//////
//////            waitForPageLoad();
//////
//////            System.out.println("Distributor Login Successful");
//////            System.out.println("Registered Mobile : " + registeredMobileNumber);
//////
//////            Thread.sleep(2000);
//////            DashboardPage dbPage = new DashboardPage(driver);
//////            dbPage.getUserProfileImg().click();
//////            
//////            Thread.sleep(2000);
//////            dbPage.getCoinOrdersLnk().click();
//////            
//////            Thread.sleep(2000);
//////            GoldCoinsOrdersHistoryPage gPage = new GoldCoinsOrdersHistoryPage(driver);
//////            gPage.clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
//////            
//////            Thread.sleep(2000);
//////            
//////            UpdateDeliveryDetailsPage uPage = new UpdateDeliveryDetailsPage(driver);
//////            uPage.updatingDeliveryDetails(driver, Name, PhNo,  IdNo, IdImage);
//////
//////        } finally {
//////            
//////             if (driver != null) driver.quit();
//////        }
//////    }
//////	
////// // TEST 8: ADMIN CHECK PICKER DETAILS
//////    @Test(priority = 8, dependsOnMethods = "distributorUpdatingDeliveryDetailsTest")
//////    public void adminCheckingPickerDetailsTest() throws Exception {
//////
//////        System.out.println("\n==================================================");
//////        System.out.println("TEST 8 : ADMIN CHECK PICKER DETAILS");
//////        System.out.println("==================================================");
//////
//////        // Get the Order ID generated by the distributor in Test 5
//////        Assert.assertNotNull(OrderID, "Order ID is NULL. Distributor booking test may have failed.");
//////
//////        Assert.assertFalse(OrderID.trim().isEmpty(), "Order ID is EMPTY. Distributor booking test may have failed.");
//////
//////        // Store the same distributor-generated Order ID
//////        String distributorBookedOrderID = OrderID.trim();
//////
//////        System.out.println("DISTRIBUTOR BOOKED ORDER ID : "+ distributorBookedOrderID);
//////
//////        String adminUrl = pUtil.readDataFromPropertyFile("adminurl");
//////        String username = pUtil.readDataFromPropertyFile("adminusername");
//////        String password = pUtil.readDataFromPropertyFile("adminpassword");
//////
//////        driver = createChromeDriver();
//////
//////        try {
//////            driver.get(adminUrl);
//////            waitForPageLoad();
//////
//////            com.goldbharath.objectrepository.admin.LoginPage adminLoginPage =
//////                    new com.goldbharath.objectrepository.admin.LoginPage(driver);
//////            
//////            adminLoginPage.loginToAdmin(username, password);
//////            waitForPageLoad();
//////
//////            System.out.println("Admin Login Successful");
//////
//////            com.goldbharath.objectrepository.admin.DashboardPage dbPage =
//////                    new com.goldbharath.objectrepository.admin.DashboardPage(driver);
//////
//////            dbPage.clickOnManageOrdersLnk();
//////
//////            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
//////
//////            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
//////                            .executeScript("return document.readyState")
//////                            .equals("complete"));
//////
//////            com.goldbharath.objectrepository.admin.ManageOrdersPage moPage = new com.goldbharath.objectrepository.admin.ManageOrdersPage(driver);
//////
//////            // Use the exact Order ID generated in Test 5
//////            moPage.clickOnPickerDetailsBtnBasedOnOrderIDAndAllActionsInThatPage(
//////                    driver,
//////                    distributorBookedOrderID
//////            );
//////
//////            System.out.println("\n==================================================");
//////            System.out.println("ADMIN PICKER DETAILS FLOW COMPLETED");
//////            System.out.println("ORDER ID : " + distributorBookedOrderID);
//////            System.out.println("REGISTERED MOBILE : " + registeredMobileNumber);
//////            System.out.println("==================================================");
//////
//////        } finally {
//////            if (driver != null) {
//////                driver.quit();
//////            }
//////        }
//////    }
//////    
//////}
