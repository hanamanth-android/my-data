package com.example.mynotes.dutypackage.dutyAdapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mynotes.R;
import com.example.mynotes.dutypackage.model.DutySheet;
import com.example.mynotes.notePackage.entities.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DutyAdapter extends RecyclerView.Adapter<DutyAdapter.DutyViewHolder> {

    List<DutySheet> dutySheetList;
    private Timer timer;
    List<DutySheet> dutySheetlists;

    public DutyAdapter(List<DutySheet> ds) {
        this.dutySheetList = ds;
        this.dutySheetlists = ds;
    }

    @NonNull
    @Override
    public DutyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DutyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_duty, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull DutyViewHolder holder, int position) {

        DutySheet dutySheet = dutySheetList.get(position);
        holder.setDutyList(dutySheet);

    }

    @Override
    public int getItemCount() {
        return dutySheetList.size();
    }

    public void setDutySheets(List<DutySheet> dutySheets) {
        dutySheetList = dutySheets;
        notifyDataSetChanged();
    }
    public List<DutySheet> getlist(){
        return dutySheetList;
    }

    public void searchNotes(final String searchkeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchkeyword.trim().isEmpty()) {
                    dutySheetList = dutySheetlists;
                } else {
                    ArrayList<DutySheet> temp = new ArrayList<>();
                    for (DutySheet note : dutySheetlists) {
                        if (note.getDuty().toLowerCase().contains(searchkeyword.toLowerCase()) ||
                                note.getDate().toLowerCase().contains(searchkeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    dutySheetList = temp;
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

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public List<DutySheet> searchMonth(final String searchkeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchkeyword.equals("0")) {
                    dutySheetList = dutySheetlists;
                } else {
                    ArrayList<DutySheet> temp = new ArrayList<>();
                    for (DutySheet note : dutySheetlists) {
                        String[] dates = note.getDate().split("/");
                        if (dates[1].equals(searchkeyword)) {
                            temp.add(note);
                        }
                    }
                    dutySheetList = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 100);
        return dutySheetList;
    }



    static class DutyViewHolder extends RecyclerView.ViewHolder {
        TextView date, duty, time, km, night, remarks;

        public DutyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.view_date1);
            duty = itemView.findViewById(R.id.view_duty1);
            time = itemView.findViewById(R.id.view_time1);
            km = itemView.findViewById(R.id.view_km1);
            night = itemView.findViewById(R.id.view_night1);
            remarks = itemView.findViewById(R.id.view_remarks1);
        }

        void setDutyList(DutySheet dutyList) {
            date.setText(dutyList.getDate());
            duty.setText(dutyList.getDuty());
            time.setText(dutyList.getTime());
            km.setText(dutyList.getKm());
            night.setText(dutyList.getNight());
            remarks.setText(dutyList.getRemarks());

        }
    }
}
