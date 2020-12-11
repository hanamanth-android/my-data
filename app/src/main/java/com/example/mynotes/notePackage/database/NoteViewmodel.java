package com.example.mynotes.notePackage.database;

import android.app.Application;


import com.example.mynotes.notePackage.entities.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NoteViewmodel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;


    public NoteViewmodel(@NonNull Application application) {
        super(application);
        repository=new NoteRepository(application);
        allNotes=repository.getAllNotes();

    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
    public void insert(Note note){
        repository.insert(note);
    }
    public void delete(Note note){
        repository.delete(note);
    }
}
