
package com.example.Notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EventStudent extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView eventname;
    private String stringDateSelected;
    private DatabaseReference databaseReference;
    HashMap<String,String> events = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_student);

        calendarView = findViewById(R.id.studentevent);
        eventname = findViewById(R.id.eventname);
        databaseReference = FirebaseDatabase.getInstance().getReference("Event");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                stringDateSelected = Integer.toString(i) + Integer.toString(i1+1) + Integer.toString(i2);
                if(events.containsKey(stringDateSelected)){
                    eventname.setText(events.get(stringDateSelected));
                }
                else {
                    eventname.setText("Event Name");
                }
                calendarClicked();

            }
        });

    }

    private void calendarClicked(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing event dates
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        events.put(event.getEventDate(),event.getEventName());
                        if(events.containsKey(stringDateSelected)){
                            eventname.setText(events.get(stringDateSelected));
                        }
                        else {
                            eventname.setText("Event Name");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any database error
            }
        });
        }
    }
