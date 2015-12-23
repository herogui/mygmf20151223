package com.aoc.gmf.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by giser on 2015/7/15.
 */
public class ImageHelper {
 public static String  tag = "ImageHelper";

 public static byte[] getImageViewInputStream(String URL_PATH) throws IOException {
  InputStream inputStream = null;
  URL url = new URL(URL_PATH);                    //服务器地址
  byte[] bytes = null;
  if (url != null) {
//打开连接
   HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
   try {
    httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
    httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
    httpURLConnection.setDoInput(true);                //打开输入流
    int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
    if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
     inputStream = httpURLConnection.getInputStream();        //获取输入流
     bytes = InputStreamToByte(inputStream);
    }

   } finally {
    httpURLConnection.disconnect();
   }
  }
  return bytes;
 }

 public static byte[] InputStreamToByte(InputStream is) throws IOException {
  ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
  int ch;
  while ((ch = is.read()) != -1) {
   bytestream.write(ch);
  }
  byte imgdata[] = bytestream.toByteArray();
  bytestream.close();
  return imgdata;
 }

 public static Bitmap Bytes2Bimap(byte[] b) {
  if (b.length != 0) {
   return BitmapFactory.decodeByteArray(b, 0, b.length);
  } else {
   return null;
  }
 }
 public  static  Bitmap drawable2Bitmap(Drawable drawable) {
  if (drawable instanceof BitmapDrawable) {
   return ((BitmapDrawable) drawable).getBitmap();
  } else if (drawable instanceof NinePatchDrawable) {
   Bitmap bitmap = Bitmap
           .createBitmap(drawable.getIntrinsicWidth(),
                   drawable.getIntrinsicHeight(),drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                           : Bitmap.Config.RGB_565);
   Canvas canvas = new Canvas(bitmap);
   drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
           drawable.getIntrinsicHeight());
   drawable.draw(canvas);
   return bitmap;
  } else {
   return null;
  }
 }


}
