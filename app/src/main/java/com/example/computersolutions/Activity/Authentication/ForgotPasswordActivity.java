package com.example.computersolutions.Activity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.computersolutions.Model.ForgotPasswordModel;
import com.example.computersolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etEmail;
    FirebaseAuth firebaseAuth;
    Button sendLink, backLogin;
    ProgressBar progressBar;
    ForgotPasswordModel forgotPasswordModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.reset);
        progressBar = findViewById(R.id.progressBarForgotPassword);
        backLogin = findViewById(R.id.backLoginBtn);
        sendLink = findViewById(R.id.sendLinkBtn);
        progressBar = findViewById(R.id.progressBarForgotPassword);

        forgotPasswordModel = new ForgotPasswordModel(etEmail.getText().toString());

        if(verifyForgotPasswordModel(forgotPasswordModel)) {
            sendLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(forgotPasswordModel.getEmail())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Request has been sent", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Error resetting password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
    private boolean verifyForgotPasswordModel(ForgotPasswordModel forgotPasswordModel) {
        if (forgotPasswordModel.getEmail().isEmpty()) {
            etEmail.setError("Email is Required.");
            return false;
        } else {
            return true;
        }
    }

}