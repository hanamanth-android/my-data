package com.example.mynotes.notePackage.database;

import android.content.Context;


import com.example.mynotes.notePackage.dao.NoteDao;
import com.example.mynotes.notePackage.entities.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static NotesDatabase notesDatabase;
    public abstract NoteDao noteDao();
     static final ExecutorService databasewriteExecutor= Executors.newFixedThreadPool(4);

     static synchronized NotesDatabase getNotesDatabase(final Context context){
        if(notesDatabase== null){
            notesDatabase= Room.databaseBuilder(context.getApplicationContext()
            ,NotesDatabase.class
            ,"notes_db").build();
        }
        return  notesDatabase;
    }

    /*private static RoomDatabase.Callback callback=new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databasewriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDao dao=notesDatabase.noteDao();

                }
            });
        }
    };
*/

}
