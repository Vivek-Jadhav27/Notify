package com.example.Notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView eventcard, noticecard;
    ImageView logout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventcard = findViewById(R.id.eventpic);
        noticecard = findViewById(R.id.noticepic);
        logout =findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish();
            }
        });
        String role = getIntent().getStringExtra("Role");


        eventcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role.equals("Department")){
                    startActivity(new Intent(MainActivity.this,EventDepart.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this,EventStudent.class));
                }
            }
        });


        noticecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role.equals("Department")){
                    startActivity(new Intent(MainActivity.this,DepartHomePage.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this,StudentHomePage.class));
                }
            }
        });
    }
}