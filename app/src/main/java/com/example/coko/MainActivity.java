package com.example.coko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import javax.xml.parsers.ParserConfigurationException;

import static com.skt.Tmap.TMapMarkerItem.*;

public class MainActivity extends AppCompatActivity {
    TMapView tmap;
    final ArrayList alTMapPoint = new ArrayList();
    TMapData tmapdata=new TMapData();
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
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
setGps();
        alTMapPoint.add(new TMapPoint(37.576016,126.976867)); //광화문
        alTMapPoint.add(new TMapPoint(37.2844, 127.1052)); //남한산성
        alTMapPoint.add(new TMapPoint(37.321232,127.128381)); //단국대
        //alTMapPoint.add(new TMapPoint(35.175804,129.043426)); //삼광사
        //alTMapPoint.add(new TMapPoint(33.458771,126.942672)); //성산 일출봉
        //alTMapPoint.add(new TMapPoint(36.496896,126.335286)); //꽃지해수욕장
        //alTMapPoint.add(new TMapPoint(34.727673,127.894119)); //남해 가천 다랭이 마을
        //alTMapPoint.add(new TMapPoint(35.147823,129.130080)); //부산 광안대교
        //alTMapPoint.add(new TMapPoint(38.119597,128.465550)); //설악산
        //alTMapPoint.add(new TMapPoint(37.582978,126.983661)); //북촌한옥마을
        //마커 이미지 생성
        final Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.pin);
        for(int i=0;i<3;i++){
            TMapMarkerItem markerItem1=new TMapMarkerItem();
            //마커 아이콘 지정
            markerItem1.setTMapPoint((TMapPoint) alTMapPoint.get(i));
            //지도에 마커 추가
            tmap.addMarkerItem("markerItem"+i,markerItem1);
        }
        /*자동차 경로 안내 : 출발지 위치 좌표 & 도착지 위치 좌표 사용 */
        /* 현재 위치 좌표 반환 (지금 내가 있는 좌표가 아니라 SKT 타워로 나옴!! 수정해야됨**)*/
        TMapPoint tpoint = tmap.getLocationPoint();
        double Latitude=tpoint.getLatitude();
        double Longitude=tpoint.getLongitude();

        TMapPoint tMapPointStart=new TMapPoint(Latitude,Longitude);
        TMapPoint tMapPointEnd=new TMapPoint(37.321232,127.128381); //단국대
        /*출발, 목적지 값으로 경로탐색을 요청, 경로 그림*/
        tmapdata.findPathData(tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tmap.addTMapPath(polyLine);
            }
        });

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
