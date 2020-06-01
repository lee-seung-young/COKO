package com.example.coko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ListSortingActivity extends AppCompatActivity {
    ArrayList<Place> list=new ArrayList<>();
    double latitude;
    double longitude;
    MiniComparator comp=new MiniComparator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sorting);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListSortingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
        }
        Log.d("*******","위도: "+ toString().valueOf(latitude));
        list.add(new Place(1, "홍대입구", 37.557699, 126.924472, "서울특별시 서교동", "매일 10:00 - 21:00", "홍대입구역은 서울특별시 마포구 양화로에 있는 용산선, 서울 지하철 2호선, 인천국제공항선의 전철역이자 서울 지하철 2호선과 수도권 전철 경의·중앙선, 인천국제공항철도의 환승역이다. 개통 이전에는 동교역이었으나, 인근에 홍익대학교가 있어 현재의 역명을 쓰게 되었다.", null, false));
        list.add(new Place(2, "남산타워", 37.551348, 126.988248, "서울특별시 용산구 용산2가동 남산공원길 105", "매일 10:00 - 23:00", "N서울타워는 대한민국 서울특별시 용산구 용산동 2가 남산 공원 정상 부근에 위치한 전파 송출 및 관광용 타워이다. 1969년에 착공하여 1975년 7월 30일 완공되었다. 높이는 236.7m, 해발 479.7m이다. 수도권의 지상파 방송사들이 이 타워를 이용하여 전파를 송출한다.", null, false));
        list.add(new Place(3, "이태원", 37.539776, 126.991364, "서울특별시 용산구 이태원1동 녹사평대로46길 16", "매일 00:00 - 24:00", "도회적인 분위기의 음식점과 나이트라이프로 유명한 이태원에는 삼겹살집, 고급 비스트로, 심야까지 운영하는 소박한 케밥집이 어우러져 있습니다. 거리에는 DJ들이 힙합과 하우스 음악을 트는 트렌디한 댄스 클럽과 캐주얼한 비어 바, 게이 펍이 늘어서 있습니다. 이태원 앤틱가구 거리에서는 가정용품을 판매하는 독립 상점을 찾아볼 수 있고, 인근에는 탱크와 항공기가 전시된 전쟁기념관이 위치해 있습니다.", null, false));
        list.add(new Place(4, "청계천", 37.569306, 126.978658, "서울특별시 종로구 서린동 청계천로 1", "매일 00:00 - 24:00", "청계천은 대한민국 서울특별시 내부에 있는 지방하천으로, 한강 수계에 속하며 중랑천의 지류이다. 최장 발원지는 종로구 청운동에 위치한 ‘백운동 계곡’이며, 남으로 흐르다가 청계광장 부근의 지하에서 삼청동천을 합치며 몸집을 키운다.", null, false));
        list.add(new Place(5, "북촌한옥마을", 37.582586, 126.983557, "서울특별시 종로구 가회동 계동길 37", "매일 09:00 - 18:00", "북촌 한옥마을은 서울특별시 종로구 가회동, 삼청동 내의 위치한 한옥 마을이다. 청계천 상복를 중심을덕 남쪽에 형서된 죽사이이면 남촌, 북쪽에 형성된 죽겆징 면 북촌일두 불리었다.", null, false));
        list.add(new Place(6, "롯데월드", 37.511365, 127.098108, "서울특별시 송파구 잠실동 올림픽로 240", "매일 10:00 - 21:00", "롯데월드(영어: Lotte World)는 대한민국 서울특별시 송파구 올림픽로 240에 위치한 테마파크이다. 롯데그룹의 계열사인 호텔롯데 운영월드 사업부.  놀이시설은 실내의 롯데월드 어드벤처(Lotte World Adventure)와 야외의 매직아일랜드가 운영되고 있고, 민속박물관, 아이스링크, 백화점, 마트, 호텔이 포함된다.", null, false));
        for(int i=0;i<list.size();i++){
            Log.d("************","place_id"+toString().valueOf(list.get(i).getPlace_id()));
            Log.d("***********","distance"+toString().valueOf(list.get(i).getDistance()));
        }
        sortByDistance();
        for(int i=0;i<list.size();i++){
            Log.d("************","place_id "+toString().valueOf(list.get(i).getPlace_id())+" distance "+toString().valueOf(list.get(i).getDistance())
                    +" popularity "+toString().valueOf(list.get(i).getVisiters()));
        }
        chosenByPopularity();
        for(int i=0;i<list.size();i++){
            Log.d("************","place_id "+toString().valueOf(list.get(i).getPlace_id())+" distance "+toString().valueOf(list.get(i).getDistance())
            +" popularity "+toString().valueOf(list.get(i).getVisiters()));
        }
        sortByDistance();
        for(int i=0;i<list.size();i++){
            Log.d("************","place_id "+toString().valueOf(list.get(i).getPlace_id())+" distance "+toString().valueOf(list.get(i).getDistance())
                    +" popularity "+toString().valueOf(list.get(i).getVisiters()));
        }
        Place place=ret_Place();
        Log.d("************","place_id "+toString().valueOf(place.getPlace_id())+" distance "+toString().valueOf(place.getDistance())
                +" popularity "+toString().valueOf(place.getVisiters()));
        this.latitude=place.getLatitude();
        this.longitude=place.getLongitude();
        Log.d("*******","위도: "+ toString().valueOf(latitude));
        list.remove(0);
        sortByDistance();
        for(int i=0;i<list.size();i++){
            Log.d("************","place_id "+toString().valueOf(list.get(i).getPlace_id())+" distance "+toString().valueOf(list.get(i).getDistance())
                    +" popularity "+toString().valueOf(list.get(i).getVisiters()));
        }
    }

    public void makeList(ArrayList<Place> list) {
        for (int i = 0; i < list.size(); i++) {
            this.list.add(list.get(i));
        }
    }

    public void sortByDistance() {
        Iterator<Place> iterator = this.list.iterator();
        double distance = 0;
        getDistance gd = new getDistance();
        Place place;

        //ArrayList에 저장된 Place들과 현재위치의 거리를 필드값으로 저장
        while (iterator.hasNext()) {
            place = iterator.next();
            distance = gd.getDistance(latitude, longitude, place.getLatitude(), place.getLongitude());
            place.setDistance(distance);
        }

        //Place의 필드값으로 저장된 거리들로 ArrayList 정렬
        Collections.sort(this.list,comp);
        for (int i = 0; i < this.list.size(); i++) {
            if (i >= 5) {
                list.remove(i);
            }
        }

    }

    public void chosenByPopularity() {
        Iterator<Place> iterator=this.list.iterator();
        int popularity=0;
        Place place;

        while(iterator.hasNext()){
            place=iterator.next();
            place.setVisiters(popularity);
            popularity++;
        }
        Collections.sort(this.list,comp);
        for(int i=0;i<this.list.size();i++){
            if(i>=2)
            {
                list.remove(i);
            }
        }
    }

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
    public Place ret_Place(){
        return list.get(0);
    }
}
