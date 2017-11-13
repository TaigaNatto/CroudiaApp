package com.example.taiga.croudiaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String tokenCode;

    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        tokenCode=intent.getStringExtra("CODE");

        accessToken=null;

        getAccessToken();
    }

    //ささやく
    public void whisper(View v){
        RequestBody formBody = new FormBody.Builder()
                .add("status", "hello!")
                .build();

        final Request request = new Request.Builder()
                .url("https://api.croudia.com/2/statuses/update.json")
                .header("Authorization","Bearer")
                .header("Authorization",accessToken)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"ささやけなかった！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,"ささやいた！",Toast.LENGTH_SHORT).show();
                        Log.d("AAA",res);
                    }
                });
            }
        });
    }

    // GET
    private void getTest() {
        Request request = new Request.Builder()
                .url("http://weather.livedoor.com/forecast/webservice/json/v1?city=130010")     // 130010->東京
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    // POST
    //AccessToken取得
    private void getAccessToken() {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", "d643130e33cc5e1ac0692d40f97d3b0baac499cf8d549b66830bb54137888aad")
                .add("client_secret", "1546d49ba798b512f8493e8a9e87b01313b01f3f69d62d87c00773068e4bd233")
                .add("code", tokenCode)
                .build();

        final Request request = new Request.Builder()
                .url("https://api.croudia.com/oauth/token")       // HTTPアクセス POST送信 テスト確認用ページ
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"失敗！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject json;
                        try {
                            json = new JSONObject(res);
                            accessToken=json.getString("access_token");
                            Toast.makeText(MainActivity.this,"トークンゲット！",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
