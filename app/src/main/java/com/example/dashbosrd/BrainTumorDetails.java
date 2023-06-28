package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.io.FileReader;

public class BrainTumorDetails extends AppCompatActivity {

    TextView classHeading, whatIsTex, overViewTex, characteristicsTex, symptomTex, diagnosisTex, treatmentTex;

    ImageView tumorImage;

    YouTubePlayerView youtubePlayerCharacteristics, youtubePlayerTreatment;

    DatabaseReference referenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tumor_details);

        classHeading = findViewById(R.id.classHeadingId);
        whatIsTex = findViewById(R.id.whatIsTex);
        overViewTex = findViewById(R.id.overViewTex);
        characteristicsTex = findViewById(R.id.characteristicsTex);
        symptomTex = findViewById(R.id.symptomTex);
        diagnosisTex = findViewById(R.id.diagnosisTex);
        treatmentTex = findViewById(R.id.treatmentTex);
        tumorImage = findViewById(R.id.tumorImage);
        youtubePlayerCharacteristics = findViewById(R.id.youtubePlayerCharacteristics);
        youtubePlayerTreatment = findViewById(R.id.youtubePlayerTreatment);

        String type = getIntent().getStringExtra("type");

        referenceClass = FirebaseDatabase.getInstance().getReference("classes").child(type);
        referenceClass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classHeading.setText(Html.fromHtml(snapshot.child("className").getValue(String.class).replace("\\n", "<br/>")));
                whatIsTex.setText(Html.fromHtml(snapshot.child("whatis").getValue(String.class).replace("\\n", "<br/>")));
                overViewTex.setText(Html.fromHtml(snapshot.child("overview").getValue(String.class).replace("\\n", "<br/>")));
                characteristicsTex.setText(Html.fromHtml(snapshot.child("characteristics").getValue(String.class).replace("\\n", "<br/>")));
                symptomTex.setText(Html.fromHtml(snapshot.child("symptom").getValue(String.class).replace("\\n", "<br/>")));
                diagnosisTex.setText(Html.fromHtml(snapshot.child("diagnosis").getValue(String.class).replace("\\n", "<br/>")));
                treatmentTex.setText(Html.fromHtml(snapshot.child("treatment").getValue(String.class).replace("\\n", "<br/>")));

                Picasso.get().load(snapshot.child("image").getValue(String.class)).into(tumorImage);

                try {
                    getLifecycle().addObserver(youtubePlayerCharacteristics);

                    youtubePlayerCharacteristics.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(snapshot.child("youtube_characteristics").getValue(String.class), 0);
                        }
                    });

                    getLifecycle().addObserver(youtubePlayerTreatment);

                    youtubePlayerTreatment.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(snapshot.child("youtube_treatment").getValue(String.class), 0);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}