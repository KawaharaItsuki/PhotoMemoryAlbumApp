package com.example.photomemoryalbumapp;


public class ArrayCell {
    ArrayData data;
    ArrayCell next;

    public ArrayCell(ArrayData data){
        this.data = data;
        next = null;
    }

    public ArrayData getData(){
        return this.data;
    }


}
