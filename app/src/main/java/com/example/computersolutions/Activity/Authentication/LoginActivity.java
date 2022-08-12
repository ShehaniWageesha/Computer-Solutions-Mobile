package com.example.computersolutions.Activity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computersolutions.Activity.Profile.UserProfileActivity;
import com.example.computersolutions.MainActivity;
import com.example.computersolutions.Model.LoginModel;
import com.example.computersolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin, btnSignUp;
    FirebaseAuth firebaseAuth;
    LoginModel loginModel;
    TextView tvForgotTextLink, adminLoginTextLink;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarLogin);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginBtn);
        btnSignUp = findViewById(R.id.signInBtn);
        tvForgotTextLink = findViewById(R.id.forgotPassword);
        adminLoginTextLink = findViewById(R.id.adminLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginModel = new LoginModel(etEmail.getText().toString(),
                                            etPassword.getText().toString());

                if(verifyLoginModel(loginModel)) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(loginModel.getEmail(), loginModel.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Successfully logged in",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        tvForgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

            }
        });

        adminLoginTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


    private boolean verifyLoginModel(LoginModel loginModel){
        if (loginModel.getEmail().isEmpty()) {
            etEmail.setError("Email is Required.");
            return false;
        }

        if (loginModel.getPassword().isEmpty()) {
            etPassword.setError("Password is Required.");
            return false;
        }

        if (loginModel.getPassword().length() < 6) {
            etPassword.setError("Password Must be >= 6 Characters");
            return false;
        } else {
            return true;
        }
    }
}