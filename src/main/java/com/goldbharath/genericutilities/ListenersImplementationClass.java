package com.goldbharath.genericutilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Advanced TestNG Listener for GoldBharath Admin and Distributor
 * Automation Framework.
 *
 * Features:
 * 1. ExtentSpark HTML reporting
 * 2. Pass, fail and skip logging
 * 3. Failure stack traces and exception details
 * 4. Automatic failure screenshots
 * 5. Parallel-safe test mapping
 * 6. Test duration and execution timestamps
 * 7. Test groups and author metadata
 * 8. Browser, environment, OS and Java metadata
 * 9. Automatic report directory creation
 * 10. Report lifecycle management across TestNG contexts
 * 11. Execution summary
 *
 * @author Vikram
 */
public class ListenersImplementationClass
        implements ITestListener, ISuiteListener, IExecutionListener {

    private static volatile ExtentReports report;

    private static final Map<ITestResult, ExtentTest> testMap =
            new ConcurrentHashMap<>();

    private static final Object REPORT_LOCK = new Object();

    private static final AtomicInteger passedTests =
            new AtomicInteger(0);

    private static final AtomicInteger failedTests =
            new AtomicInteger(0);

    private static final AtomicInteger skippedTests =
            new AtomicInteger(0);

    private static volatile String reportPath;

    private static volatile long executionStartTime;

    private static volatile boolean reportFlushed = false;

    /**
     * Initialize ExtentReports only once per TestNG execution.
     */
    private static void initializeReport() {

        if (report != null) {
            return;
        }

        synchronized (REPORT_LOCK) {

            if (report != null) {
                return;
            }

            try {
                File reportDirectory = new File("Extent Reports");

                if (!reportDirectory.exists()
                        && !reportDirectory.mkdirs()
                        && !reportDirectory.isDirectory()) {

                    throw new IOException(
                            "Unable to create report directory: "
                                    + reportDirectory.getAbsolutePath());
                }

                String timestamp = new SimpleDateFormat(
                        "yyyyMMdd_HHmmss_SSS").format(new Date());

                File reportFile = new File(
                        reportDirectory,
                        "GoldBharath_Report_" + timestamp + ".html");

                reportPath = reportFile.getAbsolutePath();

                ExtentSparkReporter spark =
                        new ExtentSparkReporter(reportPath);

                spark.config().setDocumentTitle(
                        "GoldBharath Automation Execution Report");

                spark.config().setReportName(
                        "GoldBharath Automation Report");

                spark.config().setTheme(Theme.STANDARD);
                spark.config().setEncoding("UTF-8");

                ExtentReports extent = new ExtentReports();

                extent.attachReporter(spark);

                extent.setSystemInfo(
                        "Framework", "Selenium + TestNG");

                extent.setSystemInfo(
                        "Browser",
                        System.getProperty("browser", "Chrome"));

                extent.setSystemInfo(
                        "Base URL",
                        System.getProperty("baseUrl",
                                "Admin and Distributor Application"));

                extent.setSystemInfo(
                        "Environment",
                        System.getProperty("env", "STAGING"));

                extent.setSystemInfo(
                        "Operating System",
                        System.getProperty("os.name"));

                extent.setSystemInfo(
                        "OS Architecture",
                        System.getProperty("os.arch"));

                extent.setSystemInfo(
                        "Java Version",
                        System.getProperty("java.version"));

                extent.setSystemInfo(
                        "Executed By", "Vikram Gangavarapu");

                report = extent;
                reportFlushed = false;

                System.out.println(
                        "Extent Report Initialized: " + reportPath);

            } catch (IOException e) {

                throw new IllegalStateException(
                        "Failed to initialize Extent Report", e);
            }
        }
    }

    /**
     * TestNG execution starts.
     */
    @Override
    public void onExecutionStart() {

        executionStartTime = System.currentTimeMillis();

        passedTests.set(0);
        failedTests.set(0);
        skippedTests.set(0);

        initializeReport();

        System.out.println(
                "========== TestNG Execution Started ==========");
    }

    /**
     * A TestNG suite starts.
     */
    @Override
    public void onStart(ISuite suite) {

        initializeReport();

        System.out.println(
                "========== Suite Started: "
                        + suite.getName() + " ==========");
    }

    /**
     * A TestNG test context starts.
     */
    @Override
    public void onStart(ITestContext context) {

        initializeReport();

        System.out.println(
                "========== Test Context Started: "
                        + context.getName() + " ==========");
    }

    /**
     * A test method starts.
     */
    @Override
    public void onTestStart(ITestResult result) {

        initializeReport();

        String methodName = result.getMethod().getMethodName();

        String description = result.getMethod().getDescription();

        if (description == null || description.trim().isEmpty()) {
            description = methodName;
        }

        ExtentTest test = report.createTest(methodName, description);

        test.assignAuthor(
                result.getTestClass().getRealClass().getSimpleName());

        String[] groups = result.getMethod().getGroups();

        if (groups != null) {
            for (String group : groups) {
                test.assignCategory(group);
            }
        }

        test.getModel().setStartTime(
                new Date(result.getStartMillis()));

        test.log(Status.INFO,
                "Test Started: " + methodName);

        test.log(Status.INFO,
                "Test Class: "
                        + result.getTestClass().getRealClass().getName());

        test.log(Status.INFO,
                "Start Time: "
                        + new Date(result.getStartMillis()));

        testMap.put(result, test);

        System.out.println("STARTED: " + methodName);
    }

    /**
     * A test method passes.
     */
    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = getTest(result);

        long duration = getDuration(result);

        test.getModel().setEndTime(
                new Date(result.getEndMillis()));

        test.log(Status.PASS,
                "Test Passed Successfully");

        test.log(Status.INFO,
                "Execution Duration: " + duration + " ms");

        test.log(Status.INFO,
                "End Time: " + new Date(result.getEndMillis()));

        passedTests.incrementAndGet();

        System.out.println(
                "PASSED: " + result.getMethod().getMethodName()
                        + " (" + duration + " ms)");
    }

    /**
     * A test method fails.
     */
    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = getTest(result);

        long duration = getDuration(result);

        test.getModel().setEndTime(
                new Date(result.getEndMillis()));

        test.log(Status.FAIL, "Test Failed");

        test.log(Status.INFO,
                "Execution Duration: " + duration + " ms");

        test.log(Status.INFO,
                "End Time: " + new Date(result.getEndMillis()));

        Throwable throwable = result.getThrowable();

        if (throwable != null) {

            test.log(Status.FAIL,
                    "Exception Type: "
                            + throwable.getClass().getName());

            test.log(Status.FAIL,
                    "Exception Message: "
                            + String.valueOf(throwable.getMessage()));

            // Includes the exception and stack trace in the report.
            test.fail(throwable);
        }

        captureFailureScreenshot(result, test);

        failedTests.incrementAndGet();

        System.out.println(
                "FAILED: " + result.getMethod().getMethodName());
    }

    /**
     * A test method is skipped.
     */
    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = getTest(result);

        test.getModel().setEndTime(
                new Date(result.getEndMillis()));

        test.log(Status.SKIP, "Test Skipped");

        if (result.getThrowable() != null) {
            test.skip(result.getThrowable());
        }

        skippedTests.incrementAndGet();

        System.out.println(
                "SKIPPED: " + result.getMethod().getMethodName());
    }

    /**
     * Failed test within success percentage.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(
            ITestResult result) {

        getTest(result).log(
                Status.WARNING,
                "Failed within configured success percentage");
    }

    /**
     * Test timeout callback.
     */
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {

        onTestFailure(result);
    }

    /**
     * Capture and attach the failure screenshot.
     */
    private void captureFailureScreenshot(
            ITestResult result, ExtentTest test) {

        try {
            WebDriver driver = CommonBaseClass.getDriver();

            if (driver == null) {
                test.log(Status.WARNING,
                        "Screenshot unavailable: WebDriver is null");
                return;
            }

            String timestamp = new SimpleDateFormat(
                    "yyyyMMdd_HHmmss_SSS").format(new Date());

            String screenshotName =
                    result.getMethod().getMethodName()
                            + "_" + timestamp;

            String path = new WebDriverUtility().takeScreenShot(
                    driver, screenshotName);

            if (path == null || path.trim().isEmpty()) {

                test.log(Status.WARNING,
                        "Screenshot utility returned an empty path");
                return;
            }

            File screenshotFile = new File(path);

            if (!screenshotFile.isFile()) {

                test.log(Status.WARNING,
                        "Screenshot file does not exist: " + path);
                return;
            }

            String absolutePath =
                    screenshotFile.getAbsolutePath();

            test.fail(
                    "Failure Screenshot",
                    MediaEntityBuilder
                            .createScreenCaptureFromPath(absolutePath)
                            .build());

            test.log(Status.INFO,
                    "Screenshot Path: " + absolutePath);

        } catch (Exception e) {

            test.log(Status.WARNING,
                    "Unable to capture or attach screenshot: "
                            + e.getMessage());
        }
    }

    /**
     * Calculate the test duration.
     */
    private long getDuration(ITestResult result) {

        return Math.max(
                0L, result.getEndMillis() - result.getStartMillis());
    }

    /**
     * Return the ExtentTest for the exact test result.
     */
    private ExtentTest getTest(ITestResult result) {

        initializeReport();

        ExtentTest test = testMap.get(result);

        if (test == null) {

            synchronized (REPORT_LOCK) {

                test = testMap.get(result);

                if (test == null) {

                    String methodName =
                            result.getMethod().getMethodName();

                    test = report.createTest(methodName);

                    test.assignAuthor(
                            result.getTestClass()
                                    .getRealClass().getSimpleName());

                    testMap.put(result, test);
                }
            }
        }

        return test;
    }

    /**
     * A TestNG test context finishes.
     */
    @Override
    public void onFinish(ITestContext context) {

        System.out.println(
                "========== Test Context Finished: "
                        + context.getName() + " ==========");

        System.out.println(
                "Context Passed: "
                        + context.getPassedTests().size());

        System.out.println(
                "Context Failed: "
                        + context.getFailedTests().size());

        System.out.println(
                "Context Skipped: "
                        + context.getSkippedTests().size());

        // Do not flush or clear the report here.
        // Other TestNG contexts may still be executing.
    }

    /**
     * A TestNG suite finishes.
     */
    @Override
    public void onFinish(ISuite suite) {

        System.out.println(
                "========== Suite Finished: "
                        + suite.getName() + " ==========");

        // Final report flushing is performed after execution finishes.
    }

    /**
     * Final callback after TestNG execution.
     */
    @Override
    public void onExecutionFinish() {

        synchronized (REPORT_LOCK) {

            if (report != null && !reportFlushed) {

                report.flush();
                reportFlushed = true;
            }
        }

        long duration = executionStartTime == 0
                ? 0
                : System.currentTimeMillis() - executionStartTime;

        int passed = passedTests.get();
        int failed = failedTests.get();
        int skipped = skippedTests.get();

        System.out.println();
        System.out.println("========== EXECUTION SUMMARY ==========");
        System.out.println("Passed Tests : " + passed);
        System.out.println("Failed Tests : " + failed);
        System.out.println("Skipped Tests: " + skipped);
        System.out.println("Total Results: "
                + (passed + failed + skipped));
        System.out.println("Total Duration: " + duration + " ms");
        System.out.println("HTML Report: " + reportPath);
        System.out.println("=======================================");

        testMap.clear();
    }
}



















//package com.goldbharath.genericutilities;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.testng.ITestContext;
//import org.testng.ITestListener;
//import org.testng.ITestResult;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.MediaEntityBuilder;
//import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.reporter.configuration.Theme;
//
///**
// * Common TestNG Listener for Admin and User Pannels.
// *
// * @author Vikram
// */
//public class ListenersImplementationClass implements ITestListener {
//
//    private static ExtentReports report;
//
//    private static final Map<Long, ExtentTest> testMap =
//            new ConcurrentHashMap<>();
//
//    @Override
//    public void onStart(ITestContext context) {
//
//        System.out.println("========== Suite Started : "
//                + context.getName() + " ==========");
//
//        if (report == null) {
//
//            synchronized (ListenersImplementationClass.class) {
//
//                if (report == null) {
//
//                    String reportPath = ".\\Extent Reports\\Report-"
//                            + new JavaUtility().getSystemDateInFormat()
//                            + ".html";
//
//                    ExtentSparkReporter spark =
//                            new ExtentSparkReporter(reportPath);
//
//                    spark.config().setDocumentTitle("Gold Bharat Execution Report");
//                    spark.config().setReportName("Gold Bharat Automation Report");
//                    spark.config().setTheme(Theme.STANDARD);
//                    spark.config().setEncoding("utf-8");
//
//                    report = new ExtentReports();
//                    report.attachReporter(spark);
//
//                    report.setSystemInfo("Browser",
//                            System.getProperty("browser", "Chrome"));
//
//                    report.setSystemInfo("Base URL",
//                            System.getProperty("baseUrl", "Admin and User pannels are Involved"));
//
//                    report.setSystemInfo("Environment",
//                            System.getProperty("env", "STAGING"));
//
//                    report.setSystemInfo("Platform",
//                            System.getProperty("os.name"));
//
//                    report.setSystemInfo("Executed By",
//                            "Vikram Gangavarapu");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onTestStart(ITestResult result) {
//
//        String methodName = result.getMethod().getMethodName();
//
//        ExtentTest test =
//                report.createTest(methodName,
//                        result.getMethod().getDescription());
//
//        for (String group : result.getMethod().getGroups()) {
//            test.assignCategory(group);
//        }
//
//        test.assignAuthor(result.getTestClass().getRealClass().getSimpleName());
//
//        test.log(Status.INFO, "Test Execution Started");
//
//        testMap.put(getId(result), test);
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//
//        ExtentTest test = getTest(result);
//
//        long duration =
//                result.getEndMillis() - result.getStartMillis();
//
//        test.log(Status.PASS,
//                "Test Passed (" + duration + " ms)");
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//
//        ExtentTest test = getTest(result);
//
//        test.log(Status.FAIL, "Test Failed");
//
//        if (result.getThrowable() != null) {
//            test.fail(result.getThrowable());
//        }
//
//        try {
//
//            String screenshotName =
//                    result.getMethod().getMethodName()
//                            + "_"
//                            + new JavaUtility().getSystemDateInFormat();
//
//            String path =
//                    new WebDriverUtility().takeScreenShot(
//                            CommonBaseClass.getDriver(),
//                            screenshotName);
//
//            test.fail("Failure Screenshot",
//                    MediaEntityBuilder
//                            .createScreenCaptureFromPath(path)
//                            .build());
//
//        } catch (IOException e) {
//
//            test.log(Status.WARNING,
//                    "Unable to capture screenshot : "
//                            + e.getMessage());
//
//        } catch (Exception e) {
//
//            test.log(Status.WARNING,
//                    e.getMessage());
//        }
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {
//
//        ExtentTest test = getTest(result);
//
//        test.log(Status.SKIP, "Test Skipped");
//
//        if (result.getThrowable() != null) {
//            test.skip(result.getThrowable());
//        }
//    }
//
//    @Override
//    public void onTestFailedButWithinSuccessPercentage(
//            ITestResult result) {
//
//        getTest(result).log(Status.WARNING,
//                "Failed but within success percentage.");
//    }
//
//    @Override
//    public void onTestFailedWithTimeout(ITestResult result) {
//
//        onTestFailure(result);
//    }
//
//    @Override
//    public void onFinish(ITestContext context) {
//
//        System.out.println("========== Suite Finished : "
//                + context.getName() + " ==========");
//
//        if (report != null) {
//            report.flush();
//        }
//
//        testMap.clear();
//    }
//
//    /**
//     * Unique ID for parallel execution.
//     */
//    private long getId(ITestResult result) {
//
//        return result.hashCode()
//                + Thread.currentThread().getId() * 31L;
//    }
//
//    /**
//     * Returns ExtentTest object.
//     */
//    private ExtentTest getTest(ITestResult result) {
//
//        ExtentTest test = testMap.get(getId(result));
//
//        if (test == null) {
//
//            test =
//                    report.createTest(
//                            result.getMethod().getMethodName());
//
//            testMap.put(getId(result), test);
//        }
//
//        return test;
//    }
//}