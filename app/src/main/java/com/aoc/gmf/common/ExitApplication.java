package com.aoc.gmf.common;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

public class ExitApplication extends Application {
    private ArrayList<Activity> activityList = new ArrayList<Activity>();
    private static ExitApplication instance;

    private ExitApplication() {

    }
    //单例模式中获取唯一的ExitApplication实例
    //
    //

    public static ExitApplication getInstance() {
        if (null == instance) {
            instance = new ExitApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        int len = activityList.size();
        for (int i = 0; i < len; i++) {
            activityList.get(i).finish();
        }
        System.exit(0);
    }
}