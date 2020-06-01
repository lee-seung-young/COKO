package com.example.coko;

public class Place {

    private int place_id;
    private String  name;
    private double latitude;
    private double longitude;
    private String location;
    private String time;
    private String description;
    private String pic;
    private boolean likeox;
    private double distance = 0;
    private int visiters =0;


    public Place() { }

    public Place(int place_id,String name,double latitude, double longitude,String location,String time, String description,String pic, boolean likeox){
        this.place_id=place_id;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.location=location;
        this.time=time;
        this.description=description;
        this.pic=pic;
        this.likeox=likeox;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isLikeox() {
        return likeox;
    }

    public void setLikeox(boolean likeox) {
        this.likeox = likeox;
    }

    public void setDistance(double distance){
        this.distance=distance;
    }

    public double getDistance(){
        return distance;
    }

    public int getVisiters(){ return visiters; }

    public void setVisiters(int visiters){ this.visiters=visiters;}



}
