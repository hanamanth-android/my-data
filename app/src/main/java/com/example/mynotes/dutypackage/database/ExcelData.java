package com.example.mynotes.dutypackage.database;

import android.content.Context;
import android.widget.Toast;

import com.example.mynotes.dutypackage.dao.DutyDao;
import com.example.mynotes.dutypackage.model.DutySheet;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class ExcelData implements DutyDao {
    Workbook workbook;
    Sheet sheet;
    File file;


    public List<DutySheet> getAllExcelData() {
        return null;
    }

    public void createSheet(Context context) {
         file = new File(context.getExternalFilesDir(null), "data.xls");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(Calendar.getInstance().get(Calendar.YEAR)+"");

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("DATE");

        cell = row.createCell(1);
        cell.setCellValue("DUTY");

        cell = row.createCell(2);
        cell.setCellValue("TIME");

        cell = row.createCell(3);
        cell.setCellValue("KM");

        cell = row.createCell(4);
        cell.setCellValue("NIGHT HOUR");

        cell = row.createCell(5);
        cell.setCellValue("REMARKS");
        sheet.setColumnWidth(0, (15 * 200));
        sheet.setColumnWidth(1, (15 * 200));
        sheet.setColumnWidth(2, (15 * 200));
        sheet.setColumnWidth(3, (15 * 200));
        sheet.setColumnWidth(4, (15 * 200));
        sheet.setColumnWidth(5, (15 * 500));


        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSheet(Context context, DutySheet ds) {
       file= new File(context.getExternalFilesDir(null), "data.xls");
        Workbook workbook;
        Sheet sheet;
        String[] yearString=ds.getDate().split("/");
        try {
            if (!file.exists()) {
               createNewWorkBook(file);
            }
                FileInputStream inputStream = new FileInputStream(file);
                workbook = new HSSFWorkbook(inputStream);

            Row row;
               if (workbook.getSheetIndex(yearString[2])==-1){
                   sheet=workbook.createSheet(yearString[2]);
                   row = sheet.createRow(0);
                   Cell cell = row.createCell(0);
                   cell.setCellValue("DATE");

                   cell = row.createCell(1);
                   cell.setCellValue("DUTY");

                   cell = row.createCell(2);
                   cell.setCellValue("TIME");

                   cell = row.createCell(3);
                   cell.setCellValue("KM");

                   cell = row.createCell(4);
                   cell.setCellValue("NIGHT HOUR");

                   cell = row.createCell(5);
                   cell.setCellValue("REMARKS");
               }else {
                   sheet = workbook.getSheet(yearString[2]);
               }

                   int rowCount = sheet.getLastRowNum();
                    row = sheet.createRow(++rowCount);

                Cell cell = row.createCell(0);
                cell.setCellValue(ds.getDate());

                cell = row.createCell(1);
                cell.setCellValue(ds.getDuty());

                cell = row.createCell(2);
                cell.setCellValue(ds.getTime());

                cell = row.createCell(3);
                cell.setCellValue(ds.getKm());

                cell = row.createCell(4);
                cell.setCellValue(ds.getNight());

                cell = row.createCell(5);
                cell.setCellValue(ds.getRemarks());

                inputStream.close();


            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }

    void createNewWorkBook(File file){

        workbook = new HSSFWorkbook();

     /*   sheet = workbook.createSheet(Calendar.getInstance().get(Calendar.YEAR)+"");
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("DATE");

        cell = row.createCell(1);
        cell.setCellValue("DUTY");

        cell = row.createCell(2);
        cell.setCellValue("TIME");

        cell = row.createCell(3);
        cell.setCellValue("KM");

        cell = row.createCell(4);
        cell.setCellValue("NIGHT HOUR");

        cell = row.createCell(5);
        cell.setCellValue("REMARKS");
*/

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }catch (Exception e){

        }

    }

    @Override
    public void deleteExcel(DutySheet dutySheet) {

    }

    @Override
    public List<DutySheet> readExcel(Context context,String sheetName) {
        List<DutySheet> sheetList = new ArrayList<>();
         file = new File(context.getExternalFilesDir(null), "data.xls");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(inputStream);
            if (workbook.getSheetIndex(sheetName)!=-1) {
                Sheet sheet = workbook.getSheet(sheetName);
                Iterator<Row> iterator = sheet.rowIterator();
                iterator.next();
                while (iterator.hasNext()) {
                    DutySheet ds = new DutySheet();
                    Row nextRow = iterator.next();
                    ds.setDate(nextRow.getCell(0).getStringCellValue());
                    ds.setDuty(nextRow.getCell(1).getStringCellValue());
                    ds.setTime(nextRow.getCell(2).getStringCellValue());
                    ds.setKm(nextRow.getCell(3).getStringCellValue());
                    ds.setNight(nextRow.getCell(4).getStringCellValue());
                    ds.setRemarks(nextRow.getCell(5).getStringCellValue());
                    sheetList.add(ds);
                }
                workbook.close();
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheetList;
    }


}
