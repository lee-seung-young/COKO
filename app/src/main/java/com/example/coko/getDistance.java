package com.example.coko;

import android.location.Location;

public class getDistance {

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
//        double distance;
//        double distancemt;
//
//        Location locationA = new Location("point A");
//        locationA.setLatitude(lat1);
//        locationA.setLongitude(lng1);
//
//        Location locationB = new Location("point B");
//        locationB.setLatitude(lat2);
//        locationB.setLongitude(lng2);
//
//        distance = (locationA.distanceTo(locationB))/1000; //단위 m에서 km로 변환
//        distancemt = Math.round(distance*10000)/10000.0; // 소숫점 4째자리까지 보여줌
//        return distance;
        double theta;
        double dist;

        if(lng1>=lng2)
            theta=lng1-lng2;
        else
            theta=lng2-lng1;

        dist=Math.sin(deg2rad(lat1))*Math.sin(deg2rad(lat2))+Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));

        dist=Math.acos(dist);
        dist=rad2deg(dist);
        dist=dist*6*1.1515*1.609344;    //거리를 kilometer 표시

        return dist;
    }
    private double deg2rad(double deg){
        return deg*Math.PI/180.0;
    }

    private double rad2deg(double rad)
    {
        return rad*180/Math.PI;
    }
}
