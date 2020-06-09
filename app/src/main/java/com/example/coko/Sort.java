package com.example.coko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Sort {
    ArrayList<Place> list;
    double longitude;
    double latitude;
    Descending comp1=new Descending();
    Ascending comp2=new Ascending();

    public Sort(ArrayList<Place> list, double longitude, double latitude) {
        this.list = list;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void sortByDistance(int x) {
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
        Collections.sort(this.list,comp1);
       while(list.size()>x){
           list.remove(list.size()-1);
       }
    }

    public void sortByPopularity() {
        Iterator<Place> iterator=this.list.iterator();

        Collections.sort(this.list,comp2);

    }

    public void Sort(){
        sortByDistance(10);
        sortByPopularity();
        sortByDistance(5);
    }
}
