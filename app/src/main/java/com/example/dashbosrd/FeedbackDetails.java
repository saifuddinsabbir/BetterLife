package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FeedbackDetails extends AppCompatActivity {

    ScrollView scrollView;

    ImageView postBackButton;

    ImageView feedbackPostUserImage, feedbackCommentUserImage;
    TextView feedbackPostFullName, feedbackPostUserName, feedbackPostTimestamp, feedbackPostExperience,
            feedbackPostDescription, feedbackPostLikeCount, feedbackPostCommentCount, postHeading;
    EditText feedbackCommentEditText;
    LinearLayout feedbackPostLikeButton;
    ImageView feedbackPostImage, feedbackPostLikeButtonImage, feedbackCommentAddButton;
    RatingBar feedbackPostRatingBar;

    RecyclerView recycleViewComment;

    DatabaseReference referenceFeedbacks, referenceUsers, referenceComments, referenceCommentsAdd, referenceLikes,
            referenceLikesFetch;
    Query checkFeedbackPost, checkPostUser, checkCommentUser;
    String postKeyGlobal, userNameGlobal;

    List<Like> listLike;
    List<Comment> listComment;

    CommentAdapter commentAdapter;

    String postUserImageFromDB, postFullNameFromDB, postUserNameFromDB, dateTimeFromDB, ratingFromDB, postImageFromDB,
            postDescFromDB, commentUserImage, commentFullName, likeCountFromDB, commentCountFromDB;

    Boolean userOnceLiked = false, userLiked = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);

        setUserNameGlobally();
        setPostKeyGlobally();

        //connect to database
        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");
        //checkFeedbackPost = referenceFeedbacks.orderByChild("postKey").equalTo(postKeyGlobal);

        postBackButton = findViewById(R.id.postBackButtonId);
        scrollView = findViewById(R.id.scrollViewId);
        postHeading = findViewById(R.id.postHeadingId);
        feedbackPostUserImage = findViewById(R.id.feedbackPostUserImage);
        feedbackPostFullName = findViewById(R.id.feedbackPostFullName);
        feedbackPostUserName = findViewById(R.id.feedbackPostUserNameId);
        feedbackPostTimestamp = findViewById(R.id.feedbackPostTimestampId);
        feedbackPostRatingBar = findViewById(R.id.feedbackPostRatingBarId);
        feedbackPostExperience = findViewById(R.id.feedbackPostExperienceId);
        feedbackPostImage = findViewById(R.id.feedbackPostImageId);
        feedbackPostDescription = findViewById(R.id.feedbackPostDescriptionId);
        feedbackPostLikeCount = findViewById(R.id.feedbackPostLikeCountId);
        feedbackPostCommentCount = findViewById(R.id.feedbackPostCommentCountId);
        feedbackPostLikeButtonImage = findViewById(R.id.feedbackPostLikeButtonImageId);
        feedbackPostLikeButton = findViewById(R.id.feedbackPostLikeButtonId);
        feedbackCommentUserImage = findViewById(R.id.feedbackCommentUserImageId);
        feedbackCommentEditText = findViewById(R.id.feedbackCommentEditTextId);
        feedbackCommentAddButton = findViewById(R.id.feedbackCommentAddButtonId);

        recycleViewComment = findViewById(R.id.recycleViewCommentId);

        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.setSmoothScrollingEnabled(true);

        postBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setDataFromExtras();

        feedbackPostLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLikeOnDatabase();
            }
        });

        feedbackCommentAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackCommentAddButton.setVisibility(View.INVISIBLE);
                storeCommentOnDatabase();
            }
        });
    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    private void setPostKeyGlobally() {
        postKeyGlobal = getIntent().getStringExtra("postKey");
    }

    private void setDataFromExtras() {
        postUserNameFromDB = getIntent().getStringExtra("userName");
        dateTimeFromDB = timestampToString(getIntent().getExtras().getLong("dateTime"));
        ratingFromDB = getIntent().getStringExtra("rating");
        postDescFromDB = getIntent().getStringExtra("desc");
        postImageFromDB = getIntent().getStringExtra("postImage");
        likeCountFromDB = getIntent().getStringExtra("likes");
        commentCountFromDB = getIntent().getStringExtra("comments");

        referenceUsers = FirebaseDatabase.getInstance().getReference("users");
        checkCommentUser = referenceUsers.orderByChild("userName").equalTo(postUserNameFromDB);
        checkCommentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postUserImageFromDB = snapshot.child(postUserNameFromDB).child("dp").getValue(String.class);
                Picasso.get().load(postUserImageFromDB).into(feedbackPostUserImage);
                postFullNameFromDB = snapshot.child(postUserNameFromDB).child("fullName").getValue(String.class);
                feedbackPostFullName.setText(postFullNameFromDB);
                postHeading.setText(postFullNameFromDB + "'s Post");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Picasso.get().load(postImageFromDB).into(feedbackPostImage);

        feedbackPostUserName.setText("@" + postUserNameFromDB);
        feedbackPostTimestamp.setText(dateTimeFromDB);
        feedbackPostRatingBar.setRating(Float.parseFloat(ratingFromDB));

        if (Float.parseFloat(ratingFromDB) <= 1) {
            feedbackPostExperience.setText("Very bad");
        } else if (Float.parseFloat(ratingFromDB) <= 2) {
            feedbackPostExperience.setText("Bad");
        } else if (Float.parseFloat(ratingFromDB) <= 3) {
            feedbackPostExperience.setText("Not bad");
        } else if (Float.parseFloat(ratingFromDB) <= 4) {
            feedbackPostExperience.setText("Good");
        } else {
            feedbackPostExperience.setText("Very good");
        }

        feedbackPostDescription.setText(postDescFromDB);


        referenceUsers = FirebaseDatabase.getInstance().getReference("users");
        checkCommentUser = referenceUsers.orderByChild("userName").equalTo(userNameGlobal);
        checkCommentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentUserImage = snapshot.child(userNameGlobal).child("dp").getValue(String.class);
                Picasso.get().load(commentUserImage).into(feedbackCommentUserImage);
                commentFullName = snapshot.child(userNameGlobal).child("fullName").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fetchlikes();

        //fetch comments in recycleview
        fetchComments();
    }

    private void fetchlikes() {
        referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(postKeyGlobal);
        referenceLikesFetch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String userNameThis = snap.child("userName").getValue(String.class);
                    Boolean d = snap.child("liked").getValue(Boolean.class);

                    if (userNameThis.equals(userNameGlobal)) {
                        userOnceLiked = true;
                        userLiked = d;

                        if(d) {
                            feedbackPostLikeButtonImage.setImageResource(R.drawable.liked_vector);
                        } else {
                            feedbackPostLikeButtonImage.setImageResource(R.drawable.like3);
                        }
                    }

                    if (d) {
                        count++;
                    }
//                    Toast.makeText(FeedbackDetails.this, userNameThis + " " + d, Toast.LENGTH_SHORT).show();
                }

                if(count == 0) {
                    feedbackPostLikeCount.setText("No likes yet");
                } else {
                    feedbackPostLikeCount.setText(count + " likes");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchComments() {

        recycleViewComment.setLayoutManager(new LinearLayoutManager(this));

        referenceCommentsAdd = FirebaseDatabase.getInstance().getReference("comments").child(postKeyGlobal);
        referenceCommentsAdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(FeedbackDetails.this, listComment);
                recycleViewComment.setAdapter(commentAdapter);

                if(listComment.isEmpty()) {
                    feedbackPostCommentCount.setText("No comments yet");
                } else {
                    feedbackPostCommentCount.setText(listComment.size() + " comments");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void storeLikeOnDatabase() {

        if (userLiked) {
            referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(postKeyGlobal);
            referenceLikesFetch.child(userNameGlobal).child("liked").setValue(false);
            feedbackPostLikeButtonImage.setImageResource(R.drawable.like3);
//            Toast.makeText(this, "Uniked", Toast.LENGTH_SHORT).show();
        } else if (userOnceLiked) {
            referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(postKeyGlobal);
            referenceLikesFetch.child(userNameGlobal).child("liked").setValue(true);
            feedbackPostLikeButtonImage.setImageResource(R.drawable.liked_vector);
//            Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
        } else {
            referenceLikes = FirebaseDatabase.getInstance().getReference("likes").child(postKeyGlobal);
            Like like = new Like(userNameGlobal, true);
            referenceLikes.child(userNameGlobal).setValue(like);
            feedbackPostLikeButtonImage.setImageResource(R.drawable.liked_vector);
//            Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeCommentOnDatabase() {
        String userImage = commentUserImage;
        String fullName = commentFullName;
        String commentContent = feedbackCommentEditText.getText().toString();

        if (!commentContent.isEmpty()) {
            referenceComments = FirebaseDatabase.getInstance().getReference("comments").child(postKeyGlobal).push();

            Comment comment = new Comment(userImage, fullName, userNameGlobal, commentContent);

            referenceComments.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(FeedbackDetails.this, "Comment added", Toast.LENGTH_SHORT).show();
                    feedbackCommentEditText.setText("");
                    feedbackCommentAddButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Toast.makeText(this, "Empty comment", Toast.LENGTH_SHORT).show();
        }
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd MMM, yyyy | hh:mm a",calendar).toString();
        return date;
    }
}