package com.example.photomemoryalbumapp;

import android.app.Application;

//連結リストのセル
public class Cell {
    Data data;
    Cell next;

    public Cell(Data data){
        this.data = data;
        next = null;
    }

    public Data getData(){
        return this.data;
    }

    public void setData(Data data){
        this.data = data;
    }

    public Cell getNext(){
        return this.next;
    }

    public void setNext(Cell next){
        this.next = next;
    }
}
