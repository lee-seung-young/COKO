package com.example.coko;

import android.location.Location;

public class getDistance {

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
        double distance;
        double distancemt;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = (locationA.distanceTo(locationB))/1000; //단위 m에서 km로 변환
        distancemt = Math.round(distance*10000)/10000.0; // 소숫점 4째자리까지 보여줌
        return distance;
    }
}
