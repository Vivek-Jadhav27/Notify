package com.example.Notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private EditText inputUserName;
    private EditText inputPassword;
    String selectedRole;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            // Check the user's role
            checkUserRoleAndRedirect(currentUser);
        }
    }

    private void checkUserRoleAndRedirect(FirebaseUser user) {
        String role ;
        if (user != null) {
            String username = user.getEmail().split("@")[0];
                if (username.equals("cedept")) {
                    role ="Department";
                } else{
                    role ="Student";
                }
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    intent.putExtra("Role",role);
                    startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        inputUserName = findViewById(R.id.inputusername);
        inputPassword = findViewById(R.id.inputpassword);
        Button loginbtn = findViewById(R.id.loginbtn);
        Button registerbtn = findViewById(R.id.registerbtn);
        mAuth = FirebaseAuth.getInstance();

        RadioGroup radioGroup = findViewById(R.id.radiogrp);

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    closeKeyboardIfOutsideEditText(inputUserName, event);
                    closeKeyboardIfOutsideEditText(inputPassword, event);
                }
                return false; // Allow the event to continue propagating
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this,RegisterPage.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateRole() && validateUser()) {
                    // Get user input
                    String username = inputUserName.getText().toString();
                    String password = inputPassword.getText().toString();

                    // Sign in with Firebase Authentication
                    mAuth.signInWithEmailAndPassword(username + "@yourdomain.com", password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if ("Department".equals(selectedRole)) {
                                            if(username.equals("cedept")){
                                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                                intent.putExtra("Role","Department");
                                                startActivity(intent);

                                            }
                                            else {
                                                Toast.makeText(LoginPage.this, "Role selection is incorrect", Toast.LENGTH_SHORT).show();
                                            }
                                           }
                                        else if ("Student".equals(selectedRole)) {
                                            if(!username.equals("cedept")){
                                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                                intent.putExtra("Role","Student");
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(LoginPage.this, "Role selection is incorrect", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    } else {
                                        Toast.makeText(LoginPage.this, "Authentication failed. Check your username and password.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }
    private void closeKeyboardIfOutsideEditText(EditText editText, MotionEvent event) {
        if (editText != null && editText.isFocused()) {
            Rect outRect = new Rect();
            editText.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                // The touch was outside the EditText; close the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    private boolean validateRole(){
        RadioButton depart = findViewById(R.id.department);
        RadioButton student = findViewById(R.id.student);


        if (depart.isChecked()){
                selectedRole = "Department";
                return true;
            }
            else if(student.isChecked()){
                selectedRole ="Student";
                return true;
            }
            else {
                Toast.makeText(this, "Role is Not selected", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    private boolean validateUser(){
        boolean result = true;
        String username = inputUserName.getText().toString();
        String password =inputPassword.getText().toString();
        if(password.isEmpty()){
            inputPassword.setError("Empty Field");
            result =false;
        }
        if(username.isEmpty()){
            inputUserName.setError("Empty Field");
            result = false;
        }
        return result;
    }
}