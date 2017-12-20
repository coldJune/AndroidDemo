package com.inspur.dengxj.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Created by deng.xj on 2017/12/13.
 */

public class IpObtainUtil {

    /**
     * 获取内网IP地址
     * @param context
     * @return
     */
    public static String getLocalIPAdress(Context context){
        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                try{
                    for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                        NetworkInterface networkInterface = en.nextElement();
                        for(Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses();enumIpAddr.hasMoreElements();) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo= wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            }
        }
        return null;
    }
    public static void getNetIp(final Context context){
        (new Thread(){
            @Override
            public void run() {
                super.run();
                HttpURLConnection urlConnection = null;
                String result;
                JSONObject jsonObject;
                try{
                    URL url = new URL("http://ip.chinaz.com/getip.aspx");
                    urlConnection = (HttpURLConnection)url.openConnection();
                    InputStream in  = new BufferedInputStream(urlConnection.getInputStream());
                    result = readInStream(in);
                    if(!result.isEmpty()){
                        jsonObject = new JSONObject(result);
                        String ip = jsonObject.getString("ip");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
    private static String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
