package com.example.mynotes.notePackage.database;

import android.app.Application;

import com.example.mynotes.notePackage.dao.NoteDao;
import com.example.mynotes.notePackage.entities.Note;

import java.util.List;

import androidx.lifecycle.LiveData;

import static com.example.mynotes.notePackage.database.NotesDatabase.databasewriteExecutor;
import static com.example.mynotes.notePackage.database.NotesDatabase.getNotesDatabase;


public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> listLiveData;

    public NoteRepository(Application application){
        NotesDatabase db= getNotesDatabase(application);
        noteDao=db.noteDao();
        listLiveData=noteDao.getAllNotes();
    }
    LiveData<List<Note>> getAllNotes(){
        return listLiveData;
    }
    void insert(final Note note){
        databasewriteExecutor.execute(new Runnable() {
                                          @Override
                                          public void run() {
                                              noteDao.insertNote(note);
                                          }
        });

    }
    void delete(final Note note){
        databasewriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteNote(note);
            }
        });
    }
}
