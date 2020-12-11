package com.example.mynotes.dutypackage.database;

import android.app.Application;
import android.content.Context;

import com.example.mynotes.dutypackage.dao.DutyDao;
import com.example.mynotes.dutypackage.model.DutySheet;

import java.util.List;

import androidx.lifecycle.LiveData;

import static com.example.mynotes.dutypackage.database.DutyDatabase.databasewriteExecutor;


public class DutyRepository {
    private DutyDao dutyDao;
  //  private List<DutySheet> listLiveData;
    private Context context;
    private ExcelData excelData;

    public DutyRepository(Application application) {

        context = application;
        excelData = new ExcelData();
       // listLiveData = dutyDao.getAllExcelData();
    }

    List<DutySheet> getAllNotes() {
      //  return listLiveData;
        return null;
    }

    void createSheet() {
        databasewriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                excelData.createSheet(context);
            }
        });

    }

    void updateSheet( DutySheet dutySheet) {
        databasewriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                excelData.updateSheet(context, dutySheet);
            }
        });
    }

   List<DutySheet> readExcel(String sheetName) {

              return excelData.readExcel(context,sheetName);
    }

    void delete(final DutySheet note) {
        databasewriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                excelData.deleteExcel(note);
            }
        });
    }
}
