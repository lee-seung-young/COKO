package com.example.coko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {
    TMapView tmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        /*지도 불러오기*/
        /*TMapView tMapView=new TMapView(this);
        tMapView.setSKTMapApiKey( "l7xx84f4860b8e1b4a5a92b716682a24c0b8" );
        linearLayoutTmap.addView( tMapView );*/

        /*현재위치를 받아 표시해줌*/
        tmap=new TMapView(this);
        tmap.setSKTMapApiKey("l7xx84f4860b8e1b4a5a92b716682a24c0b8");
        linearLayoutTmap.addView(tmap);
        tmap.setIconVisibility(true); //현재위치로 표시될 아이콘을 표시할지 여부 설정
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
setGps();
    }
    private final LocationListener mLocationListener = new LocationListener(){
        public void onLocationChanged(Location location){
            //현재위치의 좌표를 알수있는 부분
            if(location !=null){
                double latitude=location.getLatitude();
                double longitude=location.getLongitude();
                tmap.setLocationPoint(longitude,latitude);
                tmap.setCenterPoint(longitude,latitude);
                Log.d("TmapTest",""+longitude+","+latitude);
            }
        }
        public void onProviderDisabled(String provider){

        }
        public void onProviderEnabled(String provider){

        }
        public void onStatusChanged(String provider, int status, Bundle extras){

        }
    };
    public void setGps(){
       final LocationManager lm=(LocationManager)this.getSystemService(LOCATION_SERVICE);
       if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
       ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                   android.Manifest.permission.ACCESS_FINE_LOCATION},1);
       }
       lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, //등록할 위치제공자 (실내에선 NETWORK_PROVIDER 권장)
               1000,//통지사이의 최소 시간 간격(MILLISECOND)
               1, //통지사이의 최소 변경 거리 (m)
               mLocationListener);
    }
}
