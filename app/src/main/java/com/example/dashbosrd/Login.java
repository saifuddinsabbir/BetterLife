package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    LottieAnimationView loginIcon;

    TextView loginMainText, loginSecondaryText, testText;
    TextInputLayout loginUserNameTextInputLayout, loginpasswordTextInputLayout;
    Button loginLoginButton, loginSignupButton;

    LottieAnimationView loginProgressBar, noInternetLogin;

    CheckBox checkboxRememberMe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_login);

        //------Hooks
        loginIcon = findViewById(R.id.loginIconId);
        loginMainText = findViewById(R.id.loginMainTextId);
        loginSecondaryText = findViewById(R.id.loginSecondaryTextId);
        loginUserNameTextInputLayout = findViewById(R.id.loginUserNameTextInputLayoutId);
        loginpasswordTextInputLayout = findViewById(R.id.loginpasswordTextInputLayoutId);
        loginLoginButton = findViewById(R.id.loginLoginButtonId);
        loginSignupButton = findViewById(R.id.loginSignupButtonId);
        loginProgressBar = findViewById(R.id.loginProgressBarId);
        noInternetLogin = findViewById(R.id.noInternetLoginId);

        checkboxRememberMe = findViewById(R.id.checkboxRememberMeId);

        loginProgressBar.setVisibility(View.INVISIBLE);
        noInternetLogin.setVisibility(View.INVISIBLE);

        loginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
            }
        });
        loginSignupButtonAnimation();

    }

    private void checkInternetConnection() {
        try {
            String command = "ping -c 1 google.com";
            if(Runtime.getRuntime().exec(command).waitFor() != 0) {
                noInternetLogin.setVisibility(View.VISIBLE);
            } else {
                loginUser();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please connect to INTERNET", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean validateUserName() {
        String val = loginUserNameTextInputLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            loginUserNameTextInputLayout.setError("Empty field");
            return false;
        } else {
            loginUserNameTextInputLayout.setError(null);
            loginUserNameTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {

        String val = loginpasswordTextInputLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            loginpasswordTextInputLayout.setError("Empty field");
            return false;
        } else {
            loginpasswordTextInputLayout.setError(null);
            loginpasswordTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser() {
        // hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        loginProgressBar.setVisibility(View.VISIBLE);
        if (!validateUserName() | !validatePassword()) {
            loginProgressBar.setVisibility(View.INVISIBLE);
            return;
        } else {
            isUser();
            loginProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void isUser() {
        String userEnteredUsername = loginUserNameTextInputLayout.getEditText().getText().toString().trim();
        String userEnteredPassword = loginpasswordTextInputLayout.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("userName").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    loginProgressBar.setVisibility(View.VISIBLE);

                    loginUserNameTextInputLayout.setError(null);
                    loginUserNameTextInputLayout.setErrorEnabled(false);
                    loginpasswordTextInputLayout.setError(null);
                    loginpasswordTextInputLayout.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        //Toast.makeText(Login.this, "Logging in..", Toast.LENGTH_SHORT).show();
                        loginUserNameTextInputLayout.setError(null);
                        loginUserNameTextInputLayout.setErrorEnabled(false);

                        String dpFromDB = snapshot.child(userEnteredUsername).child("dp").getValue(String.class);
                        String fullNameFromDB = snapshot.child(userEnteredUsername).child("fullName").getValue(String.class);
                        String userNameFromDB = snapshot.child(userEnteredUsername).child("userName").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String dateOfBirthFromDB = snapshot.child(userEnteredUsername).child("dateOfBirth").getValue(String.class);
                        String genderFromDB = snapshot.child(userEnteredUsername).child("gender").getValue(String.class);
                        String bloodGroupFromDB = snapshot.child(userEnteredUsername).child("bloodGroup").getValue(String.class);
                        String addressFromDB = snapshot.child(userEnteredUsername).child("address").getValue(String.class);

                        //create a session
                        SessionManager sessionManager = new SessionManager(Login.this);
                        sessionManager.createLoginSession(dpFromDB, fullNameFromDB, userNameFromDB,emailFromDB, phoneNoFromDB,dateOfBirthFromDB, genderFromDB, bloodGroupFromDB, addressFromDB, passwordFromDB);

                        storeSessionInfo(userEnteredUsername);

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("userName", userEnteredUsername);
                        startActivity(intent);
                    } else {
                        loginpasswordTextInputLayout.setError("Wrong Password");
                        loginpasswordTextInputLayout.requestFocus();
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    loginUserNameTextInputLayout.setError("No such User exist");
                    loginUserNameTextInputLayout.requestFocus();
                    loginProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void storeSessionInfo(String userEnteredUsername) {
        SharedPreferences.Editor editor = getSharedPreferences("UserInfo",
                MODE_PRIVATE).edit();
        editor.putString("userName", userEnteredUsername);
        editor.apply();

        if(checkboxRememberMe.isChecked()) {
            SharedPreferences.Editor editor2 = getSharedPreferences("RememberMe",
                    MODE_PRIVATE).edit();
            editor2.putBoolean("isRememberMeChecked",true);
            editor2.apply();
        } else {
            SharedPreferences.Editor editor2 = getSharedPreferences("RememberMe",
                    MODE_PRIVATE).edit();
            editor2.putBoolean("isRememberMeChecked",false);
            editor2.apply();
        }
    }


    public void loginSignupButtonAnimation() {
        loginSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);

                //----------------------------------- Animation (start) --------------------------------------
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(loginIcon, "logo_transition");
                pairs[1] = new Pair<View, String>(loginMainText, "maintext_transition");
                pairs[2] = new Pair<View, String>(loginSecondaryText, "secondarytext_transition");
                pairs[3] = new Pair<View, String>(loginUserNameTextInputLayout, "username_transition");
                pairs[4] = new Pair<View, String>(loginpasswordTextInputLayout, "password_transition");
                pairs[5] = new Pair<View, String>(loginLoginButton, "enter_button_transition");
                pairs[6] = new Pair<View, String>(loginSignupButton, "login_signup_transition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
                //----------------------------------- Animation (start) ---------------------------------------
            }
        });
    }
}