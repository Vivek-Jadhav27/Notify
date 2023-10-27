package com.example.Notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterPage extends AppCompatActivity {

    EditText username , password;
    Button regbtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.registerusername);
        password = findViewById(R.id.registerpassword);
        regbtn = findViewById(R.id.registerpagebtn);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateUser()){
                    mAuth.createUserWithEmailAndPassword(username.getText().toString()+ "@yourdomain.com",password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterPage.this, "User is Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterPage.this,LoginPage.class));
                                    }
                                    else {
                                        Exception e = task.getException();
                                        if (e instanceof FirebaseAuthUserCollisionException) {
                                            // This exception is thrown if the email is already in use
                                            // Handle the specific error
                                        } else {
                                            // Handle other registration errors
                                        }

                                        // Display a toast message with the Firebase error
                                        Toast.makeText(RegisterPage.this, "Firebase Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                }
                 }
        });


    }

    private boolean validateUser() {
        boolean result = true;
        String name = username.getText().toString();
        String pass = password.getText().toString();
        if(name.isEmpty()){
            username.setError("Empty Field");
            result = false;
        }
        else{
            if(name.length() <6){
                username.setError("UserName should be of 6 digit");
                result =false;
            }
        }
        if (pass.isEmpty()){
            password.setError("Empty Field");
            result = false;
        }

        return result;

    }
}