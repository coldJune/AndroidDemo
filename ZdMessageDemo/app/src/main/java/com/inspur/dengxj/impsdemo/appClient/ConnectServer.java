package com.inspur.dengxj.impsdemo.appClient;

import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by deng.xj on 2017/9/15.
 */

public class ConnectServer {
    private final String IP="172.1.1.135";
    private final int PORT=9999;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket client=null;
    Handler handler;
    public void init(){

//        (new Thread(){
//            public void run(){
                try{
                    while(true){
                        client = new Socket(IP,PORT);
                        //读取服务器数据
                        in = new DataInputStream(client.getInputStream());
                        //向服务器发送数据
                        System.out.println(in.readUTF());
                        out = new DataOutputStream(client.getOutputStream());
                        out.writeUTF("建立连接");
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
//        }).start();
//
//    }

    public String  getMsg(){

        try{

            return in.readUTF();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void close(){
        try {
            if(client!=null){
                out.close();
                in.close();
                client.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
