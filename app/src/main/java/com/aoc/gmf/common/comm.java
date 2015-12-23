package com.aoc.gmf.common;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by giser on 2015/5/29.
 */
public class comm {

    public static String getDateFormat(String sourDateFormat,String sourDate) {
        // 分析从 ParsePosition 给定的索引处开始的文本。如果分析成功，则将 ParsePosition 的索引更新为所用最后一个字符后面的索引
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sourDateFormat);
        Date dateValue = simpleDateFormat.parse(sourDate, position);
        String returnString = simpleDateFormat.format(dateValue);
        return returnString;
    }

    public static String readInstream(InputStream inputStream)
    {
        StringBuffer s = new StringBuffer();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String sResponse;


            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
        }
        catch (Exception exp)
        {

        }
        return  s.toString();
    }

    public  static  String getDateByLong(String longdate)
    {
        return  DateFormat.format("yyyy/MM/dd kk:mm", Long.parseLong(longdate)).toString();
    }

    public  static  String getDateByLong2(String longdate)
    {
        return  DateFormat.format("yyyy/MM/dd", Long.parseLong(longdate)).toString();
    }

    public  static String  getStringByJSONObject(JSONObject obj,String key)
    {
        String str = "";
        try {
            str=obj.getString(key);
        } catch (Exception exp) {
        }
        return str;
    }

    public static BigDecimal  getNumer(int num,double decemalNum)
    {
        BigDecimal bd = new BigDecimal(decemalNum);
        bd = bd.setScale(num,BigDecimal.ROUND_HALF_UP);
        return  bd;
    }

    public static Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    public static ArrayList<HashMap<String, String>> readAllSMS(Activity content) {
        Cursor cursor =content.managedQuery(SMS_INBOX, new String[]{"address", "person", "body", "date"},
                null, null, null);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>> ();
        if(cursor.moveToFirst()) {
            int addrIdx = cursor.getColumnIndex("address");
            int personIdx = cursor.getColumnIndex("person");
            int bodyIdx = cursor.getColumnIndex("body");
            int dateIdx = cursor.getColumnIndex("date");
            do {
                String addr = cursor.getString(addrIdx);
                String person = cursor.getString(personIdx);
                String body = cursor.getString(bodyIdx);
                String time = cursor.getString(dateIdx);

                HashMap<String, String> item = new HashMap<String, String>();
                item.put("addr", addr);
                item.put("person", person);
                item.put("body", body);
                item.put("date",time);

                list.add(item);
            } while(cursor.moveToNext());
        }
        return list;
    }

    public static boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    public static int[]  getViewSize(Activity act,View v)
    {
        final View view = v;
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height =view.getMeasuredHeight();
        int width =view.getMeasuredWidth();
        int[] res = new int[2];
        res[0] = width;
        res[1] = height;
        return  res;
    }

    /**
          * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
          */
public static  int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
        }

/**
          * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
          */
public static int pxTodip(Context context, float pxValue) {
     final float scale = context.getResources().getDisplayMetrics().density;
       return (int) (pxValue / scale + 0.5f);
   }

    //保留小数点位数
    public static double GetDecimal(double d,int n)
    {
        StringBuffer strb = new StringBuffer();
        strb.append("#.");
       for(int i = 0;i<n;i++)
       {
          strb.append("#");
       }
      DecimalFormat df=new DecimalFormat(strb.toString());
       return Double.parseDouble(df.format(d));
    }

    /// <summary>
    /// Url的参数格式化
    /// </summary>
    /// <param name="url"></param>
    /// <param name="dic"></param>
    /// <returns></returns>
    public static String UrlFormat(String url, HashMap<String, String> dic)
    {
        int len = dic.size();
        Iterator iter = dic.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object val = dic.get(key);

            if (url.contains("?"))
            {
                url = url + "&" + key + "=" + val.toString();
            }
            else
            {
                url = url + "?" + key + "=" + val.toString();
            }
        }
        return  url;
    }
}
