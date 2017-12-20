package com.inspur.dengxj.messagepushdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created by deng.xj on 2017/12/11.
 */

public class MessagePushService extends Service{
    private Intent intent;
    private DatagramSocket mSocket;
    private String message ="app消息";
    private ConnectThread connect;
    private ReceiveThread mReceive;
    private SendThread mSend;
    private InetAddress  address ;
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        connect = new ConnectThread();
//        connect.start();
        mReceive = new ReceiveThread();
        mReceive.start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 创建UDP连接
     */
    private class ConnectThread extends Thread {
        @Override
        public void run() {
            super.run();
            if(mSocket==null||mSocket.isClosed()){
                try{
                    mSocket=new DatagramSocket(19991);
                    address = InetAddress.getByName(URL_CONFIG.MESSAGEPUSH_URL);
                    mSocket.connect(address,URL_CONFIG.MESSAGEPUSH_PORT);
                    message = "确认在线";
                    mHandler.sendEmptyMessage(3);
                }catch (SocketException e){
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 控制接收消息和发送消息的线程自启动
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2) {
                mReceive.interrupt();
                mReceive=null;
                mSend=new SendThread();
                mSend.start();
            }
            if(msg.what==1){
                mSend.interrupt();
                mSend=null;
                mReceive = new ReceiveThread();
                mReceive.start();
            }
            if(msg.what==3){
                mSend = new SendThread();
                mSend.start();
            }
        }
    };

    /**
     * 发送消息的线程
     */
    private class SendThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                if(mSocket ==null ||mSocket.isClosed())
                    return;
                Log.v("信息:","进入发送方法");
                byte[] datas = message.getBytes("UTF8");
                DatagramPacket packet = new DatagramPacket(datas,datas.length,address,URL_CONFIG.MESSAGEPUSH_PORT);
                mSocket.send(packet);
                Log.v("信息:","发送完成");
                mHandler.sendEmptyMessage(1);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收消息的线程
     */
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            if(mSocket == null||mSocket.isClosed()) {
                try {
                    Log.v("信息:","初始化连接");
                    mSocket=new DatagramSocket(URL_CONFIG.MESSAGEPUSH_PORT);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
            try {
                byte[] datas = new byte[512];
                DatagramPacket packet = new DatagramPacket(datas,datas.length);
                Log.v("信息:","准备接受信息;address:"+address);
                mSocket.receive(packet);
                address = packet.getAddress();
                String receiveMsg = new String(packet.getData()).trim();
                Log.v("信息:","接受信息为:"+receiveMsg);
                mHandler.sendEmptyMessage(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}