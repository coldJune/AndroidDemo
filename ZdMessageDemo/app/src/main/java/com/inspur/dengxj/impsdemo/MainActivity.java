package com.inspur.dengxj.impsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    private MyReceiver myReceiver=new MyReceiver();

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.inspur.dengxj.impsdemo.MyService");
        MainActivity.this.registerReceiver(myReceiver,filter);
        webView =(WebView) findViewById(R.id.mainView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/main.html");



        Button button1 =(Button) findViewById(R.id.conn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), MyService.class);
                startService(startIntent);

            }
        });

        Button button2=(Button)findViewById(R.id.got);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent startIntent=new Intent(getApplicationContext(),secondActivity.class);
                startActivity(startIntent);
            }
        });
    }
}
