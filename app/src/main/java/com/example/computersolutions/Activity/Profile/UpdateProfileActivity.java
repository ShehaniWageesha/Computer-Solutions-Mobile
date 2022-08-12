package com.example.computersolutions.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.computersolutions.Model.PaymentModel;
import com.example.computersolutions.Model.UpdateUserModel;
import com.example.computersolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName, etEmail, etCard, etAddress, etPostal;
    Button btnUpdate, btnBack;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    UpdateUserModel updateUserModel;
    PaymentModel paymentModel;
    DatabaseReference databaseReference;
    UserProfileChangeRequest userProfileChangeRequest;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Payment");

        progressBar = findViewById(R.id.progressBarUpdateProfile);
        etName = findViewById(R.id.editTextUpdateProfileUserName);
        etEmail = findViewById(R.id.editTextUpdateProfileEmail);
        etCard = findViewById(R.id.editTextUpdateProfileCardNumber);
        etAddress = findViewById(R.id.editTextUpdateProfilePostalAddress);
        etPostal = findViewById(R.id.editTextUpdateProfilePostalCode);
        btnUpdate = findViewById(R.id.buttonUpdateProfileUpdate);
        btnBack = findViewById(R.id.buttonUpdateProfileBack);

        etName.setText(firebaseUser.getDisplayName());
        etEmail.setText(firebaseUser.getEmail());
        paymentModel = new PaymentModel();

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);

                if(dataSnapshot.child("paymentType").getValue() != null){
                    paymentModel.setPaymentType(dataSnapshot.child("paymentType").getValue().toString());
                } else {
                    paymentModel.setPaymentType(null);
                }

                if(dataSnapshot.child("expirationDate").getValue() != null){
                    paymentModel.setExpirationDate(dataSnapshot.child("expirationDate").getValue().toString());
                } else {
                    paymentModel.setExpirationDate(null);
                }

                if(dataSnapshot.child("cardNumber").getValue() != null){
                    etCard.setText(dataSnapshot.child("cardNumber").getValue().toString());
                } else {
                    etCard.setText(R.string.nullValue);
                }

                if(dataSnapshot.child("billingAddress").getValue() != null){
                    etAddress.setText(dataSnapshot.child("billingAddress").getValue().toString());
                } else {
                    etAddress.setText(R.string.nullValue);
                }

                if(dataSnapshot.child("postalCode").getValue() != null){
                    etPostal.setText(dataSnapshot.child("postalCode").getValue().toString());
                } else {
                    etPostal.setText(R.string.nullValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error in getting data",Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfileActivity.this, UserProfileActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserModel = new UpdateUserModel(etName.getText().toString(),
                        etEmail.getText().toString(),
                        etCard.getText().toString(),
                        etAddress.getText().toString(),
                        etPostal.getText().toString());

                paymentModel.setBillingAddress(updateUserModel.getAddress());
                paymentModel.setCardNumber(updateUserModel.getCard());
                paymentModel.setPostalCode(updateUserModel.getPostal());

                if(verifyUserProfileModel(updateUserModel)){
                    progressBar.setVisibility(View.VISIBLE);

                    userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(updateUserModel.getName())
                            .build();

                    firebaseUser.updateProfile(userProfileChangeRequest)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Information Updated Successfully",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UpdateProfileActivity.this, UserProfileActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Error Updating Information",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.child(firebaseUser.getUid()).setValue(paymentModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Information Updated Successfully",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Error Updating Information",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private boolean verifyUserProfileModel(UpdateUserModel updateUserModel){
        if (updateUserModel.getName().isEmpty()) {
            etName.setError("Name is Required.");
            return false;
        }

        if (updateUserModel.getEmail().isEmpty()) {
            etEmail.setError("Email is Required.");
            return false;
        }

        if (updateUserModel.getCard().isEmpty()) {
            etCard.setError("Card is Required.");
            return false;
        }

        if (updateUserModel.getAddress().isEmpty()) {
            etAddress.setError("Address is required");
            return false;
        }

        if (updateUserModel.getPostal().isEmpty()) {
            etPostal.setError("Postal Code is Required.");
            return false;
        } else {
            return true;
        }
    }
}