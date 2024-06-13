package com.example.photomemoryalbumapp;

import java.util.ArrayList;

//ArrayListのデータ
public class ArrayData {
    public ArrayList<Integer> arrayList;
    public int year;
    public int month;
    public int day;
    public int size;

    public ArrayData(ArrayList<Integer> arrayList, int year, int month, int day){
        this.arrayList = arrayList;
        this.arrayList.add(-1);
        this.year = year;
        this.month = month;
        this.day = day;
        this.size =this.arrayList.size();
    }

    public ArrayList<Integer> getArrayList(){
        return this.arrayList;
    }

    //numをarrayListの末尾に追加
    public void setIndexArray(int num){
        this.arrayList.add(num);
    }

    public int getSize(){
        return  this.size;
    }

    public int getYear(){
        return this.year;
    }

    public int getMonth(){
        return this.month;
    }

    public int getDay(){
        return  this.day;
    }

}
