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


    public Place() { }

    public Place(int place_id, String name, double latitude, double longitude){};

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


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}