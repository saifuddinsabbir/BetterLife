package com.example.dashbosrd;

import static org.checkerframework.checker.units.UnitsTools.min;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Consultation extends AppCompatActivity {

    Dialog popupRegister;
    AutoCompleteTextView formDistrictAutoCom, formUpazilaAutoCom, formScheduleAutoCom;

    RadioGroup genderRadioGroup = null;
    RadioButton selectedRadioButton, maleRadioButton = null, femaleRadioButton = null;

    ImageView formCancelButton;
    TextInputLayout formFullNameInputLayout, formUserNameInputLayout, fromPhoneNoInputLayout, formGenderInputLayout,
            formDateOfBirthInputLayer, formBloodGroupInputLayout, formAppointmentNoInputLayout, profileUpazilaInputLayout,
            formScheduleInputLayout;
    TextInputEditText dateOfBirthEditText, formAppointmentNoET;
    DatePickerDialog.OnDateSetListener setListener;
    Button popupSubmitButton;

    String fullNameFromDB, userNameFromDB, emailFromDB, phoneNoFromDB, dateOfBirthFromDB, genderFromDB = "", bloodGroupFromDB, addressFromDB, passwordFromDB;

    Button registrationButton;

    LottieAnimationView noInternetLottieAnimation;

    Spinner spinner, spinner2;
    DatabaseReference referenceLocation;
    ArrayList<String> spinnerList, spinnerList2, spinnerList3;
    ArrayAdapter<String> adapter, adapter2, adapter3;

    String userNameGlobal;
    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    String speciality, doctor;
    TextView resultFromSpinner1;

    DatabaseReference referenceAppointment;
    DatabaseReference doctorReference;

    RecyclerView appointmentListRecycleView;
    AppointmentAdapter appointmentAdapter;

    LottieAnimationView nothingPrescribeLottieAnimation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

        nothingPrescribeLottieAnimation = findViewById(R.id.nothingPrescribeLottieAnimationId);
        appointmentListRecycleView = findViewById(R.id.appointmentListRecycleViewId);
        appointmentListRecycleView.setLayoutManager(new LinearLayoutManager(Consultation.this));
        appointmentListRecycleView.setHasFixedSize(true);

        referenceLocation = FirebaseDatabase.getInstance().getReference("location");
        registrationButton = findViewById(R.id.registrationButtonId);

        iniPopup();

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupRegister.show();
            }
        });

    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAppointments();
    }

    private void loadAppointments() {
        doctorReference = FirebaseDatabase.getInstance().getReference("appointments");

        doctorReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> appointmentsList = new ArrayList<>();
                for (DataSnapshot appointmentSnap : snapshot.getChildren()) {
                    String userName = appointmentSnap.child("userName").getValue(String.class);

//                    Toast.makeText(Medicine.this, doctor + " " + doctorName, Toast.LENGTH_SHORT).show();

                    if (userName.equals(userNameGlobal)) {
                        Appointment appointment = appointmentSnap.getValue(Appointment.class);
                        appointmentsList.add(appointment);
                    }
                }

                if (!appointmentsList.isEmpty()) {
                    nothingPrescribeLottieAnimation.setVisibility(View.INVISIBLE);
                } else {
                    nothingPrescribeLottieAnimation.setVisibility(View.VISIBLE);
                }

                appointmentAdapter = new AppointmentAdapter(Consultation.this, appointmentsList);
                appointmentListRecycleView.setAdapter(appointmentAdapter);
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void iniPopup() {
        popupRegister = new Dialog(this);
        popupRegister.setContentView(R.layout.popup_register);
        popupRegister.getWindow().setWindowAnimations(R.style.animation);
        popupRegister.getWindow().setBackgroundDrawable(getDrawable(R.drawable.popup_background));
        popupRegister.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupRegister.getWindow().getAttributes().gravity = Gravity.CENTER;
        popupRegister.setCancelable(false);

        formCancelButton = popupRegister.findViewById(R.id.writeFeedbakCancelButtonId);
        noInternetLottieAnimation = popupRegister.findViewById(R.id.noInternetLottieAnimationId);
        formUserNameInputLayout = popupRegister.findViewById(R.id.formUserNameInputLayoutId);
        formFullNameInputLayout = popupRegister.findViewById(R.id.formFullNameInputLayoutId);
        fromPhoneNoInputLayout = popupRegister.findViewById(R.id.fromPhoneNoInputLayoutId);
        formDateOfBirthInputLayer = popupRegister.findViewById(R.id.formDateOfBirthInputLayerId);
        formDistrictAutoCom = popupRegister.findViewById(R.id.formDistrictAutoComId);
        formUpazilaAutoCom = popupRegister.findViewById(R.id.formUpazilaAutoComId);
        formScheduleAutoCom = popupRegister.findViewById(R.id.formScheduleAutoComId);
        formAppointmentNoInputLayout = popupRegister.findViewById(R.id.formAppointmentNoInputLayoutId);
        formAppointmentNoET = popupRegister.findViewById(R.id.formAppointmentNoETID);
        popupSubmitButton = popupRegister.findViewById(R.id.popupSubmitButtonId);
        profileUpazilaInputLayout = popupRegister.findViewById(R.id.profileUpazilaInputLayoutId);
        formScheduleInputLayout = popupRegister.findViewById(R.id.formScheduleInputLayoutId);

        noInternetLottieAnimation.setVisibility(View.INVISIBLE);

        formCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formUserNameInputLayout.getEditText().setText("");
                formFullNameInputLayout.getEditText().setText("");
                fromPhoneNoInputLayout.getEditText().setText("");
                formDateOfBirthInputLayer.getEditText().setText("");
                formDistrictAutoCom.setText("");
                formUpazilaAutoCom.setText("");
                formScheduleAutoCom.setText("");
                formAppointmentNoET.setText("");
                popupRegister.dismiss();

                profileUpazilaInputLayout.setVisibility(View.GONE);
                formScheduleInputLayout.setVisibility(View.GONE);
                formAppointmentNoInputLayout.setVisibility(View.GONE);
                formUserNameInputLayout.setVisibility(View.GONE);
                formFullNameInputLayout.setVisibility(View.GONE);
                formDateOfBirthInputLayer.setVisibility(View.GONE);
                fromPhoneNoInputLayout.setVisibility(View.GONE);
                popupSubmitButton.setVisibility(View.GONE);
            }
        });

        popupSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternet
                try {
                    String command = "ping -c 1 google.com";
                    if (Runtime.getRuntime().exec(command).waitFor() != 0) {
                        noInternetLottieAnimation.setVisibility(View.VISIBLE);
                    } else {

                        String speciality = formDistrictAutoCom.getText().toString();
                        String doctor = formUpazilaAutoCom.getText().toString();
                        String schedule = formScheduleAutoCom.getText().toString();
                        String serialNo = formAppointmentNoET.getText().toString();
                        String userName = formUserNameInputLayout.getEditText().getText().toString();
                        String fullName = formFullNameInputLayout.getEditText().getText().toString();
                        String phoneNo = fromPhoneNoInputLayout.getEditText().getText().toString();
                        String age = formDateOfBirthInputLayer.getEditText().getText().toString();

                        Appointment appointment = new Appointment(speciality, doctor, schedule, serialNo, userName, age);

                        referenceAppointment = FirebaseDatabase.getInstance().getReference("appointments").push();

                        String key = referenceAppointment.getKey();
                        appointment.setAppointmentKey(key);

                        referenceAppointment.setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Consultation.this, "Appointment registered successfully", Toast.LENGTH_SHORT).show();
                                formUserNameInputLayout.getEditText().setText("");
                                formFullNameInputLayout.getEditText().setText("");
                                fromPhoneNoInputLayout.getEditText().setText("");
                                formDateOfBirthInputLayer.getEditText().setText("");
                                formDistrictAutoCom.setText("");
                                formUpazilaAutoCom.setText("");
                                formScheduleAutoCom.setText("");
                                formAppointmentNoET.setText("");
                                popupRegister.dismiss();

                                profileUpazilaInputLayout.setVisibility(View.GONE);
                                formScheduleInputLayout.setVisibility(View.GONE);
                                formAppointmentNoInputLayout.setVisibility(View.GONE);
                                formUserNameInputLayout.setVisibility(View.GONE);
                                formFullNameInputLayout.setVisibility(View.GONE);
                                formDateOfBirthInputLayer.setVisibility(View.GONE);
                                fromPhoneNoInputLayout.setVisibility(View.GONE);
                                popupSubmitButton.setVisibility(View.GONE);

                                final AlertDialog.Builder builder = new AlertDialog.Builder(Consultation.this);

                                builder.setMessage("Your appointment is added successfully!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Consultation.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                        referenceAppointment.child("prescribed").setValue("false");

                    }
                } catch (Exception e) {
                    Toast.makeText(Consultation.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        formUserNameInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Toast.makeText(Consultation.this, "Hi", Toast.LENGTH_SHORT).show();

                DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users");
                String typedUserName = formUserNameInputLayout.getEditText().getText().toString();

                //Toast.makeText(Consultation.this, typedUserName, Toast.LENGTH_SHORT).show();

                mData.orderByChild("userName").equalTo(typedUserName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            formFullNameInputLayout.setVisibility(View.VISIBLE);
                            formDateOfBirthInputLayer.setVisibility(View.VISIBLE);
                            fromPhoneNoInputLayout.setVisibility(View.VISIBLE);
                            popupSubmitButton.setVisibility(View.VISIBLE);

                            fullNameFromDB = snapshot.child(typedUserName).child("fullName").getValue(String.class);
                            userNameFromDB = snapshot.child(typedUserName).child("userName").getValue(String.class);
                            emailFromDB = snapshot.child(typedUserName).child("email").getValue(String.class);
                            phoneNoFromDB = snapshot.child(typedUserName).child("phoneNo").getValue(String.class);
                            dateOfBirthFromDB = snapshot.child(typedUserName).child("dateOfBirth").getValue(String.class);
                            genderFromDB = snapshot.child(typedUserName).child("gender").getValue(String.class);
                            bloodGroupFromDB = snapshot.child(typedUserName).child("bloodGroup").getValue(String.class);
//                            addressFromDB = snapshot.child(typedUserName).child("address").getValue(String.class);

                            if (dateOfBirthFromDB.equals("Empty") || genderFromDB.equals("Empty") || bloodGroupFromDB.equals("Empty")) {

                                final AlertDialog.Builder builder = new AlertDialog.Builder(Consultation.this);

                                builder.setMessage("Please, complete your profile first!").setCancelable(false).setPositiveButton("Edit Profile", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Consultation.this, UserProfile.class));
                                    }
                                }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        popupRegister.dismiss();
                                    }
                                });
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();



                            } else {
                                formFullNameInputLayout.getEditText().setText(fullNameFromDB);
                                fromPhoneNoInputLayout.getEditText().setText(phoneNoFromDB);


                                String substr = dateOfBirthFromDB.substring(dateOfBirthFromDB.length() - 4, dateOfBirthFromDB.length());
                                Calendar cal = Calendar.getInstance();
                                int year = cal.get(Calendar.YEAR);

                                formDateOfBirthInputLayer.getEditText().setText(String.valueOf(year - Integer.parseInt(substr) + 1));
                                //Toast.makeText(Consultation.this, String.valueOf(year - Integer.parseInt(substr) + 1) , Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            formFullNameInputLayout.setVisibility(View.GONE);
                            formDateOfBirthInputLayer.setVisibility(View.GONE);
                            fromPhoneNoInputLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });


        genderRadioGroup = findViewById(R.id.genderRadioGroupId);
        maleRadioButton = findViewById(R.id.maleId);
        femaleRadioButton = findViewById(R.id.femaleId);

        spinnerList = new ArrayList<>();
        spinnerList2 = new ArrayList<>();
        spinnerList3 = new ArrayList<>();

        adapter = new ArrayAdapter<String>(Consultation.this, R.layout.drop_down_item, spinnerList);
        adapter2 = new ArrayAdapter<String>(Consultation.this, R.layout.drop_down_item, spinnerList2);
        adapter3 = new ArrayAdapter<String>(Consultation.this, R.layout.drop_down_item, spinnerList3);

        formDistrictAutoCom.setAdapter(adapter);
        formUpazilaAutoCom.setAdapter(adapter2);
        formScheduleAutoCom.setAdapter(adapter3);

        showSchedule();
        showData();

        formDistrictAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerList2.clear();
                speciality = formDistrictAutoCom.getText().toString();
                showSecondList();
                profileUpazilaInputLayout.setVisibility(View.VISIBLE);
                formScheduleInputLayout.setVisibility(View.GONE);
                formAppointmentNoInputLayout.setVisibility(View.GONE);
                formUserNameInputLayout.setVisibility(View.GONE);
                formFullNameInputLayout.setVisibility(View.GONE);
                formDateOfBirthInputLayer.setVisibility(View.GONE);
                fromPhoneNoInputLayout.setVisibility(View.GONE);
                popupSubmitButton.setVisibility(View.GONE);
            }
        });

        formUpazilaAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doctor = formUpazilaAutoCom.getText().toString();
                formScheduleInputLayout.setVisibility(View.VISIBLE);
                formAppointmentNoInputLayout.setVisibility(View.GONE);
                formUserNameInputLayout.setVisibility(View.GONE);
                formFullNameInputLayout.setVisibility(View.GONE);
                formDateOfBirthInputLayer.setVisibility(View.GONE);
                fromPhoneNoInputLayout.setVisibility(View.GONE);
                popupSubmitButton.setVisibility(View.GONE);
                //Toast.makeText(Consultation.this, speciality + " " + , Toast.LENGTH_SHORT).show();
            }
        });

        formScheduleAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!speciality.isEmpty() && !doctor.isEmpty()) {
                    formAppointmentNoInputLayout.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int randomNumber = r.nextInt(50);
                    formAppointmentNoET.setText(String.valueOf(randomNumber));
                    formUserNameInputLayout.setVisibility(View.VISIBLE);
                    formFullNameInputLayout.setVisibility(View.GONE);
                    formDateOfBirthInputLayer.setVisibility(View.GONE);
                    fromPhoneNoInputLayout.setVisibility(View.GONE);
                    popupSubmitButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Consultation.this, "Please select Speciality and Doctor first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSchedule() {
        referenceLocation.child("schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (!item.getValue().toString().equals("schedule")) {
                        spinnerList3.add(item.getValue().toString());
                    }
                }
                adapter3.notifyDataSetChanged();
                ;
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
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}