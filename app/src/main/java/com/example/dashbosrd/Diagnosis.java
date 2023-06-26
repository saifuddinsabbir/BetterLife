package com.example.dashbosrd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Diagnosis extends AppCompatActivity {

    TextInputLayout chefComplaintsInputLayout, clinicalFindingsInputLayout, clinicalDiagnosisInputLayout,
            examinationInputLayout, investigationInputLayout, treatmentPlanInputLayout, adviceInputLayout,
            notesInputLayout;

    Button generatePDFBtnDiagnosis;

    String appointmentKey;

    DatabaseReference referenceAppointment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        chefComplaintsInputLayout = findViewById(R.id.chefComplaintsInputLayoutId);
        clinicalFindingsInputLayout = findViewById(R.id.clinicalFindingsInputLayoutId);
        clinicalDiagnosisInputLayout = findViewById(R.id.clinicalDiagnosisInputLayoutId);
        examinationInputLayout = findViewById(R.id.examinationInputLayoutId);
        investigationInputLayout = findViewById(R.id.investigationInputLayoutId);
        treatmentPlanInputLayout = findViewById(R.id.treatmentPlanInputLayoutId);
        adviceInputLayout = findViewById(R.id.adviceInputLayoutId);
        notesInputLayout = findViewById(R.id.notesInputLayoutId);
        generatePDFBtnDiagnosis = findViewById(R.id.generatePDFBtnDiagnosis);

        appointmentKey = getIntent().getStringExtra("appointmentKey");

        generatePDFBtnDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataIntoDatabase();
            }
        });
    }

    private void setDataIntoDatabase() {
        referenceAppointment2 = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentKey);

        referenceAppointment2.child("chiefComplaints").setValue(chefComplaintsInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("clinicalFindings").setValue(clinicalFindingsInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("clinicalDiagnosis").setValue(clinicalDiagnosisInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("examination").setValue(examinationInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("investigation").setValue(investigationInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("treatmentPlan").setValue(treatmentPlanInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("advice").setValue(adviceInputLayout.getEditText().getText().toString());
        referenceAppointment2.child("notes").setValue(notesInputLayout.getEditText().getText().toString());

        referenceAppointment2.child("prescribed").setValue("true");
    }
}