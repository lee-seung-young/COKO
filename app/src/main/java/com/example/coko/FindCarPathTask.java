package com.example.coko;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class FindCarPathTask extends AsyncTask<TMapPoint,Void,Double> { // 다중 경로화 함수
    Context context;
    TMapPolyLine tMapPolyLine;

    ArrayList<TMapPolyLine> tMapPolyLines=new ArrayList();
    TMapView tMapView;
    int length;

    public FindCarPathTask(Context context, TMapView tMapView){
        super();
        this.context=context;
        this.tMapView=tMapView;
    }
    @Override
    protected void onPostExecute(Double distance){
        super.onPreExecute();
    }
    @SuppressLint("WrongThread")
    @Override
    protected Double doInBackground(TMapPoint... tMapPoints) {
        TMapData tMapData = new TMapData();
        length=tMapPoints.length;
        for(int i=0;i<tMapPoints.length-1;i++) {
            try {
//            tMapPolyLine1 = tMapData.findPathData(tMapPoints[0], tMapPoints[1]);
//            tMapPolyLine1.setLineColor(Color.RED);
//            tMapPolyLine1.setOutLineColor(Color.RED);
//            tMapPolyLine1.setLineWidth(10);
                tMapPolyLine=tMapData.findPathData(tMapPoints[i], tMapPoints[i+1]);
                tMapPolyLine.setLineColor(Color.RED);
                tMapPolyLine.setOutLineColor(Color.RED);
                tMapPolyLine.setLineWidth(10);
                tMapPolyLines.add(tMapPolyLine);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<length-1;i++){
            tMapView.addTMapPolyLine("Line"+i,tMapPolyLines.get(i));
        }
        return tMapPolyLines.get(0).getDistance();
    }
}
