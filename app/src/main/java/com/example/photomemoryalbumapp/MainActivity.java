package com.example.photomemoryalbumapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //建物情報を格納する連結リスト(グローバル変数)
    public static LinkedList photoList = new LinkedList();

    //日付に対応する画像のインデックスを格納する連結リスト
    public static ArrayLinkedList matchIndexList = new ArrayLinkedList();

    //APIKey
    public static final String CLOUD_VISION_API_KEY = "Your API Key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ボタンの宣言
        ImageButton addbutton = (ImageButton) findViewById(R.id.addButton);
        ImageButton returnbutton = (ImageButton) findViewById(R.id.returnButton);
        ImageButton earthbutton = (ImageButton) findViewById(R.id.earthButton);
        ImageButton locationbutton = (ImageButton) findViewById(R.id.locationButton);

        //ログ
        Log.i("要素数", String.valueOf(photoList.getSize()));

        Log.i("ArrayLinkedList", String.valueOf(matchIndexList.getSize()));

        //ボタンを押す時の挙動を設定
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhotoAdd.class);
                startActivity(intent);
            }
        });

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhotoRetrun.class);
                startActivity(intent);
            }
        });

        earthbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NowLocation.class);
                startActivity(intent);
            }
        });

        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PustMap.class);
                startActivity(intent);
            }
        });

    }
}