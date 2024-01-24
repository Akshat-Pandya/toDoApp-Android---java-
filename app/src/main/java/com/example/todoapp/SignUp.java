package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private Button createAccount;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=findViewById(R.id.editTextEmailSignup);
        password=findViewById(R.id.editTextPasswordSignup);
        createAccount=findViewById(R.id.btnsignup);
        progressBar=findViewById(R.id.progressBar);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail=email.getText().toString();
                String spassword=password.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(semail!=null && isEmailValid(semail) ){

                    auth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SignUp.this, "Account Created Successfully ! ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(SignUp.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(SignUp.this, "Could not create account", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
                else{
                    email.setError("Invalid email id ");
                }
            }
        });


    }
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}