package com.example.photomemoryalbumapp;

import android.app.Application;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Common extends Application{
    //グローバルに扱う変数
    //画像の情報，緯度，軽度，日付をdataとする連結リストを作成
    public LinkedList photolist = new LinkedList();
    public Data data;

    @Override
    public void onCreate(){
        super.onCreate();
    }


    public LinkedList getPhotolist(){
        return photolist;
    }

    public void setLinkedList(Data data){

    }
    /*配列の初期値の格納について，以下に後ほど記述*/




}
