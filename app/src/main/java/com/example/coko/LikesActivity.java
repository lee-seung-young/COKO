package com.example.coko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class
LikesActivity extends AppCompatActivity  {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<LikesInfo> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Button goPath;
    private double latitude;
    private double longitude;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes_list);

        recyclerView = findViewById(R.id.lists);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        ref = database.getReference("Likes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LikesInfo likes = snapshot.getValue(LikesInfo.class);
                    arrayList.add(likes);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LikesActivity", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new ListAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        goPath = (Button) findViewById(R.id.gopath);
        goPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                longitude=intent.getDoubleExtra("longitude",0);
                latitude=intent.getDoubleExtra("latitude",0);
                ArrayList <Place> list=new ArrayList<Place>();

                for(int i=0;i<arrayList.size();i++){
                    Place place=new Place(new Integer(arrayList.get(i).getPlace_id()),arrayList.get(i).getName(),
                            new Double(arrayList.get(i).getLatitude()),new Double(arrayList.get(i).getLongitude()));
                    list.add(place);
                }
                Log.d("************","latitude "+toString().valueOf(latitude)+" longitude "+toString().valueOf(longitude));

                Sort sort=new Sort(list,latitude,longitude,arrayList.size());
                list=sort.Sort2();
                for(int i=0;i<list.size();i++)
                Log.d("************","name "+toString().valueOf(list.get(i).getName())+" distance "+toString().valueOf(list.get(i).getDistance()));

                Intent intent2 = new Intent(LikesActivity.this, PathActivity.class);
                for(int i=0;i<list.size();i++){
                    intent2.putExtra("latitude"+i,list.get(i).getLatitude());
                    intent2.putExtra("longitude"+i,list.get(i).getLongitude());
                    intent2.putExtra("placeid"+i,list.get(i).getPlace_id());
                    intent2.putExtra("name"+i,list.get(i).getName());
                }
                intent2.putExtra("size",arrayList.size());
                startActivity(intent2);
            }
        });

    }


}
