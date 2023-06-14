package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class AppRating extends AppCompatActivity {

    float myRating;
    RatingBar writeFeedbackRatingBar;

    TextView writeFeedbackAvarageRating;

    double sumOfAllrating;
    String averageRating;
    int totalNoOfRating;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    DatabaseReference referenceFeedbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rating);

        referenceFeedbacks = FirebaseDatabase.getInstance().getReference("feedbacks");

        writeFeedbackRatingBar = findViewById(R.id.appFeedbackRatingBarId);
        writeFeedbackAvarageRating = findViewById(R.id.averageRatingAppId);

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

                    writeFeedbackRatingBar.setRating((float) (sumOfAllrating) / (totalNoOfRating));

                    averageRating = decfor.format((sumOfAllrating) / (totalNoOfRating));

                    writeFeedbackAvarageRating.setText("Average Rating: "  + averageRating + "/5.00");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(AppRating.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}