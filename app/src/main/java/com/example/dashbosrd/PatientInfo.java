package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientInfo extends AppCompatActivity {

    ImageButton patientInfoNextBtn;

    TextInputLayout patientNameInputLayout, ageInputLayout, genderInputLayout, bloodInputLayout,
            emailPatientInputLayout, contactPatientInputLayout, appIDInputLayout, dateInputLayout;

    String appointmentKey, userName;;

    DatabaseReference referenceAppointment, referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        patientNameInputLayout = findViewById(R.id.patientNameInputLayoutId);
        ageInputLayout = findViewById(R.id.ageInputLayoutId);
        genderInputLayout = findViewById(R.id.genderInputLayoutId);
        bloodInputLayout = findViewById(R.id.bloodInputLayoutId);
        emailPatientInputLayout = findViewById(R.id.emailPatientInputLayoutId);
        contactPatientInputLayout = findViewById(R.id.contactPatientInputLayoutId);
        appIDInputLayout = findViewById(R.id.appIDInputLayoutId);
        dateInputLayout = findViewById(R.id.dateInputLayoutId);
        patientInfoNextBtn = findViewById(R.id.patientInfoNextBtn);

        appointmentKey = getIntent().getStringExtra("appointmentKey");

        loadFromDB();

        patientInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInfo.this, Diagnosis.class);
                startActivity(intent);
            }
        });
    }

    private void loadFromDB() {
        referenceAppointment = FirebaseDatabase.getInstance().getReference("appointments");

        referenceAppointment.child(appointmentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String userName = snapshot.child("userName").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String serialNo = snapshot.child("serialNo").getValue(String.class);
                    String schedule = snapshot.child("schedule").getValue(String.class);

                    ageInputLayout.getEditText().setText(age);
                    appIDInputLayout.getEditText().setText(serialNo);
                    dateInputLayout.getEditText().setText(schedule);

                    Toast.makeText(PatientInfo.this, userName, Toast.LENGTH_SHORT).show();
                    referenceUser = FirebaseDatabase.getInstance().getReference("users");

                    referenceUser.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String fullName = snapshot.child("fullName").getValue(String.class);
                                String gender = snapshot.child("gender").getValue(String.class);
                                String blood = snapshot.child("bloodGroup").getValue(String.class);
                                String email = snapshot.child("email").getValue(String.class);
                                String phoneNo = snapshot.child("phoneNo").getValue(String.class);

                                patientNameInputLayout.getEditText().setText(fullName);
                                genderInputLayout.getEditText().setText(gender);
                                bloodInputLayout.getEditText().setText(blood);
                                emailPatientInputLayout.getEditText().setText(email);
                                contactPatientInputLayout.getEditText().setText(phoneNo);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}