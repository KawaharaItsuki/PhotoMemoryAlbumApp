package com.example.photomemoryalbumapp;

import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class Location implements LocationListener {
    LocationManager locationManager;

    public void locationStart(){
        Log.d("debug", "locationStart()");

        //LocationManagerインスタンス生成
        //locationManager = (LocationManager) getSyst
    }
    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<android.location.Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
