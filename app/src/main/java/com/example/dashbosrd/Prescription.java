package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Prescription extends AppCompatActivity {

    LinearLayout downloadLayout2;
    final static int REQUEST_CODE = 1232;
    private static final int CREATE_FILE = 1;
    Bitmap bmp, scaledcmp, bitmap;

    TextView doctorNameTex, qualificationTex, specialityTextview, doctorContactTex, doctorEmailTex,
            serialNoTex, dateTex, patientNameTextView, ageTextView, genderTex, bloodTex, weightTex, heightTex,
            chiefProblemTex, clinicalFindingsTex, clinicalDiagnosisTex, examinationTex, treatmentPlanTex, adviceTex,
            medicineTex;

    LinearLayout downloadPDFBtn1, sharePDFBtn1;

    String appointmentKey;

    DatabaseReference referenceAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        downloadLayout2 = findViewById(R.id.downloadLayout2Id);

        doctorNameTex = findViewById(R.id.doctorNameTex);
        qualificationTex = findViewById(R.id.qualificationTex);
        specialityTextview = findViewById(R.id.specialityTextview);
        doctorContactTex = findViewById(R.id.doctorContactTex);
        doctorEmailTex = findViewById(R.id.doctorEmailTex);
        serialNoTex = findViewById(R.id.serialNoTex);
        dateTex = findViewById(R.id.dateTex);
        patientNameTextView = findViewById(R.id.patientNameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        genderTex = findViewById(R.id.genderTex);
        bloodTex = findViewById(R.id.bloodTex);
        weightTex = findViewById(R.id.weightTex);
        heightTex = findViewById(R.id.heightTex);
        chiefProblemTex = findViewById(R.id.chiefProblemTex);
        clinicalFindingsTex = findViewById(R.id.clinicalFindingsTex);
        clinicalDiagnosisTex = findViewById(R.id.clinicalDiagnosisTex);
        examinationTex = findViewById(R.id.examinationTex);
        treatmentPlanTex = findViewById(R.id.treatmentPlanTex);
        adviceTex = findViewById(R.id.adviceTex);
        medicineTex = findViewById(R.id.medicineTex);

        downloadPDFBtn1 = findViewById(R.id.downloadPDFBtn1);
        sharePDFBtn1 = findViewById(R.id.sharePDFBtn1);

        appointmentKey = getIntent().getStringExtra("appointmentKey");

        loadFromDB();

        downloadPDFBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
                bitmap = loadBitmapFromView(downloadLayout2,downloadLayout2.getWidth(),downloadLayout2.getHeight());
                covertXMLtoPDF();
                downloadPDFBtn1.setVisibility(View.INVISIBLE);

                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1500);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                sharePDFBtn1.setAnimation(anim);

                sharePDFBtn1.setVisibility(View.VISIBLE);
            }
        });

        sharePDFBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String fileName = "example_XML.pdf";
                File file = new File(downloadsDir, fileName);

                if(file.exists()) {
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    share.setType("application/pdf");
                    startActivity(share);
                }
            }
        });
    }

    private void loadFromDB() {
        referenceAppointment = FirebaseDatabase.getInstance().getReference("appointments");

        referenceAppointment.child(appointmentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    doctorNameTex.setText(snapshot.child("doctor").getValue(String.class));
                    qualificationTex.setText(snapshot.child("qualification").getValue(String.class));
                    specialityTextview.setText(snapshot.child("speciality").getValue(String.class));
                    doctorContactTex.setText(snapshot.child("doctorContact").getValue(String.class));
                    doctorEmailTex.setText(snapshot.child("doctorEmail").getValue(String.class));
                    serialNoTex.setText(snapshot.child("serialNo").getValue(String.class));
                    dateTex.setText(snapshot.child("appDate").getValue(String.class));
                    patientNameTextView.setText(snapshot.child("patientName").getValue(String.class));
                    ageTextView.setText(snapshot.child("age").getValue(String.class));
                    genderTex.setText(snapshot.child("gender").getValue(String.class));
                    bloodTex.setText(snapshot.child("blood").getValue(String.class));
                    weightTex.setText(snapshot.child("weight").getValue(String.class));
                    heightTex.setText(snapshot.child("height").getValue(String.class));
                    chiefProblemTex.setText(snapshot.child("chiefComplaints").getValue(String.class));
                    clinicalFindingsTex.setText(snapshot.child("clinicalFindings").getValue(String.class));
                    clinicalDiagnosisTex.setText(snapshot.child("clinicalDiagnosis").getValue(String.class));
                    examinationTex.setText(snapshot.child("examination").getValue(String.class));
                    treatmentPlanTex.setText(snapshot.child("treatmentPlan").getValue(String.class));
                    adviceTex.setText(snapshot.child("advice").getValue(String.class));
                    medicineTex.setText(snapshot.child("investigation").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && false == Environment.isExternalStorageManager()) {
            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    private Bitmap loadBitmapFromView(LinearLayout linearLayout, int width, int height) {
        bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }

    private void covertXMLtoPDF() {
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_app_rating, null);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            this.getDisplay().getRealMetrics(displayMetrics);
//        } else {
//            this.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
//        }
//
//        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
//
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        //-----------------------------------------------------
        DisplayMetrics displayMetrics=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height=displayMetrics.heightPixels;
        float width=displayMetrics.widthPixels;

        int convertHeight=(int)height,
                convertWidth=(int)width;

        Toast.makeText(this, convertWidth + " " + convertHeight, Toast.LENGTH_SHORT).show();

        PdfDocument document=new PdfDocument();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(convertWidth,convertHeight,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);

        Canvas canvas=page.getCanvas();
        Paint paint=new Paint();
        canvas.drawPaint(paint);
        bitmap=Bitmap.createScaledBitmap(bitmap,convertWidth,convertHeight,true);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);
        //---------------------------------------------------------

//        PdfDocument document = new PdfDocument();
//
//        int viewWidth = view.getMeasuredWidth();
//        int viewHeight = view.getMeasuredHeight();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//        view.draw(canvas);
//
//        document.finishPage(page);

//        String targetPdf="page.pdf";
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example_XML.pdf";
        File file = new File(downloadsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "File converted successfully", Toast.LENGTH_SHORT).show();
            openPdf();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void openPdf() {
        try {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "example_XML.pdf";
            File file = new File(downloadsDir, fileName);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri=Uri.fromFile(file);
            intent.setDataAndType(uri,"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application found for pdf reader", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }

}