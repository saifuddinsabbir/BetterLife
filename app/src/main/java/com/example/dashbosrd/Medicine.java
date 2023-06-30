package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

    AutoCompleteTextView formDistrictAutoCom, formUpazilaAutoCom;
    ArrayList<String> spinnerList, spinnerList2, spinnerList3;
    ArrayAdapter<String> adapter, adapter2, adapter3;
    String speciality, doctor;

    String doctorName, searchText;

    DatabaseReference doctorReference;
    DatabaseReference referenceLocation;

    RecyclerView appointmentListRecycleView;
    AppointmentAdapter appointmentAdapter;

    LottieAnimationView nothingPrescribeLottieAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        searchDoctorBox = findViewById(R.id.searchDoctorBox);
        searchBtn = findViewById(R.id.searchBtn);
        formDistrictAutoCom = findViewById(R.id.formDistrictAutoComId);
        formUpazilaAutoCom = findViewById(R.id.formUpazilaAutoComId);
        nothingPrescribeLottieAnimation = findViewById(R.id.nothingPrescribeLottieAnimationId);

        appointmentListRecycleView = findViewById(R.id.appointmentListRecycleViewId);
        appointmentListRecycleView.setLayoutManager(new LinearLayoutManager(Medicine.this));
        appointmentListRecycleView.setHasFixedSize(true);

        spinnerList = new ArrayList<>();
        spinnerList2 = new ArrayList<>();
        spinnerList3 = new ArrayList<>();

        adapter = new ArrayAdapter<String>(Medicine.this, R.layout.drop_down_item, spinnerList);
        adapter2 = new ArrayAdapter<String>(Medicine.this, R.layout.drop_down_item, spinnerList2);
        adapter3 = new ArrayAdapter<String>(Medicine.this, R.layout.drop_down_item, spinnerList3);

        formDistrictAutoCom.setAdapter(adapter);
        formUpazilaAutoCom.setAdapter(adapter2);

        referenceLocation = FirebaseDatabase.getInstance().getReference("location");

        showData();

        formDistrictAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerList2.clear();
                speciality = formDistrictAutoCom.getText().toString();
                showSecondList();
            }
        });

        formUpazilaAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doctor = formUpazilaAutoCom.getText().toString();

                searchText = doctor.toLowerCase();
                loadAppointments();
//                Toast.makeText(Medicine.this, speciality + " " + doctor , Toast.LENGTH_SHORT).show();
            }
        });

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

                if (doctorName.length() >= 4) {
                    if(!((doctorName.substring(0, 3)).toLowerCase()).equals("dr.")) {
                        searchText = "dr. " + doctorName.toLowerCase();
                    } else {
                        searchText = doctorName.toLowerCase();
                    }
                }
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

                    if ((doctor.toLowerCase()).equals(searchText)) {
                        Appointment appointment = appointmentSnap.getValue(Appointment.class);
                        appointmentsList.add(appointment);
                    }

//                    Toast.makeText(Medicine.this, doctor.toLowerCase() + " " + searchText, Toast.LENGTH_SHORT).show();
                }

                if (!appointmentsList.isEmpty()) {
                    nothingPrescribeLottieAnimation.setVisibility(View.INVISIBLE);
                } else {
                    nothingPrescribeLottieAnimation.setVisibility(View.VISIBLE);
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

    private void showData() {
        referenceLocation.child("doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (!item.getValue().toString().equals("doctors")) {
                        spinnerList.add(item.getValue().toString());
                    }
                }
                adapter.notifyDataSetChanged();
                ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSecondList() {
        referenceLocation.child(speciality).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    spinnerList2.add(item.getValue().toString());
                }
                adapter2.notifyDataSetChanged();
                ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}