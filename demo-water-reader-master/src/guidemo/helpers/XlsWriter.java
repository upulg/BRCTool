/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.helpers;

import guidemo.models.ReticEntry;
import guidemo.models.WaterDetail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 */
public class XlsWriter {
    public static boolean writeReticData(
            String fileName, ArrayList<ReticEntry> data,
            int beginRow, int sheetNumber){
        
        int beginCell = 8;
        
        try {
            FileInputStream excelFile = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            XSSFSheet datatypeSheet = workbook.getSheetAt(sheetNumber);
            
            if (datatypeSheet == null){
                datatypeSheet = workbook.createSheet();
            }
            
            int numOfRow = beginRow + data.size();
            
            //1. Create the date cell style
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            XSSFCellStyle cellStyle         = workbook.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
            
            for(int i = beginRow; i < numOfRow; i++){
                XSSFRow dataRow = datatypeSheet.getRow(i);
                ReticEntry retic = data.get(i - beginRow);
                
                if (dataRow == null) {
                    dataRow = datatypeSheet.createRow(i);
                }
                
                dataRow = writeDateCell(dataRow, beginCell, retic.date, cellStyle);
                dataRow = writeFloatCell(dataRow, beginCell + 1, retic.totalChlorine);
                dataRow = writeFloatCell(dataRow, beginCell + 2, retic.temperature);
                dataRow = writeFloatCell(dataRow, beginCell + 3, retic.nh3);
                dataRow = writeFloatCell(dataRow, beginCell + 4, retic.no2);
            }
            
            excelFile.close();

            FileOutputStream outFile = new FileOutputStream(new File(fileName));
            workbook.write(outFile);
            workbook.close();
            outFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public static boolean writeWaterData(
            String fileName, ArrayList<WaterDetail> data,
            int beginRow, int sheetNumber){
        
        int beginCell = 0;
        
        try {
            FileInputStream excelFile = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            XSSFSheet datatypeSheet = workbook.getSheetAt(sheetNumber);
            
            if (datatypeSheet == null){
                datatypeSheet = workbook.createSheet();
            }
            
            int numOfRow = beginRow + data.size();
            
            //1. Create the date cell style
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            XSSFCellStyle cellStyle         = workbook.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
            
            for(int i = beginRow; i < numOfRow; i++){
                XSSFRow dataRow = datatypeSheet.getRow(i);
                WaterDetail retic = data.get(i - beginRow);
                
                if (dataRow == null) {
                    dataRow = datatypeSheet.createRow(i);
                }
                
                dataRow = writeDateCell(dataRow, beginCell, retic.date, cellStyle);
                dataRow = writeFloatCell(dataRow, beginCell + 1, retic.tciIn);
                dataRow = writeFloatCell(dataRow, beginCell + 2, retic.tciOut);
                dataRow = writeFloatCell(dataRow, beginCell + 3, retic.temperature);
                dataRow = writeFloatCell(dataRow, beginCell + 4, retic.nh3);
                dataRow = writeFloatCell(dataRow, beginCell + 5, retic.no2);
                dataRow = writeStringCell(dataRow, beginCell + 6, retic.getDosed());
            }
            
            excelFile.close();

            FileOutputStream outFile = new FileOutputStream(new File(fileName));
            workbook.write(outFile);
            workbook.close();
            outFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    static XSSFRow writeDateCell(XSSFRow row, int col, Date date, XSSFCellStyle style) {
        XSSFCell cell = row.getCell(col);
        
        if(cell == null) {
            cell = row.createCell(col);
        }
        
        cell.setCellValue(date);
        cell.setCellStyle(style);
        
        return row;
    }
    
    static XSSFRow writeFloatCell(XSSFRow row, int col, Float val) {
        XSSFCell cell = row.getCell(col);
        
        if(cell == null) {
            cell = row.createCell(col);
        }
        
        cell.setCellValue(val);
        
        return row;
    }
    
    static XSSFRow writeStringCell(XSSFRow row, int col, String val) {
        XSSFCell cell = row.getCell(col);
        
        if(cell == null) {
            cell = row.createCell(col);
        }
        
        cell.setCellValue(val);
        
        return row;
    }
}
