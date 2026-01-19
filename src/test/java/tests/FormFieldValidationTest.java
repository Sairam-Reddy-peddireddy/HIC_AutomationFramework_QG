package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utilities.DataProviderUtility;
import utilities.Reporter;

public class FormFieldValidationTest extends BaseTest {

    @Test( dataProvider = "validTestData", dataProviderClass = DataProviderUtility.class, priority = 1,groups = {"functional","smoke","sanity","regression"})
    public void testInputFieldsWithValidData(String name, String age, String pulse, String sysBP, String diaBP, String field, String expectedResult, String testStatus, String testType){
        reporter.info("Test Started");
        SoftAssert softAssert = new SoftAssert();
        resultPage = indexPage.submitHealthIndexForm(name, age, pulse, sysBP, diaBP);
        softAssert.assertTrue(resultPage.isResultCardDisplayed());
        reporter.info("Input Form Submitted");
        resultPage.clickResetBtn();
        reporter.info("Reset Button Clicked");
        log.info("Input Form submitted with values"+name+" "+age+" "+pulse+" "+sysBP+" "+diaBP);

    }

    @Test( dataProvider = "invalidTestData", dataProviderClass = DataProviderUtility.class, priority = 2,groups = {"functional","regression"} )
    public void testInputFieldsWithInvalidData(String name, String age, String pulse, String sysBP, String diaBP, String field, String expectedResult, String testStatus, String testType){;
        reporter.info("Test Started: "+field);
        resultPage=indexPage.submitHealthIndexForm(name, age, pulse, sysBP, diaBP);
        reporter.info("Input Form Submitted");
        if(field.equalsIgnoreCase("name field")){

            if(resultPage.isResultCardDisplayed()){
                resultPage.clickResetBtn();
                reporter.info("Reset Button Clicked");
                Assert.fail();
            }else{
                String actualResult = indexPage.getNameErrorMessage();
                Assert.assertEquals(expectedResult, actualResult);
            }
        }
        else if(field.equalsIgnoreCase("age field")){
            if(resultPage.isResultCardDisplayed()){
                resultPage.clickResetBtn();
                reporter.info("Reset Button Clicked");
                Assert.fail();
            }else{
                String actualResult = indexPage.getAgeErrorMessage();
                Assert.assertEquals(expectedResult, actualResult);
            }

        }
        else if(field.equalsIgnoreCase("pulse field")){
            if(resultPage.isResultCardDisplayed()){
                resultPage.clickResetBtn();
                reporter.info("Reset Button Clicked");
                Assert.fail();
            }else{
                String actualResult = indexPage.getPulse();
                Assert.assertEquals(expectedResult, actualResult);
            }
        }
        else if(field.equalsIgnoreCase("systolic bp field")){
            if(resultPage.isResultCardDisplayed()){
                resultPage.clickResetBtn();
                reporter.info("Reset Button Clicked");
                Assert.fail();
            }else{
                String actualResult = indexPage.getSystolicBpErrorMessage();
                Assert.assertEquals(expectedResult, actualResult);
            }
        }
        else if(field.equalsIgnoreCase("diastolic bp field")){
            if(resultPage.isResultCardDisplayed()){
                resultPage.clickResetBtn();
                reporter.info("Reset Button Clicked");
                Assert.fail();
            }else{
                String actualResult = indexPage.getDiastolicBpErrorMessage();
                Assert.assertEquals(expectedResult, actualResult);
            }
        }
    }

}
