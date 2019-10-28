package com.example.yourweather;

public class ThoiTiet {

    public String day;
    public String status;
    public String image;
    public String maxTemp;
    public String minTemp;

    public ThoiTiet(String day, String status, String image, String maxTemp, String minTemp) {
        this.day = day;
        this.status = status;
        this.image = image;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
}
