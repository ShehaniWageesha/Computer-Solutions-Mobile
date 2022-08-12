package com.example.computersolutions.Activity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.computersolutions.Activity.Profile.UserProfileActivity;
import com.example.computersolutions.Model.PaymentModel;
import com.example.computersolutions.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etCardNumber, etExpirationDate, etBillingAddress, etPostalCode;
    Button btnSignUp;
    Spinner spnPaymentType;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    PaymentModel paymentModel;
    ArrayAdapter<CharSequence> spinnerAdapter;
    String paymentTypeStr;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.progressBarPayment);
        spnPaymentType = findViewById(R.id.paymentMethod);
        etCardNumber = findViewById(R.id.cardNo);
        etExpirationDate = findViewById(R.id.expireDate);
        etBillingAddress = findViewById(R.id.address);
        etPostalCode = findViewById(R.id.postalCode);
        btnSignUp = findViewById(R.id.btnSignUp);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.paymentTypes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentType.setAdapter(spinnerAdapter);
        spnPaymentType.setOnItemSelectedListener(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentModel = new PaymentModel(paymentTypeStr,
                        etCardNumber.getText().toString(),
                        etExpirationDate.getText().toString(),
                        etBillingAddress.getText().toString(),
                        etPostalCode.getText().toString());
                if (verifyPaymentModel(paymentModel)) {
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.child("Payment").child(firebaseUser.getUid()).setValue(paymentModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Payment Details Added",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PaymentActivity.this, UserProfileActivity.class));
                                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Cannot Update Details",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    private boolean verifyPaymentModel(PaymentModel paymentModel){
        if (paymentModel.getCardNumber().isEmpty()) {
            etCardNumber.setError("Account Number is Required.");
            return false;
        }

        if (paymentModel.getExpirationDate().isEmpty()) {
            etExpirationDate.setError("Expiration date is Required.");
            return false;
        }

        if (paymentModel.getBillingAddress().isEmpty()) {
            etBillingAddress.setError("Address is Required.");
            return false;
        }

        if (paymentModel.getPostalCode().isEmpty()) {
            etPostalCode.setError("Postal Code is Required.");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        paymentTypeStr = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getApplicationContext(), paymentTypeStr, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        paymentTypeStr = adapterView.getItemAtPosition(1).toString();
    }
}