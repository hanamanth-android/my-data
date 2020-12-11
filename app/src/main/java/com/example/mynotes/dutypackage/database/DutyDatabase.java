package com.example.mynotes.dutypackage.database;

import android.content.Context;

import com.example.mynotes.dutypackage.dao.DutyDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


    public  class DutyDatabase  {


        private static DutyDatabase notesDatabase;

        static final ExecutorService databasewriteExecutor= Executors.newFixedThreadPool(4);

        static synchronized DutyDatabase getDutyDatabase(final Context context){
            if(notesDatabase== null){
                new DutyDatabase();
            }
            return  notesDatabase;
        }

    }
