package com.example.dengxj.wifiandsingal.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

public class WifiUtil {


    public static List<ScanResult> getWifiList(Context context){
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wm.startScan();
        List<ScanResult> wifiList  = wm.getScanResults();
       List<WifiConfiguration> networks =  wm.getConfiguredNetworks();
        WifiInfo wi = wm.getConnectionInfo();
        return wifiList;
    }
}
