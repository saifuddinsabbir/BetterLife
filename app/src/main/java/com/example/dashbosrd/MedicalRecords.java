package com.example.dashbosrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicalRecords extends AppCompatActivity {

    ImageView historyBackButton;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView historyListRecycleView;
    HistoryAdapter historyAdapter;
    List<History> historyList, historyList2 = new ArrayList<>();

    ProgressBar progressBar1;

    LottieAnimationView searchingLottieAnimation, nothingLottieAnimation;

    DatabaseReference referenceHistory;

    boolean isLoading = false;
    String key = null;

    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    String userNameGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_records);

        setUserNameGlobally();

        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUsersDetailFromSession();

        searchingLottieAnimation = findViewById(R.id.searchingLottieAnimationId);
        nothingLottieAnimation = findViewById(R.id.nothingLottieAnimationId);

        nothingLottieAnimation.setVisibility(View.INVISIBLE);

        //History list
        progressBar1 = findViewById(R.id.progressBar1);
        historyBackButton = findViewById(R.id.historyBackButtonId);
        swipeRefreshLayout = findViewById(R.id.swip);
        historyListRecycleView = findViewById(R.id.historyListRecycleViewId);
        historyListRecycleView.setLayoutManager(new LinearLayoutManager(MedicalRecords.this));
        historyListRecycleView.setHasFixedSize(true);

        historyAdapter = new HistoryAdapter(MedicalRecords.this);
        historyListRecycleView.setAdapter(historyAdapter);

        loadData();

        historyListRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) historyListRecycleView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (totalItem < lastVisible + 3) {
                    if (!isLoading) {
                        isLoading = true;
                        loadData();

                    }
                }
            }
        });

        historyBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        referenceHistory = FirebaseDatabase.getInstance().getReference("history").child(userNameGlobal);

        Query query;
        if (key == null) {
            query = referenceHistory.orderByKey().limitToFirst(3);
        } else {
            query = referenceHistory.orderByKey().startAfter(key).limitToFirst(3);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList = new ArrayList<>();
                for (DataSnapshot historySnap : snapshot.getChildren()) {
                    String uid = historySnap.child("userId").getValue(String.class);

                    Toast.makeText(MedicalRecords.this, uid + " " + userNameGlobal, Toast.LENGTH_SHORT).show();

                    key = historySnap.getKey();

                    if (uid.equals(userNameGlobal)) {
                        History history = historySnap.getValue(History.class);
                        historyList.add(history);
                        historyList2.add(history);
                    }
                }

                if (historyList2.isEmpty()) {
                    nothingLottieAnimation.setVisibility(View.VISIBLE);
                }

                swipeRefreshLayout.setRefreshing(false);

                if(!historyList.isEmpty()) {
                    historyAdapter.setItems((ArrayList<History>) historyList);
                    historyAdapter.notifyDataSetChanged();
                    isLoading = false;
                    searchingLottieAnimation.setVisibility(View.INVISIBLE);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setUserNameGlobally() {
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        userNameGlobal = prefs.getString("userName", "null");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //fetchHistoryListFromDatabase();
    }

    private void fetchHistoryListFromDatabase() {
        //Get list of Feedback from database
        referenceHistory = FirebaseDatabase.getInstance().getReference("history");
        referenceHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList = new ArrayList<>();
                for (DataSnapshot historySnap : snapshot.getChildren()) {
                    String uid = historySnap.child("userId").getValue(String.class);

                    if (uid.equals(userNameGlobal)) {
                        History history = historySnap.getValue(History.class);
                        historyList.add(history);

                    }
                    //Toast.makeText(MedicalRecords.this, uid, Toast.LENGTH_SHORT).show();
                }
                searchingLottieAnimation.setVisibility(View.INVISIBLE);
                //historyAdapter = new HistoryAdapter(MedicalRecords.this, historyList);
                historyListRecycleView.setAdapter(historyAdapter);
                if (historyList.isEmpty()) {
                    nothingLottieAnimation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}