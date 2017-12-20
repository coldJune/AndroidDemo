package com.inspur.dengxj.messagepushdemo;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.inspur.dengxj.util.IpObtainUtil;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private NetWorkStateReceiver netWorkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.login);
        netWorkStateReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver,filter);
        IpObtainUtil.getNetIp(getApplicationContext());
       Intent intent = new Intent(getApplicationContext(), MessagePushService.class);
        startService(intent);
        webView.loadUrl("http://10.0.2.2:8080/MessagePushDemo/message?ip="+IpObtainUtil.getLocalIPAdress(getApplicationContext()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkStateReceiver);
    }
}
