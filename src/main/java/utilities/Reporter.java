package utilities;

import org.apache.logging.log4j.Logger;

public class Reporter {
    private final Class<?> classname;
    private final Logger log;

    public Reporter(Class<?> classname) {
        this.classname = classname;
        this.log = LoggerUtility.getLogger(this.classname);
    }

    public void info(String message) {
        log.info(message);
        ExtentReportUtility.logInfo("[" + classname.getSimpleName() + "] " + message);
    }

    public void pass(String message) {
        log.info("PASS: " + message);
        ExtentReportUtility.logPass("[" + classname.getSimpleName() + "] " + message);
    }

    public void fail(String message) {
        log.error("FAIL: " + message);
        ExtentReportUtility.logFail("[" + classname.getSimpleName() + "] " + message);
    }

    public void skip(String message) {
        log.warn("SKIP: " + message);
        ExtentReportUtility.logSkip("[" + classname.getSimpleName() + "] " + message);
    }
}
