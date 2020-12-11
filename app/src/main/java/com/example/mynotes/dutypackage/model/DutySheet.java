package com.example.mynotes.dutypackage.model;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dutySheets")
public class DutySheet implements Serializable {

        @PrimaryKey(autoGenerate = true)
        private  int id;


    public DutySheet() {
    }

    @ColumnInfo(name = "date")
        private String date;

        @ColumnInfo(name = "duty")
        private String duty;

        @ColumnInfo(name = "time")
        private String time;

        @ColumnInfo(name = "km")
        private String km;

        @ColumnInfo(name = "night")
        private String night;

        @ColumnInfo(name = "remarks")
        private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
