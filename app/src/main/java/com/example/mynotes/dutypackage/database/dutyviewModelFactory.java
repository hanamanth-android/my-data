package com.example.mynotes.dutypackage.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class dutyviewModelFactory implements ViewModelProvider.Factory {
   private final Application application;
    public dutyviewModelFactory(Application application1) {
        this.application=application1;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      if (modelClass==DutyViewmodel.class){
          return (T) new DutyViewmodel(application);
      }
      return null;
    }
}
