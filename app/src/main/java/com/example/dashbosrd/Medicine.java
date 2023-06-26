package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Medicine extends AppCompatActivity {

    TextInputEditText searchDoctorBox;
    ImageButton searchBtn;

    String doctorName;

    DatabaseReference doctorReference;

    RecyclerView appointmentListRecycleView;
    AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        searchDoctorBox = findViewById(R.id.searchDoctorBox);
        searchBtn = findViewById(R.id.searchBtn);

        appointmentListRecycleView = findViewById(R.id.appointmentListRecycleViewId);
        appointmentListRecycleView.setLayoutManager(new LinearLayoutManager(Medicine.this));
        appointmentListRecycleView.setHasFixedSize(true);

        searchDoctorBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doctorName = String.valueOf(s);
                loadAppointments();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorName = searchDoctorBox.getText().toString();
                loadAppointments();
            }
        });
    }

    private void loadAppointments() {


        doctorReference = FirebaseDatabase.getInstance().getReference("appointments");

        doctorReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> appointmentsList = new ArrayList<>();
                for (DataSnapshot appointmentSnap : snapshot.getChildren()) {
                    String doctor = appointmentSnap.child("doctor").getValue(String.class);

//                    Toast.makeText(Medicine.this, doctor + " " + doctorName, Toast.LENGTH_SHORT).show();

                    if(doctor.equals(doctorName)) {
                        Appointment appointment = appointmentSnap.getValue(Appointment.class);
                        appointmentsList.add(appointment);
                    }
                }

                appointmentAdapter = new AppointmentAdapter(Medicine.this, appointmentsList);
                appointmentListRecycleView.setAdapter(appointmentAdapter);
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}