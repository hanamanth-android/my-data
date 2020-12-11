package com.example.mynotes.notePackage.listeners;


import com.example.mynotes.notePackage.entities.Note;

public interface NotesListener {
    public void onNoteClicked(Note note, int position);
}
