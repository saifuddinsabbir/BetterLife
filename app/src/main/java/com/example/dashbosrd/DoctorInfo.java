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

public class DoctorInfo extends AppCompatActivity {

    ImageButton doctorInfoNextBtn;

    TextInputLayout doctorNameInputLayout, specialityInputLayout, qualificationInputLayout, registrationInputLayout,
            timeInputLayout, EmailInputLayout, contactInputLayout, otherInputLayout;

    DatabaseReference referenceAppointment, referenceAppointment2;

    String appointmentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        doctorNameInputLayout = findViewById(R.id.doctorNameInputLayoutId);
        specialityInputLayout = findViewById(R.id.specialityInputLayoutId);
        qualificationInputLayout = findViewById(R.id.qualificationInputLayoutId);
        registrationInputLayout = findViewById(R.id.registrationInputLayoutId);
        timeInputLayout = findViewById(R.id.timeInputLayoutId);
        EmailInputLayout = findViewById(R.id.EmailInputLayoutId);
        contactInputLayout = findViewById(R.id.contactInputLayoutId);
        otherInputLayout = findViewById(R.id.otherInputLayoutId);
        doctorInfoNextBtn = findViewById(R.id.doctorInfoNextBtn);

        appointmentKey = getIntent().getStringExtra("appointmentKey");

        loadFromDB();

        doctorInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataIntoDatabase();
                Intent intent = new Intent(DoctorInfo.this, PatientInfo.class);
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
                    String doctorName = snapshot.child("doctor").getValue(String.class);
                    String speciality = snapshot.child("speciality").getValue(String.class);

                    doctorNameInputLayout.getEditText().setText(doctorName);
                    specialityInputLayout.getEditText().setText(speciality);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDataIntoDatabase() {
        referenceAppointment2 = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentKey);

        referenceAppointment2.child("qualification").setValue(qualificationInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("regNo").setValue(registrationInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("doctorDayTime").setValue(timeInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("doctorEmail").setValue(EmailInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("doctorContact").setValue(contactInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("otherDocDetails").setValue(otherInputLayout.getEditText().getText().toString());

        referenceAppointment2.child("prescribed").setValue("onProgress");
    }
}