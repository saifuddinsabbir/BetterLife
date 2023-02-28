package com.example.dashbosrd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
//    List<Post> mData;
    ArrayList<Post> mData = new ArrayList<>();
    HashMap<String, Boolean> postsLike = new HashMap<String, Boolean>();
    SessionManager sessionManager;
    HashMap<String, String> userDetails;

    public PostAdapter(Context mContext) {
        this.mContext = mContext;
//        this.mData = mData;
    }

    public void setItems(ArrayList<Post> data)
    {
        mData.addAll(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String userName = mData.get(position).getUserId();

        sessionManager = new SessionManager(mContext.getApplicationContext());
        userDetails = sessionManager.getUsersDetailFromSession();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("userName").equalTo(userName);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameFromDB = snapshot.child(userName).child("fullName").getValue(String.class);
                holder.fullName.setText(fullNameFromDB);
                String imageFromDB = snapshot.child(userName).child("dp").getValue(String.class);
                if (imageFromDB != null && imageFromDB != "") {
                    Picasso.get().load(imageFromDB).into(holder.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.userName.setText("@" + userName);
        holder.time.setText(timestampToString((Long) mData.get(position).getTimeStamp()));
        holder.feedbackDesc.setText(mData.get(position).getDescription());
        Picasso.get().load(mData.get(position).getPostImage()).into(holder.postImage);
        holder.feedbackRating.setRating(mData.get(position).getMyRating());

        if (mData.get(position).getMyRating() <= 1) {
            holder.feedbackExperience.setText("Very bad");
        } else if (mData.get(position).getMyRating() <= 2) {
            holder.feedbackExperience.setText("Bad");
        } else if (mData.get(position).getMyRating() <= 3) {
            holder.feedbackExperience.setText("Not bad");
        } else if (mData.get(position).getMyRating() <= 4) {
            holder.feedbackExperience.setText("Good");
        } else {
            holder.feedbackExperience.setText("Very good");
        }

        DatabaseReference referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(mData.get(position).getPostKey());
        referenceLikesFetch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                Boolean onceLiked = false;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String userNameThis = snap.child("userName").getValue(String.class);
                    Boolean d = snap.child("liked").getValue(Boolean.class);

                    onceLiked = true;

                    if (userNameThis.equals(userDetails.get(SessionManager.KEY_USERNAME))) {
                        postsLike.put(mData.get(position).getPostKey(), d);

                        if(d) {

                            holder.feedbackCardLikeButtonImage.setImageResource(R.drawable.liked_vector);
                        } else {
                            holder.feedbackCardLikeButtonImage.setImageResource(R.drawable.like3);
                        }
                    }

                    if(!onceLiked) {
                        holder.feedbackCardLikeButtonImage.setImageResource(R.drawable.like3);
                    }

                    if (d) {
                        count++;
                    }
                }
                if(count == 0) {
                    holder.feedbackLikesCount.setText("No likes yet");
                } else {
                    holder.feedbackLikesCount.setText(count + " likes");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference referenceCommentsAdd = FirebaseDatabase.getInstance().getReference("comments").child(mData.get(position).getPostKey());
        referenceCommentsAdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    count++;
                }

                if(count == 0) {
                    holder.feedbackCommentsCount.setText("No comments yet");
                } else {
                    holder.feedbackCommentsCount.setText(count + " comments");
                }
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView fullName;
        TextView userName;
        TextView time;
        TextView feedbackDesc;
        ImageView postImage;
        RatingBar feedbackRating;
        TextView feedbackExperience;
        TextView feedbackLikesCount;
        TextView feedbackCommentsCount;
        ImageView feedbackCardLikeButtonImage;
        LinearLayout feedbackLikeButton;
        LinearLayout feedbackCardCommentButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.feedbackCardUserImage);
            fullName = itemView.findViewById(R.id.feedbackCardFullName);
            userName = itemView.findViewById(R.id.feedbackCardUserNameId);
            time = itemView.findViewById(R.id.feedbackCardTimestampId);
            feedbackDesc = itemView.findViewById(R.id.feedbackCardDescriptionId);
            postImage = itemView.findViewById(R.id.feedbackCardPosImageId);
            feedbackRating = itemView.findViewById(R.id.feedbackCardRatingBarId);
            feedbackExperience = itemView.findViewById(R.id.feedbackCardExperianceId);
            feedbackLikesCount = itemView.findViewById(R.id.feedbackPostLikeCountId);
            feedbackCommentsCount = itemView.findViewById(R.id.feedbackPostCommentCountId);
            feedbackCardLikeButtonImage = itemView.findViewById(R.id.feedbackCardLikeButtonImageId);
            feedbackLikeButton = itemView.findViewById(R.id.feedbackCardLikeButtonId);
            feedbackCardCommentButton = itemView.findViewById(R.id.feedbackCardCommentButtonId);


            feedbackLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    DatabaseReference referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(mData.get(position).getPostKey());

                    if (!postsLike.containsKey(mData.get(position).getPostKey())) {
                        DatabaseReference referenceLikes = FirebaseDatabase.getInstance().getReference("likes").child(mData.get(position).getPostKey());
                        Like like = new Like(userDetails.get(SessionManager.KEY_USERNAME), true);
                        referenceLikes.child(userDetails.get(SessionManager.KEY_USERNAME)).setValue(like);
//                        Toast.makeText(mContext.getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                    } else if (postsLike.get(mData.get(position).getPostKey()) == false) {
                        referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(mData.get(position).getPostKey());
                        referenceLikesFetch.child(userDetails.get(SessionManager.KEY_USERNAME)).child("liked").setValue(true);
//                        Toast.makeText(mContext.getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                    } else {
                        referenceLikesFetch = FirebaseDatabase.getInstance().getReference("likes").child(mData.get(position).getPostKey());
                        referenceLikesFetch.child(userDetails.get(SessionManager.KEY_USERNAME)).child("liked").setValue(false);
//                        Toast.makeText(mContext.getApplicationContext(), "Uniked", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            feedbackCardCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intentToFeedbackDetails = new Intent(mContext, FeedbackDetails.class);

                    intentToFeedbackDetails.putExtra("postKey", mData.get(position).getPostKey());
                    intentToFeedbackDetails.putExtra("userName", mData.get(position).getUserId());
                    intentToFeedbackDetails.putExtra("dateTime", (Long) mData.get(position).getTimeStamp());
                    intentToFeedbackDetails.putExtra("rating", mData.get(position).getMyRating()+"");
                    intentToFeedbackDetails.putExtra("desc", mData.get(position).getDescription());
                    intentToFeedbackDetails.putExtra("postImage", mData.get(position).getPostImage());
                    intentToFeedbackDetails.putExtra("likes", mData.get(position).getLikes());
                    intentToFeedbackDetails.putExtra("comments", mData.get(position).getComments());

                    mContext.startActivity(intentToFeedbackDetails);
                }
            });
        }
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd MMM, yyyy | hh:mm a",calendar).toString();
        return date;
    }
}
