package com.inspur.dengxj.savepic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * 该类用于请求动态权限
 * Created by deng.xj on 2017/11/1.
 */

public class DynamicPermission {
    private  Activity activity;

    /**
     *
     * @param activity 传入需要申请权限的activity
     */
    public DynamicPermission(Activity activity){
        this.activity = activity;

    }

    /**
     * 检查是否具有相应权限，一旦有一个没有，则返回false，全部都有则返回true
     * @param permissions 权限数组
     * @return
     */
    public  boolean checkPermissionAllGranted(String[] permissions){
        for (String permission : permissions){
            if(ActivityCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 向系统申请权限
     * @param permissions 权限数组
     * @param requestCode 请求码，用于比对请求回调函数
     */
    public void requestPermission(String[] permissions ,int requestCode){
        ActivityCompat.requestPermissions( activity,permissions,requestCode);
    }

    /**
     * 如果权限请求失败，则引导用户手动获取权限
     * @param message 提示内容
     */
    public  void openAppDetails(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
