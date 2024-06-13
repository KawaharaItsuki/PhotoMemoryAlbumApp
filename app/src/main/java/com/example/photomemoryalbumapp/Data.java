package com.example.photomemoryalbumapp;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class Data {
    public Bitmap img_name; //画像のファイル名

    public String landmark_name; //ランドマークの名前
    public double latitude; //緯度
    public double longitude; //経度
    public int year; //年
    public int month; //月
    public int day; //日


    public Data(Bitmap img_name, String landmark_name,  double latitude, double longitude, int year, int month, int day){
        this.img_name = img_name;
        this.landmark_name = landmark_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
        this.month = month;
        this.day = day;
    }



    public Data getData(){
        return  this;
    }

    public Bitmap getImg_name(){
        return this.img_name;
    }

    public String getLandmark_name(){
        return this.landmark_name;
    }

    public void setImg_name(Bitmap img_name){
        this.img_name = img_name;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(int latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(int longitude){
        this.longitude = longitude;
    }

    public int getYear(){
        return this.year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getMonth(){
        return this.month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getDay(){
        return this.day;
    }

    public void setDay(int day){
        this.day = day;
    }


}
