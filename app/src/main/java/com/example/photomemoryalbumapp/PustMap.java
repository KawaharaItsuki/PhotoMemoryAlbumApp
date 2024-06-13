package com.example.photomemoryalbumapp;

import static com.example.photomemoryalbumapp.MainActivity.photoList;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.photomemoryalbumapp.databinding.ActivityPustMapBinding;

/**
 * 過去訪れた場所をマーカーで示すクラス
 */
public class PustMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPustMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPustMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //同じ場所を違う日付で登録したときは，最近写真登録したときのが参照される
        Data[] location_data = photoList.get_AllCellData();
        for(int  i = 0; i < location_data.length; i++){
            double latitude = location_data[i].getLatitude();
            double longitude = location_data[i].getLongitude();
            String landmark_name = location_data[i].getLandmark_name();
            int year = location_data[i].getYear();
            int month = location_data[i].getMonth();
            int day = location_data[i].getDay();
            LatLng addmap = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(addmap).title(String.format("%d/%d/%d %s", year, month, day, landmark_name)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(addmap));
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}