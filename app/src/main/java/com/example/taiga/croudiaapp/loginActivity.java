package com.example.taiga.croudiaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //関連付け
        webView=(WebView)findViewById(R.id.login_web);
        //リンクをタップしたときに標準ブラウザを起動させない
        webView.setWebViewClient(new WebViewClient());
        //最初にログインページを表示する。
        webView.loadUrl("https://api.croudia.com/oauth/authorize?response_type=code&client_id=d643130e33cc5e1ac0692d40f97d3b0baac499cf8d549b66830bb54137888aad");
        //jacascriptを許可する
        webView.getSettings().setJavaScriptEnabled(true);
    }

    public void intent_main(View v){
        //webに表示されてるurlを取得
        String urlStr= webView.getUrl();
        String code=getCode(urlStr);
        if(!code.contains("=")) {
            //Mainへ遷移したい
            Intent intent = new Intent(this, MainActivity.class);
            //取得したコードを持たせる
            intent.putExtra("CODE", code);
            //intent!
            startActivity(intent);
            Toast.makeText(this,"ログイン成功！",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"まだです！",Toast.LENGTH_SHORT).show();
        }
    }

    //todo =の部分は改善すべき
    public String getCode(String origin){
        int index=origin.indexOf("=");
        if(index!=-1){
            String code=origin.substring(index+1,origin.length());
            return code;
        }
        return null;
    }

}
