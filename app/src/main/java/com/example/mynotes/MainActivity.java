package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mynotes.dutypackage.view.DutyHomeActivity;
import com.example.mynotes.notePackage.activities.NoteHomeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity  {
    CardView cardDuty,cardNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardDuty=findViewById(R.id.cardDuty);
        cardNotes=findViewById(R.id.cardNotes);

        cardDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DutyHomeActivity.class);
                startActivity(intent);
            }
        });
        cardNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, NoteHomeActivity.class);
                startActivity(intent);
            }
        });





    }
    
}