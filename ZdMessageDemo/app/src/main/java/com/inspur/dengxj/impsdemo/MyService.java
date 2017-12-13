package com.inspur.dengxj.impsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MyService extends Service {
    private HttpURLConnection connection = null;
    private Intent inten;
    private final  String ZDURL= "http://10.0.2.2:8080/zddemo/demo";
    private int preChatRecordNum;
    private int currentChatRecordNum;
    public MyService() {
    }

    public void onCreate(){
        inten = new Intent();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {


        (new Thread(){
            public void run() {
                try{
                    inten.setAction("com.inspur.dengxj.impsdemo.MyService");
                    while(true){

                        String jsonStr = readParse(ZDURL);
                        if(jsonStr != null && !"".equalsIgnoreCase(jsonStr)){
                            JSONObject jsonObj = new JSONObject(jsonStr);
                            int status = jsonObj.getInt("status");
                            String message = jsonObj.getString("message");
                            currentChatRecordNum = jsonObj.getInt("chatRecordNum");
                            if(status==1){
                                inten.putExtra("status",1);
                                if(currentChatRecordNum==0){
                                    inten.putExtra("chatRecordNum","");
                                    sendBroadcast(inten);
                                }else if(preChatRecordNum!=currentChatRecordNum){
                                    inten.putExtra("chatRecordNum",currentChatRecordNum+"");
                                    sendBroadcast(inten);
                                }
                            }else {
                                inten.putExtra("status",0);
                                sendBroadcast(inten);
                            }
                        }else{
                            inten.putExtra("status",0);
                            sendBroadcast(inten);
                        }
                        preChatRecordNum = currentChatRecordNum;
                        Thread.sleep(3000);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        return super.onStartCommand(intent,flags,startId);

    }

    private String readParse(String url) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        connection = (HttpURLConnection)new URL(ZDURL).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000);
        int code= connection.getResponseCode();
        if(code==200){
            InputStream inputStream = connection.getInputStream();
            while((len=inputStream.read(data))!=-1){
                outStream.write(data,0,len);
            }
            inputStream.close();
            return new String(outStream.toByteArray());
        }
        return null;

    }
}
