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

//    long count = 0;
    ImageView infoPic;
    TextView infoName, infoAddr, infoTime, infoDescription, heartClick;
    ImageView infoHeart;

//    String place_id;
//    String plcae_name;
//    private ListView listView;
//    private FirebaseDatabase database;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


//        place_id = getIntent().getStringExtra(place_id);
//        place_name = getIntent().getStringExtra("place_name");
        Intent i = getIntent();
        final String place_id = i.getStringExtra("place_id");

//        place_id = "22";

        infoPic = (ImageView) findViewById(R.id.info_pic);
        infoName = (TextView) findViewById(R.id.info_name);
        infoAddr = (TextView) findViewById(R.id.info_addr);
        infoTime = (TextView) findViewById(R.id.info_time);
        infoDescription = (TextView) findViewById(R.id.info_description);
        infoHeart = (ImageView)findViewById(R.id.heart);
        heartClick = (TextView)findViewById(R.id.click);

        database = FirebaseDatabase.getInstance();
//        final String placeNum = "place22";
//        placeId = FirebaseDatabase.getInstance().getReference().child("Place").get
//        place_id = FirebaseDatabase.getInstance().getReference().child("Place").child(place_id);
//        final String id = FirebaseDatabase.getInstance().getReference().child("Place").getKey();
//        String id = FirebaseDatabase.getInstance()

        String place = "place";
//        final String placeId = place.concat(place_id);
//        final String placeId = place.concat("10");
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


//                 for(DataSnapshot snap : dataSnapshot.getChildren()){
//                     ss = snap.getChildrenCount();
//                 }

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


//                long counter = 0;
                infoHeart.setOnClickListener(new View.OnClickListener() {
                    //                    int count = 100;
//                    String addNum = "likes" + (ss+1);
                    String addNum = "likes";
                    @Override
                    public void onClick(View view) {
                        if(!likeox){
//                            count += 1;
//                            String addNum1 =  addNum.concat(String.valueOf(count));
                            String addNum1 =  addNum.concat(name);

                            infoHeart.setImageResource(R.drawable.full_heart);
                            heartClick.setVisibility(View.INVISIBLE);
                            database.getReference().child("Place").child(placeId).child("likeox").setValue(true);


                            // likes 목록에 추가
                            HashMap<String, String> likesList = new HashMap<>();
                            likesList.put("name", name);
                            likesList.put("location", location);
                            likesList.put("pic", picUrl);
//                            likesList.put("placeid",place_id);
                            database.getReference().child("Likes").child(addNum1).setValue(likesList);

                        } else {
//                            String addNum2 = addNum.concat(String.valueOf(count));
                            String addNum2 = addNum.concat(name);
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

    private void saveData(String name, String location, String pic, String placeid) {
        HashMap<String, String> likesList = new HashMap<>();
        likesList.put("name", name);
        likesList.put("location", location);
        likesList.put("pic", pic);
        likesList.put("placeid",placeid);
        ref.setValue(likesList);
    }

}