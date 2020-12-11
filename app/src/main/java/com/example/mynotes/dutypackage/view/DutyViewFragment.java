package com.example.mynotes.dutypackage.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mynotes.R;
import com.example.mynotes.dutypackage.database.DutyViewmodel;
import com.example.mynotes.dutypackage.database.dutyviewModelFactory;
import com.example.mynotes.dutypackage.dutyAdapter.DutyAdapter;
import com.example.mynotes.dutypackage.model.DutySheet;

import java.util.ArrayList;
import java.util.List;


public class DutyViewFragment extends Fragment {



    public DutyViewFragment() {
        // Required empty public constructor
    }

    public static DutyViewFragment newInstance(String param1, String param2) {
        return new DutyViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_duty_view, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}