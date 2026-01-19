package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utilities.DataProviderUtility;

public class ResultUIValidationTest extends BaseTest {

    @Test(priority = 1,groups = {"ui","smoke","regression"})
    private void resultCardUiValidation()
    {   resultPage = indexPage.submitHealthIndexForm("Bhaskar","22","60-80 BPM","120","80");
        SoftAssert softAssert=new SoftAssert();
        //toCheckResultCardIsDisplayed
        softAssert.assertTrue(resultPage.isResultCardDisplayed() );
        //toCheckResultHeadingDisplayed
        softAssert.assertTrue(resultPage.isResultHeadingDisplayed());
        //toCheckResultName
        softAssert.assertEquals(resultPage.getResultHeader(),"BHASKAR"+"'S RESULT");
        //isRemarksDisplayed
        softAssert.assertTrue(resultPage.isRemarksDisplayed());
        //isBackGroundColorDisplayed
        String color= resultPage.getResultCardBackgroundColor();
        softAssert.assertTrue(color.matches("^rgba\\(\\d{1,3},\\d{1,3},\\d{1,3},(0|1|0?\\.\\d+)\\)$\n"));
    }
    @Test(priority = 2,groups = {"ui","regression"})
    private void summaryCardUIValidation()
    {
        //toCheckSummaryCardIsDisplayed
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertTrue(resultPage.isSummaryCardDisplayed());
        //toCheckSummaryHeadingDisplayed
        softAssert.assertTrue(resultPage.isSummaryHeadingDisplayed());
        //isOverAllScoreDisplayed
        softAssert.assertTrue(resultPage.isOverAllDisplayed());
        //For AgeScore
        softAssert.assertTrue(resultPage.isAgeScoreLabelDisplayed());
        //For pulseScore
        softAssert.assertTrue(resultPage.isPulseScoreLabelDisplayed());
        //Bp Score
        softAssert.assertTrue(resultPage.isBpScoreLabelDisplayed());
        //For reset Btn
        softAssert.assertTrue(resultPage.isResetBtnDisplayed());
        softAssert.assertAll();
    }
}
