package com.example.dashbosrd;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Feedback extends AppCompatActivity {

    //Feedback list
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView feedbackListRecycleView;
    PostAdapter postAdapter;

    ImageView postBackButton;

    //POP_UP
    Dialog popAddPost;
    ImageView  popupAddBtn, writeFeedbakCancelButton, popupUserImage, popupPostImage, postListImage;
    TextInputEditText popupDescription;
    ProgressBar popupClickProgress;
    TextView writeFeedbackFullName, writeFeedbackUserName, writeFeedbackAvarageRating, writeFeedbackExperianceRating;

    Uri pickedPhotoUri;

    float myRating;
    RatingBar writeFeedbackRatingBar;

    Post post;

    LinearLayout prev_btn, next_btn;
    ImageView prevArrowImage;
    TextView one, prevTex;



    //DATABASE----------------
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference reference, referenceFeedbacks;
    StorageReference referenceImage;
    Query checkUser;

    RelativeLayout relativeLayout;
    LottieAnimationView lottieAnimation, imagePostLottieAnimation, noInternetLottieAnimation;

    double sumOfAllrating;
    int totalNoOfRating;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    String key = null, startKey = null;
    boolean isLoading = false;

    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    String userNameGlobal;
    int pageNumber = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");

        postBackButton = findViewById(R.id.postBackButtonId);
        lottieAnimation = findViewById(R.id.lottieAnimationId);
        relativeLayout = findViewById(R.id.relativeLayoutId);
        prevArrowImage = findViewById(R.id.prevArrowImage);
        prevTex = findViewById(R.id.prevTex);

        //Feedback list
        feedbackListRecycleView = findViewById(R.id.feedbackListRecycleViewId);
        feedbackListRecycleView = findViewById(R.id.feedbackListRecycleViewId);
        feedbackListRecycleView.setLayoutManager(new LinearLayoutManager(Feedback.this));
        feedbackListRecycleView.setHasFixedSize(true);

        postAdapter = new PostAdapter(Feedback.this);
        feedbackListRecycleView.setAdapter(postAdapter);

        loadLastData();
//        loadData();

        prev_btn = findViewById(R.id.prev_btn);
        one = findViewById(R.id.one);
        next_btn = findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber++;
                loadData();
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber--;
                loadPrevData();
            }
        });

        postBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iniPopup();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });
    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    private void loadLastData() {
        int contentSize = 3;

        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");

        referenceFeedbacks.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot feedbackSnap : snapshot.getChildren()) {
                    key = feedbackSnap.getKey();
                }

                Query query;
                query = referenceFeedbacks.orderByKey().endAt(key).limitToLast(contentSize);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Post> feedbackList = new ArrayList<>();
                        for(DataSnapshot feedbackSnap : snapshot.getChildren()) {
                            Post feedback = feedbackSnap.getValue(Post.class);
                            if(feedbackList.isEmpty()) {
                                startKey = feedbackSnap.getKey();
                            }
                            feedbackList.add(feedback);
                            key = feedbackSnap.getKey();
                        }

                        one.setText(pageNumber+"");

                        if(pageNumber == 1) {
                            prev_btn.setBackgroundResource(R.drawable.solid_btn_2);
                            prev_btn.setClickable(false);
                            prev_btn.setElevation(0);
                            prev_btn.setEnabled(false);
                        } else {
                            prev_btn.setBackgroundResource(R.drawable.solid_btn);
                            prev_btn.setClickable(true);
                            prev_btn.setElevation(5);
                            prev_btn.setEnabled(true);
                        }

                        if(feedbackList.size() < contentSize) {
                            next_btn.setBackgroundResource(R.drawable.solid_btn_2);
                            next_btn.setClickable(false);
                            next_btn.setElevation(0);
                            next_btn.setEnabled(false);
                        } else {
                            next_btn.setBackgroundResource(R.drawable.solid_btn);
                            next_btn.setClickable(true);
                            next_btn.setElevation(5);
                            next_btn.setEnabled(true);
                        }

                        Collections.reverse(feedbackList);
                        postAdapter.setItems((ArrayList<Post>) feedbackList);
                        postAdapter.notifyDataSetChanged();
                        feedbackListRecycleView.smoothScrollToPosition(0);
                        isLoading = false;
                        relativeLayout.setBackgroundColor(Color.parseColor("#1B03A9F4"));
                        lottieAnimation.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void loadData() {
        int contentSize = 3;

        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");

        Query query;
        if(key == null) {
            query = referenceFeedbacks.orderByKey().limitToFirst(contentSize);
        } else {
            query = referenceFeedbacks.orderByKey().endBefore(startKey).limitToLast(contentSize);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> feedbackList = new ArrayList<>();
                for(DataSnapshot feedbackSnap : snapshot.getChildren()) {
                    Post feedback = feedbackSnap.getValue(Post.class);
                    if(feedbackList.isEmpty()) {
                        startKey = feedbackSnap.getKey();
                    }
                    feedbackList.add(feedback);
                    key = feedbackSnap.getKey();
                }

                one.setText(pageNumber+"");

                if(pageNumber == 1) {
                    prev_btn.setBackgroundResource(R.drawable.solid_btn_2);
                    prev_btn.setClickable(false);
                    prev_btn.setElevation(0);
                    prev_btn.setEnabled(false);
                } else {
                    prev_btn.setBackgroundResource(R.drawable.solid_btn);
                    prev_btn.setClickable(true);
                    prev_btn.setElevation(5);
                    prev_btn.setEnabled(true);
                }

                if(feedbackList.size() < contentSize) {
                    next_btn.setBackgroundResource(R.drawable.solid_btn_2);
                    next_btn.setClickable(false);
                    next_btn.setElevation(0);
                    next_btn.setEnabled(false);
                } else {
                    next_btn.setBackgroundResource(R.drawable.solid_btn);
                    next_btn.setClickable(true);
                    next_btn.setElevation(5);
                    next_btn.setEnabled(true);
                }

                Collections.reverse(feedbackList);
                postAdapter.setItems((ArrayList<Post>) feedbackList);
                postAdapter.notifyDataSetChanged();
                feedbackListRecycleView.smoothScrollToPosition(0);
                isLoading = false;
                relativeLayout.setBackgroundColor(Color.parseColor("#1B03A9F4"));
                lottieAnimation.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadPrevData() {
        int contentSize = 3;
        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");

        Query query;
        if(key == null) {
            query = referenceFeedbacks.orderByKey().limitToFirst(contentSize);
        } else {
            query = referenceFeedbacks.orderByKey().startAfter(key).limitToFirst(contentSize);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> feedbackList = new ArrayList<>();
                for(DataSnapshot feedbackSnap : snapshot.getChildren()) {
                    Post feedback = feedbackSnap.getValue(Post.class);
                    if(feedbackList.isEmpty()) {
                        startKey = feedbackSnap.getKey();
                    }
                    feedbackList.add(feedback);
                    key = feedbackSnap.getKey();
                }

                one.setText(pageNumber+"");

                if(pageNumber == 1) {
                    prev_btn.setBackgroundResource(R.drawable.solid_btn_2);
                    prev_btn.setClickable(false);
                    prev_btn.setElevation(0);
                    prev_btn.setEnabled(false);
                } else {
                    prev_btn.setBackgroundResource(R.drawable.solid_btn);
                    prev_btn.setClickable(true);
                    prev_btn.setElevation(5);
                    prev_btn.setEnabled(true);
                }

                if(feedbackList.size() < contentSize) {
                    next_btn.setBackgroundResource(R.drawable.solid_btn_2);
                    next_btn.setClickable(false);
                    next_btn.setElevation(0);
                    next_btn.setEnabled(false);
                } else {
                    next_btn.setBackgroundResource(R.drawable.solid_btn);
                    next_btn.setClickable(true);
                    next_btn.setElevation(5);
                    next_btn.setEnabled(true);
                }

                Collections.reverse(feedbackList);
                postAdapter.setItems((ArrayList<Post>) feedbackList);
                postAdapter.notifyDataSetChanged();
                feedbackListRecycleView.smoothScrollToPosition(0);
                isLoading = false;
                relativeLayout.setBackgroundColor(Color.parseColor("#1B03A9F4"));
                lottieAnimation.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //fetchFeedbackListFromDatabase();
    }

    private void fetchFeedbackListFromDatabase() {
        //Get list of Feedback from database
        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");
        referenceFeedbacks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post>feedbackList = new ArrayList<>();
                for(DataSnapshot feedbackSnap : snapshot.getChildren()) {
                    Post feedback = feedbackSnap.getValue(Post.class);
                    feedbackList.add(feedback);
                }

//                postAdapter = new PostAdapter(Feedback.this, feedbackList);
                feedbackListRecycleView.setAdapter(postAdapter);
                lottieAnimation.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setWindowAnimations(R.style.animation );
        popAddPost.getWindow().setBackgroundDrawable(getDrawable(R.drawable.popup_background));
        popAddPost.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.CENTER;
        popAddPost.setCancelable(false);

        // ini popup widgets
        writeFeedbakCancelButton = popAddPost.findViewById(R.id.writeFeedbakCancelButtonId);
        imagePostLottieAnimation = popAddPost.findViewById(R.id.imagePostLottieAnimationId);
        noInternetLottieAnimation = popAddPost.findViewById(R.id.noInternetLottieAnimationId);
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

//        Animation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(1500);
//        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
//        anim.setRepeatCount(Animation.INFINITE);
//        postListImage.setAnimation(anim);

        pickImageForPost();

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
                    //Toast.makeText(Feedback.this, sumOfAllrating + " " + totalNoOfRating, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(Feedback.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUser = reference.orderByChild("userName").equalTo(userNameGlobal);
        storage = FirebaseStorage.getInstance();
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
                    Toast.makeText(Feedback.this, "Please verify all input fields and choose Post Image", Toast.LENGTH_SHORT).show();
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void pickImageForPost() {

        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        pickedPhotoUri = result.getData().getData();
                        postListImage.setImageURI(pickedPhotoUri);
                        //addImage.setVisibility(View.INVISIBLE);
                        imagePostLottieAnimation.setVisibility(View.INVISIBLE);
                    }
                }

        );
        imagePostLottieAnimation.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });
    }

    private void storePostImageIntoDB(String key) {

        referenceImage = FirebaseStorage.getInstance().getReference("posts").child(userDetails.get(SessionManager.KEY_USERNAME));
        referenceImage.child(key).putFile(pickedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(Feedback.this, "image uploaded", Toast.LENGTH_SHORT).show();
                referenceImage.child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        post.setPostImage(uri.toString());

                        referenceFeedbacks.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);

                                builder.setMessage("The post is added as a feedback").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                popAddPost.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Feedback.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //referenceUser.child("dp").setValue(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Feedback.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Feedback.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

        super.onBackPressed();
    }
}