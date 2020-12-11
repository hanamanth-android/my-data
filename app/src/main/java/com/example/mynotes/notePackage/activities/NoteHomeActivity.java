package com.example.mynotes.notePackage.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.notePackage.adapters.NoteAdapter;
import com.example.mynotes.notePackage.database.NoteViewmodel;
import com.example.mynotes.notePackage.entities.Note;
import com.example.mynotes.notePackage.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class NoteHomeActivity extends AppCompatActivity implements NotesListener {
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTE = 3;

    private RecyclerView noteRecyclerview;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private NoteViewmodel viewmodel;
    private int noteClickedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_home);

        ImageView imageAddNoteMain = findViewById(R.id.imageaddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), CreateNotesActivity.class),
                        REQUEST_CODE_ADD_NOTE);
            }
        });

        TextView backBtn=findViewById(R.id.textBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        noteRecyclerview = findViewById(R.id.notesrecyclerview);

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, this);
        noteRecyclerview.setAdapter(noteAdapter);
        noteRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        viewmodel = new ViewModelProvider(this).get(NoteViewmodel.class);
        viewmodel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setNotes(notes);
                noteList = notes;

            }
        });
        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (noteList.size() != 0) {
                    noteAdapter.searchNotes(editable.toString());
                }

            }
        });
        // getNotes();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNotesActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_NOTE) {
                Note note = null;
                if (data != null) {
                    note = (Note) data.getSerializableExtra("extra_reply");
                }
                viewmodel.insert(note);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                Note note = null;
                if (data != null) {
                    note = (Note) data.getSerializableExtra("extra_reply");
                }
                // viewmodel.delete(note);
                viewmodel.insert(note);
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

            }


        }

    }

}