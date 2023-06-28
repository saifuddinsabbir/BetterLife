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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientInfo extends AppCompatActivity {

    ImageButton patientInfoNextBtn;

    TextInputLayout patientNameInputLayout, ageInputLayout, genderInputLayout, bloodInputLayout, weightInputLayout,
            heightInputLayout, emailPatientInputLayout, contactPatientInputLayout, appIDInputLayout, dateInputLayout;

    String appointmentKey, userName;;

    DatabaseReference referenceAppointment, referenceAppointment2, referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        patientNameInputLayout = findViewById(R.id.patientNameInputLayoutId);
        ageInputLayout = findViewById(R.id.ageInputLayoutId);
        genderInputLayout = findViewById(R.id.genderInputLayoutId);
        bloodInputLayout = findViewById(R.id.bloodInputLayoutId);
        weightInputLayout = findViewById(R.id.weightInputLayoutId);
        heightInputLayout = findViewById(R.id.heightInputLayoutId);
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
                setDataIntoDatabase();
                Intent intent = new Intent(PatientInfo.this, Diagnosis.class);
                intent.putExtra("appointmentKey", appointmentKey);
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
                    String schedule = snapshot.child("schedule").getValue(String.class);

                    ageInputLayout.getEditText().setText(snapshot.child("age").getValue(String.class));
                    weightInputLayout.getEditText().setText(snapshot.child("weight").getValue(String.class));
                    heightInputLayout.getEditText().setText(snapshot.child("height").getValue(String.class));
                    appIDInputLayout.getEditText().setText(snapshot.child("serialNo").getValue(String.class));

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    dateInputLayout.getEditText().setText(formattedDate);


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


    private void setDataIntoDatabase() {
        referenceAppointment2 = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentKey);

        referenceAppointment2.child("patientName").setValue(patientNameInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("age").setValue(ageInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("gender").setValue(genderInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("blood").setValue(bloodInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("weight").setValue(weightInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("height").setValue(heightInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("patientEmail").setValue(emailPatientInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("patientContact").setValue(contactPatientInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("appDate").setValue(dateInputLayout.getEditText().getText().toString());
    }
}