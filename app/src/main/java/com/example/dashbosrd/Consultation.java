package com.example.dashbosrd;

import static org.checkerframework.checker.units.UnitsTools.min;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Consultation extends AppCompatActivity {

    Dialog popupRegister;
    AutoCompleteTextView formDistrictAutoCom, formUpazilaAutoCom, formScheduleAutoCom;

    RadioGroup genderRadioGroup = null;
    RadioButton selectedRadioButton, maleRadioButton = null, femaleRadioButton = null;

    ImageView formCancelButton;
    TextInputLayout formFullNameInputLayout, formUserNameInputLayout, fromPhoneNoInputLayout, formGenderInputLayout,
            formDateOfBirthInputLayer, formBloodGroupInputLayout, formAppointmentNoInputLayout;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

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

    private void iniPopup() {
        popupRegister = new Dialog(this);
        popupRegister.setContentView(R.layout.popup_register);
        popupRegister.getWindow().setWindowAnimations(R.style.animation );
        popupRegister.getWindow().setBackgroundDrawable(getDrawable(R.drawable.popup_background));
        popupRegister.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupRegister.getWindow().getAttributes().gravity = Gravity.CENTER;
        popupRegister.setCancelable(false);

        formCancelButton = popupRegister.findViewById(R.id.writeFeedbakCancelButtonId);
        noInternetLottieAnimation = popupRegister.findViewById(R.id.noInternetLottieAnimationId);
        formUserNameInputLayout = popupRegister.findViewById(R.id.formUserNameInputLayoutId);
        formFullNameInputLayout = popupRegister.findViewById(R.id.formFullNameInputLayoutId);
        fromPhoneNoInputLayout = popupRegister.findViewById(R.id.fromPhoneNoInputLayoutId);
//        formGenderInputLayout = popupRegister.findViewById(R.id.formGenderInputLayoutId);
        formDateOfBirthInputLayer = popupRegister.findViewById(R.id.formDateOfBirthInputLayerId);
//        formBloodGroupInputLayout = popupRegister.findViewById(R.id.formBloodGroupInputLayoutId);
        formDistrictAutoCom = popupRegister.findViewById(R.id.formDistrictAutoComId);
        formUpazilaAutoCom = popupRegister.findViewById(R.id.formUpazilaAutoComId);
        formScheduleAutoCom = popupRegister.findViewById(R.id.formScheduleAutoComId);
        formAppointmentNoInputLayout = popupRegister.findViewById(R.id.formAppointmentNoInputLayoutId);
        formAppointmentNoET= popupRegister.findViewById(R.id.formAppointmentNoETID);
        popupSubmitButton = popupRegister.findViewById(R.id.popupSubmitButtonId);

        noInternetLottieAnimation.setVisibility(View.INVISIBLE);

        formCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formUserNameInputLayout.getEditText().setText("");
                formFullNameInputLayout.getEditText().setText("");
                fromPhoneNoInputLayout.getEditText().setText("");
//                formGenderInputLayout.getEditText().setText("");
                formDateOfBirthInputLayer.getEditText().setText("");
//                formBloodGroupInputLayout.getEditText().setText("");
                formDistrictAutoCom.setText("");
                formUpazilaAutoCom.setText("");
                formScheduleAutoCom.setText("");
                formAppointmentNoET.setText("");
                popupRegister.dismiss();
            }
        });

        popupSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternet
                try {
                    String command = "ping -c 1 google.com";
                    if(Runtime.getRuntime().exec(command).waitFor() != 0) {
                        noInternetLottieAnimation.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(Consultation.this, "Appointment registered successfully", Toast.LENGTH_SHORT).show();
                        formUserNameInputLayout.getEditText().setText("");
                        formFullNameInputLayout.getEditText().setText("");
                        fromPhoneNoInputLayout.getEditText().setText("");
//                formGenderInputLayout.getEditText().setText("");
                        formDateOfBirthInputLayer.getEditText().setText("");
//                formBloodGroupInputLayout.getEditText().setText("");
                        formDistrictAutoCom.setText("");
                        formUpazilaAutoCom.setText("");
                        formScheduleAutoCom.setText("");
                        formAppointmentNoET.setText("");
                        popupRegister.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(Consultation.this, "Please connect to INTERNET", Toast.LENGTH_SHORT).show();
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

                            fullNameFromDB = snapshot.child(typedUserName).child("fullName").getValue(String.class);
                            userNameFromDB = snapshot.child(typedUserName).child("userName").getValue(String.class);
                            emailFromDB = snapshot.child(typedUserName).child("email").getValue(String.class);
                            phoneNoFromDB = snapshot.child(typedUserName).child("phoneNo").getValue(String.class);
                            dateOfBirthFromDB = snapshot.child(typedUserName).child("dateOfBirth").getValue(String.class);
                            genderFromDB = snapshot.child(typedUserName).child("gender").getValue(String.class);
                            bloodGroupFromDB = snapshot.child(typedUserName).child("bloodGroup").getValue(String.class);
                            addressFromDB = snapshot.child(typedUserName).child("address").getValue(String.class);

                            formFullNameInputLayout.getEditText().setText(fullNameFromDB);
//                            formBloodGroupInputLayout.getEditText().setText(bloodGroupFromDB);
                            fromPhoneNoInputLayout.getEditText().setText(phoneNoFromDB);
//                            formGenderInputLayout.getEditText().setText(genderFromDB);

                            String substr=dateOfBirthFromDB.substring(dateOfBirthFromDB.length()-4, dateOfBirthFromDB.length());
                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR);

                            formDateOfBirthInputLayer.getEditText().setText(String.valueOf(year - Integer.parseInt(substr) + 1));
                            //Toast.makeText(Consultation.this, String.valueOf(year - Integer.parseInt(substr) + 1) , Toast.LENGTH_SHORT).show();
                        }  else {
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
            }
        });

        formUpazilaAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doctor = formUpazilaAutoCom.getText().toString();
                //Toast.makeText(Consultation.this, speciality + " " + , Toast.LENGTH_SHORT).show();
            }
        });

        formScheduleAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!speciality.isEmpty() && !doctor.isEmpty()) {
                    Random r = new Random();
                    int randomNumber = r.nextInt(50);
                    formAppointmentNoET.setText(String.valueOf(randomNumber));
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
                for(DataSnapshot item : snapshot.getChildren()) {
                    if(!item.getValue().toString().equals("schedule")) {
                        spinnerList3.add(item.getValue().toString());
                    }
                }
                adapter3.notifyDataSetChanged();;
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
                for(DataSnapshot item : snapshot.getChildren()) {
                    if(!item.getValue().toString().equals("doctors")) {
                        spinnerList.add(item.getValue().toString());
                    }
                }
                adapter.notifyDataSetChanged();;
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
                for(DataSnapshot item : snapshot.getChildren()) {
                    spinnerList2.add(item.getValue().toString());
                }
                adapter2.notifyDataSetChanged();;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}