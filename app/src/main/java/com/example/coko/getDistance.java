package com.example.coko;

import android.location.Location;

//두 지점 거리 구하는 함수 클래스 (km 단위, 소수점 4째 까지)
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

        distance = (locationA.distanceTo(locationB))/1000;

        distancemt = Math.round(distance*10000)/10000.0;

        return distancemt;
    }
}
