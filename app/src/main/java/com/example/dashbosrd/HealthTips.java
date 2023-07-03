package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class HealthTips extends AppCompatActivity {

    interface RequestWeather {
        @GET
        Call<WeatherData> getWeather(@Url String url);
    }

    TextInputEditText searchDoctorBox;
    ImageButton searchBtn;

    ImageView currentLocationBtn;

    LinearLayout weatherCard;

    TextView locationName, weatherState, temperatureTex, feelsLikeTex, windSpeedTex, windDirectionTex,
            latitudeTex, longitudeTex, dayTex, lastUpdatedTex;

    LottieAnimationView loadingLottieAnimation, notFoundLottieAnimation;
    TextView notFoundTex;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Double latitudeGlobal, longitudeGlobal;

    DatabaseReference referenceLocations;

    Boolean z = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        searchDoctorBox = findViewById(R.id.searchLocationBox);
        searchBtn = findViewById(R.id.searchBtn);
        currentLocationBtn = findViewById(R.id.currentLocationBtn);
        weatherCard = findViewById(R.id.weatherCard);
        locationName = findViewById(R.id.locationName);
        weatherState = findViewById(R.id.weatherState);
        temperatureTex = findViewById(R.id.temperatureTex);
        feelsLikeTex = findViewById(R.id.feelsLikeTex);
        windSpeedTex = findViewById(R.id.windSpeedTex);
        windDirectionTex = findViewById(R.id.windDirectionTex);
        latitudeTex = findViewById(R.id.latitudeTex);
        longitudeTex = findViewById(R.id.longitudeTex);
        dayTex = findViewById(R.id.dayTex);
        lastUpdatedTex = findViewById(R.id.lastUpdatedTex);

        loadingLottieAnimation = findViewById(R.id.loadingLottieAnimation);
        notFoundLottieAnimation = findViewById(R.id.notFoundLottieAnimation);
        notFoundTex = findViewById(R.id.notFoundTex);

        loadingLottieAnimation.setVisibility(View.GONE);
        notFoundLottieAnimation.setVisibility(View.GONE);
        notFoundTex.setVisibility(View.GONE);

        Animation anim = new AlphaAnimation(0.5f, 1.0f);
        anim.setDuration(1500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        currentLocationBtn.setAnimation(anim);

        weatherCard.setVisibility(View.GONE);

        //Add permission
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherCard.setVisibility(View.GONE);
                notFoundLottieAnimation.setVisibility(View.GONE);
                notFoundTex.setVisibility(View.INVISIBLE);
                loadingLottieAnimation.setVisibility(View.VISIBLE);
                setLocationFromDB();
            }
        });

        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherCard.setVisibility(View.GONE);
                notFoundLottieAnimation.setVisibility(View.GONE);
                notFoundTex.setVisibility(View.INVISIBLE);
                loadingLottieAnimation.setVisibility(View.VISIBLE);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//Check gps is enable or not
                    OnGPS();    //Write Function To enable gps
                } else {
                    getLocation();  //GPS is already On then
                }

                try {
                    createCard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void setLocationFromDB() {
        String typedLocation = searchDoctorBox.getText().toString();

        referenceLocations = FirebaseDatabase.getInstance().getReference("locations");

        z = false;

        referenceLocations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot locationSnap : snapshot.getChildren()) {
                    String city = locationSnap.child("city").getValue(String.class);


                    if ((city.toLowerCase()).equals((typedLocation.replace(" ", "").replace(".", "")).toLowerCase())) {
                        latitudeGlobal = locationSnap.child("latitude").getValue(Double.class);
                        longitudeGlobal = locationSnap.child("longitude").getValue(Double.class);

                        locationName.setText(city);
                        z = true;

//                        Toast.makeText(HealthTips.this, latitudeGlobal + " " + longitudeGlobal, Toast.LENGTH_SHORT).show();
                    }

                    if(z) {
                        try {
                            createCard();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        loadingLottieAnimation.setVisibility(View.INVISIBLE);
                        notFoundLottieAnimation.setVisibility(View.VISIBLE);
                        notFoundTex.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(HealthTips.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HealthTips.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

//                latitudeGlobal = String.valueOf(lat);
//                longitudeGlobal = String.valueOf(longi);

                latitudeGlobal = lat;
                longitudeGlobal = longi;

//                Toast.makeText(this, "Your Location:" + "\n" + "Latitude= " + latitudeGlobal + "\n" + "Longitude= " + longitudeGlobal, Toast.LENGTH_SHORT).show();
            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitudeGlobal = lat;
                longitudeGlobal = longi;

//                Toast.makeText(this, "Your Location:" + "\n" + "Latitude= " + latitudeGlobal + "\n" + "Longitude= " + longitudeGlobal, Toast.LENGTH_SHORT).show();

            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitudeGlobal = lat;
                longitudeGlobal = longi;

//                Toast.makeText(this, "Your Location:" + "\n" + "Latitude= " + latitudeGlobal + "\n" + "Longitude= " + longitudeGlobal, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            if (latitudeGlobal > 23) {
                locationName.setText("Feni");
            } else {
                locationName.setText("Chittagong");
            }
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createCard() throws InterruptedException {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.open-meteo.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            RequestWeather requestWeather = retrofit.create(RequestWeather.class);


            double latitude = 19.0760, longitude = 72.8777;

            try {
                requestWeather.getWeather("/v1/forecast?latitude=" + latitudeGlobal + "&longitude=" + longitudeGlobal + "&current_weather=true").enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
//                        textView.setText(response.body().currentWeather.temperature + "");
//                        Toast.makeText(HealthTips.this, "Temperature: " + response.body().current_weather.temperature +
//                                "\nWind Speed: " + response.body().current_weather.windspeed +
//                                "\nTime: " + response.body().current_weather.time, Toast.LENGTH_SHORT).show();

                        temperatureTex.setText(response.body().current_weather.temperature+"°C");
                        feelsLikeTex.setText(String.format("%.2f", (response.body().current_weather.temperature+2.67))+"°C");
                        windSpeedTex.setText((response.body().current_weather.windspeed)+" km/h");
                        windDirectionTex.setText((response.body().current_weather.winddirection)+"°");

                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                        dayTex.setText(day);

                        String lasUpdated = response.body().current_weather.time.substring(11);
                        lastUpdatedTex.setText(lasUpdated);

                        latitudeTex.setText(latitudeGlobal + "° N");
                        longitudeTex.setText(longitudeGlobal + "° E");


                        notFoundLottieAnimation.setVisibility(View.GONE);
                        notFoundTex.setVisibility(View.INVISIBLE);
                        loadingLottieAnimation.setVisibility(View.INVISIBLE);
                        weatherCard.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Toast.makeText(HealthTips.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}