package com.example.photomemoryalbumapp;

import static com.example.photomemoryalbumapp.MainActivity.photoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class PhotoRetrun extends AppCompatActivity {
    Button btn_backhome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_retrun);

        btn_backhome = findViewById(R.id.btnbackhome);

        //ログ
        //Log.i("getYear", String.valueOf(photoList.getfirstCell().getData().getYear()));
        //ボタンをクリックしたと気の処理
        btn_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backintent = new Intent(PhotoRetrun.this, MainActivity.class);
                startActivity(backintent);
            }
        });

    //カレンダーの作成
    CalendarView calendar = findViewById(R.id.calendar);
    calendar.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener(){
                    //日付選択したとき
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {

                        Intent intent = new Intent(PhotoRetrun.this, PhotoPrint.class);
                        //Toastで表示
                        String message = year + "/" + (month + 1) + "/" + date;
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        //ログ
                        Log.i("Year", String.valueOf(year));
                        Log.i("month", String.valueOf(month + 1));
                        Log.i("day", String.valueOf(date));

                        Log.i("matchcount_p", String.valueOf(photoList.getMatchCount()));

                        //データ受け渡し
                        intent.putExtra("selectYear", year);
                        intent.putExtra("selectMonth", (month + 1));
                        intent.putExtra("selectDay", date);

                        startActivity(intent);

                    }

                }
     );

    }
}