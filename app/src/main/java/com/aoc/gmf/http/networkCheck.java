package com.aoc.gmf.http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by giser on 2015/5/28.
 */
public class networkCheck
{
    // 1 表示可以连接外网  2 表示wifi连接 但不能连接外网
    public static int isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return 0;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        // 判断wifi当前网络状态是否为连接状态
//                        if(networkInfo[i].getTypeName().equals("WIFI"))
//                        {
//                            boolean iscon = CheckNetwork(activity);
//                            if(iscon)
//                            return  1;
//                            else  return 2;
//                        }
//                        else   return 1;
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    //判断网络连接状态
    public static boolean CheckNetwork(Activity mContext) {
        boolean flag = false;
        String result = null;
        ConnectivityManager netManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =netManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            flag = netManager.getActiveNetworkInfo().isAvailable();
                        /*State wifi = netManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();*/ //判断wiff的连接状态
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            try {
                Process p = Runtime.getRuntime().exec("ping -c 1 -w 3 " + ip);// ping网址1次 超时为3秒
                // ping的状态
                int status = p.waitFor();
                if (status == 0) {
                    flag = true;
                } else {
                    flag = false;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return flag;
    }
}
