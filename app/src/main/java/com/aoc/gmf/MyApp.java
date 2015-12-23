package com.aoc.gmf;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by giser on 2015/7/3.
 */
public class MyApp extends Application {

    // LogUtil log = new LogUtil(getClass());

    private static MyApp sInstance;

    private String username;
    private String mainUrl = "http://www.aidongsports.com";
    private String fileUrl = "http://www.aidongsports.com/files/imgs/";

    private String nickName;
    private int screemWidth = -1;
    private String roleId;

    private String idCard;
    private String email;
    private String header;
    private String userid;

    private String tag = "007";

    private int pagesize = 10;

    private  int msgWaittime = 61;

    public static RequestQueue requestQueue;


    String cookieName = "myCookie";
    public static SharedPreferences sp;

    public static String tempImgView = "TestSyncListView";

    @Override
    public void onCreate() {
        super.onCreate();

        File f = new File(Environment.getExternalStorageDirectory()+"/"+tempImgView+"/");
        if(!f.exists()){
            f.mkdir();
        }

        sInstance = this;

        // 不必为每一次HTTP请求都创建一个RequestQueue对象，推荐在application中初始化
        requestQueue = Volley.newRequestQueue(this);

        //cookie
        sp = getSharedPreferences("cookie", Context.MODE_PRIVATE);

    }

    public void  setCookie(String _cookie)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(cookieName,_cookie);
        editor.commit();
    }

    public String getCookie()
    {
        return sp.getString(cookieName,null);
    }

    public static MyApp getInstance() {
        return sInstance;
    }

    public void  clearUserInfo()
    {
        this.nickName = null;
        this.idCard = null;
        this.header = null;
        this.email = null;
        this.username = null;
        this.roleId = null;
        this.userid = null;
    }

    public void setnickName(String _nickName) {
        nickName = _nickName;
    }
    public void setidCard(String _idCard) {
        idCard = _idCard;
    }
    public void setUserid(String _userid) {
        userid = _userid;
    }
    public void setemail(String _email) {
        email = _email;
    }
    public void setUsername(String _Username) {
        username = _Username;
    }

    public void setRoleId(String _RoleId) {
        roleId = _RoleId;
    }
    public String getRoleId() {
       return  this.roleId;
    }
    public void setheader(String _header) {
        header = _header;
    }

    public String getUsername() {
        return username;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public String getNeterrorTip() {
        return "未能连接服务器！";
    }

    public String getTag() {
        return this.tag;
    }
    public String getUserid() {
        return userid;
    }
    public String getnickName() {
        return nickName;
    }
    public String getidCard() {
        return idCard;
    }
    public String getemail() {
        return email;
    }
    public String getheader() {
        return header;
    }

    public int getPagesize()
    {
        return  this.pagesize;
    }

    public int getMsgWaittime()
    {
        return  this.msgWaittime;
    }

    public int getScreemWidth() {
        return screemWidth;
    }

    public void setScreemWidth(int _screemWidth) {
        screemWidth = _screemWidth;
    }
}