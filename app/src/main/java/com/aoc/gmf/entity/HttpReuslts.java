package com.aoc.gmf.entity;

import java.util.ArrayList;

/**
 * Created by giser on 2015/7/2.
 */
public class HttpReuslts<T> {

    private int code=1;
    private String msg="OK";
    private ArrayList<T> data;

    public int getcode() {
        return code;
    }
    public void setcode(int code) {
        code = code;
    }
    public String getmsg() {
        return msg;
    }
    public void setmsg(String msg) {
        msg = msg;
    }
    public void setdata(ArrayList<T> data) {
        data=data;

    }
    public ArrayList<T> getdata() {
        return data;
    }
    public void adddata(T t) {

        if(data==null)
        {
            data=new ArrayList<T>();
        }
        data.add(t);
    }
    public void removedata(T t)
    {
        data.remove(t);
    }
}
