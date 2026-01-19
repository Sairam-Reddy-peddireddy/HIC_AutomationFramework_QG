package tests;

import base.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utilities.DataProviderUtility;

import java.time.Duration;

public class ScoreValidationTest extends BaseTest {

    @Test(dataProvider = "ScoreData", dataProviderClass = DataProviderUtility.class, priority = 1,groups = {"functional","smoke","regression"})
    public void ScoreValidation(String TestCaseID, String name, String age, String pulse, String sysBP, String diaBP, String field, String score, String testStatus,String testType,String extraCol) {
        reporter.info(TestCaseID + " Test Started for " + field);
        SoftAssert softAssert = new SoftAssert();
        resultPage = indexPage.submitHealthIndexForm(name, age, pulse, sysBP, diaBP);
        if (field.equalsIgnoreCase("Age Score")) {
            String actualScore = resultPage.getAgeScore();
            softAssert.assertEquals(actualScore, score);
            resultPage.clickResetBtn();
        } else if (field.equalsIgnoreCase("BP Score")) {
            String actualScore = resultPage.getBpScore();
            softAssert.assertEquals(actualScore, score);
            resultPage.clickResetBtn();
        } else if (field.equalsIgnoreCase("Pulse Score")) {
            String actualScore = resultPage.getPulseScore();
            softAssert.assertEquals(actualScore, score);
            resultPage.clickResetBtn();
        } else if (field.equalsIgnoreCase("Alert")) {
            String alertText = indexPage.acceptAlertAndGetText();
            softAssert.assertTrue(alertText.contains("Provided BP values are not valid"));

        }

        softAssert.assertAll();

    }


    @Test(dataProvider = "OverallScoreData", dataProviderClass = DataProviderUtility.class, priority = 2,groups = {"functional","smoke","regression"})
    public void OverallHealthScoreValidation(String testCase, String name, String age, String pulse, String sysBP, String diasBP, String expScore, String expRemark, String expColor,String exCol1,String exCol2) {
        reporter.info(testCase + " Test Started");
        SoftAssert softAssert = new SoftAssert();

        resultPage = indexPage.submitHealthIndexForm(name, age, pulse, sysBP, diasBP);

        if(isAlertPresent()){
            String alertText = indexPage.acceptAlertAndGetText();
            reporter.info("Unexpected Alert found: " + alertText);
            Assert.fail("Test Failed: Unexpected Alert [" + alertText + "] appeared for " + testCase);

        }else{
            String actualScore = resultPage.getOverAllScore();
            softAssert.assertEquals(actualScore, expScore, "Overall Score mismatch in " + testCase);

            String actualRemark = resultPage.getRemark();
            softAssert.assertEquals(actualRemark, expRemark, "Remark mismatch in " + testCase);

            String actualColor = resultPage.getResultCardBackgroundColor();
            softAssert.assertEquals(actualColor, expColor, "Color mismatch in " + testCase);

            resultPage.clickResetBtn();
        }

        softAssert.assertAll();
    }

    public boolean isAlertPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

