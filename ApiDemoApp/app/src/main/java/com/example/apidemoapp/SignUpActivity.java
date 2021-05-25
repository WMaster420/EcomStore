package com.example.apidemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout username_til;
    TextInputLayout email_til;
    TextInputLayout password_til;
    TextInputEditText username_tie;
    TextInputEditText email_tie;
    TextInputEditText password_tie;
    Button signup_btn;
    TextView signin_btn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        username_til = findViewById(R.id.signup_username_til);
        email_til = findViewById(R.id.signup_email_til);
        password_til = findViewById(R.id.signup_password_til);
        username_tie = findViewById(R.id.signup_username_tie);
        email_tie = findViewById(R.id.signup_email_tie);
        password_tie = findViewById(R.id.signup_password_tie);
        signup_btn = findViewById(R.id.signup_btn);
        signin_btn = findViewById(R.id.signup_signin_btn);


        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username_til.setError(null);
                email_til.setError(null);
                password_til.setError(null);

                if(username_tie.getText().toString().isEmpty()){
                    username_til.setError("Username cannot be empty!");
                }
                else if(email_tie.getText().toString().isEmpty()){
                    email_til.setError("Please enter a valid Email Id!");
                }
                else if(password_tie.getText().toString().isEmpty() || password_tie.getText().toString().length() < 6){
                    email_til.setError(null);
                    password_til.setError("Password must be at least 6 characters long!");
                }
                else{
                    username_til.setError(null);
                    email_til.setError(null);
                    password_til.setError(null);
                    mAuth.createUserWithEmailAndPassword(email_tie.getText().toString(), password_tie.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Log.e("Auth Exception", task.getException().toString());
                                        Toast.makeText(SignUpActivity.this, "Authentication Failed!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}