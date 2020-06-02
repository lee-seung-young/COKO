package com.example.coko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity
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

    double gpsLatitude;
    double gpsLongitude;

    @Override
    public void onLocationChange(Location location) { //위치 변경 확인
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            TMapPoint pointh = tMapView.getLocationPoint();
            gpsLatitude = pointh.getLatitude();
            gpsLongitude = pointh.getLongitude();

            // 거리 구하는 함수 사용 , 현재위치 위도, 경도, 목적지 위도, 경도
            getDistance distance = new getDistance();
            double dtresult = distance.getDistance(gpsLatitude, gpsLongitude, 37.582978, 126.983661); //거리 비교 값 dtresult에 저장
            Log.v("거리", toString().valueOf(dtresult)); // 저장된 결과값 로그로 찍기
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        tMapView = new TMapView(this);
        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(mApiKey);

        addPoint();
        showMarkerPoint();

        /* 현재 보는 방향 */
        //tMapView.setCompassMode(true);

        /*현위치 아이콘표시*/
        tMapView.setIconVisibility(true);

        /*줌레벨*/
        tMapView.setZoomLevel(12);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(MapActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        tmapgps.OpenGps();


        //화면중심을 단말의 현재위치로 이동
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);


        // 맵 화면 버튼 3가지 구성
        Button buttonZoomIn = (Button)findViewById(R.id.buttonZoomIn); // 확대 버튼
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });
        Button buttonZoomOut = (Button)findViewById(R.id.buttonZoomOut); // 축소 버튼
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });
        Button buttongps = (Button)findViewById(R.id.buttongps); // 현재위치로 화면을 옮기는 버튼
        buttongps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setCenterPoint(gpsLongitude, gpsLatitude,true);
            }
        });

    }



    public void showMarkerPoint() { //마커 찍는거

        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);

            final String markName = m_mapPoint.get(i).getName();

            item1.setTMapPoint(point);
            item1.setName(markName);
            item1.setVisible(item1.VISIBLE);
            item1.setIcon(bitmap);

            //풍선뷰 안의 항목에 글을 지정
            item1.setCalloutTitle(markName);

            //item1.setCalloutSubTitle( ); //서브 타이틀 지정
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(false);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.impo1);
            item1.setCalloutRightButtonImage(bitmap_i);

            int place_num = m_mapPoint.get(i).getPlace_id(); //place_id를 place_num으로 가져옴
            final String strID = String.valueOf(place_num); // place_num을 strID로 저장
            tMapView.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
//            Log.v("sadasda id",strID); strID 로그로 확인

        }
        //마커풍선에서 터치(우측 버튼 클릭)시 할 행동 -> 팝업 띄우기, 2가지 버튼(취소/세부정보)
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(final TMapMarkerItem markerItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("정보 보기")
                        .setMessage(markerItem.getName()+"의 자세한 정보를 보려면 아래 [세부정보] 버튼을 누르시오.")
                        .setIcon(R.drawable.impo1)
                        .setCancelable(true);
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setNegativeButton("세부정보", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), InfoAcitivity.class);
                        intent.putExtra("place_id", markerItem.getID());
//                        Log.v("#######id", markerItem.getID()); // 로그로 id 확인
                        startActivity(intent);
                    }
                })
                        .show();
            }
        });
    }

    public void addPoint() { //여기에 핀을 꼽을 포인트들을 배열에 add해주세요!
        m_mapPoint.add(new MapPoint(1, "홍대입구", 37.557699, 126.924472));
        m_mapPoint.add(new MapPoint(2, "남산타워", 37.551348, 126.988248));
        m_mapPoint.add(new MapPoint(3, "이태원", 37.539776, 126.991364));
        m_mapPoint.add(new MapPoint(4, "청계천", 37.569306, 126.978658));
        m_mapPoint.add(new MapPoint(5, "북촌한옥마을", 37.582586, 126.983557));
        m_mapPoint.add(new MapPoint(6, "롯데월드", 37.511365, 127.098108));
        m_mapPoint.add(new MapPoint(7, "한국민속촌", 37.258602, 127.117036));
        m_mapPoint.add(new MapPoint(8, "단국대", 37.32188, 127.126804));
        m_mapPoint.add(new MapPoint(9, "동대문디자인프라자", 37.566925, 127.009408));
        m_mapPoint.add(new MapPoint(10, "광화문", 37.577978, 126.976288));
        m_mapPoint.add(new MapPoint(11, "에버랜드", 37.29559, 127.202605));
        m_mapPoint.add(new MapPoint(12, "광교호수공원", 37.283217, 127.065992));
        m_mapPoint.add(new MapPoint(13, "물향기수목원", 37.166741, 127.056633));
        m_mapPoint.add(new MapPoint(14, "여의도한강공원", 37.52864, 126.934258));
        m_mapPoint.add(new MapPoint(15, "북한산국립공원", 37.620065, 126.995541));
        m_mapPoint.add(new MapPoint(16, "올림픽공원", 37.521038, 127.121592));
        m_mapPoint.add(new MapPoint(17, "남한산성", 37.479097, 127.181246));
        m_mapPoint.add(new MapPoint(18, "호암미술관", 37.294688, 127.191687));
        m_mapPoint.add(new MapPoint(19, "국립과천박물관", 37.438463, 127.005781));
        m_mapPoint.add(new MapPoint(20, "낙산공원", 37.580844, 127.007462));
        m_mapPoint.add(new MapPoint(21, "국립중앙박물관", 37.523949, 126.980469));
        m_mapPoint.add(new MapPoint(22, "성남아트센터", 37.403173, 127.132034));
        m_mapPoint.add(new MapPoint(23, "성남중앙공원", 37.377092, 127.123784));
        m_mapPoint.add(new MapPoint(24, "봉은사", 37.514906, 127.057375));
        m_mapPoint.add(new MapPoint(25, "별마당도서관", 37.509971, 127.060277));
        m_mapPoint.add(new MapPoint(26, "율동공원책테마파크", 37.380344, 127.148917));
        m_mapPoint.add(new MapPoint(27, "경희궁", 37.571248, 126.968169));
        m_mapPoint.add(new MapPoint(28, "신사동 가로수길", 37.52059, 127.02293));
        m_mapPoint.add(new MapPoint(29, "남산골한옥마을", 37.559332, 126.994499));
        m_mapPoint.add(new MapPoint(30, "한류스타거리", 37.527364, 127.04246));
        m_mapPoint.add(new MapPoint(31, "예술의전당", 37.479078, 127.011708));
        m_mapPoint.add(new MapPoint(32, "한양도성 순성길", 37.568425, 127.010181));
        m_mapPoint.add(new MapPoint(33, "양재시민의숲", 37.470806, 127.035728));
        m_mapPoint.add(new MapPoint(34, "덕수궁", 37.565819, 126.975125));
        m_mapPoint.add(new MapPoint(35, "세빛섬", 37.511955, 126.995028));
        m_mapPoint.add(new MapPoint(36, "몽마르뜨공원", 37.495188, 127.003502));
        m_mapPoint.add(new MapPoint(37, "잠실종합운동장", 37.514904, 127.073014));
        m_mapPoint.add(new MapPoint(38, "LG아트센터", 37.501869, 127.037276));
        m_mapPoint.add(new MapPoint(39, "유니버설아트센터", 37.550865, 127.087918));
        m_mapPoint.add(new MapPoint(40, "수원화성", 37.288541, 127.013936));
        m_mapPoint.add(new MapPoint(41, "전쟁기념관", 37.536819, 126.977129));
        m_mapPoint.add(new MapPoint(42, "인사동", 37.571762, 126.985619));
        m_mapPoint.add(new MapPoint(43, "명동", 37.561087, 126.986213));
        m_mapPoint.add(new MapPoint(44, "창덕궁", 37.578073, 126.989372));
        m_mapPoint.add(new MapPoint(45, "디뮤지엄", 37.537387, 127.011437));
        m_mapPoint.add(new MapPoint(46, "서울숲", 37.544037, 127.036336));
        m_mapPoint.add(new MapPoint(47, "어린이대공원", 37.547965, 127.074591));
        m_mapPoint.add(new MapPoint(48, "푸른수목원", 37.482317, 126.82311));
        m_mapPoint.add(new MapPoint(49, "63빌딩", 37.520637, 126.939952));
        m_mapPoint.add(new MapPoint(50, "낙성대공원", 37.471993, 126.958604));
        m_mapPoint.add(new MapPoint(51, "서울식물원", 37.57098, 126.836204));
        m_mapPoint.add(new MapPoint(52, "월미도", 37.471488, 126.596351));
        m_mapPoint.add(new MapPoint(53, "국립수목원", 37.754688, 127.168278));
        m_mapPoint.add(new MapPoint(54, "경인아라뱃길", 37.572936, 126.701339));
        m_mapPoint.add(new MapPoint(55, "상동호수공원", 37.501536, 126.74673));
        m_mapPoint.add(new MapPoint(56, "아인스월드", 37.511908, 126.744218));
        m_mapPoint.add(new MapPoint(57, "한국만화박물관", 37.507557, 126.742726));
        m_mapPoint.add(new MapPoint(58, "플레이아쿠아리움", 37.49976, 126.743402));
        m_mapPoint.add(new MapPoint(59, "만안교", 37.414154, 126.91188));
        m_mapPoint.add(new MapPoint(60, "현충원", 37.503812, 126.975519));
    }

}
