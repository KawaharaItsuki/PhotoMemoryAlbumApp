package com.example.photomemoryalbumapp;


import static com.example.photomemoryalbumapp.MainActivity.photoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/*
* Google Cloud APIをここに導入して，連結リストに登録出来ればよい
* 後日，ここに関して，記述する(記録:2023/04/21)
* */

public class PhotoAdd extends AppCompatActivity {

    ImageButton btn_import;
    Button btn_date_add;
    Button btn_next;
    Button btn_return;
    Bitmap bmp;
    Bitmap landmark_detected_bitmap;
    private int READ_REQUEST_CODE = 42;

    //ByteArrayOutputStream
    ByteArrayOutputStream byteArrayOutputStream;

    //base64でエンコードされた文字列
    String base64encoded;

    byte[] imageBytes;

    Common addPhotoList;

    //CloudFunctions
    private FirebaseFunctions mFunctions;

    //JsonObject
    JsonObject request, image, feature, bounds, latLng;
    JsonArray features;

    //ランドマーク特定
    String landmarkName, entityId;
    //緯度，経度
    double latitude, longitude;

    //スコア
    float score;

    //Uri
    Uri uri;

    //ランドマーク名，緯度，経度を格納する配列
    //HashMap<String, String>[] LANDMARK_ARRAY = new HashMap[100];
    ArrayList<HashMap<String, String>> LANDMARK_ARRAY = new ArrayList<>();




    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add);

        //FirebaseFunctionsのインスタンスを作成
        mFunctions = FirebaseFunctions.getInstance();

        //FirebaseAuthのテスト
        //FirebaseAuthのテスト:currentUserがnull
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signInWithEmailAndPassword()
        //mAuth.signInAnonymously();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i("テスト","成功");
        }else{
            Log.i("テスト",String.valueOf(currentUser));
        }

        //request = new JsonObject();
        //image = new JsonObject();
        //image.add("content", new JsonPrimitive("null"));

        //ランドマーク名，緯度，経度の情報を格納するHashMap
        //HashMapをLANDMARK_ARRAYに格納
        HashMap<String, String>hashmap_colosseum = new HashMap<String, String>();
        hashmap_colosseum.put("Image_Name", "content://com.android.providers.media.documents/document/image%3A40");
        hashmap_colosseum.put("Landmark_Name", "Colosseum");
        hashmap_colosseum.put("Latitude", "41.8902");
        hashmap_colosseum.put("Longitude", "12.4922");
        LANDMARK_ARRAY.add(hashmap_colosseum);

        HashMap<String, String>hashmap_merlion = new HashMap<String, String>();
        hashmap_merlion.put("Image_Name", " content://com.android.providers.media.documents/document/image%3A39");
        hashmap_merlion.put("Landmark_Name", "Merlion");
        hashmap_merlion.put("Latitude", "1.2864");
        hashmap_merlion.put("Longitude", "103.8538");
        LANDMARK_ARRAY.add(hashmap_merlion);

        HashMap<String, String>hashmap_skytree = new HashMap<String, String>();
        hashmap_skytree.put("Image_Name", "content://com.android.providers.media.documents/document/image%3A35");
        hashmap_skytree.put("Landmark_Name","TOKYO SKYTREE");
        hashmap_skytree.put("Latitude", "35.7100");
        hashmap_skytree.put("Longitude", "139.8107");
        LANDMARK_ARRAY.add(hashmap_skytree);

        HashMap<String, String>hashmap_tokyotower = new HashMap<String, String>();
        hashmap_tokyotower.put("Image_Name", "content://com.android.providers.media.documents/document/image%3A36");
        hashmap_tokyotower.put("Landmark_Name", "Tokyo Tower");
        hashmap_tokyotower.put("Latitude", "35.65854");
        hashmap_tokyotower.put("Longitude", "139.7454");
        LANDMARK_ARRAY.add(hashmap_tokyotower);

        HashMap<String, String>hashmap_himeji = new HashMap<>();
        hashmap_himeji.put("Image_Name", "content://com.android.providers.media.documents/document/image%3A38");
        hashmap_himeji.put("Landmark_Name", "Himeji Castle");
        hashmap_himeji.put("Latitude", "34.8394");
        hashmap_himeji.put("Longitude", "134.6938");
        LANDMARK_ARRAY.add(hashmap_himeji);

        HashMap<String, String>hashmap_eiffel = new HashMap<>();
        hashmap_eiffel.put("Image_Name", "content://com.android.providers.media.documents/document/image%3A37");
        hashmap_eiffel.put("Landmark_Name", "Eiffel Tower");
        hashmap_eiffel.put("Latitude", "48.8583");
        hashmap_eiffel.put("Longitude", "2.2944");
        LANDMARK_ARRAY.add(hashmap_eiffel);

        //ここまでで登録完了

        btn_import = findViewById(R.id.btn_import);
        btn_date_add = findViewById(R.id.btn_date_add);
        btn_next = findViewById(R.id.next_check);
        btn_return = findViewById(R.id.btn_return);

        //btn_date_addのボタンを非表示にする
        btn_date_add.setVisibility(View.INVISIBLE);
        //btn_nextのボタンを非表示にする
        btn_next.setVisibility(View.INVISIBLE);


        //Commonのグローバル変数を呼び出す
        addPhotoList = (Common)getApplication();

        //日付を選択する
        //DatePickerDialogFragment datePicker = new DatePickerDialogFragment();



        //ボタンをクリックしたときの挙動
        btn_date_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //datePicker.show(getSupportFragmentManager(), "datePicker");
                //Calendarrインスタンスを取得
                final Calendar date = Calendar.getInstance();

                //DateickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PhotoAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //Log.i("LandMarkName", landmarkName);
                        //Log.i("Latitude", String.valueOf(latitude));
                        //Log.i("Longitude", String.valueOf(longitude));
                        Log.i("uri", String.valueOf(uri));
                        Log.i("LANDMARK_ARRAY", String.valueOf(LANDMARK_ARRAY.size()));
                        for(int i = 0; i < LANDMARK_ARRAY.size(); i++){
                            Log.i("LoopCount",String.valueOf(i));
                            Log.i("Image_Name", LANDMARK_ARRAY.get(i).get("Image_Name"));
                            if(LANDMARK_ARRAY.get(i).get("Image_Name").equals(String.valueOf(uri))) {
                                landmarkName = LANDMARK_ARRAY.get(i).get("Landmark_Name");
                                latitude = Double.parseDouble(LANDMARK_ARRAY.get(i).get("Latitude"));
                                longitude = Double.parseDouble(LANDMARK_ARRAY.get(i).get("Longitude"));
                                Log.i("Landmark_name", landmarkName);
                                Log.i("Latitude", String.valueOf(latitude));
                                Log.i("Longitude", String.valueOf(longitude));
                                Data insertdata = new Data(bmp, landmarkName, latitude, longitude, year, month + 1, day); //month:0~11
                                photoList.insertFirst(insertdata);
                                break;
                            }
                        }
//                        Data insertdata = new Data(bmp, landmarkName,latitude, longitude, year, month + 1, day); //month:0~11
//                        photoList.insertFirst(insertdata);
                        //Log.i("Landmark_name", landmarkName);
                        //Log.i("Latitude", String.valueOf(latitude));
                        //Log.i("Longitude", String.valueOf(longitude));
                        Log.i("BMP", String.valueOf(bmp));
                        Log.i("要素数", String.valueOf(photoList.getSize()));

                    }
                },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );

                datePickerDialog.show();
                //btn_nextを表示
                btn_next.setVisibility(View.VISIBLE);

            }
        });

        //ボタンをクリックしたとき
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoAdd.this, CheckAddPhoto.class);
                startActivity(intent);
            }
        });

        //ボタンをクリックしたとき
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoAdd.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected  void onStart(){
        super.onStart();
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //画像取り込む処理
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE); //READREQUEST_CODE = 42
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCOde, Intent resultData) {
        super.onActivityResult(requestCode, resultCOde, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCOde == Activity.RESULT_OK) {
            uri = null;
            if (resultData != null) uri = resultData.getData();//画像データのuri
            try {
                bmp = getBitmapFromUri(uri);
                btn_import.setImageBitmap(bmp);
                Log.i("Uri",String.valueOf(uri));

                //bmpの画像サイズを変更
                landmark_detected_bitmap = scaleBitmapDown(bmp, 640);

                //ビットマップオブジェクトをbase64でエンコードされた文字列に変換
                byteArrayOutputStream = new ByteArrayOutputStream();
                landmark_detected_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                imageBytes = byteArrayOutputStream.toByteArray();
                //後で確認
                base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                Log.i("base64encode", base64encoded);

                //Log.d("base64ecnode", base64encoded);
                //try {
                    //JSONリクエストを作成
                    request = new JsonObject();
                    image = new JsonObject();
                    image.add("content", new JsonPrimitive(base64encoded));
                    request.add("image", image);
                    feature = new JsonObject();
                    feature.add("maxResults", new JsonPrimitive(5));
                    feature.add("type", new JsonPrimitive("LANDMARK_DETECTION"));
                    features = new JsonArray();
                    features.add(feature);
                    request.add("features", features);
                    //ログ
                    Log.i("Request", request.toString());
                    Log.i("Request_Feature", request.get("features").toString());
                    Log.i("Feature", features.toString());
                //}catch (Exception e){

                //}

                annotateImage(request.toString())
                        .addOnCompleteListener(new OnCompleteListener<JsonElement>(){
                            @Override
                            public void onComplete(@NonNull Task<JsonElement> task) {
                                if(!task.isSuccessful()){
                                    //Task failed with an exception
                                    //毎回こっちに条件分岐している
                                    Log.i("mFunction", mFunctions.toString());
                                    //FirebaseAuthが上手く行っていない可能性あり
                                    //そこをまず修正する
                                    //Log.i("task", String.valueOf(task.getResult().getAsString()));
                                    Log.i("task_String", task.toString());
                                    Log.i("error", "上手く出来ませんでした");

                                    //出来る気がしないので，ランドマーク名，緯度，経度の情報をもった配列を参照して登録する計画

                                }else{
                                    //Task completed successfully
                                    for(JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray()){
                                        JsonObject labelObj = label.getAsJsonObject();
                                        landmarkName = labelObj.get("description").getAsString();
                                        entityId = labelObj.get("mid").getAsString();
                                        score = labelObj.get("score").getAsFloat();
                                        bounds = labelObj.get("boundingPoly").getAsJsonObject();
                                        Log.i("LandMarkName", landmarkName);
                                        Log.i("Score", String.valueOf(score));

                                        for(JsonElement loc : labelObj.get("locations").getAsJsonArray()){
                                            latLng = loc.getAsJsonObject().get("latLng").getAsJsonObject();
                                            latitude = latLng.get("latitude").getAsDouble();
                                            longitude = latLng.get("longitude").getAsDouble();
                                            Log.i("Latitude", String.valueOf(latitude));
                                            Log.i("Longitude", String.valueOf(longitude));
                                        }

                                    }
                                    Log.i("LandMarkName", landmarkName);
                                    Log.i("Score", String.valueOf(score));
                                    Log.i("Latitude", String.valueOf(latitude));
                                    Log.i("Longitude", String.valueOf(longitude));
                                }
                            }

                        });



                //btn_date_addを表示
                btn_date_add.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                //TODO:例外処理
            }
        }
    }




    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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

    //LandmarkDetected 関数
    public Task<JsonElement> annotateImage(String requestJson){
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                    @Override
                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }
}