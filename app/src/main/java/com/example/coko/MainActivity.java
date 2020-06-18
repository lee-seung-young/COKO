package com.example.coko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import static com.skt.Tmap.TMapMarkerItem.*;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACCESS_FINE_LOCATION = 1000;
    private Button btn_map1; // 관광 정보 검색
    private Button btn_map2; // 바로 경로 추천

    private Button likeslist;
    private Button placeinfo;

    private Button btn_map3;

    private TextView txtResult;
    public double longitude; //ListSortingActivity에서 쓸 변수
    public double latitude; //listSortingActivity에서 쓸 변수
    public static Context context;

    //add Firebase Database stuff
    private FirebaseDatabase  FirebaseDatabase;
    private DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
    private DatabaseReference yourRef=rootRef.child("Place");
    ValueEventListener eventListener;
    ArrayList<Place> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(List<Place> places) {
                btn_map2 = findViewById(R.id.btn_map2);

                Sort sort=new Sort(list,latitude,longitude);
                list=sort.Sort1();
                //checkList();
                btn_map2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(MainActivity.this, PathActivity.class);
                        intent2.putExtra("present_latitude",latitude);
                        intent2.putExtra("present_longitude",longitude);

                        for(int i=0;i<list.size();i++){
                            intent2.putExtra("latitude"+i,list.get(i).getLatitude());
                            intent2.putExtra("longitude"+i,list.get(i).getLongitude());
                            intent2.putExtra("placeid"+i,list.get(i).getPlace_id());
                            intent2.putExtra("name"+i,list.get(i).getName());
                        }
                        intent2.putExtra("size",list.size());
                        startActivity(intent2);
                    }
                });
            }
        });

        /*현재 위치 받아오기 위해서 만듦*/
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
        }

        btn_map1 = findViewById(R.id.btn_map1);
//        btn_map2 = findViewById(R.id.btn_map2);
//        btn_map3=findViewById(R.id.btn_map3);

        likeslist = findViewById(R.id.button_likeslist);
//        placeinfo = findViewById(R.id.button_placeinfo);

        btn_map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent1); //액티비티 이동
            }
        });
//        btn_map2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(MainActivity.this, PathActivity.class);
//                startActivity(intent2);
//            }
//        });
//        btn_map3.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent3=new Intent(MainActivity.this,ListSortingActivity.class);
//                startActivity(intent3);
//            }
//        });
        likeslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LikesActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });
//        placeinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, InfoAcitivity.class);
//                startActivity(intent);
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 위치 권한 없을시 권한 요청
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                // 권한 없음
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION);
            } else {
                // ACCESS_FINE_LOCATION 에 대한 권한이 이미 있음.
            }
        }
// OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
        else {
        }
    }

    //데이터 베이스에서 데이터를 가져오는 메소드
    public void readData(final FirebaseCallback firebaseCallback){
        eventListener = yourRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                    Place place = dSnapshot.getValue(Place.class);
                    list.add(place);
                }
                firebaseCallback.onCallback(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Firebase가 데이터를 반환 할때까지 기다리는 콜백
    private interface FirebaseCallback{
        void onCallback(List<Place> places);
    }
    //현재 위치가 바뀌면 갱신된 좌표를 받기위한 함수
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private void checkList() {
        for(int i=0;i<this.list.size();i++){
            Log.d("************","name "+toString().valueOf(list.get(i).getName())+" visitors "+toString().valueOf(list.get(i).getVisitors())+ " distance "+toString().valueOf(list.get(i).getDistance()));
        }
    }
    //
    //
}
