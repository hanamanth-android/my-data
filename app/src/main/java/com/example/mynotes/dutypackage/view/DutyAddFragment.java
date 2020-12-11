package com.example.mynotes.dutypackage.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.dutypackage.database.DutyViewmodel;
import com.example.mynotes.dutypackage.database.dutyviewModelFactory;
import com.example.mynotes.dutypackage.model.DutySheet;

import java.util.Calendar;


public class DutyAddFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hh";
    EditText duty, time, km, night, remarks;
    TextView date;
    Button save, read;
    private DutyViewmodel dutyViewmodel;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    public DutyAddFragment() {
        // Required empty public constructor
    }

   
    public static DutyAddFragment newInstance(String param1, String param2) {

        return new DutyAddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dutyViewmodel = new ViewModelProvider(requireActivity(),new dutyviewModelFactory(getActivity().getApplication())).get(DutyViewmodel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_duty_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date = view.findViewById(R.id.edit_text_date);
        duty = view.findViewById(R.id.edit_text_duty);
        time = view.findViewById(R.id.edit_text_time);
        km = view.findViewById(R.id.edit_text_km);
        night = view.findViewById(R.id.edit_text_night);
        remarks = view.findViewById(R.id.edit_text_remarks);
        save = view.findViewById(R.id.button_cancel);
        read = view.findViewById(R.id.button_write);

        save.setOnClickListener(this);
        read.setOnClickListener(this);
        date.setOnClickListener(this);

        Calendar calendar=Calendar.getInstance();
        int year,month,day;
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        month++;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        String ddd = day + "/" + month + "/" + year;
        date.setText(ddd);


        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                int mm=m+1;
                String dd = d + "/" + mm + "/" + y;
                date.setText(dd);
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
               requireActivity().onBackPressed();

                break;
            case R.id.button_write:
                if (night.getText().toString().contains(":")||night.getText().toString().equals("")){
                    dutyViewmodel.updateSheet(addData());
                    Toast.makeText(getContext(), "data saved", Toast.LENGTH_SHORT).show();

                }else{
                    alertDailog();
                }

                break;
            case R.id.edit_text_date:
                datePick();

            default:
        }
    }



    private DutySheet addData() {
        DutySheet dutySheet = new DutySheet();

        dutySheet.setDate(date.getText().toString());
        dutySheet.setDuty(duty.getText().toString());
        dutySheet.setTime(time.getText().toString());
        dutySheet.setNight(night.getText().toString());
        dutySheet.setRemarks(remarks.getText().toString());
        dutySheet.setKm(km.getText().toString());
        return dutySheet;
    }
    public void alertDailog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity());
        builder.setMessage("Night hour -> Enter HH:mm format only ");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void datePick(){
        Calendar calendar=Calendar.getInstance();
        int year,month,day;
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(requireContext(),
                R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                dateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.show();

    }
}