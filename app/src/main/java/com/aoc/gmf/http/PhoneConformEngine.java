package com.aoc.gmf.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.aoc.gmf.MyApp;

/**
 * Created by giser on 2015/9/6.
 */
public class PhoneConformEngine {

    int TIME = 1000;
    int haveTime = 0;
    int waitTime = MyApp.getInstance().getMsgWaittime();//重新验证的秒数
    String tip = "重新验证";
    String tip2 = "验证";

    TextView tvTip;
    public PhoneConformEngine(TextView tvTip)
    {
        this.tvTip = tvTip;
    }

    public void start()
    {
        handler.postDelayed(runnable, TIME); //每隔1s执行
    }

    public void  stop()
    {
        handler.removeCallbacks(runnable);
        tvTip.setText(tip2);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            haveTime ++;
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("value", haveTime);
            msg.setData(data);
            handler.sendMessage(msg);

            if(haveTime<waitTime)
                handler.postDelayed(runnable, TIME); //每隔1s执行
            else  haveTime = 0;
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int intTime = waitTime - data.getInt("value");//剩下的时间
            if(intTime>0)
                tvTip.setText(String.valueOf(intTime));
            else
            {tvTip.setText(tip);}
        }
    };

}
