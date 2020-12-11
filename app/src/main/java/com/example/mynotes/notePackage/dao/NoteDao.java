package com.example.mynotes.notePackage.dao;


import com.example.mynotes.notePackage.entities.Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;


@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<Note>> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
