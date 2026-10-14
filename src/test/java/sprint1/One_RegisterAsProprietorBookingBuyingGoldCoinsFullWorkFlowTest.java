package sprint1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import com.goldbharath.objectrepository.user.KycAsProprietorPage;
import com.goldbharath.objectrepository.user.LoginPage;
import com.goldbharath.objectrepository.user.RegisterPage;
import com.goldbharath.objectrepository.user.ReviewYourGoldOrderPage;
import com.goldbharath.objectrepository.user.UpdateDeliveryDetailsPage;
import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldCoinsPage;
import com.goldbharath.objectrepository.user.UserProfilePage;
import com.goldbharath.objectrepository.user.WelcomePage;
import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class One_RegisterAsProprietorBookingBuyingGoldCoinsFullWorkFlowTest {

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

    // Test 1: User Registration

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
            if (driver != null) driver.quit();
        }
    }

    // Test 2: User Login and Proprietor KYC

    @Test(priority = 2, dependsOnMethods = "RegisteringToGoldbharathTest")
    public void loginUpdatingKYC() throws Exception {
        System.out.println("\n========== TEST 2: LOGIN AND PROPRIETOR KYC ==========");

        if (registeredMobileNumber == null || registeredMobileNumber.trim().isEmpty()) {
            throw new Exception("Registered mobile number is not available.");
        }

        String proprietorName = eUtil.readDataFromExcel(SHEET_NAME, 34, 2);
        String proprietorPan = eUtil.readDataFromExcel(SHEET_NAME, 35, 2);
        String gstNumber = eUtil.readDataFromExcel(SHEET_NAME, 36, 2);
        String bankName = eUtil.readDataFromExcel(SHEET_NAME, 37, 2);
        String accountNumber = eUtil.readDataFromExcel(SHEET_NAME, 38, 2);
        String ifscCode = eUtil.readDataFromExcel(SHEET_NAME, 39, 2);
        String accountHolderName = eUtil.readDataFromExcel(SHEET_NAME, 40, 2);
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

            new KycAsProprietorPage(driver).proprietorKyc(
                    driver, proprietorName, proprietorPan, gstNumber, bankName,
                    accountNumber, ifscCode, accountHolderName);

            System.out.println("KYC completed successfully. Mobile: " + registeredMobileNumber);
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // Test 3: Admin KYC Approval and Gold/Silver Limits

    @Test(priority = 3, dependsOnMethods = "loginUpdatingKYC")
    public void loginToAdminApproveKycAndGivingGoldAndSilverLimit() throws Exception {
        System.out.println("\n========== TEST 3: ADMIN DISTRIBUTOR FLOW ==========");

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
                    .editingAndGivingSilverAndGoldLimit(silverLimit, goldLimit);

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

    @Test(priority = 4, dependsOnMethods = "loginToAdminApproveKycAndGivingGoldAndSilverLimit")
    public void distributorBookingGoldTest() throws Exception {
        System.out.println("\n========== TEST 4: DISTRIBUTOR GOLD BOOKING ==========");

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

    @Test(priority = 5, dependsOnMethods = "distributorBookingGoldTest")
    public void distributorBuyingGoldTest() throws Exception {
        String serialNo = "1";
        String transactionImage = System.getProperty("user.dir")
                + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
        String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);

        System.out.println("\n========== TEST 5: DISTRIBUTOR GOLD BUYING ==========");

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
            System.out.println("Order ID generated: " + OrderID);

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

    // Test 6: Admin Payment Confirmation and Dispatch

    @Test(priority = 6, dependsOnMethods = "distributorBuyingGoldTest")
    public void adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest() throws Exception {
        System.out.println("\n========== TEST 6: ADMIN PAYMENT CONFIRMATION ==========");

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

            moPage.checkingRecentlyBuyedCoinsDetails(driver, OrderID);
            Thread.sleep(2000);

            moPage.clickOnVerifyOrderBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            moPage.allActionsInConfirmDispatchPageInLLpPage(deliveryMsg, daysToDeliver);
            Thread.sleep(2000);
            
            moPage.confirmingOrderStatus(driver, OrderID);

        } finally {
             if (driver != null) driver.quit();
        }
    }

    // Test 7: Distributor Delivery Details Update

    @Test(priority = 7, dependsOnMethods = "adminCheckingPurchasedQuantityGoldAndConfirmPaymentTest")
    public void distributorUpdatingDeliveryDetailsTest() throws Exception {
        System.out.println("\n========== TEST 7: UPDATE DELIVERY DETAILS ==========");

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

            new GoldCoinsOrdersHistoryPage(driver)
                    .clickOnUpdateDeliveryDetailsBtnBasedOnOrderID(driver, OrderID);
            Thread.sleep(2000);

            new UpdateDeliveryDetailsPage(driver)
                    .updatingDeliveryDetails(name, phoneNumber, idNumber, idImage);

            System.out.println("Delivery details updated successfully. Order ID: " + OrderID);
        } finally {
             if (driver != null) driver.quit();
        }
    }
}

