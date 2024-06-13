package com.example.photomemoryalbumapp;

import static com.example.photomemoryalbumapp.MainActivity.photoList;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

//写真を確認する画面
public class CheckAddPhoto extends AppCompatActivity {

    Button btn_yes;
    Button btn_no;
    TextView txt_date,txt_landmarkinfo;
    ImageView check_photo;

    double latitude, longitude;
    String landmark_name;

    //Firebase
    private FirebaseAnalytics mFirebaseAnalytics;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_add_photo);

        //Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //ランドマーク検出ツール

        btn_yes = findViewById(R.id.ansYes);
        btn_no = findViewById(R.id.ansNo);
        txt_date = findViewById(R.id.checkDate);
        check_photo = findViewById(R.id.checkPhoto);
        txt_landmarkinfo = findViewById(R.id.LandMarkInfo_text);


        Cell firstcell = photoList.getfirstCell();
        Bitmap image_name = firstcell.getData().getImg_name();
        Log.i("Bitmap", String.valueOf(image_name));
        int get_year = firstcell.getData().getYear();
        int get_month = firstcell.getData().getMonth();
        int get_day = firstcell.getData().getDay();

        //登録画面で表示
        check_photo.setImageBitmap(image_name);
        txt_date.setText(String.format("%d年 %d月 %d日", get_year, get_month, get_day));

        //ログ
        Log.i("ランドマーク", photoList.getCell(get_year, get_month, get_day).getData().getLandmark_name());
        Log.i("Latitude", String.valueOf(photoList.getCell(get_year, get_month, get_day).getData().getLatitude()));
        Log.i("Longtitude", String.valueOf(photoList.getCell(get_year, get_month, get_day).getData().getLongitude()));
        landmark_name = photoList.getCell(get_year, get_month, get_day).getData().getLandmark_name();
        latitude = photoList.getCell(get_year, get_month, get_day).getData().getLatitude();
        longitude = photoList.getCell(get_year, get_month, get_day).getData().getLongitude();

        Log.i("DataArray", String.valueOf(photoList.get_AllCellData().length));

        //緯度，経度，ランドマークを表記
        if(latitude > 0 && longitude > 0){
            txt_landmarkinfo.setText(String.format("%s %s Location:%s", String.valueOf(latitude), String.valueOf(longitude), landmark_name));
        }else if(latitude > 0 && longitude < 0){
            txt_landmarkinfo.setText(String.format("%s -%s Location:%s", String.valueOf(latitude), String.valueOf(longitude), landmark_name));
        }else if(latitude < 0 && longitude > 0){
            txt_landmarkinfo.setText(String.format("-%s %s Location:%s", String.valueOf(latitude), String.valueOf(longitude), landmark_name));
        }else if(latitude < 0 && longitude < 0){
            txt_landmarkinfo.setText(String.format("-%s -%s Location:%s", String.valueOf(latitude), String.valueOf(longitude), landmark_name));
        }



        //ボタンをクリックしたとき
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckAddPhoto.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //登録したものを削除
                photoList.deleteFirst();
                Intent intent = new Intent(CheckAddPhoto.this, PhotoAdd.class);
                startActivity(intent);

            }
        });
    }

}