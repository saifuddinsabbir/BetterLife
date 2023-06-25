package com.example.dashbosrd;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>{
    Context mContext;
    List<Appointment> mData;

    public AppointmentAdapter(Context mContext, List<Appointment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_appointment, parent, false);
        return new AppointmentAdapter.MyViewHolder(row);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.doctorNameText.setText(mData.get(position).getDoctor());
        holder.specialityText.setText(mData.get(position).getSpeciality());
        holder.appointmentNoText.setText("Appointment No: " + mData.get(position).getSerialNo());
        holder.scheduleText.setText("Schedule: " + mData.get(position).getSchedule());
        holder.ageText.setText("Age: " + mData.get(position).getAge());

        String userName = mData.get(position).getUserName();
        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("users");
        referenceUser.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameFromDB = snapshot.child("fullName").getValue(String.class);
                String phoneNoFromDB = snapshot.child("phoneNo").getValue(String.class);
                String genderFromDB = snapshot.child("gender").getValue(String.class);
                String bloodGroupFromDB = snapshot.child("bloodGroup").getValue(String.class);

                holder.patientNameText.setText(fullNameFromDB);
                holder.genderText.setText("Gender: "+genderFromDB);
                holder.bloodText.setText("Blood Group: " + bloodGroupFromDB);
                holder.contactText.setText("Contact: " + phoneNoFromDB);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView doctorNameText;
        TextView specialityText;
        TextView appointmentNoText;
        TextView scheduleText;
        TextView patientNameText;
        TextView ageText;
        TextView genderText;
        TextView bloodText;
        TextView contactText;
        ConstraintLayout constraintLayoutAppointment;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorNameText = itemView.findViewById(R.id.doctorNameText);
            specialityText = itemView.findViewById(R.id.specialityText);
            appointmentNoText = itemView.findViewById(R.id.appointmentNoText);
            scheduleText = itemView.findViewById(R.id.scheduleText);
            patientNameText = itemView.findViewById(R.id.patientNameText);
            ageText = itemView.findViewById(R.id.ageText);
            genderText = itemView.findViewById(R.id.genderText);
            bloodText = itemView.findViewById(R.id.bloodText);
            contactText = itemView.findViewById(R.id.contactText);
            constraintLayoutAppointment = itemView.findViewById(R.id.constraintLayoutAppointment);

            constraintLayoutAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intentToDoctorInfo = new Intent(mContext, DoctorInfo.class);
                    intentToDoctorInfo.putExtra("appointmentKey", mData.get(position).getAppointmentKey());
                    mContext.startActivity(intentToDoctorInfo);
                }
            });
        }
    }
}
