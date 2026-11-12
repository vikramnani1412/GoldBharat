package com.goldbharath.genericutilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
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
 * Advanced TestNG Listener for GoldBharath Automation Framework.
 *
 * Features:
 * 1. Extent Spark HTML reporting
 * 2. Pass, Fail and Skip logging
 * 3. Exception details and stack traces
 * 4. Automatic failure screenshots without BaseClass dependency
 * 5. Parallel-safe ExtentTest mapping
 * 6. Test execution timestamps and duration
 * 7. Test groups and author metadata
 * 8. Browser, environment, OS and Java metadata
 * 9. Automatic report directory creation
 * 10. Execution summary
 *
 * @author Vikram
 */

public class ListenersImplementationClassWithoutBaseClass
        implements ITestListener, ISuiteListener,
        IExecutionListener, IInvokedMethodListener {

    private static volatile ExtentReports report;

    private static final Map<ITestResult, ExtentTest> testMap =
            new ConcurrentHashMap<>();

    private static final Object REPORT_LOCK = new Object();

    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);
    private static final AtomicInteger skippedTests = new AtomicInteger(0);

    private static volatile String reportPath;
    private static volatile long executionStartTime;
    private static volatile boolean reportFlushed = false;

    private static final String SCREENSHOT_PATH_ATTRIBUTE = "failureScreenshotPath";
    private static final String SCREENSHOT_ERROR_ATTRIBUTE = "failureScreenshotError";

    /**
     * Initialize ExtentReports only once.
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

                extent.setSystemInfo("Framework", "Selenium + TestNG");

                extent.setSystemInfo(
                        "Browser",
                        System.getProperty("browser", "Chrome"));

                extent.setSystemInfo(
                        "Base URL",
                        System.getProperty(
                                "baseUrl",
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

                extent.setSystemInfo("Executed By", "Vikram");

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
     * Suite starts.
     */
    @Override
    public void onStart(ISuite suite) {

        initializeReport();

        System.out.println(
                "========== Suite Started: "
                        + suite.getName() + " ==========");
    }

    /**
     * Test context starts.
     */
    @Override
    public void onStart(ITestContext context) {

        initializeReport();

        System.out.println(
                "========== Test Context Started: "
                        + context.getName() + " ==========");
    }

    /**
     * Test method starts.
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

        test.log(Status.INFO, "Test Started: " + methodName);

        test.log(Status.INFO,
                "Test Class: "
                        + result.getTestClass().getRealClass().getName());

        test.log(Status.INFO,
                "Start Time: " + new Date(result.getStartMillis()));

        testMap.put(result, test);

        System.out.println("STARTED: " + methodName);
    }

    /**
     * IMPORTANT:
     * Capture screenshot immediately after the failed test method.
     * This callback runs before the @AfterMethod teardown.
     */
    @Override
    public void afterInvocation(
            IInvokedMethod method,
            ITestResult result) {

        if (method.isTestMethod()
                && result.getStatus() == ITestResult.FAILURE) {

            try {

                WebDriver driver = getDriverFromTestInstance(result);

                if (driver == null) {

                    result.setAttribute(
                            SCREENSHOT_ERROR_ATTRIBUTE,
                            "WebDriver is null or unavailable before teardown.");

                    return;
                }

                if (!(driver instanceof TakesScreenshot)) {

                    result.setAttribute(
                            SCREENSHOT_ERROR_ATTRIBUTE,
                            "WebDriver does not support screenshots.");

                    return;
                }

                String timestamp = new SimpleDateFormat(
                        "yyyyMMdd_HHmmss_SSS").format(new Date());

                String screenshotName =
                        result.getMethod().getMethodName()
                                + "_" + timestamp;

                File screenshotDirectory =
                        new File("Extent Reports/Screenshots");

                if (!screenshotDirectory.exists()
                        && !screenshotDirectory.mkdirs()
                        && !screenshotDirectory.isDirectory()) {

                    throw new IOException(
                            "Unable to create screenshot directory.");
                }

                File screenshotFile = new File(
                        screenshotDirectory,
                        screenshotName + ".png");

                File sourceFile = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.FILE);

                Files.copy(
                        sourceFile.toPath(),
                        screenshotFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                result.setAttribute(
                        SCREENSHOT_PATH_ATTRIBUTE,
                        screenshotFile.getAbsolutePath());

                System.out.println(
                        "Screenshot Captured: "
                                + screenshotFile.getAbsolutePath());

            } catch (Exception e) {

                result.setAttribute(
                        SCREENSHOT_ERROR_ATTRIBUTE,
                        e.getClass().getSimpleName()
                                + " - " + e.getMessage());

                System.err.println(
                        "Screenshot capture failed: " + e.getMessage());

                e.printStackTrace();
            }
        }
    }

    /**
     * Test passes.
     */
    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = getTest(result);
        long duration = getDuration(result);

        test.getModel().setEndTime(
                new Date(result.getEndMillis()));

        test.log(Status.PASS, "Test Passed Successfully");

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
     * Test fails.
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

            test.fail(throwable);
        }

        // Attach the screenshot captured before @AfterMethod.
        attachFailureScreenshot(result, test);

        failedTests.incrementAndGet();

        System.out.println(
                "FAILED: " + result.getMethod().getMethodName());
    }

    /**
     * Attach the previously captured screenshot to Extent Report.
     */
    private void attachFailureScreenshot(
            ITestResult result,
            ExtentTest test) {

        String screenshotPath =
                (String) result.getAttribute(SCREENSHOT_PATH_ATTRIBUTE);

        if (screenshotPath != null) {

            try {

                test.fail(
                        "Failure Screenshot",
                        MediaEntityBuilder
                                .createScreenCaptureFromPath(screenshotPath)
                                .build());

                test.log(Status.INFO,
                        "Screenshot Path: " + screenshotPath);

            } catch (Exception e) {

                test.log(Status.WARNING,
                        "Unable to attach screenshot: " + e.getMessage());
            }

        } else {

            String error =
                    (String) result.getAttribute(SCREENSHOT_ERROR_ATTRIBUTE);

            test.log(Status.WARNING,
                    "Screenshot unavailable: "
                            + (error != null ? error : "No screenshot was captured."));
        }
    }

    /**
     * Test skipped.
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
     * Failed within success percentage.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(
            ITestResult result) {

        getTest(result).log(
                Status.WARNING,
                "Failed within configured success percentage");
    }

    /**
     * Test timeout.
     */
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    /**
     * Find WebDriver from the actual TestNG test instance.
     *
     * Supports:
     * - Public/private WebDriver instance fields
     * - Static WebDriver fields
     * - Inherited WebDriver fields
     */
    private WebDriver getDriverFromTestInstance(ITestResult result)
            throws IllegalAccessException {

        Object testInstance = result.getInstance();

        if (testInstance == null) {
            return null;
        }

        Class<?> currentClass = testInstance.getClass();

        while (currentClass != null
                && currentClass != Object.class) {

            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {

                if (!WebDriver.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                field.setAccessible(true);

                Object value;

                if (Modifier.isStatic(field.getModifiers())) {
                    value = field.get(null);
                } else {
                    value = field.get(testInstance);
                }

                if (value instanceof WebDriver) {
                    return (WebDriver) value;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return null;
    }

    /**
     * Calculate test execution duration.
     */
    private long getDuration(ITestResult result) {

        return Math.max(
                0L,
                result.getEndMillis() - result.getStartMillis());
    }

    /**
     * Get ExtentTest for exact TestNG result.
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
     * Test context finishes.
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

        // Do not flush here. Other contexts may still execute.
    }

    /**
     * Suite finishes.
     */
    @Override
    public void onFinish(ISuite suite) {

        System.out.println(
                "========== Suite Finished: "
                        + suite.getName() + " ==========");

        // Final flush happens after TestNG execution.
    }

    /**
     * Final TestNG execution callback.
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

        System.out.println(
                "Total Results: " + (passed + failed + skipped));

        System.out.println("Total Duration: " + duration + " ms");
        System.out.println("HTML Report: " + reportPath);

        System.out.println("=======================================");

        testMap.clear();
    }
}