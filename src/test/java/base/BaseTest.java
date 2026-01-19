package base;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.base.BasePage;
import pages.pageobjects.IndexPage;
import pages.pageobjects.ResultPage;
import utilities.ExtentReportUtility;
import utilities.LoggerUtility;
import utilities.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class BaseTest {
    public  WebDriver driver;
    public  static Properties prop;
    public Logger log = LoggerUtility.getLogger(BaseTest.class);
    public BasePage basePage;
    public IndexPage indexPage;
    public ResultPage resultPage;
    protected Reporter reporter = new Reporter(this.getClass());

    public void loadconfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties"))
        {   prop = new Properties();
            prop.load(input);
            log.info("Config file loaded successfully");
        } catch (Exception e) {
            log.error("Failed to load config file"+e.getMessage());
        }
    }

    public String getProperty(String key){
        return prop.getProperty(key);
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        loadconfig();
        ExtentReportUtility.getInstance();
        log.info("Test Suite Started");
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(String browser) throws MalformedURLException {
        if(getProperty("execution").equals("remote")){
            switch(browser){
                case "chrome":
                    driver=new RemoteWebDriver(new URL(getProperty("hubUrl")),new ChromeOptions());
                    break;
                case "edge":
                    driver=new RemoteWebDriver(new URL(getProperty("hubUrl")),new EdgeOptions());
                    break;
                case "firefox":
                    driver=new RemoteWebDriver(new URL(getProperty("hubUrl")),new FirefoxOptions());
                    break;
                default: throw new IllegalArgumentException("No matching browser: " + browser);
            }
        }else{
            switch(browser){
                case "chrome":
                    driver=new ChromeDriver();
                    break;
                case "edge":
                    driver=new EdgeDriver();
                    break;
                case "firefox":
                    driver=new FirefoxDriver();
                    break;
                default: throw new IllegalArgumentException("No matching browser: " + browser);}

        }
        log.info(browser+"browser launched");
        driver.get(getProperty("url"));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        int implicitWait = Integer.parseInt(getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        log.info("Navigated to: "+getProperty("url"));
        basePage = new BasePage(driver);
        indexPage = new IndexPage(driver);
    }
    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method){
        ExtentReportUtility.createTest(method.getName());
    }
    @AfterMethod(alwaysRun = true)
    public void endTest(ITestResult result){
        if(result.getStatus()==ITestResult.FAILURE){
            String screenShotPath = takeScreenshot(result.getName());
            ExtentReportUtility.addScreenshot(screenShotPath);
            reporter.fail("Test Failed: "+result.getName());
        }else if(result.getStatus()==ITestResult.SUCCESS) {
            reporter.info("Test Passed: "+result.getName());
        }else if(result.getStatus()==ITestResult.SKIP){
            reporter.skip("Test Skipped: "+result.getName());
        }
    }
    @AfterClass(alwaysRun = true)
    public void closeBrowser(){
        if(driver!=null){
            driver.quit();
            reporter.info("Browser closed");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown(){
        ExtentReportUtility.flushReport();
    }

    public String takeScreenshot(String testName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = System.getProperty("user.dir")
                + File.separator + getProperty("screenshotPath")
                + testName + "_" + timestamp + ".png";
        System.out.println(path);
        try {
            FileUtils.copyFile(src, new File(path));
            reporter.info("Screenshot captured for test: " + testName);
        } catch (IOException e) {
            reporter.fail("Failed to save screenshot"+ e.getMessage());
        }
        return "screenshots/" + testName + "_" + timestamp + ".png";
    }


}
