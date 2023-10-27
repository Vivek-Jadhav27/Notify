package com.example.Notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventDepart extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText inputevent;
    private String stringDateSelected;
    Button addevent;
    private DatabaseReference databaseReference;
    private List<String> eventDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_depart);

        calendarView = findViewById(R.id.calendarevent);
        inputevent = findViewById(R.id.inputEvent);
        addevent = findViewById(R.id.eventbutton);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputevent.getText().toString().isEmpty()  ){
                    inputevent.setError("Empty Field");
                }
                else{
                    Event event = new Event(inputevent.getText().toString() , stringDateSelected);
                    databaseReference.child(databaseReference.push().getKey()).setValue(event);
                    Toast.makeText(EventDepart.this, "Event Uploaded", Toast.LENGTH_SHORT).show();
                    inputevent.setText("");
                }
                }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                stringDateSelected = Integer.toString(i) + Integer.toString(i1+1) + Integer.toString(i2);
                calendarClicked();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Event");
    }

    private void calendarClicked(){
        databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    inputevent.setText(snapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}