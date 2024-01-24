package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {

    private EditText password,email;
    private boolean flag;
    private Button login;
    private TextView signuplink;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        password=findViewById(R.id.editTextPasswordLogin);
        email=findViewById(R.id.editTextEmailLogin);
        login=findViewById(R.id.btnLogin);
        signuplink=findViewById(R.id.textViewSignUpLink);
        auth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail=email.getText().toString();
                String spassword=password.getText().toString();
                if(semail!=null && spassword!=null){

                        auth.signInWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Login.this, "Sign In Successful ", Toast.LENGTH_SHORT).show();
                                        getFirebaseToken();
                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(Login.this, "Please Create an Account to Sign - in ", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });


                }
                else{
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                    }
                });
    }

    private void gmailIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mailto:"));
        intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

        // Verify that the Gmail app is installed on the device
        PackageManager packageManager = getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfo != null) {
            // Gmail app is installed, start the intent
            startActivity(intent);
        }
    }
    private boolean isAccountCreated() {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            flag=false;

                        } else {
                           flag=true;
                        }
                    } else {
                        Toast.makeText(this, "Please try later", Toast.LENGTH_SHORT).show();
                    }
                });
        return flag;
    }

}