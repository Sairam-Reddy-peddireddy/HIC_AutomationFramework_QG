package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentReportUtility implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public static WebDriver driver;
    private static final Logger log = LogManager.getLogger(ExtentReportUtility.class);

    @Override
    public void onStart(ITestContext context) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportName = "Test-Report-" + timeStamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter("./reports/" + reportName);
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("Functional Testing");
        spark.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));

        log.info("Extent Report initialized: " + reportName);
    }

    @Override
    public void onTestStart(ITestResult result) {
        Object[] params = result.getParameters();
        String testName = result.getMethod().getMethodName();

        if (params != null && params.length > 0) {
            String tcId = (String) params[0];     // first param = TC ID
            testName = testName + " - " + tcId;
        }

        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        getTest().log(Status.INFO, result.getName() + "Test Started");
        log.info("Test Started");
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, result.getName() + " passed");
        log.info("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().log(Status.FAIL, result.getName() + " failed");
        getTest().log(Status.INFO, result.getThrowable().getMessage());
        log.error("Test Failed: " + result.getName() + " - " + result.getThrowable());

        // Capture screenshot on failure
        String screenshotPath = takeScreenshot(result.getName());
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
            log.info("Screenshot attached for failed test: " + screenshotPath);
        } catch (Exception e) {
            log.error("Failed to attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().log(Status.SKIP, result.getName() + " skipped");
        log.warn("Test Skipped: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        log.info("Extent Report flushed.");
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static String takeScreenshot(String testName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String screenshotDir = System.getProperty("user.dir")
                + File.separator + "reports"
                + File.separator + "screenshots";

        String fullPath = screenshotDir + File.separator + testName + "_" + timestamp + ".png";

        try {
            FileUtils.copyFile(src, new File(fullPath));
            log.info("Screenshot captured for test: " + testName);
        } catch (IOException e) {
            log.error("Failed to save screenshot: " + e.getMessage());
        }

        return "screenshots/" + testName + "_" + timestamp + ".png";
    }

}
