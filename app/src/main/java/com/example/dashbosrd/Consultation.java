package com.example.dashbosrd;

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
import java.util.HashMap;

public class Consultation extends AppCompatActivity {

    Dialog popupRegister;
    AutoCompleteTextView formDistrictAutoCom, formUpazilaAutoCom;

    RadioGroup genderRadioGroup = null;
    RadioButton selectedRadioButton, maleRadioButton = null, femaleRadioButton = null;

    ImageView formCancelButton;
    TextInputLayout formFullNameInputLayout, formUserNameInputLayout, fromPhoneNoInputLayout, formGenderInputLayout, formDateOfBirthInputLayer, formBloodGroupInputLayout;
    TextInputEditText dateOfBirthEditText;
    DatePickerDialog.OnDateSetListener setListener;
    Button popupSubmitButton;

    String fullNameFromDB, userNameFromDB, emailFromDB, phoneNoFromDB, dateOfBirthFromDB, genderFromDB = "", bloodGroupFromDB, addressFromDB, passwordFromDB;

    Button registrationButton;

    LottieAnimationView noInternetLottieAnimation;

    Spinner spinner, spinner2;
    DatabaseReference referenceLocation;
    ArrayList<String> spinnerList, spinnerList2;
    ArrayAdapter<String> adapter, adapter2;

    String userNameGlobal;
    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    String district;
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
        formGenderInputLayout = popupRegister.findViewById(R.id.formGenderInputLayoutId);
        formDateOfBirthInputLayer = popupRegister.findViewById(R.id.formDateOfBirthInputLayerId);
        formBloodGroupInputLayout = popupRegister.findViewById(R.id.formBloodGroupInputLayoutId);
        formDistrictAutoCom = popupRegister.findViewById(R.id.formDistrictAutoComId);
        formUpazilaAutoCom = popupRegister.findViewById(R.id.formUpazilaAutoComId);
        popupSubmitButton = popupRegister.findViewById(R.id.popupSubmitButtonId);

        noInternetLottieAnimation.setVisibility(View.INVISIBLE);

        formCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formUserNameInputLayout.getEditText().setText("");
                formFullNameInputLayout.getEditText().setText("");
                fromPhoneNoInputLayout.getEditText().setText("");
                formGenderInputLayout.getEditText().setText("");
                formDateOfBirthInputLayer.getEditText().setText("");
                formBloodGroupInputLayout.getEditText().setText("");
                formDistrictAutoCom.setText("");
                formUpazilaAutoCom.setText("");
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
                            formDateOfBirthInputLayer.getEditText().setText(dateOfBirthFromDB);
                            formBloodGroupInputLayout.getEditText().setText(bloodGroupFromDB);
                            fromPhoneNoInputLayout.getEditText().setText(phoneNoFromDB);
                            formGenderInputLayout.getEditText().setText(genderFromDB);


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

        adapter = new ArrayAdapter<String>(Consultation.this, R.layout.drop_down_item, spinnerList);
        adapter2 = new ArrayAdapter<String>(Consultation.this, R.layout.drop_down_item, spinnerList2);

        formDistrictAutoCom.setAdapter(adapter);
        formUpazilaAutoCom.setAdapter(adapter2);

        showData();

        formDistrictAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerList2.clear();
                district = formDistrictAutoCom.getText().toString();
                showSecondList();
            }
        });

        formUpazilaAutoCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Consultation.this, district + " " + formUpazilaAutoCom.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData() {
        referenceLocation.child("district").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()) {
                    if(!item.getValue().toString().equals("district")) {
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
        referenceLocation.child(district).addValueEventListener(new ValueEventListener() {
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