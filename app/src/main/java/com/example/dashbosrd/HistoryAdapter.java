package com.example.dashbosrd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context mContext;
//    List<History> mData;

    ArrayList<History> mData = new ArrayList<>();

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
        //this.mData = mData;
    }

    public void setItems(ArrayList<History> data)
    {
        mData.addAll(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_history_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.historyCategory.setText(mData.get(position).getCategory());
        holder.historyClass.setText(mData.get(position).getStage());
        holder.historyProbability.setText(mData.get(position).getProbability()+"%");
        holder.historyTime.setText(timestampToString((Long) mData.get(position).getTimeStamp()));
        Picasso.get().load(mData.get(position).getPickedImage()).into(holder.historyImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView historyCategory;
        TextView historyClass;
        TextView historyProbability;
        TextView historyTime;
        ImageView historyImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            historyCategory = itemView.findViewById(R.id.historyCardCategoryId);
            historyClass = itemView.findViewById(R.id.historyCardClassId);
            historyProbability = itemView.findViewById(R.id.historyCardProId);
            historyTime = itemView.findViewById(R.id.historyCardTimestampId);
            historyImage = itemView.findViewById(R.id.historyCardPosImageId);
        }
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm a\ndd MMM, yyyy",calendar).toString();
        return date;
    }
}
