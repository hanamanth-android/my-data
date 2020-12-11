package com.example.mynotes.dutypackage.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.dutypackage.database.DutyViewmodel;
import com.example.mynotes.dutypackage.database.dutyviewModelFactory;
import com.example.mynotes.dutypackage.dutyAdapter.DutyAdapter;
import com.example.mynotes.dutypackage.model.DutySheet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainFragment extends Fragment {

    private DutyViewmodel dutyViewmodel;
    private List<DutySheet> dutySheetList = new ArrayList<>();
    RecyclerView recyclerView;
    DutyAdapter dutyAdapter;
    Spinner spinnerYear;


    private NavController navController;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dutyViewmodel = new ViewModelProvider(this, new dutyviewModelFactory(getActivity().getApplication())).get(DutyViewmodel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.addDuty:
                    navController.navigate(R.id.action_mainFragment_to_dutyAddFragment);
                    Toast.makeText(requireActivity(), "add duty", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.dutysheet:

                    Toast.makeText(requireActivity(), " duty sheet", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return true;
        });
        toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        recyclerView = view.findViewById(R.id.recyler_duty);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setHasFixedSize(true);
        TextView total_km = view.findViewById(R.id.total_km);
        TextView total_night = view.findViewById(R.id.total_night);

        FloatingActionButton fab = view.findViewById(R.id.fab_addDuty);
        fab.setOnClickListener(view1 -> navController.navigate(R.id.action_mainFragment_to_dutyAddFragment));

        //year spinner
        ArrayList<String> years=new ArrayList<>();
        int thisYear= Calendar.getInstance().get(Calendar.YEAR);
        for (int j=thisYear;j<=(thisYear+10);j++){
            years.add(Integer.toString(j));
        }
        ArrayAdapter<String> adapter= new ArrayAdapter<>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, years);
         spinnerYear =view.findViewById(R.id.year_spinner);
        spinnerYear.setAdapter(adapter);

        //month spinner
        final String[] months=new String[]{"All","JAN","FEB","MAR","APR","MAY","JUN","JULY","AUG","SEPT","OCT","NOV","DEC"};
        ArrayAdapter<String> adapterMonth= new ArrayAdapter<>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, months);
        Spinner monthSpin=view.findViewById(R.id.month_spinner);
        monthSpin.setAdapter(adapterMonth);
        dutyViewmodel.setList((String) spinnerYear.getSelectedItem());


        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             dutyViewmodel.setList((String) adapterView.getSelectedItem());
         }
         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {
         }
     });

        dutyViewmodel.getList((String) spinnerYear.getSelectedItem()).observe(requireActivity(), dutySheets -> {
            //  dutyAdapter.setDutySheets(dutySheets);
            dutySheetList = dutySheets;
            dutyAdapter = new DutyAdapter(dutySheets);
            recyclerView.setAdapter(dutyAdapter);

            total_km.setText(new StringBuilder().append(calculateSumKm(dutySheetList)).append("").toString());
            total_night.setText(calculateSumNight(dutySheetList));
        });



        monthSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dutyAdapter.cancelTimer();
               if(dutySheetList.size() !=0) {
                 List<DutySheet> ds=  dutyAdapter.searchMonth(String.valueOf(i));
                 dutyAdapter.setDutySheets(ds);
                   total_km.setText(new StringBuilder().append(calculateSumKm(ds)).append("").toString());
                   total_night.setText(calculateSumNight(ds));
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        EditText inputSearch = view.findViewById(R.id.search_duty);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dutyAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (dutySheetList.size() != 0) {
                    dutyAdapter.searchNotes(editable.toString());
                }

            }
        });


    }


    int calculateSumKm( List<DutySheet> dss) {
        int i = 0;
        for (DutySheet ds : dss) {
            i += Integer.parseInt(ds.getKm());

        }
        return i;
    }

    String calculateSumNight( List<DutySheet> dss) {
        long i = 0;
        for (DutySheet ds : dss) {
            if (!ds.getNight().equals("")) {
                String[] nights = ds.getNight().split(":");

                i += 60 * Integer.parseInt(nights[0]);
                i += Integer.parseInt(nights[1]);
            }
        }
        long h = i / 60;
        i %= 60;
        long m = i;
        String night = h + ":" + m;
        return night;
    }

}