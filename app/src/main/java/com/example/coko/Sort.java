package com.example.coko;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Sort {
    ArrayList<Place> list;
    ArrayList<Place> temp=new ArrayList<Place>();
    ArrayList<Place> ret=new ArrayList<Place>();

    double longitude;
    double latitude;
    Descending comp1=new Descending();
    Ascending comp2=new Ascending();
    int size;

    public Sort(ArrayList<Place> list, double latitude, double longitude) {
        this.list = list;
        for(int i=0;i<list.size();i++){
            temp.add(list.get(i));
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Sort(ArrayList<Place> list, double latitude, double longitude,int size) {
        this.list = list;
        for(int i=0;i<list.size();i++){
            temp.add(list.get(i));
        }
        this.longitude = longitude;
        this.latitude = latitude;
        this.size=size;
    }

    public void sortByDistance(int x) {
        Iterator<Place> iterator = temp.iterator();
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
        Collections.sort(temp,comp2);
       while(temp.size()>x){
           temp.remove(temp.size()-1);
       }
//       for(int i=0;i<temp.size();i++)
//                Log.d("******","name: "+(temp.get(i).getName())+" distance "+toString().valueOf(temp.get(i).getDistance())+" visitors "+(temp.get(i).getVisitors()));
//        Log.d("******","\n\n");

    }

    public void sortByDistance() {
        Iterator<Place> iterator = temp.iterator();
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
        Collections.sort(temp,comp2);
        for(int i=0;i<temp.size();i++)
             Log.d("******","name: "+(temp.get(i).getName())+" distance "+toString().valueOf(temp.get(i).getDistance())+" visitors "+(temp.get(i).getVisitors()));
    }

    public void sortByPopularity(int x) {
        Iterator<Place> iterator=temp.iterator();

        Collections.sort(temp,comp1);
        while(temp.size()>x){
            temp.remove(temp.size()-1);
        }
//        for(int i=0;i<temp.size();i++)
//             Log.d("******","name: "+(temp.get(i).getName())+" distance "+toString().valueOf(temp.get(i).getDistance())+" visitors "+(temp.get(i).getVisitors()));

    }

    public void sortByPopularity() {
        Iterator<Place> iterator=temp.iterator();

        Collections.sort(temp,comp2);
    }

    public void update_coordinates(){
        this.latitude=temp.get(0).getLatitude();
        this.longitude=temp.get(0).getLongitude();
        Log.d("******","latitude: "+toString().valueOf(latitude)+" longitude "+toString().valueOf(longitude));
        ret.add(temp.get(0));
//        Log.d("******","name: "+(temp.get(0).getName())+" distance "+toString().valueOf(temp.get(0).getDistance())+" visitors "+(temp.get(0).getVisitors()));
//        Log.d("******","name: "+(temp.get(1).getName())+" distance "+toString().valueOf(temp.get(1).getDistance())+" visitors "+(temp.get(1).getVisitors()));
//        Log.d("******","name: "+(temp.get(2).getName())+" distance "+toString().valueOf(temp.get(2).getDistance())+" visitors "+(temp.get(2).getVisitors()));
//        Log.d("******","name: "+(temp.get(3).getName())+" distance "+toString().valueOf(temp.get(3).getDistance())+" visitors "+(temp.get(3).getVisitors()));
//        Log.d("******","name: "+(temp.get(4).getName())+" distance "+toString().valueOf(temp.get(4).getDistance())+" visitors "+(temp.get(4).getVisitors()));

        //checkList();

        list.remove(temp.get(0));
        temp.clear();
        for(int i=0;i<list.size();i++){
            temp.add(list.get(i));
        }

    }

    //Sort1은 거리로 10개 뽑고 -> 인기순으로 5개 뽑고 -> 거리순으로 5개 뽑음
    public ArrayList<Place> Sort1(){
        for(int i=0;i<5;i++) {
            sortByDistance(10);
            sortByPopularity(5);
            sortByDistance();
            update_coordinates();
            Log.d("**********"," \n");
        }
        return ret;
    }

    public ArrayList<Place> Sort2(){
        for(int i=0;i<size;i++) {
            sortByDistance();
            update_coordinates();
        }
        return ret;
    }

}
