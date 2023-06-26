package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Prescription extends AppCompatActivity {

    TextView doctorNameTex, qualificationTex, specialityTextview, doctorContactTex, doctorEmailTex,
            serialNoTex, dateTex, patientNameTextView, ageTextView, genderTex, bloodTex, weightTex, heightTex,
            chiefProblemTex, clinicalFindingsTex, clinicalDiagnosisTex, examinationTex, treatmentPlanTex, adviceTex,
            medicineTex;

    String appointmentKey;

    DatabaseReference referenceAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        doctorNameTex = findViewById(R.id.doctorNameTex);
        qualificationTex = findViewById(R.id.qualificationTex);
        specialityTextview = findViewById(R.id.specialityTextview);
        doctorContactTex = findViewById(R.id.doctorContactTex);
        doctorEmailTex = findViewById(R.id.doctorEmailTex);
        serialNoTex = findViewById(R.id.serialNoTex);
        dateTex = findViewById(R.id.dateTex);
        patientNameTextView = findViewById(R.id.patientNameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        genderTex = findViewById(R.id.genderTex);
        bloodTex = findViewById(R.id.bloodTex);
        weightTex = findViewById(R.id.weightTex);
        heightTex = findViewById(R.id.heightTex);
        chiefProblemTex = findViewById(R.id.chiefProblemTex);
        clinicalFindingsTex = findViewById(R.id.clinicalFindingsTex);
        clinicalDiagnosisTex = findViewById(R.id.clinicalDiagnosisTex);
        examinationTex = findViewById(R.id.examinationTex);
        treatmentPlanTex = findViewById(R.id.treatmentPlanTex);
        adviceTex = findViewById(R.id.adviceTex);
        medicineTex = findViewById(R.id.medicineTex);

        appointmentKey = getIntent().getStringExtra("appointmentKey");

        loadFromDB();
    }

    private void loadFromDB() {
        referenceAppointment = FirebaseDatabase.getInstance().getReference("appointments");

        referenceAppointment.child(appointmentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    doctorNameTex.setText(snapshot.child("doctor").getValue(String.class));
                    qualificationTex.setText(snapshot.child("qualification").getValue(String.class));
                    specialityTextview.setText(snapshot.child("speciality").getValue(String.class));
                    doctorContactTex.setText(snapshot.child("doctorContact").getValue(String.class));
                    doctorEmailTex.setText(snapshot.child("doctorEmail").getValue(String.class));
                    serialNoTex.setText(snapshot.child("serialNo").getValue(String.class));
                    dateTex.setText(snapshot.child("appDate").getValue(String.class));
                    patientNameTextView.setText(snapshot.child("patientName").getValue(String.class));
                    ageTextView.setText(snapshot.child("age").getValue(String.class));
                    genderTex.setText(snapshot.child("gender").getValue(String.class));
                    bloodTex.setText(snapshot.child("blood").getValue(String.class));
                    weightTex.setText(snapshot.child("weight").getValue(String.class));
                    heightTex.setText(snapshot.child("height").getValue(String.class));
                    chiefProblemTex.setText(snapshot.child("chiefComplaints").getValue(String.class));
                    clinicalFindingsTex.setText(snapshot.child("clinicalFindings").getValue(String.class));
                    clinicalDiagnosisTex.setText(snapshot.child("clinicalDiagnosis").getValue(String.class));
                    examinationTex.setText(snapshot.child("examination").getValue(String.class));
                    treatmentPlanTex.setText(snapshot.child("treatmentPlan").getValue(String.class));
                    adviceTex.setText(snapshot.child("advice").getValue(String.class));
                    medicineTex.setText(snapshot.child("investigation").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}