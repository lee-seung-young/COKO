package com.example.coko;

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

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class PathActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

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
    TMapData tmapdata = null;

    @Override
    public void onLocationChange(Location location) { //위치 변경 확인
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            TMapPoint pointh = tMapView.getLocationPoint();
            gpsLatitude = pointh.getLatitude();
            gpsLongitude = pointh.getLongitude();
            //GetCarPath(new TMapPoint(gpsLatitude, gpsLongitude), new TMapPoint(38.079666, 128.447609)); 경로 1개
            FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(),tMapView);


            findCarPathTask.execute(new TMapPoint(gpsLatitude,gpsLongitude),new TMapPoint(37.576016, 126.976867),new TMapPoint(37.2844, 127.1052),
                    new TMapPoint(35.175804, 129.043426),new TMapPoint(38.079666, 128.447609),new TMapPoint(37.582978, 126.983661));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        mContext = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        tMapView = new TMapView(this);
        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(mApiKey);
        addPoint();
        showMarkerPoint();

        /*현위치 아이콘표시*/
        tMapView.setIconVisibility(true);

        /*줌레벨*/
        tMapView.setZoomLevel(13);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(PathActivity.this);
        tmapgps.setMinTime(0);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        tmapgps.OpenGps();


        /*화면중심을 단말의 현재위치로 이동*/
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        //마커풍선에서 터치(우측 버튼 클릭)시 할 행동 -> 팝업 띄우기, 2가지 버튼(찜하기/세부정보)
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("필요한 항목을 선택하시오.")
                        .setCancelable(true)
                        .setPositiveButton("세부정보", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();

            }

        });


    }

    /*차로가는 경로 보여주는 함수*/
    void GetCarPath(TMapPoint startPoint, TMapPoint endPoint) {
        tmapdata = new TMapData();
        tmapdata.findPathData(startPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tMapView.addTMapPath(polyLine);
            }
        });
    }

    public void addPoint() { //여기에 핀을 꼽을 포인트들을 배열에 add해주세요!
        //강남//
        m_mapPoint.add(new MapPoint(1,"광화문", 37.576016, 126.976867));
        m_mapPoint.add(new MapPoint(12,"남한산성", 37.2844, 127.1052));
        m_mapPoint.add(new MapPoint(13,"삼광사", 35.175804, 129.043426));
        m_mapPoint.add(new MapPoint(6,"설악산", 38.079666, 128.447609));
        m_mapPoint.add(new MapPoint(7,"북촌한옥마을", 37.582978, 126.983661));
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


            //풍선뷰 안의 항목에 글을 지정
            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.impo1);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarker%d", mMarkerID++);

            tMapView.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);

        }
    }

}
