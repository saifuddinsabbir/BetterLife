package com.example.dashbosrd;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Signup extends AppCompatActivity {

    LottieAnimationView signupIcon, successfulLottieAnimation, noInternetSignup;
    TextView signupMainText, signupSecondaryText;
    TextInputLayout signupFullNameTextInputLayout, signUpUsernameTextInputLayout, signUpEmailTextInputLayout,
            signUpPhoneNoTextInputLayout, signupPasswordTextInputLayout, signupConfirmPasswordTextInputLayout;
    TextInputEditText signupFullNameTextEditText, signUpUsernameTextInputEditText, signUpEmailTextInputEditText,
            signUpPhoneNoTextEditText, signupPasswordTextEditText, signupConfirmPasswordTextEditText;
    Button signUpSignUpButton, signUpLoginButton;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    public boolean z, clicked = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_signup);

        //------Hooks
        signupIcon = findViewById(R.id.signupIconId);
        noInternetSignup = findViewById(R.id.noInternetSignupId);
        successfulLottieAnimation = findViewById(R.id.successfulLottieAnimationId);
        signupMainText = findViewById(R.id.signupMainTextId);
        signupSecondaryText = findViewById(R.id.signupSecondaryTextId);
        signupFullNameTextInputLayout = findViewById(R.id.signupFullNameTextInputLayoutId);
        signUpUsernameTextInputLayout = findViewById(R.id.signUpUsernameTextInputLayoutId);
        signUpEmailTextInputLayout = findViewById(R.id.signUpEmailTextInputLayoutId);
        signUpPhoneNoTextInputLayout = findViewById(R.id.signUpPhoneNoTextInputLayoutId);
        signupPasswordTextInputLayout = findViewById(R.id.signupPasswordTextInputLayoutId);
        signupConfirmPasswordTextInputLayout = findViewById(R.id.signupConfirmPasswordTextInputLayoutId);
        signUpSignUpButton = findViewById(R.id.signUpSignUpButtonId);
        signUpLoginButton = findViewById(R.id.signUpLoginButtonId);
        signUpUsernameTextInputEditText = findViewById(R.id.signUpUsernameTextInputEditTextId);
        signUpEmailTextInputEditText = findViewById(R.id.signUpEmailTextInputEditTextId);
        signupFullNameTextEditText = findViewById(R.id.signupFullNameTextEditTextId);
        signUpPhoneNoTextEditText = findViewById(R.id.signUpPhoneNoTextEditTextId);
        signupPasswordTextEditText = findViewById(R.id.signupPasswordTextEditTextId);
        signupConfirmPasswordTextEditText = findViewById(R.id.signupConfirmPasswordTextEditTextId);

        noInternetSignup.setVisibility(View.INVISIBLE);
        successfulLottieAnimation.setVisibility(View.INVISIBLE);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        fullNameCheck();
        userNameCheck();
        emailCheck();
        phoneNoCheck();
        passwordCheck();
        confirmPasswordCheck();

        signUpSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;

                checkInternetConnection();
            }
        });

        signupLoginButtonAnimation();
    }

    private void checkInternetConnection() {
        try {
            String command = "ping -c 1 google.com";
            if(Runtime.getRuntime().exec(command).waitFor() != 0) {
                noInternetSignup.setVisibility(View.VISIBLE);
            } else {
                storeOnDatabase();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please connect to INTERNET", Toast.LENGTH_SHORT).show();
        }
    }


    private void fullNameCheck() {
        signupFullNameTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String typename = String.valueOf(editable);

                if (typename.isEmpty()) {
                    signupFullNameTextInputLayout.setError("Empty field");
                } else {
                    signupFullNameTextInputLayout.setError(null);
                    signupFullNameTextInputLayout.setErrorEnabled(false);
                }
            }
        });
    }


    private void userNameCheck() {
        signUpUsernameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users");
                String typename = String.valueOf(editable);
                String noWhiteSpace = "\\A\\w{4,20}\\z";

                if (typename.isEmpty()) {
                    signUpUsernameTextInputLayout.setError("Empty field");
                } else if (typename.length() >= 15) {
                    signUpUsernameTextInputLayout.setError("Username is too long");
                } else if (!typename.matches(noWhiteSpace)) {
                    signUpUsernameTextInputLayout.setError("White spaces are not allowed");
                } else {
                    signUpUsernameTextInputLayout.setError(null);
                    signUpUsernameTextInputLayout.setErrorEnabled(false);
                }

                mData.orderByChild("userName").equalTo(typename).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if (snapshot.exists() && !clicked) {
                            signUpUsernameTextInputLayout.setError("Username already exists");
                        }  else {
                            signUpUsernameTextInputLayout.setError(null);
                            signUpUsernameTextInputLayout.setErrorEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }


    private void emailCheck() {
        signUpEmailTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String typename = String.valueOf(editable);

                if (typename.isEmpty()) {
                    signUpEmailTextInputLayout.setError("Empty field");
                } else if (!typename.matches(emailPattern)) {
                    signUpEmailTextInputLayout.setError("Invalid email address");
                } else {
                    signUpEmailTextInputLayout.setError(null);
                    signUpEmailTextInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    private void phoneNoCheck() {
        signUpPhoneNoTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String typename = String.valueOf(editable);

                if (typename.isEmpty()) {
                    signUpPhoneNoTextInputLayout.setError("Empty field");
                } else if (typename.length() != 11) {
                    signUpPhoneNoTextInputLayout.setError("Invalid phone number");
                } else {
                    signUpPhoneNoTextInputLayout.setError(null);
                    signUpPhoneNoTextInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    private void passwordCheck() {
        signupPasswordTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String typename = String.valueOf(editable);
                String passwordVal = "^" +
                        //"(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";


                if (typename.isEmpty()) {
                    signupPasswordTextInputLayout.setError("Empty field");
                } else if (!typename.matches(passwordVal)) {
                    signupPasswordTextInputLayout.setError("Password is too weak");
                } else {
                    signupPasswordTextInputLayout.setError(null);
                    signupPasswordTextInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    private void confirmPasswordCheck() {
        signupConfirmPasswordTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String typename = String.valueOf(editable);

                if (typename.isEmpty()) {
                    signupConfirmPasswordTextInputLayout.setError("Empty field");
                } else if (!typename.equals(signupPasswordTextInputLayout.getEditText().getText().toString())) {
                    signupConfirmPasswordTextInputLayout.setError("Passwords aren't matched");
                } else {
                    signupConfirmPasswordTextInputLayout.setError(null);
                    signupConfirmPasswordTextInputLayout.setErrorEnabled(false);
                }
            }
        });
    }
    //---------------------------------------
    private Boolean validateFullName() {
        String val = signupFullNameTextInputLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            signupFullNameTextInputLayout.setError("Empty field");
            return false;
        } else {
            signupFullNameTextInputLayout.setError(null);
            signupFullNameTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateUserName() {
        String val = signUpUsernameTextInputLayout.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            signUpUsernameTextInputLayout.setError("Empty filed");
            return false;
        } else if (val.length() >= 15) {
            signUpUsernameTextInputLayout.setError("Username is too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            signUpUsernameTextInputLayout.setError("White spaces are not allowed");
            return false;
        } else {
            signUpUsernameTextInputLayout.setError(null);
            signUpUsernameTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isUser() {
        String userEnteredUsername = signUpUsernameTextInputLayout.getEditText().getText().toString();
        Query checkUser = reference.orderByChild("userName").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    signUpUsernameTextInputLayout.setError("Username already exist");
                    z = false;
                } else {
                    z = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return z;
    }

    private Boolean validateEmail() {
        String val = signUpEmailTextInputLayout.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            signUpEmailTextInputLayout.setError("Empty filed");
            return false;
        } else if (!val.matches(emailPattern)) {
            signUpEmailTextInputLayout.setError("Invalid email address");
            return false;
        } else {
            signUpEmailTextInputLayout.setError(null);
            signUpEmailTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = signUpPhoneNoTextInputLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            signUpPhoneNoTextInputLayout.setError("Empty filed");
            return false;
        } else if (val.length() != 11) {
            signUpPhoneNoTextInputLayout.setError("Invalid phone number");
            return false;
        } else {
            signUpPhoneNoTextInputLayout.setError(null);
            signUpPhoneNoTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = signupPasswordTextInputLayout.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";


        if (val.isEmpty()) {
            signupPasswordTextInputLayout.setError("Empty filed");
            return false;
        } else if (!val.matches(passwordVal)) {
            signupPasswordTextInputLayout.setError("Password is too weak");
            return false;
        } else {
            signupPasswordTextInputLayout.setError(null);
            signupPasswordTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String val = signupConfirmPasswordTextInputLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            signupConfirmPasswordTextInputLayout.setError("Empty field");
            return false;
        } else if (!val.equals(signupPasswordTextInputLayout.getEditText().getText().toString())) {
            signupConfirmPasswordTextInputLayout.setError("Passwords aren't matched");
            return false;
        } else {
            signupConfirmPasswordTextInputLayout.setError(null);
            signupConfirmPasswordTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void storeOnDatabase() throws InterruptedException {

        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePhoneNo() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }

        successfulLottieAnimation.setVisibility(View.VISIBLE);

        //get all values
        String fullName = signupFullNameTextInputLayout.getEditText().getText().toString();
        String userName = signUpUsernameTextInputLayout.getEditText().getText().toString();
        String email = signUpEmailTextInputLayout.getEditText().getText().toString();
        String phoneNo = signUpPhoneNoTextInputLayout.getEditText().getText().toString();
        String dateOfBirth = "Empty";
        String gender = "Empty";
        String bloodGroup = "Empty";
        String address = "Empty";
        String password = signupPasswordTextInputLayout.getEditText().getText().toString();

        UserHelperClass helperClass = new UserHelperClass(fullName, userName, email, phoneNo, dateOfBirth, gender, bloodGroup, address, password);

        reference.child(userName).setValue(helperClass);

        successfulLottieAnimation.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Signup.this, Login.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
                finish();
            }
        }, 4000);
    }

    //signup-login animation
    public void signupLoginButtonAnimation() {
        signUpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);

                //----------------------------------- Animation (start) --------------------------------------
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(signupIcon, "logo_transition");
                pairs[1] = new Pair<View, String>(signupMainText, "maintext_transition");
                pairs[2] = new Pair<View, String>(signupSecondaryText, "secondarytext_transition");
                pairs[3] = new Pair<View, String>(signUpUsernameTextInputLayout, "username_transition");
                pairs[4] = new Pair<View, String>(signupPasswordTextInputLayout, "password_transition");
                pairs[5] = new Pair<View, String>(signUpSignUpButton, "enter_button_transition");
                pairs[6] = new Pair<View, String>(signUpLoginButton, "login_signup_transition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this, pairs);
                startActivity(intent, options.toBundle());
                //----------------------------------- Animation (end) --------------------------------------
            }
        });
    }
}