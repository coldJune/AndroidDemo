package com.inspur.dengxj.savepic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements CustomPopupWindow.OnItemClickListener {
    private WebView webView=null;
    private CustomPopupWindow mPop;
    private String imageUrl;
    private Boolean isAllGranted=false;
    private MainActivity currentActivity;
    private final int MY_PERMISSION_REQUEST_CODE = 100;
    DynamicPermission dp;
    private final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String operID = "sgddxt";
        String operSrc = "sgddxt";
        Long argutsname = System.currentTimeMillis();
        String key = "sc028abcdefghijklm";
        String hashCode = "";
        hashCode = (new MD5()).getMD5ofStr(operID+argutsname+key); //encoderByMd5(operID+argutsname+key);
        String url = "https://www.baidu.com";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        mPop=new CustomPopupWindow(this);
        mPop.setOnItemClickListener(this);
        dp = new DynamicPermission(MainActivity.this);
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl(url);
        webView.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                WebView.HitTestResult result = ((WebView)v).getHitTestResult();
                if(null == result){
                    return false;
                }
                int type = result.getType();
                if(type == WebView.HitTestResult.UNKNOWN_TYPE){
                    return false;
                }
                if(type==WebView.HitTestResult.EDIT_TEXT_TYPE){
                }
                switch (type){
                    case WebView.HitTestResult.IMAGE_TYPE:
                        imageUrl= result.getExtra();
                        mPop.showAtLocation(MainActivity.this.findViewById(R.id.webView), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                        break;
                }
                return true;
            }
        });
    }

    public void setOnItemClick(View v){
        switch (v.getId()){
            case R.id.savePic:

                isAllGranted=dp.checkPermissionAllGranted(permissions);
                if(!isAllGranted){
                    dp.requestPermission(permissions,MY_PERMISSION_REQUEST_CODE);
                }else{
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhuangwei";
                    new ImageUtil(this,path).execute(imageUrl);
                    mPop.dismiss();
                    Toast.makeText(this,"保存图片成功",Toast.LENGTH_LONG).show();
                }
        }
    }


    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == MY_PERMISSION_REQUEST_CODE){
            boolean isAllGranted =true;
            for (int grant: grantResults){
                if(grant != PackageManager.PERMISSION_GRANTED){
                    isAllGranted=false;
                    break;
                }
            }
            if(isAllGranted){
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhuangwei";
                new ImageUtil(this,path).execute(imageUrl);
                mPop.dismiss();
                Toast.makeText(this,"保存图片成功",Toast.LENGTH_LONG).show();
            }else{
                String message="保存图片需要访问存储权限，请到权限管理中进行授权";
                dp.openAppDetails(message);
            }
        }
    }

}

