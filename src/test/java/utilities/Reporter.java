package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.Status;

public class Reporter {
    private final Logger log;

    public Reporter(Class clazz) {
        this.log = LogManager.getLogger(clazz);
    }

    public void info(String message) {
        log.info(message);
        ExtentReportUtility.getTest().log(Status.INFO, message);
    }

    public void pass(String message) {
        log.info("Test Passed: " + message);
        ExtentReportUtility.getTest().log(Status.PASS, message);
    }

    public void fail(String message) {
        log.error("Test Failed: " + message);
        ExtentReportUtility.getTest().log(Status.FAIL, message);
    }

    public void skip(String message) {
        log.warn("Test Skipped: " + message);
        ExtentReportUtility.getTest().log(Status.SKIP, message);
    }

    // Manual screenshot logging
    public void screenshot(String testName) {
        String path = ExtentReportUtility.takeScreenshot(testName);
        try {
            ExtentReportUtility.getTest().addScreenCaptureFromPath(path);
            log.info("Screenshot attached: " + path);
        } catch (Exception e) {
            log.error("Screenshot attach failed: " + e.getMessage());
            ExtentReportUtility.getTest().log(Status.WARNING, "Screenshot attach failed: " + e.getMessage());
        }
    }
}
