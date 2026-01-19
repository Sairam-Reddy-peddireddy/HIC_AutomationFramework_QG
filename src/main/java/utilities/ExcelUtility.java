package utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.text.DateFormatter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtility {
    public static FileInputStream fis;
    public static FileOutputStream fos;
    public static XSSFWorkbook workbook;
    public static XSSFSheet sheet;
    public static XSSFRow row;
    public static XSSFCell cell;


    public static int getRowCount(String filePath, String sheetName) throws Exception{
        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        int rowNum = sheet.getLastRowNum();
        workbook.close();
        fis.close();
        return rowNum;
    }

    public static int getCellCount(String filePath, String sheetName) throws Exception{
        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        int cellNum = sheet.getRow(0).getLastCellNum();
        workbook.close();
        fis.close();
        return cellNum;
    }

    public static String getCellData(String filePath, String sheetName, int rowNum, int cellNum) throws Exception {
        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rowNum);
        cell = row.getCell(cellNum);

        String data;
        try{
            DataFormatter df = new DataFormatter();
            data = df.formatCellValue(cell);
        }catch(Exception e){
            data = "";
        }
        workbook.close();
        fis.close();
        return data;
    }

    public static void setCellData(String filePath, String sheetName, int rowNum, int colNum, String data) throws Exception{
        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rowNum);
        cell = row.createCell(colNum);
        cell.setCellValue(data);
        fos = new FileOutputStream(filePath);
        workbook.write(fos);
        workbook.close();
        fos.close();
        fis.close();
    }

    public static Object[][] getData(String filePath, String sheetName) throws Exception{
        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        int rowNum = sheet.getLastRowNum();
        int cellNum = sheet.getRow(0).getLastCellNum();
        Object[][] data = new String[rowNum][cellNum];
        DataFormatter df = new DataFormatter();
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<cellNum;j++){
                data[i][j] = df.formatCellValue(sheet.getRow(i+1).getCell(j));
            }
        }
        workbook.close();
        fis.close();
        return data;
    }


}
