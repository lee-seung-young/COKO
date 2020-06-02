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

import javax.xml.parsers.ParserConfigurationException;

public class FindCarPathTask extends AsyncTask<TMapPoint,Void,Double> { // 다중 경로화 함수
    Context context;
    TMapPolyLine tMapPolyLine1;
    TMapPolyLine tMapPolyLine2;
    TMapPolyLine tMapPolyLine3;
    TMapPolyLine tMapPolyLine4;
    TMapPolyLine tMapPolyLine5;
    TMapView tMapView;

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
        try {
            tMapPolyLine1 = tMapData.findPathData(tMapPoints[0], tMapPoints[1]);
            tMapPolyLine1.setLineColor(Color.RED);
            tMapPolyLine1.setOutLineColor(Color.RED);
            tMapPolyLine1.setLineWidth(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        try {
            tMapPolyLine2 = tMapData.findPathData(tMapPoints[1], tMapPoints[2]);
            tMapPolyLine2.setLineColor(Color.RED);
            tMapPolyLine2.setOutLineColor(Color.RED);
            tMapPolyLine2.setLineWidth(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        try {
            tMapPolyLine3 = tMapData.findPathData(tMapPoints[2], tMapPoints[3]);
            tMapPolyLine3.setLineColor(Color.RED);
            tMapPolyLine3.setOutLineColor(Color.RED);
            tMapPolyLine3.setLineWidth(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        try {
            tMapPolyLine4 = tMapData.findPathData(tMapPoints[3], tMapPoints[4]);
            tMapPolyLine4.setLineColor(Color.RED);
            tMapPolyLine4.setOutLineColor(Color.RED);
            tMapPolyLine4.setLineWidth(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        try {
            tMapPolyLine5 = tMapData.findPathData(tMapPoints[4], tMapPoints[5]);
            tMapPolyLine5.setLineColor(Color.RED);
            tMapPolyLine5.setOutLineColor(Color.RED);
            tMapPolyLine5.setLineWidth(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine1);
        tMapView.addTMapPolyLine("Line2", tMapPolyLine2);
        tMapView.addTMapPolyLine("Line3", tMapPolyLine3);
        tMapView.addTMapPolyLine("Line4", tMapPolyLine4);
        tMapView.addTMapPolyLine("Line5", tMapPolyLine5);

        return tMapPolyLine1.getDistance();
    }
}
