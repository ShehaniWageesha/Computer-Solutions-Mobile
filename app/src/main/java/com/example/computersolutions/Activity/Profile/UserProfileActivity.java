package com.example.computersolutions.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computersolutions.Activity.Authentication.LoginActivity;
import com.example.computersolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    Button btnLogOut, btnDeleteAccount, btnUpdateProfile;
    TextView tvName, tvEmail, tvPaymentType, tvPayment, tvAddress, tvPostalCode;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Payment");
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvName = findViewById(R.id.tvUserProfileName);
        tvEmail = findViewById(R.id.tvUserProfileEmail);
        tvPaymentType = findViewById(R.id.tvUserProfilePaymentType);
        tvPayment = findViewById(R.id.tvUserProfilePayment);
        tvAddress = findViewById(R.id.tvUserProfileAddress);
        tvPostalCode = findViewById(R.id.tvUserProfilePostalCode);
        btnLogOut = findViewById(R.id.btnLogout);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        progressBar = findViewById(R.id.progressBarUserProfile);

        progressBar.setVisibility(View.VISIBLE);

        tvName.setText(firebaseUser.getDisplayName());
        tvEmail.setText(firebaseUser.getEmail());
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.child("paymentType").getValue() != null) {
                    tvPaymentType.setText(dataSnapshot.child("paymentType").getValue().toString());
                } else {
                    tvPaymentType.setText(R.string.nullValue);
                }

                if (dataSnapshot.child("cardNumber").getValue() != null) {
                    tvPayment.setText(dataSnapshot.child("cardNumber").getValue().toString());
                } else {
                    tvPayment.setText(R.string.nullValue);
                }

                if (dataSnapshot.child("billingAddress").getValue() != null) {
                    tvAddress.setText(dataSnapshot.child("billingAddress").getValue().toString());
                } else {
                    tvAddress.setText(R.string.nullValue);
                }

                if (dataSnapshot.child("postalCode").getValue() != null) {
                    tvPostalCode.setText(dataSnapshot.child("postalCode").getValue().toString());
                } else {
                    tvPostalCode.setText(R.string.nullValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error in getting Data", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, UpdateProfileActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child(firebaseUser.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Error in deleting payment details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error in deleting user", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }
        });
    }
}