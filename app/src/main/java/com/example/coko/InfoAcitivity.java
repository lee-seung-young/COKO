package com.example.coko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InfoAcitivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;

    ImageView infoPic;
    TextView infoName, infoAddr, infoTime, infoDescription, heartClick;
    ImageView infoHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent i = getIntent();
        final String place_id = i.getStringExtra("place_id");

        infoPic = (ImageView) findViewById(R.id.info_pic);
        infoName = (TextView) findViewById(R.id.info_name);
        infoAddr = (TextView) findViewById(R.id.info_addr);
        infoTime = (TextView) findViewById(R.id.info_time);
        infoDescription = (TextView) findViewById(R.id.info_description);
        infoHeart = (ImageView)findViewById(R.id.heart);
        heartClick = (TextView)findViewById(R.id.click);

        database = FirebaseDatabase.getInstance();

        String place = "place";
        final String placeId = place.concat(place_id);

        ref = FirebaseDatabase.getInstance().getReference().child("Place").child(placeId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("name").getValue().toString();
                final String location = dataSnapshot.child("location").getValue().toString();
                String time = dataSnapshot.child("time").getValue().toString();
                String description = dataSnapshot.child("description").getValue().toString();
                final boolean likeox = (Boolean)dataSnapshot.child("likeox").getValue();
                final String picUrl = dataSnapshot.child("pic").getValue().toString();

                final long place_num = (long) dataSnapshot.child("place_id").getValue();
                final double latitude = (double) dataSnapshot.child("latitude").getValue();
                final double longitude = (double) dataSnapshot.child("longitude").getValue();

                Picasso.get().load(picUrl).into(infoPic);
                infoName.setText(name);
                infoAddr.setText(location);
                infoTime.setText(time);
                infoDescription.setText(description);

                if(!likeox){
                    infoHeart.setImageResource(R.drawable.empty_heart);
                    heartClick.setVisibility(View.VISIBLE);
                } else{
                    infoHeart.setImageResource(R.drawable.full_heart);
                    heartClick.setVisibility(View.INVISIBLE);
                }

                infoHeart.setOnClickListener(new View.OnClickListener() {

                    String addNum = "likes";
                    @Override
                    public void onClick(View view) {
                        if(!likeox){

                            String addNum1 =  addNum.concat(place_id);

                            infoHeart.setImageResource(R.drawable.full_heart);
                            heartClick.setVisibility(View.INVISIBLE);
                            database.getReference().child("Place").child(placeId).child("likeox").setValue(true);


                            // likes 목록에 추가
                            HashMap<String, String> likesList = new HashMap<>();
                            likesList.put("name", name);
                            likesList.put("location", location);
                            likesList.put("pic", picUrl);
                            likesList.put("place_id",String.valueOf(place_num));
                            likesList.put("latitude", String.valueOf(latitude));
                            likesList.put("longitude", String.valueOf(longitude));

                            database.getReference().child("Likes").child(addNum1).setValue(likesList);

                        } else {

                            String addNum2 = addNum.concat(place_id);
                            infoHeart.setImageResource(R.drawable.empty_heart);
                            heartClick.setVisibility(View.VISIBLE);
                            database.getReference().child("Place").child(placeId).child("likeox").setValue(false);
                            database.getReference().child("Likes").child(addNum2).removeValue();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}