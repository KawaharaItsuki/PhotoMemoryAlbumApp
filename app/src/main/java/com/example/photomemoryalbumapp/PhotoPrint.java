package com.example.photomemoryalbumapp;

import static com.example.photomemoryalbumapp.MainActivity.matchIndexList;
import static com.example.photomemoryalbumapp.MainActivity.photoList;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class PhotoPrint extends AppCompatActivity {
    //public int getyear, getmonth, getday;
    //public int matchsize;
    Button btn_back;
    TextView date_text, print_text;
    LinearLayout layout;
     //none.jpgを格納する配列
    int[] change_photo_list = new int[Byte.MAX_VALUE];

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_print);

        change_photo_list[1] = R.id.imageView1;
        change_photo_list[2] = R.id.imageView2;
        change_photo_list[3] = R.id.imageView3;
        change_photo_list[4] = R.id.imageView4;
        change_photo_list[5] = R.id.imageView5;
        change_photo_list[6] = R.id.imageView6;
        change_photo_list[7] = R.id.imageView7;
        change_photo_list[8] = R.id.imageView8;
        change_photo_list[9] = R.id.imageView9;
        change_photo_list[10] = R.id.imageView10;

        int matchcount = 0;

        date_text = findViewById(R.id.text);

        btn_back = findViewById(R.id.btnback);
        //ボタンクリックの処理
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_intent = new Intent(PhotoPrint.this, PhotoRetrun.class);
                startActivity(back_intent);
            }
        });

        //現在の日付
        Calendar cal = Calendar.getInstance();
        int currentYaer = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        //データの受け取り
        //受け取りデータがない場合は，現在の日付を受け取る
        Intent intent = getIntent();
        int getyear = intent.getIntExtra("selectYear", currentYaer);
        int getmonth = intent.getIntExtra("selctMonth", currentMonth);
        int getday = intent.getIntExtra("selectDay", currentDay);

        //ログ
        /*
        * データの受け渡しができているか
        */
        Log.i("getIntent_year", String.valueOf(getyear));
        Log.i("getIntent_month", String.valueOf(getmonth));
        Log.i("getIntent_day", String.valueOf(getday));

        //TextViewに選択した日付を表示
        date_text = findViewById(R.id.DatePrint);
        date_text.setText(String.format("%d/ %d/ %d", getyear, getmonth, getday));

        //登録しているかを示すTextView
        print_text = findViewById(R.id.text);

        //LinearLayout
        layout = (LinearLayout) findViewById(R.id.linerlayout);

        //選択した日付に対応するデータの個数
        try{
            matchcount = photoList.matchCount(getyear, getmonth, getday, matchIndexList);
        }catch (Exception e){
        }
        //photoList.matchCount(getyear, getmonth, getday);
        matchcount = photoList.getMatchCount();
        Log.i("matchcount", String.valueOf(matchcount));
        Log.i("MatchArrayLinkedList 要素", String.valueOf(matchIndexList.getSize()));


        //ScrollView
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                matchParent, wrapContent));

        //登録されている写真がないかあるか
        if(matchcount != 0){
            //テキストを非表示にする
            print_text.setVisibility(View.INVISIBLE);
            Log.i("MatchIndexListSize", String.valueOf(matchIndexList.getSize()));
            ArrayList<Integer> matcharray = matchIndexList.getArrayList(getyear, getmonth, getday);
            Log.i("match_array_size", String.valueOf(matcharray.size()));

            //写真を設定する
            for(int i = 0; i < matcharray.size(); i++){
                //ArrayList<Integer> matcharray = matchIndexList.getArrayList(getyear, getmonth, getday);
                int match_index = matcharray.get(i);
                if(match_index != -1){
                    Log.i("match_index_now", String.valueOf(match_index));
                    Data get_match_Data = photoList.getIndexData(match_index);
                    //nullがでてしまった
                    //LinkedListのgetIndexDataがおかしいかもしれない
                    Log.i("Match_LandmarkName", get_match_Data.getLandmark_name());
                    Log.i("mathch_data_day", String.valueOf(get_match_Data.getDay()));
                    ImageView imageView = findViewById(change_photo_list[i]);

                    imageView.setImageBitmap(scaleBitmapDown(get_match_Data.getImg_name(), 1200));

                }
                //Log.i("match_array_size", String.valueOf(matcharray.size()));
                //Data get_match_Data = photoList.getIndexData(match_index);
                //Log.i("mathch_data_day", String.valueOf(get_match_Data.getDay()));
                //ImageView imageView = findViewById(change_photo_list[i]);
                //imageView.setImageBitmap(get_match_Data.getImg_name());
                //imageView.setLayoutParams(new LinearLayout.LayoutParams(
                //        matchParent, wrapContent
                //));
                //layout.addView(imageView);
                //setContentView(scrollView);
            }

        }else{
            //テキストを表示する
            print_text.setVisibility(View.VISIBLE);


            /*
            *多分，必要なし
            *あとで要確認
            *ここから
             */
            //TextView text = new TextView(this);
            //TextViewの設定
            //text.setText("登録された写真はありません");
            //text.setTextSize(28);
            //text.setGravity(Gravity.TOP|Gravity.CENTER);
            //text.setWidth();

            //text.setLayoutParams(new LinearLayout.LayoutParams(
            //        matchParent, wrapContent
            //));

            //scrollView.addView(text);
            //setContentView(scrollView);
            /*
            * ここまで
            */
        }

    }
    /**
     * CloudVisionの推奨サイズに画像を変更させるメソッド
     * LANDMARK_DETECTION:640x480
     * @param bitmap 入力画像
     * @param maxDimension
     * @return
     */
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension){
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizeWidth = maxDimension;
        int resizeHeight = maxDimension;

        if(originalHeight > originalWidth){
            resizeHeight = maxDimension;
            resizeWidth = (int)(resizeHeight * (float) originalWidth/ (float)originalHeight);
        }else if(originalWidth > originalHeight){
            resizeWidth = maxDimension;
            resizeHeight = (int)(resizeWidth * (float)originalHeight /(float)originalWidth);
        }else if(originalWidth == originalHeight){
            resizeWidth = maxDimension;
            resizeWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizeWidth, resizeHeight, false);
    }
}