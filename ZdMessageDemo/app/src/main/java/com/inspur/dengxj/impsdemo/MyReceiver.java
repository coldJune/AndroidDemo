package com.inspur.dengxj.impsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private int Notification_ID_BASE;
    NotificationManager mNotificationManager;
    private static int numbers=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification_ID_BASE=110;
        String result=intent.getStringExtra("result");
        Log.v("!","收到的数据："+result);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context);
        Intent intt=new Intent(context,secondActivity.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context,MainActivity.class));
        stackBuilder.addNextIntent(intt);
        PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(Notification_ID_BASE,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle("测试标题")
                .setContentText(result)
                .setSmallIcon(R.drawable.fb)
                .setNumber(numbers++)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);
//           try{
//               Thread.sleep(5000);
//           }catch (InterruptedException e){
//               e.printStackTrace();
//           }

        Notification notification=mBuilder.build();
        mNotificationManager.notify(Notification_ID_BASE,notification);
    }
}
