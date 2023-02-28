package com.example.dashbosrd;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class LeukemiaDetect extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView pickImage;
    Button galleryButton, cameraButton;

    //----------------------- Bottom Navigation --------------------
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leukemia_detect);

        pickImage = findViewById(R.id.pickImageLeukemiaId);
        cameraButton = findViewById(R.id.cameraButtonLeukemiaId);
        galleryButton = findViewById(R.id.galleryButtonLeukemiaId);

        //take photos with CAMERA
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData()!=null)
                {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap =(Bitmap) bundle.get("data");
                    pickImage.setImageBitmap(bitmap);
                }
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager())!=null)
                {
                    activityResultLauncher.launch(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"There is no app that support this action",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //choose from GALLERY
        final ActivityResultLauncher<Intent> launcher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode()== RESULT_OK && result.getData()!=null){
                        Uri photoUri = result.getData().getData();
                        pickImage.setImageURI(photoUri);
                    }
                }
        );
        galleryButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });



    }



    public void chipNavigationBarLeukemiaDetect() {
        //----------------------------------- Bottom Navigation (start)--------------------------------------
        chipNavigationBar = findViewById(R.id.bottomNavbarLeukemiaDetectId);
        chipNavigationBar.setItemSelected(R.id.detectId, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aboutId:
                        startActivity(new Intent(LeukemiaDetect.this, AboutLeukemia.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.detectId:
                        //startActivity(new Intent(LeukemiaDetect.this, LeukemiaDetect.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.treatmentId:
                        startActivity(new Intent(LeukemiaDetect.this, LeukemiaTreatment.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }
        });
        //----------------------------------- Bottom Navigation (end)--------------------------------------
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LeukemiaDetect.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}