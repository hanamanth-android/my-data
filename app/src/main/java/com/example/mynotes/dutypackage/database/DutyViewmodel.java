package com.example.mynotes.dutypackage.database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.mynotes.dutypackage.model.DutySheet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DutyViewmodel extends AndroidViewModel {
    private static final String TAG = "haa";
    private final DutyRepository repository;
    private final MutableLiveData<List<DutySheet>> allDutylist = new MutableLiveData<>();
    public LiveData<List<DutySheet>> listLiveData=allDutylist;
    private static String sheet ;



    public DutyViewmodel(@NonNull Application application) {
        super(application);
        repository = new DutyRepository(application);


    }

    public void setList(String s) {
        sheet= s;
        List<DutySheet> list1 = repository.readExcel(sheet);
        allDutylist.setValue(list1);
    }

    public LiveData<List<DutySheet>> getList(String sheetName)
    {
       /* List<DutySheet> list1 = repository.readExcel(sheet);
        allDutylist.setValue(list1);*/
        return allDutylist;

    }

    public void createSheet() {
        repository.createSheet();
    }

    public void updateSheet( DutySheet dutySheet) {
        repository.updateSheet( dutySheet);
    }


    public void delete(DutySheet note) {
        repository.delete(note);
    }
}
