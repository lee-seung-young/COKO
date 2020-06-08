package com.example.coko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_map1 = findViewById(R.id.btn_map1);
        btn_map2 = findViewById(R.id.btn_map2);
//        btn_map3=findViewById(R.id.btn_map3);

//        likeslist = findViewById(R.id.button_likeslist);
//        placeinfo = findViewById(R.id.button_placeinfo);

        btn_map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent1); //액티비티 이동
            }
        });
        btn_map2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, PathActivity.class);
                startActivity(intent2);
            }
        });
//        btn_map3.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent3=new Intent(MainActivity.this,ListSortingActivity.class);
//                startActivity(intent3);
//            }
//        });
//        likeslist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, LikesActivity.class);
//                startActivity(intent);
//            }
//        });
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
}
