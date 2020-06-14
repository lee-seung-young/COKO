package com.example.coko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Sort {
    ArrayList<Place> list=new ArrayList<>();
    double latitude;
    double longitude;
    MiniComparator comp=new MiniComparator();

    public Sort(ArrayList<Place> list, double latitude, double longitude) {
        this.list = list;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Sort() {
    }

    //거리순으로 정렬, 리스트에 x개만 남기기위한 함수
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
            }
        }
    }
    //인기순으로 list정렬하고 3개만 남기고 remove
    public void chosenByPopularity() {
        Iterator<Place> iterator=this.list.iterator();

        Collections.sort(this.list,comp);
    }

    //거리순으로 10개 뽑고 뽑은거 인기순 정렬 그후 거리순으로 정렬후 5개 뽑음
    public ArrayList<Place> Sort (){
        sortByDistance();
        chosenByPopularity();
        sortByDistance();
        return this.list;
    }


}
