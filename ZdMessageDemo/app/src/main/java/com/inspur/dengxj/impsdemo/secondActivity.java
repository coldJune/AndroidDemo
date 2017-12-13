package com.inspur.dengxj.impsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.inspur.dengxj.impsdemo.R;

public class secondActivity extends AppCompatActivity {
    private WebView webView;
    private MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        webView=(WebView)findViewById(R.id.secondView);
        webView.getSettings().setJavaScriptEnabled(true);

        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.inspur.dengxj.impsdemo.MyService");
        secondActivity.this.registerReceiver(myReceiver,filter);
        webView.loadUrl("file:///android_asset/second.html");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            String chatRecordNum = intent.getStringExtra("chatRecordNum");
            webView.loadUrl("javascript:show('"+chatRecordNum+"')");

        }
    }

}
