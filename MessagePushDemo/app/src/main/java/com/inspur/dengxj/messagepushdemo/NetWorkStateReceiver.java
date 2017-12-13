package com.inspur.dengxj.messagepushdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.inspur.dengxj.util.IpObtainUtil;

/**
 * 检测网络变化
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);//获取ConnectivityManager对象

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//获取wifi连接信息
            NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//获取移动数据连接信息
            if(wifiNetworkInfo.isConnected()&&dataNetworkInfo.isConnected()){
                Toast.makeText(context,"wifi已连接，移动数据已连接;Ip:",Toast.LENGTH_LONG).show();
                IpObtainUtil.getNetIp(context);
            }else if(wifiNetworkInfo.isConnected()&&!dataNetworkInfo.isConnected()){
                Toast.makeText(context,"wifi已连接，移动数据已断开;Ip:",Toast.LENGTH_LONG).show();
                IpObtainUtil.getNetIp(context);

            }else if(!wifiNetworkInfo.isConnected()&&dataNetworkInfo.isConnected()){
                Toast.makeText(context,"wifi已断开，移动数据已连接;IP:",Toast.LENGTH_LONG).show();
                IpObtainUtil.getNetIp(context);

            }else{
                Toast.makeText(context,"未连接至网络",Toast.LENGTH_LONG).show();
                IpObtainUtil.getNetIp(context);
            }
        }else{
            Network[] networks = connectivityManager.getAllNetworks();
            StringBuilder sb = new StringBuilder();
            for (Network network:
                 networks) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                sb.append(networkInfo.getTypeName()+"connect is" +networkInfo.isConnected());
            }
            Toast.makeText(context,sb.toString(),Toast.LENGTH_LONG).show();
            IpObtainUtil.getNetIp(context);
        }
    }
}
