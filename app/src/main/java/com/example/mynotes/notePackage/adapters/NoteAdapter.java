package com.example.mynotes.notePackage.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.notePackage.entities.Note;
import com.example.mynotes.notePackage.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private NotesListener notesListener;
    private List<Note> noteSource;
    private Timer timer;
    Context context;

    private final LayoutInflater inflater;


    public NoteAdapter(Context context1, NotesListener notesListener1) {
        inflater = LayoutInflater.from(context1);
        this.context=context1;
        this.notesListener = notesListener1;

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(inflater.inflate(R.layout.item_container_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (notes != null) {
            Note note = notes.get(position);
            holder.setNote(note);
            holder.layoutNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notesListener.onNoteClicked(notes.get(position), position);
                }
            });

            holder.layoutNote.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "long press", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        } else {
            notes = new ArrayList<Note>();
        }


    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        } else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setNotes(List<Note> note) {
        notes = note;
        noteSource = note;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textsubTitle, textDateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;


        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textsubTitle = itemView.findViewById(R.id.textsubTitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imagenote);

        }

        void setNote(Note note) {
            textTitle.setText(note.getTitle());
            if (note.getSubTitle().trim().isEmpty()) {
                textsubTitle.setVisibility(View.GONE);
            } else {
                textsubTitle.setText(note.getSubTitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if (note.getImagePath() != null) {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            } else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchkeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchkeyword.trim().isEmpty()) {
                    notes = noteSource;
                } else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : noteSource) {
                        if (note.getTitle().toLowerCase().contains(searchkeyword.toLowerCase()) ||
                                note.getSubTitle().toLowerCase().contains(searchkeyword.toLowerCase())
                                || note.getNoteText().toLowerCase().contains(searchkeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }
    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }
}
