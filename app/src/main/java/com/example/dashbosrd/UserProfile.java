package com.example.dashbosrd;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RadioGroup genderRadioGroup;
    MaterialRadioButton selectedRadioButton, maleRadioButton, femaleRadioButton;

    TextView profileFullnameTextView, profileUserNameTextView, BrainTumorCheckCount;
    TextInputLayout profileFullNameInputLayout, profileUserNameInputLayout, profileEmailInputLayout, profilePhoneNoInputLayout, profileDateOfBirthInputLayer, profileGenderInputLayout, profileBloodGroupInputLayout, profileAddressInputLayout, profilePasswordInputLayout;
    TextInputEditText dateOfBirthEditText;
    DatePickerDialog.OnDateSetListener setListener;
    Button updateProfileButton;
    ImageView profileImage;
    ProgressBar profileProgressBar;
    LottieAnimationView lottieAnimationView;

    String userNameGlobal;

    FirebaseDatabase database;
    FirebaseStorage storage;

    DatabaseReference reference, referenceChange, referenceImage;
    Query checkUser;

    String fullNameFromDB, userNameFromDB, emailFromDB, phoneNoFromDB, dateOfBirthFromDB, genderFromDB, bloodGroupFromDB, addressFromDB, passwordFromDB;

    Boolean isAnyValueChanged;

    SessionManager sessionManager;
    HashMap<String, String> userDetails;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //----Set value from DATABASE
        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

        //-------Hooks
        profileImage = findViewById(R.id.profileImageId);
        profileFullnameTextView = findViewById(R.id.profileFullnameTextViewId);
        profileUserNameTextView = findViewById(R.id.profileUserNameTextViewId);
        BrainTumorCheckCount = findViewById(R.id.BrainTumorCheckCountId);
        profileFullNameInputLayout = findViewById(R.id.profileFullNameInputLayoutId);
        profileUserNameInputLayout = findViewById(R.id.profileUserNameInputLayoutId);
        profileEmailInputLayout = findViewById(R.id.profileEmailInputLayoutId);
        profilePhoneNoInputLayout = findViewById(R.id.profilePhoneNoInputLayoutId);
        profileDateOfBirthInputLayer = findViewById(R.id.profileDateOfBirthInputLayerId);

        genderRadioGroup = findViewById(R.id.genderRadioGroupId);
        maleRadioButton = findViewById(R.id.maleId);
        femaleRadioButton = findViewById(R.id.femaleId);

        profileBloodGroupInputLayout = findViewById(R.id.profileBloodGroupInputLayoutId);
        profileAddressInputLayout = findViewById(R.id.profileAddressInputLayoutId);
        profilePasswordInputLayout = findViewById(R.id.profilePasswordInputLayoutId);
        updateProfileButton = findViewById(R.id.updateProfileButtonId);
        profileProgressBar = findViewById(R.id.profileProgressBarId);
        lottieAnimationView = findViewById(R.id.lottieAnimationId);
        profileProgressBar.setVisibility(View.INVISIBLE);

        //Toast.makeText(this, userDetails.get(SessionManager.KEY_FULLNAME), Toast.LENGTH_SHORT).show();



        setValuesFromDatabaseMethod(userNameGlobal); //call method

        updateProfileButtonMethod();

        drawerLayoutMethod();

        imagePickerMethod();

        datePickerMethod();


    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    public void setValuesFromDatabaseMethod(String userNameGlobal) {
        profileProgressBar.setVisibility(View.INVISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUser = reference.orderByChild("userName").equalTo(userNameGlobal);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullNameFromDB = snapshot.child(userNameGlobal).child("fullName").getValue(String.class);
                userNameFromDB = snapshot.child(userNameGlobal).child("userName").getValue(String.class);
                emailFromDB = snapshot.child(userNameGlobal).child("email").getValue(String.class);
                phoneNoFromDB = snapshot.child(userNameGlobal).child("phoneNo").getValue(String.class);
                dateOfBirthFromDB = snapshot.child(userNameGlobal).child("dateOfBirth").getValue(String.class);
                genderFromDB = snapshot.child(userNameGlobal).child("gender").getValue(String.class);
                bloodGroupFromDB = snapshot.child(userNameGlobal).child("bloodGroup").getValue(String.class);
                addressFromDB = snapshot.child(userNameGlobal).child("address").getValue(String.class);
                passwordFromDB = snapshot.child(userNameGlobal).child("password").getValue(String.class);

                profileFullnameTextView.setText(fullNameFromDB);
                profileUserNameTextView.setText("@"+userNameFromDB);
                profileFullNameInputLayout.getEditText().setText(fullNameFromDB);
                profileUserNameInputLayout.getEditText().setText(userNameFromDB);
                profileEmailInputLayout.getEditText().setText(emailFromDB);
                profilePhoneNoInputLayout.getEditText().setText(phoneNoFromDB);
                profileDateOfBirthInputLayer.getEditText().setText(dateOfBirthFromDB);

                //profileGenderInputLayout.getEditText().setText(genderFromDB);

                if (genderFromDB.equals("Male")) {
                    maleRadioButton.setChecked(true);
                } else if (genderFromDB.equals("Female")) {
                    femaleRadioButton.setChecked(true);
                }

                profileBloodGroupInputLayout.getEditText().setText(bloodGroupFromDB);
                profileAddressInputLayout.getEditText().setText(addressFromDB);
                profilePasswordInputLayout.getEditText().setText(passwordFromDB);

                lottieAnimationView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkCounts();
    }

    private void checkCounts() {
        DatabaseReference referenceHistory = FirebaseDatabase.getInstance().getReference("history").child(userNameGlobal);
        referenceHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot historySnap : snapshot.getChildren()) {
                    String uid = historySnap.child("userId").getValue(String.class);

                    count++;

                }
                BrainTumorCheckCount.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateProfileButtonMethod() {
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(UserProfile.this)
                        .setTitle("Update Profile")
                        .setMessage("Do you want update profile?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                profileProgressBar.setVisibility(View.VISIBLE);
                                updateValuesIntoDatabaseMethod();
                            }
                        })
                        .setNegativeButton("No", null);

                builder.create();
                builder.show();
            }
        });
    }

    public void updateValuesIntoDatabaseMethod() {
//        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();

        if (isUserNameChanged()) {
            profileProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Username can't be changed", Toast.LENGTH_SHORT).show();
            return;
        }

        isAnyValueChanged = false;

        referenceChange = FirebaseDatabase.getInstance().getReference("users").child(userNameGlobal);

        changeFullName();
        changeEmail();
        changePhoneNo();
        changeDateOfBirth();
        changeGender();
        changeBloodGroup();
        changeAddress();
        changePassword();

        if (isAnyValueChanged) {
            profileProgressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfile.this, UserProfile.class));
        } else {
            profileProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Data weren't changed to be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserNameChanged() {
        if (!userNameFromDB.equals(profileUserNameInputLayout.getEditText().getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public void changeFullName() {
        if (!fullNameFromDB.equals(profileFullNameInputLayout.getEditText().getText().toString())) {
            referenceChange.child("fullName").setValue(profileFullNameInputLayout.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changeEmail() {
        if (!emailFromDB.equals(profileEmailInputLayout.getEditText().getText().toString())) {
            referenceChange.child("email").setValue(profileEmailInputLayout.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changePhoneNo() {
        if (!phoneNoFromDB.equals(profilePhoneNoInputLayout.getEditText().getText().toString())) {
            referenceChange.child("phoneNo").setValue(profilePhoneNoInputLayout.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changeDateOfBirth() {
        if (!dateOfBirthFromDB.equals(profileDateOfBirthInputLayer.getEditText().getText().toString())) {
            referenceChange.child("dateOfBirth").setValue(profileDateOfBirthInputLayer.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changeGender() {
        int ID = genderRadioGroup.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(ID);

        if (!genderFromDB.equals(selectedRadioButton.getText().toString())) {
            referenceChange.child("gender").setValue(selectedRadioButton.getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changeBloodGroup() {
        if (!bloodGroupFromDB.equals(profileBloodGroupInputLayout.getEditText().getText().toString())) {
            referenceChange.child("bloodGroup").setValue(profileBloodGroupInputLayout.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changeAddress() {
        if (!addressFromDB.equals(profileAddressInputLayout.getEditText().getText().toString())) {
            referenceChange.child("address").setValue(profileAddressInputLayout.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    private void changePassword() {
        if (!passwordFromDB.equals(profilePasswordInputLayout.getEditText().getText().toString())) {
            referenceChange.child("password").setValue(profilePasswordInputLayout.getEditText().getText().toString());
            isAnyValueChanged = true;
        }
    }

    public void imagePickerMethod() {
        //--------------Image Picker Profile (start)
        storage = FirebaseStorage.getInstance();
        profileProgressBar.setVisibility(View.VISIBLE);

        try {
            database = FirebaseDatabase.getInstance();
            referenceImage = database.getReference("users").child(userNameGlobal);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String image = snapshot.child(userNameGlobal).child("dp").getValue(String.class);
                    if(image != null) {
                        Picasso.get().load(image).into(profileImage);
                    }
                    profileProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error occurred while fetching", Toast.LENGTH_SHORT).show();
        }

        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri photoUri = result.getData().getData();
                        profileImage.setImageURI(photoUri);
                        profileProgressBar.setVisibility(View.VISIBLE);

                        StorageReference storageReference = storage.getReference().child(userNameGlobal);
                        storageReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            referenceImage.child("dp").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    profileProgressBar.setVisibility(View.INVISIBLE);

//                                                    sessionManager.createLoginSession(uri.toString(),
//                                                            fullNameFromDB,
//                                                            userNameFromDB,
//                                                            emailFromDB,
//                                                            phoneNoFromDB,
//                                                            dateOfBirthFromDB,
//                                                            genderFromDB,
//                                                            bloodGroupFromDB,
//                                                            addressFromDB,
//                                                            passwordFromDB);
                                                    Toast.makeText(UserProfile.this, "Profile image uploaded.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

                    }
                }

        );
        profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });
    }

    public void datePickerMethod() {
        //----------------------Date Picker (start)
        try {
            updateProfileButton = findViewById(R.id.updateProfileButtonId);
            profileDateOfBirthInputLayer = findViewById(R.id.profileDateOfBirthInputLayerId);
            dateOfBirthEditText = findViewById(R.id.profileDateOfBirthEditTextId);
            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(UserProfile.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            });
            setListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    i1 += 1;
                    String s = "" + i2 + "/" + i1 + "/" + i + "";
                    profileDateOfBirthInputLayer.getEditText().setText(s);
                }
            };

        } catch (Exception e) {
            Toast.makeText(UserProfile.this, "Stopped", Toast.LENGTH_SHORT).show();
        }
        //-------------------Date Picker (end)
    }


    //-------------------------------------------------------------- D R A W E R - L A Y O U T (start)---------------------------------------------------------
    public void drawerLayoutMethod() {
        //-------------Hooks----------------------------
        drawerLayout = findViewById(R.id.drawerLayoutProfileId);
        navigationView = findViewById(R.id.navViewProfile);
        toolbar = findViewById(R.id.toolbarProfile);
        //-------------Toolbar-------------------------------
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Profile");
        toolbar.setSubtitle("");
        //---------------Navigation Drawer Menu------------------
        //-----Hide or show items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_profile);
        menu.findItem(R.id.nav_logout);
        //-------------------------------------------------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //-------------------------------------------------------
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            intent.putExtra("userName", userNameGlobal);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent_home_drawer = new Intent(UserProfile.this, MainActivity.class);
                intent_home_drawer.putExtra("userName", userNameGlobal);
                startActivity(intent_home_drawer);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;

            case R.id.nav_leukemia:
                startActivity(new Intent(UserProfile.this, Leukemia.class));
                break;

            case R.id.nav_brain_tumor:
                startActivity(new Intent(UserProfile.this, BrainTumorDetect.class));
                break;

            case R.id.nav_profile:
                //startActivity(new Intent(UserProfile.this, UserProfile.class));
                break;

            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                String subject = "Android app: Better Life";

                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.share_value);
                startActivity(Intent.createChooser(intent, "Share with.."));
                Toast.makeText(getApplicationContext(), "Sharing..", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}