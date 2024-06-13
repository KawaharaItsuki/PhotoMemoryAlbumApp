package com.example.photomemoryalbumapp;

import static com.example.photomemoryalbumapp.MainActivity.photoList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.photomemoryalbumapp.databinding.ActivityNowLocationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * 現在地の情報から，選択した地点との距離を求めたり，移動経路を求めたりするクラス
 */
public class NowLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityNowLocationBinding binding;
    //LocationManager locationManager;

    //位置情報
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;

    public double currentlatitude, currentlonitude;

    public LatLng currentlocation;
    private final ActivityResultLauncher<String>
            requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    Log.i("テスト", "成功");
                    startUpdateLocation();
                } else {
                    Log.i("テスト", "失敗");
                }
            }
    );

    //public LatLng current_location;

    //位置情報2
    private LocationManager locationManager;

    private LocationCallback locationCallback;

    //public LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //LocationClientクラスのインスタンスを生成
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //位置情報取得開始
        startUpdateLocation();
        //fusedLocationClient.requestLocationUpdates(locationRequest,  new MyLocationCallback(), null);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            //位置情報取得
            //startUpdateLocation();

        }


        binding = ActivityNowLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        binding = ActivityNowLocationBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }


    protected  void createLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //現在の位置情報設定を取得
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

    }

    /**
     * 位置情報取得開始メソッド
     */
    private void startUpdateLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 権限がない場合、許可ダイアログ表示
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 2000);
            return;
        }
        // 位置情報の取得方法を設定
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);       // 位置情報更新間隔の希望
        locationRequest.setFastestInterval(5000); // 位置情報更新間隔の最速値
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest,  new MyLocationCallback(), null);




    }

    /**
     * 許可ダイアログの結果受取
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 位置情報取得開始
            startUpdateLocation();
        }
    }


    /**
     * 位置情報受取コールバッククラス
     */
    private class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.d("location", "null");
                return;
            }
            // 現在値を取得
            location = locationResult.getLastLocation();
            Log.d("location", "Location");
            Log.d("Latituide", String.valueOf(location.getLatitude()));
            Log.d("Longitude", String.valueOf(location.getLongitude()));
            //現在地がすこしずれていたので，補正した
            currentlatitude = location.getLatitude() -2.84;
            currentlonitude = (-1)*location.getLongitude() + 13.5;
            currentlocation = new LatLng(currentlatitude, currentlonitude);
            Log.i("Latitude_Map", String.valueOf(currentlatitude));
            Log.i("Longitude_Map", String.valueOf(currentlonitude));
            LatLng currentlocation = new LatLng(currentlatitude, currentlonitude);
            mMap.addMarker(new MarkerOptions().position(currentlocation).title("current"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));

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

            //クリックイベント
            //マーカーをタップしたとき，現在地とタップしたマーカーの二点間の距離をToastで表示
            mMap.setOnMapClickListener(new OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    //Toast.makeText(getApplicationContext(), "タップ緯度\n緯度:"+latLng.latitude + "\n経度:" + latLng.longitude,Toast.LENGTH_LONG).show();
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            //検証済み
                            //Toast.makeText(getApplicationContext(), "タップ緯度\n緯度:"+latLng.latitude + "\n経度:" + latLng.longitude,Toast.LENGTH_LONG).show();
                            Log.d("touch_lat", String.valueOf(latLng.latitude));
                            Log.d("toucn_lon", String.valueOf(latLng.longitude));

                            float[] resluts = new float[3];
                            Location.distanceBetween(currentlatitude, currentlonitude, latLng.latitude, latLng.longitude, resluts);
                            double distance = getDistance(currentlatitude, currentlonitude, latLng.latitude, latLng.longitude);
                            Log.d("二点間距離", String.valueOf(resluts[0]));
                            Log.d("二点間距離1", String.valueOf(distance));
                            Toast.makeText(getApplicationContext(), "現在地からの距離:" + distance + "km", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    });
                    //Location.distanceBetween(currentlatitude, currentlonitude, latLng.latitude, latLng.longitude, resluts);
                    double distance = getDistance(currentlatitude, currentlonitude, latLng.latitude, latLng.longitude);
                    Log.d("二点間距離1", String.valueOf(distance));
                }
            });

        };
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = (Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta)));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double miles = (dist * 60 * 1.1515);

        return (miles * 1.609344);

    }

    private double rad2deg(double dist) {
        return (dist * (180/Math.PI));
    }

    public double deg2rad(double degrees){
        return (degrees*(Math.PI/ 180));
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
//        Log.i("Latitude_Map", String.valueOf(currentlatitude));
//        Log.i("Longitude_Map", String.valueOf(currentlonitude));
        //currentlocation = new LatLng(currentlatitude, currentlonitude);
        //mMap.addMarker(new MarkerOptions().position(currentlocation).title("current"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));

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
        Log.i("Latitude_Map", String.valueOf(currentlatitude));
        Log.i("Longitude_Map", String.valueOf(currentlonitude));
//        LatLng currentlocation = new LatLng(currentlatitude, currentlonitude);
//        mMap.addMarker(new MarkerOptions().position(currentlocation).title("current"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}