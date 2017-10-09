/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.helpers;

import guidemo.models.ReticEntry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import guidemo.models.WaterDetail;
import java.util.ArrayList;

/**
 *
 */
public class XlsReader {

    public static void readXlsFile(String fileName) {
        String excelFilePath = fileName;
        try {
            FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            XSSFSheet datatypeSheet = workbook.getSheetAt(1);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    CellType type = cell.getCellTypeEnum();

                    if (type == CellType.STRING) {
                        System.out.print(cell.getStringCellValue());
                    }

                    if (type == CellType.BOOLEAN) {
                        System.out.print(cell.getBooleanCellValue());
                    }

                    if (type == CellType.FORMULA) {
                        System.out.print(cell.getCellFormula());
                    }

                    if (type == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue());
                    }

                    System.out.print(" - ");
                }
                System.out.println();
            }

            workbook.close();
            excelFile.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public WaterDetail[] readWaterInfo(String fileName, int beginRow, int sheetNumber) {
        
        ArrayList<WaterDetail> rs = new ArrayList<>();
        
        try {
            FileInputStream excelFile = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            XSSFSheet datatypeSheet = workbook.getSheetAt(sheetNumber);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int line = 0;
                      
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();

                if (line >= beginRow) {
                    Cell dateCell = nextRow.getCell(0);
                    Cell tciInCell = nextRow.getCell(1);
                    Cell tciOutCell = nextRow.getCell(2);
                    Cell tempCell = nextRow.getCell(3);
                    Cell nh3Cell = nextRow.getCell(4);
                    Cell no2Cell = nextRow.getCell(5);
                    Cell tabletCell = nextRow.getCell(6);

                    if (canParseDate(dateCell)) {
                        Date date = dateCell.getDateCellValue();
                        float tciIn = tryParseNumber(tciInCell);
                        float tciOut = tryParseNumber(tciOutCell);
                        float temp = tryParseNumber(tempCell);
                        float nh3 = tryParseNumber(nh3Cell);
                        float no2 = tryParseNumber(no2Cell);
                        String tablet = tryParseString(tabletCell);
                        
                        WaterDetail dt;
                        dt = new WaterDetail(date, tciIn, tciOut, temp, nh3, no2, tablet);
                        dt.calculateValue();
                        
                        rs.add(dt);
                    }
                }

                line++;
            }

            workbook.close();
            excelFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs.toArray(new WaterDetail[rs.size()]);
    }
    
    static public ReticEntry[] readReticInfo(String fileName, int beginRow, int sheetNumber) {
        
        ArrayList<ReticEntry> rs = new ArrayList<>();
        
        try {
            FileInputStream excelFile = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            XSSFSheet datatypeSheet = workbook.getSheetAt(sheetNumber);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int line = 0;
                      
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();

                if (line >= beginRow) {
                    Cell dateCell = nextRow.getCell(8);
                    Cell totalChlorineCell = nextRow.getCell(9);
                    Cell temperatureCell = nextRow.getCell(10);
                    Cell nh3Cell = nextRow.getCell(11);
                    Cell no2Cell = nextRow.getCell(12);
                    
                    if (canParseDate(dateCell)) {
                        Date date = dateCell.getDateCellValue();
                        float totalChlorine = tryParseNumber(totalChlorineCell);
                        float temperature = tryParseNumber(temperatureCell);
                        float nh3 = tryParseNumber(nh3Cell);
                        float no2 = tryParseNumber(no2Cell);
                                                
                        ReticEntry dt;
                        dt = new ReticEntry(date, totalChlorine, temperature, nh3, no2);
                        dt.calculateValue();
                        
                        rs.add(dt);
                    }
                }

                line++;
            }

            workbook.close();
            excelFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs.toArray(new ReticEntry[rs.size()]);
    }

    public static float tryParseNumber(Cell cell) {
        float val = 0;

        if (cell != null) {
            CellType type = cell.getCellTypeEnum();

            if (type == CellType.NUMERIC) {
                val = (float) cell.getNumericCellValue();
            }
            
            if (type == CellType.FORMULA) {
                
                CellType cachedType = cell.getCachedFormulaResultTypeEnum();
                if ( cachedType == CellType.NUMERIC) {
                    val = (float) cell.getNumericCellValue();
                }
                
            }
        }

        return val;
    }
    
    public static String tryParseString(Cell cell) {
        String val = "";

        if (cell != null) {
            CellType type = cell.getCellTypeEnum();

            if (type == CellType.STRING) {
                val = cell.getStringCellValue();
            }
        }

        return val;
    }

    private static Boolean canParseDate(Cell cell) {
        if (cell == null) {
            return false;
        }

        return cell.getCellTypeEnum() == CellType.NUMERIC;
    }
}
