package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportUtility {
    private static ExtentReports extent;
    private static ExtentTest test;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timestamp + ".html";
            System.out.println(reportPath);
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

            spark.config().setDocumentTitle("Automation Report");
            spark.config().setReportName("Functional Test Results");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        test = getInstance().createTest(testName);
        return test;
    }

    public static ExtentTest getTest() {
        return test;
    }

    public static void logInfo(String message) {
        getTest().info(message);
    }

    public static void logPass(String message) {
        getTest().pass(message);
    }

    public static void logFail(String message) {
        getTest().fail(message);
    }

    public static void logSkip(String message) {
        getTest().skip(message);
    }

    public static void addScreenshot(String screenshotPath) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            System.out.println("Failed to attach screenshot: " + e.getMessage());
        }
    }


    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}

