package com.inspur.dengxj.audiotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class AudioActivity extends AppCompatActivity {
    private WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        webView = findViewById(R.id.audioView);
        webView.loadUrl("file:///android_asset/audio.html");
    }
}
