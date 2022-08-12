package com.example.computersolutions.Activity.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.computersolutions.Activity.Profile.UserProfileActivity;
import com.example.computersolutions.Model.RegisterModel;
import com.example.computersolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etConfirmPassword;
    Button btnLogin, btnNext;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RegisterModel registerModel;
    UserProfileChangeRequest userProfileChangeRequest;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, UserProfileActivity.class));
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }

        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.confirm);
        btnNext = findViewById(R.id.btnNext);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBarRegister);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerModel = new RegisterModel(etName.getText().toString(),
                                                  etEmail.getText().toString(),
                                                  etPassword.getText().toString(),
                                                  etConfirmPassword.getText().toString());
                if(verifyRegisterModel(registerModel)) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(registerModel.getEmail(), registerModel.getPassword())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(registerModel.getName())
                                            .build();
                                    firebaseUser.updateProfile(userProfileChangeRequest)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(),"User is created",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(RegisterActivity.this, PaymentActivity.class));
                                                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),"Cannot create the user. ",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Error! Email address is already taken",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    private boolean verifyRegisterModel(RegisterModel registerModel){
        if (registerModel.getName().isEmpty()) {
            etName.setError("Full Name is Required.");
            return false;
        }

        if (registerModel.getEmail().isEmpty()) {
            etEmail.setError("Email is Required.");
            return false;
        }

        if (registerModel.getPassword().isEmpty()) {
            etPassword.setError("Password is Required.");
            return false;
        }

        if (registerModel.getPassword().length() < 6) {
            etPassword.setError("Password Must be >= 6 Characters");
            return false;
        }

        if (registerModel.getConfirmPassword().isEmpty()) {
            etConfirmPassword.setError("Confirm Password is Required.");
            return false;
        }

        if (registerModel.getConfirmPassword().length() < 6) {
            etConfirmPassword.setError("Confirm Password Must be >= 6 Characters");
            return false;
        }

        if (!registerModel.getPassword().equals(registerModel.getConfirmPassword())) {
            etConfirmPassword.setError("Passwords Do Not Match");
            return false;
        } else {
            return true;
        }
    }
}