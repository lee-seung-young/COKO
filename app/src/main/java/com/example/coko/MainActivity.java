package com.example.coko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity
                            implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;
    private static String mApiKey = "l7xx84f4860b8e1b4a5a92b716682a24c0b8";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        tMapView = new TMapView(this);
        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(mApiKey);

        addPoint();
        showMarkerPoint();

        /* 현재 보는 방향 */
        tMapView.setCompassMode(true);

        /*현위치 아이콘표시*/
        tMapView.setIconVisibility(true);

        /*줌레벨*/
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        // 실내일 때 유용
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); //gps로 현 위치를 잡습니다.
        tmapgps.OpenGps();

        /*화면중심을 단말의 현재위치로 이동*/
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        //풍선에서 우측 버튼 클릭시 할 행동
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                Toast.makeText(MainActivity.this, "클릭", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addPoint() { //여기에 핀을 꼽을 포인트들을 배열에 add해주세요!
        //강남//
        m_mapPoint.add(new MapPoint("단국대", 37.321232, 127.128381));
        m_mapPoint.add(new MapPoint("광화문", 37.576016, 126.976867));
        m_mapPoint.add(new MapPoint("남한산성", 37.2844, 127.1052));
        m_mapPoint.add(new MapPoint("삼광사", 35.175804, 129.043426));
        m_mapPoint.add(new MapPoint("성산 일출봉", 33.458771, 126.942672));
        m_mapPoint.add(new MapPoint("꽃지해수욕장", 36.496896, 126.335286));
        m_mapPoint.add(new MapPoint("남해 가천 다랭이 마을", 34.727673, 127.894119));
        m_mapPoint.add(new MapPoint("부산 광안대교", 35.147823, 129.130080));
        m_mapPoint.add(new MapPoint("설악산", 38.119597, 128.465550));
        m_mapPoint.add(new MapPoint("북촌한옥마을", 37.582978, 126.983661));
    }

    public void showMarkerPoint() { //마커 찍는거
        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);

            //풍선뷰 안의 항목에 글을 지정
            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            //item1.setCalloutSubTitle("  "); => sub title 생성
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarker%d", mMarkerID++);

            tMapView.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);

        }
    }
}

//        /*현재위치를 받아 표시해줌*/
//        tmap=new TMapView(this);
//        tmap.setSKTMapApiKey("l7xx84f4860b8e1b4a5a92b716682a24c0b8");
//        linearLayoutTmap.addView(tmap);
//        tmap.setIconVisibility(true); //현재위치로 표시될 아이콘을 표시할지 여부 설정
//
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
//        {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1); //위치권한 탐색 허용 관련 내용
//            }
//            return;
//        }
//setGps();
//        alTMapPoint.add(new TMapPoint(37.576016,126.976867)); //광화문
//        alTMapPoint.add(new TMapPoint(37.2844, 127.1052)); //남한산성
//        alTMapPoint.add(new TMapPoint(37.321232,127.128381)); //단국대
//        //alTMapPoint.add(new TMapPoint(35.175804,129.043426)); //삼광사
//        //alTMapPoint.add(new TMapPoint(33.458771,126.942672)); //성산 일출봉
//        //alTMapPoint.add(new TMapPoint(36.496896,126.335286)); //꽃지해수욕장
//        //alTMapPoint.add(new TMapPoint(34.727673,127.894119)); //남해 가천 다랭이 마을
//        //alTMapPoint.add(new TMapPoint(35.147823,129.130080)); //부산 광안대교
//        //alTMapPoint.add(new TMapPoint(38.119597,128.465550)); //설악산
//        //alTMapPoint.add(new TMapPoint(37.582978,126.983661)); //북촌한옥마을
//        //마커 이미지 생성
//        final Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.pin);
//        for(int i=0;i<3;i++){
//            TMapMarkerItem markerItem1=new TMapMarkerItem();
//            //마커 아이콘 지정
//            markerItem1.setTMapPoint((TMapPoint) alTMapPoint.get(i));
//            //지도에 마커 추가
//            tmap.addMarkerItem("markerItem"+i,markerItem1);
//        }
//        /*자동차 경로 안내 : 출발지 위치 좌표 & 도착지 위치 좌표 사용 */
//        /* 현재 위치 좌표 반환 (지금 내가 있는 좌표가 아니라 SKT 타워로 나옴!! 수정해야됨**)*/
//        TMapPoint tpoint = tmap.getLocationPoint();
//        double Latitude=tpoint.getLatitude();
//        double Longitude=tpoint.getLongitude();
//
//        TMapPoint tMapPointStart=new TMapPoint(Latitude,Longitude);
//        TMapPoint tMapPointEnd=new TMapPoint(37.321232,127.128381); //단국대
//        /*출발, 목적지 값으로 경로탐색을 요청, 경로 그림*/
//        tmapdata.findPathData(tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
//            @Override
//            public void onFindPathData(TMapPolyLine polyLine) {
//                tmap.addTMapPath(polyLine);
//            }
//        });
//
//    }
//    private final LocationListener mLocationListener = new LocationListener(){
//        public void onLocationChanged(Location location){
//            //현재위치의 좌표를 알수있는 부분
//            if(location !=null){
//                double latitude=location.getLatitude();
//                double longitude=location.getLongitude();
//                tmap.setLocationPoint(longitude,latitude);
//                tmap.setCenterPoint(longitude,latitude);
//                Log.d("TmapTest",""+longitude+","+latitude);
//            }
//        }
//        public void onProviderDisabled(String provider){
//
//        }
//        public void onProviderEnabled(String provider){
//
//        }
//        public void onStatusChanged(String provider, int status, Bundle extras){
//
//        }
//    };
//    public void setGps(){
//       final LocationManager lm=(LocationManager)this.getSystemService(LOCATION_SERVICE);
//       if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
//       ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//           ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                   android.Manifest.permission.ACCESS_FINE_LOCATION},1);
//       }
//       lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, //등록할 위치제공자 (실내에선 NETWORK_PROVIDER 권장)
//               1000,//통지사이의 최소 시간 간격(MILLISECOND)
//               1, //통지사이의 최소 변경 거리 (m)
//               mLocationListener);
//    }

//}
