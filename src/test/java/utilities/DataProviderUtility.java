package utilities;

import org.testng.annotations.DataProvider;

public class DataProviderUtility {

    @DataProvider(name="validTestData")
    public Object[][] supplyValidData() throws Exception{
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\FormFieldTestData.xlsx";
        String sheetName = "ValidData";
        return ExcelUtility.getData(filePath, sheetName);
    }

    @DataProvider(name="invalidTestData")
    public Object[][] supplyInvalidData() throws Exception{
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\FormFieldTestData.xlsx";
        String sheetName = "InvalidData";
        return ExcelUtility.getData(filePath, sheetName);
    }

    @DataProvider(name="ScoreData")
    public Object[][] supplyScoreValidationData() throws Exception{
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\ScoreValidationData.xlsx";
        String sheetName = "ScoreValidation";
        return ExcelUtility.getData(filePath, sheetName);
    }

    @DataProvider(name="OverallScoreData")
    public Object[][] supplyOverScoreData() throws Exception{
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\ScoreValidationData.xlsx";
        String sheetName = "OverScoreValidation";
        return ExcelUtility.getData(filePath, sheetName);
    }
}
