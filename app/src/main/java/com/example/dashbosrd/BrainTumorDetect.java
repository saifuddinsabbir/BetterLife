package com.example.dashbosrd;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dashbosrd.ml.Model2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class BrainTumorDetect extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView pickImage;
    TextView resultClassTextView, resultClassProbabilityTextView;
    Button galleryButton, cameraButton, writeFeedbackBrainTumor;
    ChipNavigationBar chipNavigationBar;
    Bitmap bitmap, bitmapGlobal;

    LottieAnimationView imageLottieAnimation, imagePostLottieAnimation, noInternetLottieAnimation;

    Uri photoUri;

    Dialog popAddPost;
    ImageView  popupAddBtn, writeFeedbakCancelButton, popupUserImage, popupPostImage, postListImage;
    TextInputEditText popupDescription;
    ProgressBar popupClickProgress;
    TextView writeFeedbackFullName, writeFeedbackUserName, writeFeedbackAvarageRating, writeFeedbackExperianceRating;


    float myRating;
    RatingBar writeFeedbackRatingBar;

    Post post;
    History history;

    String resultClass, resultProbability;

    //DATABASE----------------
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference reference, referenceFeedbacks, referenceHistory;
    StorageReference referenceImage, referenceImageHistory;
    Query checkUser;

    double sumOfAllrating;
    int totalNoOfRating;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    String userNameGlobal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tumor_detect);

        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");

        imageLottieAnimation = findViewById(R.id.imageLottieAnimationId);

        resultClassTextView = findViewById(R.id.textView17);
        resultClassProbabilityTextView = findViewById(R.id.textView18);
        writeFeedbackBrainTumor = findViewById(R.id.writeFeedbackBrainTumorId);

        writeFeedbackBrainTumor.setVisibility(View.INVISIBLE);

        toolBarFunctionalities();

        takeImageFromCameraBrainTumorDetect();
        chooseImageFromGalleryBrainTumorDetect();
        chipNavigationBarBrainTumorDetect();

        // ini popup
//        iniPopup();

        writeFeedbackBrainTumor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
            }
        });
    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    public void toolBarFunctionalities() {
        ImageView detectBrainTumorToolBarBack = findViewById(R.id.detectBrainTumorToolBarBackId);

        detectBrainTumorToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrainTumorDetect.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    public void chooseImageFromGalleryBrainTumorDetect() {
        //choose from GALLERY
        pickImage = findViewById(R.id.pickImageId);
        galleryButton = findViewById(R.id.galleryButtonId);

        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        photoUri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                            bitmapGlobal = bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pickImage.setImageURI(photoUri);
                        writeFeedbackBrainTumor.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1500);
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        writeFeedbackBrainTumor.setAnimation(anim);
                        predictTheClass();
                        iniPopup();
                        imageLottieAnimation.setVisibility(View.INVISIBLE);
                    }
                }
        );
        galleryButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void takeImageFromCameraBrainTumorDetect() {
        //take photos with CAMERA
        cameraButton = findViewById(R.id.cameraButtonId);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888,true) ;
                    pickImage.setImageBitmap(bitmap);
                    bitmapGlobal = bitmap;
                    photoUri = getImageUri(BrainTumorDetect.this, bitmap);
                    bitmap = bmp;

                    try{
                        writeFeedbackBrainTumor.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1500);
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        writeFeedbackBrainTumor.setAnimation(anim);
                        predictTheClass();
                        iniPopup();
                        imageLottieAnimation.setVisibility(View.INVISIBLE);
                    }
                    catch (Exception e){
                        Toast.makeText(BrainTumorDetect.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no app that support this action",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void predictTheClass() {
        try {
            Model2 model = Model2.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorImage image = TensorImage.fromBitmap(bitmap);

            // Runs model inference and gets result.
            Model2.Outputs outputs = model.process(image);
            List<Category> probability = outputs.getProbabilityAsCategoryList();

            double maxProbability = 0.0;
            int maxProbabilityIndex = 0;

            for (int i = 0; i < probability.size(); i++) {
                if(maxProbability <= probability.get(i).getScore()) {
                    maxProbability = probability.get(i).getScore();
                    maxProbabilityIndex = i;
                }
            }

            resultClass = "Stage: "+ probability.get(maxProbabilityIndex).getLabel();
            resultProbability = "Probability: " + maxProbability;

            //Toast.makeText(this, resultClass, Toast.LENGTH_SHORT).show();

            resultClassTextView.setText(resultClass);
            resultClassProbabilityTextView.setText(resultProbability);


            // Releases model resources if no longer used.
            model.close();

            storeHistoryOnDatabase();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private void storeHistoryImageIntoDB(String key) {
        referenceImageHistory = FirebaseStorage.getInstance().getReference("history").child(userDetails.get(SessionManager.KEY_USERNAME));
        referenceImageHistory.child(key).putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(BrainTumorDetect.this, "image uploaded", Toast.LENGTH_SHORT).show();
                referenceImageHistory.child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        history.setPickedImage(uri.toString());

                        referenceHistory.setValue(history).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(BrainTumorDetect.this, "Added to the history", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BrainTumorDetect.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //referenceUser.child("dp").setValue(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BrainTumorDetect.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BrainTumorDetect.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeHistoryOnDatabase() {
        history = new History("Brain Tumor Test", "", resultClass, resultProbability, userNameGlobal);

        referenceHistory = FirebaseDatabase.getInstance().getReference("history").push();
        String key = referenceHistory.getKey();
        history.setHistoryKey(key);

        storeHistoryImageIntoDB(key);

    }

    public void chipNavigationBarBrainTumorDetect() {
        chipNavigationBar = findViewById(R.id.bottomNavbarBrainTumorDetectId);
        chipNavigationBar.setItemSelected(R.id.detectId, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aboutId:
                        startActivity(new Intent(BrainTumorDetect.this, AboutBrainTumor.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.detectId:
                        //startActivity(new Intent(BrainTumorDetect.this, BrainTumorDetect.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.treatmentId:
                        startActivity(new Intent(BrainTumorDetect.this, BrainTumorTreatment.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }
        });
    }

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setWindowAnimations(R.style.animation);
        popAddPost.getWindow().setBackgroundDrawable(getDrawable(R.drawable.popup_background));
        popAddPost.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.CENTER;

        // ini popup widgets
        writeFeedbakCancelButton = popAddPost.findViewById(R.id.writeFeedbakCancelButtonId);
        noInternetLottieAnimation = popAddPost.findViewById(R.id.noInternetLottieAnimationId);
        imagePostLottieAnimation = popAddPost.findViewById(R.id.imagePostLottieAnimationId);
        popupUserImage = popAddPost.findViewById(R.id.writeFeedbackUserImage);
        writeFeedbackFullName = popAddPost.findViewById(R.id.writeFeedbackFullName);
        writeFeedbackUserName = popAddPost.findViewById(R.id.writeFeedbackUserNameId);
        popupDescription = popAddPost.findViewById(R.id.writeFeedbackDescId);
        postListImage = popAddPost.findViewById(R.id.postListImageId);
        popupAddBtn = popAddPost.findViewById(R.id.writeFeedbakPostButtonId);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);
        writeFeedbackAvarageRating = popAddPost.findViewById(R.id.writeFeedbackAvarageRatingId);
        writeFeedbackExperianceRating = popAddPost.findViewById(R.id.writeFeedbackExperianceRatingId);

        noInternetLottieAnimation.setVisibility(View.INVISIBLE);

        popupDescription.setText(resultClass + "\n" + resultProbability);

        imagePostLottieAnimation.setVisibility(View.INVISIBLE);

        //set image for post
        postListImage.setImageBitmap(bitmapGlobal);

        writeFeedbackRatingBar = popAddPost.findViewById(R.id.writeFeedbackRatingBarId);
        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");
        try {
            DatabaseReference referenceFeedbacksAgain = FirebaseDatabase.getInstance().getReference("feedbacks");
            referenceFeedbacksAgain.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sumOfAllrating = 0.0;
                    totalNoOfRating = 0;
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        sumOfAllrating += Double.parseDouble(snap.child("myRating").getValue().toString());
                        totalNoOfRating++;
                    }
                    writeFeedbackAvarageRating.setText("Average rating: " + decfor.format((sumOfAllrating) / (totalNoOfRating)) + "/5.00");
                    Toast.makeText(BrainTumorDetect.this, sumOfAllrating + " " + totalNoOfRating, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(BrainTumorDetect.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            writeFeedbackRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    myRating = rating;

                    if (rating <= 1) {
                        writeFeedbackExperianceRating.setText("Experience: Very bad");
                    } else if (rating <= 2) {
                        writeFeedbackExperianceRating.setText("Experience: Bad");
                    } else if (rating <= 3) {
                        writeFeedbackExperianceRating.setText("Experience: Not bad");
                    } else if (rating <= 4) {
                        writeFeedbackExperianceRating.setText("Experience: Good");
                    } else {
                        writeFeedbackExperianceRating.setText("Experience: Very good");
                    }

                    Double avererageRating = (sumOfAllrating + rating) / (totalNoOfRating + 1);
                    writeFeedbackAvarageRating.setText("Average rating: " + decfor.format(avererageRating) + "/5.00");
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        writeFeedbakCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.dismiss();
            }
        });

        //SET FULLNAME, USERNAME, IMAGE from DATABASE
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("userName").equalTo(userNameGlobal);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameFromDB = snapshot.child(userNameGlobal).child("fullName").getValue(String.class);
                writeFeedbackFullName.setText(fullNameFromDB);
                writeFeedbackUserName.setText("@"+userNameGlobal);

                String image = snapshot.child(userNameGlobal).child("dp").getValue(String.class);
                Picasso.get().load(image).into(popupUserImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupClickProgress.setVisibility(View.VISIBLE);

                if (popupDescription.getText().toString().isEmpty()) {
                    popupDescription.setError("Empty field");
                }

                if (!popupDescription.getText().toString().isEmpty()) {
                    popupDescription.setError(null);
                    storePostInfoIntoDB();

                } else {
                    Toast.makeText(BrainTumorDetect.this, "Please verify all input fields and choose Post Image", Toast.LENGTH_SHORT).show();
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void storePostImageIntoDB(String key) {
        referenceImage = FirebaseStorage.getInstance().getReference("posts").child(userDetails.get(SessionManager.KEY_USERNAME));
        referenceImage.child(key).putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(BrainTumorDetect.this, "image uploaded", Toast.LENGTH_SHORT).show();
                referenceImage.child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        post.setPostImage(uri.toString());

                        referenceFeedbacks.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(BrainTumorDetect.this, "Post Added", Toast.LENGTH_SHORT).show();
                                popAddPost.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BrainTumorDetect.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //referenceUser.child("dp").setValue(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BrainTumorDetect.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BrainTumorDetect.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storePostInfoIntoDB() {
        //checkInternet
        try {
            String command = "ping -c 1 google.com";
            if(Runtime.getRuntime().exec(command).waitFor() != 0) {
                noInternetLottieAnimation.setVisibility(View.VISIBLE);
                popupClickProgress.setVisibility(View.INVISIBLE);
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please connect to INTERNET", Toast.LENGTH_SHORT).show();
        }

        String postDesc = popupDescription.getText().toString();

        post = new Post(postDesc, myRating, "", userDetails.get(SessionManager.KEY_USERNAME), 0, 0);

        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks").push();
        String key = referenceFeedbacks.getKey();
        post.setPostKey(key);

        storePostImageIntoDB(key);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BrainTumorDetect.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}