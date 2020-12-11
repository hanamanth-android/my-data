package com.example.mynotes.dutypackage.dao;

import android.content.Context;

import com.example.mynotes.dutypackage.model.DutySheet;

import java.util.List;

import androidx.lifecycle.LiveData;


public interface DutyDao {

      // List<DutySheet> getAllExcelData();

        void createSheet(Context c);

        void updateSheet(Context c,DutySheet note);

        void deleteExcel(DutySheet dutySheet);

        List<DutySheet> readExcel(Context context,String sheetName );

    }
