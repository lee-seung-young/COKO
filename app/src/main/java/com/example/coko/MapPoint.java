package com.example.coko;

public class MapPoint {
    private String Name;
    private double latitude;
    private double longitude;
    private int Place_id;

    public MapPoint(){
        super();
    }

    public MapPoint(int place_id, String Name, double latitude, double longitude) {
        //super()
        this.Name=Name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.Place_id = place_id;
    }


    public int getPlace_id() {
        return Place_id;
    }

    public void setPlace_id(int place_id) {
        this.Place_id = place_id;
    }

    public String getName(){
        return Name;
    }

    public void setName(String Name){
        this.Name=Name;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude=latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }
}
