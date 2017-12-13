package com.inspur.dengxj.savepic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 用于异步下载图片并且将图片保存在指定文件夹下
 * Created by deng.xj on 2017/10/31.
 */

public class ImageUtil extends AsyncTask<String,Void,Bitmap>{
    private Context context;
    private String path;

    /**
     * 初始化类
     * @param context 当前上下文
     * @param path 保存的路径
     */
    public ImageUtil(Context context,String path){
        this.context=context;
        this.path = path;
    }

    /**
     * 获取网络图片
     * @param imageUrl 图片的连接
     * @param context 当前上下文
     * @return bitmap
     */
    private Bitmap getImage(String imageUrl,Context context){
        URL url;
        HttpURLConnection connection;
        Bitmap bitmap =null;
        try {
            url=new URL(imageUrl);
            connection =(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setUseCaches(false);
            InputStream is = connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context,"网络异常",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存图片
     * @param bitmap 图片数据
     * @param path 保存的路径
     * @return file 文件
     */
    private  File saveImage(Bitmap bitmap,String path){

        File appdir = new File(path);
        FileOutputStream os;
        if(!appdir.exists()){
            appdir.mkdir();
        }
        File file = new File(path,System.currentTimeMillis()+".png");
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = getImage(url,context);

        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap){
        super.onPostExecute(bitmap);
        File file =saveImage(bitmap,path);

            //MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),null);
            Uri uri=Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));


    }
}
