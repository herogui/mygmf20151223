package com.aoc.gmf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aoc.gmf.MyDlg.MyDlg;
import com.aoc.gmf.MyDlg.MyDlgIncome;
import com.aoc.gmf.MyDlg.MyDlgLIST;
import com.aoc.gmf.MyEvent.CusEvent;
import com.aoc.gmf.MyEvent.CusEventListener;
import com.aoc.gmf.MyEvent.EventSourceObject;
import com.aoc.gmf.MyUI.MyDialog;
import com.aoc.gmf.MyUI.MySlideAdapter;
import com.aoc.gmf.common.SysApplication;
import com.aoc.gmf.common.comm;
import com.aoc.gmf.http.httpHelper;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import com.roamer.slidelistview.SlideListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {
    private LocationClient mLocationClient;

    private BaiduMap mBaiduMap = null;
    MapView mMapView = null;
    boolean isPause = false;
    boolean isEdit = false;
    LatLng oLocation;
    Button saveitem;
    RequestQueue mRequestQueue;
    public  MySlideAdapter  mAdapter;
    double lon0 = 117.00;
    double lat0 = 36.7;

    public List<Map<String, String>> data;

    public ArrayList<CObj>   list;
    public  String  user;
    Button btnGyqy;
    Boolean isLBS = false;//是否手动启动定位
    public   SlideListView mSlideListView;
    Spinner  mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        list = new ArrayList<CObj>();
       user = getIntent().getStringExtra("user").replace("用户","");

        // Toast.makeText(MainActivity.this,user, Toast.LENGTH_SHORT).show();

        mSlideListView = ((SlideListView) findViewById(R.id.listview_item));

        mSlideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View[] views = (View[]) view.getTag();
                TextView tv = ((TextView) views[0]);
                String myid =tv.getTag().toString();
                LatLng pnt = getLonlat(myid);
                mapTo(pnt);
                TextView tv1 = ((TextView) views[1]);

                OverlayOptions textOption = new TextOptions()
                        .bgColor(0xAAFFFF00)
                        .fontSize(24)
                        .fontColor(0xFFFF00FF)
                        .text(tv1.getText().toString())
                        .rotate(-30)
                        .position(pnt);
//在地图上添加该文字对象并显示
                mBaiduMap.addOverlay(textOption);
            }
        });
        mRequestQueue =  Volley.newRequestQueue(MainActivity.this);

        data = new ArrayList<Map<String, String>>();
        oLocation = new LatLng(lat0,lon0);
        initMap();


        btnGyqy = (Button) findViewById(R.id.btnGyqy);

        ;
        btnGyqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "http://115.29.136.148:3333/HandlerRandom.ashx";

                // Log.d("res", netUrl);
                httpHelper http = new httpHelper();
                http.StringHttp(mRequestQueue, url);
                http.GetResEvent.addCusListener(new CusEventListener() {
                    @Override
                    public void fireCusEvent(CusEvent e) {
                        EventSourceObject eObject = (EventSourceObject) e.getSource();
                        String res = eObject.getString();
                        String[]  strs =  res.split(",");
                        final String idRandom = strs[0];
                        final String nameRandom = strs[1];

                        MyDialog dlg = new MyDialog(MainActivity.this, nameRandom + ", 是否添加", "添加", "取消");
                        dlg.setOnClickBtn(new MyDialog.IClick() {
                            @Override
                            public void OnClickBtn(Boolean isOK) {

                                //导入数据库
                                String url = "http://115.29.136.148:3333/Handler1.ashx?ischeck=0&userid="+user+"&factoryId="+idRandom;

                                // Log.d("res", netUrl);
                                httpHelper http = new httpHelper();
                                http.StringHttp(mRequestQueue, url);
                                http.GetResEvent.addCusListener(new CusEventListener() {
                                    @Override
                                    public void fireCusEvent(CusEvent e) {
                                        EventSourceObject eObject = (EventSourceObject) e.getSource();
                                        String res = eObject.getString();

                                        if (res.equals(httpHelper.errorTip)) {
                                            Toast.makeText(MainActivity.this, "未能连接网络", Toast.LENGTH_SHORT).show();
                                            return;//无结果的
                                        }
                                        else {
                                            Map<String, String> dataMap3 = new HashMap<String, String>();
                                            dataMap3.put("id",idRandom);
                                            dataMap3.put("num", String.valueOf(data.size() + 1));
                                            dataMap3.put("adr", nameRandom);
                                            data.add(dataMap3);
                                            setSoure(data);
                                            Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                // Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        ImageView imglbs = (ImageView) findViewById(R.id.imglbs);
        imglbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationClient == null) {
                    initLbs();
                } else {
                    if (!mLocationClient.isStarted()) mLocationClient.start();
                }
                isLBS = true;
            }
        });


        initTopbar();

        initMarckClk();
        initBtns();
    }
   //////////////////////////////////数据初始化////////////////////////////
    void  initBtns()
    {
        Button btnZMN = (Button) findViewById(R.id.btnZMN);
        btnZMN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDlgLIST myDlgIncome = new MyDlgLIST(MainActivity.this, "污染源列表");
                myDlgIncome.init(R.layout.list);
                myDlgIncome.initData(null, "");
                myDlgIncome.setOkBtnClick(new MyDlg.IDlgClick() {
                    @Override
                    public void OnClickBtn(Boolean isOK) {

                    }
                });
            }
        });
    }

    //企业工程
    void initFactory()
    {
        String url = "http://115.29.136.148:3333/GetGyqy.ashx?userid="+user;

        // Log.d("res", netUrl);
        httpHelper http = new httpHelper();
        http.StringHttp(mRequestQueue, url);
        http.GetResEvent.addCusListener(new CusEventListener() {
            @Override
            public void fireCusEvent(CusEvent e) {
                EventSourceObject eObject = (EventSourceObject) e.getSource();
                String res = eObject.getString();
                String[] str1 = res.split(";");
                for(int i=0;i<str1.length;i++) {
                    String[] str2s = str1[i].split(",");
                    Map<String, String> dataMap3 = new HashMap<String, String>();
                    dataMap3.put("id", str2s[1]);
                    dataMap3.put("num", String.valueOf(data.size() + 1));
                    dataMap3.put("adr",  getName(str2s[1]));
                    data.add(dataMap3);

                }
                setSoure(data);

                if (res.equals(httpHelper.errorTip)) {
                    Toast.makeText(MainActivity.this, "未能连接网络", Toast.LENGTH_SHORT).show();
                    return;//无结果的
                }
                //   else   Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public   String getName(String fid)
    {
        String name = "";
        for(int i = 0;i<list.size();i++)
        {
            if(list.get(i).Getid().equals(fid))
            {
                name = list.get(i).Getname();
                break;
            }
        }
        return  name;
    }

    LatLng getLonlat(String fid)
    {
        LatLng pnt = null;
        for(int i = 0;i<list.size();i++)
        {
            if(list.get(i).Getid().equals(fid))
            {
                String lonStr = list.get(i).Getlon();
                String latStr = list.get(i).Getlat();
                double lon = Double.parseDouble(lonStr);
                double lat = Double.parseDouble(latStr);
                pnt = new LatLng(lat,lon);
                break;
            }
        }
        return  pnt;
    }


    void setSoure(final List<Map<String, String>> datas) {
        final String[] menunames = new String[]{"详细","检查","删除"};
        mAdapter = new MySlideAdapter(MainActivity.this, datas, R.layout.activity_incomeitem,
                new String[]{"num", "adr"},
                new int[]{R.id.txtdata1_income,
                        R.id.txtdata2_income,
                },
                menunames, "id");
        mAdapter.setOnBtnClick(new MySlideAdapter.IBtnClick() {
            @Override
            public void SetOnBtnClick(int ordernum, View v) {

                View[] views = (View[]) v.getTag();
                String id = "";
                int txtNum = views.length - menunames.length;
                String[] txts = new String[txtNum];
                for (int i = 0; i < txtNum; i++) {
                    TextView tv = ((TextView) views[i]);
                    txts[i] = tv.getText().toString();

                    if (id.length() < 1)
                        id = tv.getTag().toString();
                }
                CObj obj = null;
                for(int j = 0;j<list.size();j++)
                {
                    if(list.get(j).Getid().equals(id))
                    {
                        obj = list.get(j);
                        break;
                    }
                }

                String[] str = new String[4];
                str[0] = obj.Getname();
                str[1] = obj.Getfaren();
                str[2] = obj.GetAdr();
                str[3] = obj.Getval();
                if(ordernum == 1)
                {


                    MyDlgIncome  myDlgIncome = new MyDlgIncome(MainActivity.this, "详细信息");
                    myDlgIncome.init(R.layout.activity_incomeadd);
                    myDlgIncome.initData(str, id);
                    myDlgIncome.setOkBtnClick(new MyDlg.IDlgClick() {
                        @Override
                        public void OnClickBtn(Boolean isOK) {
                            //  initInfo(pagesize,0);
                        }
                    });
                    //  Toast.makeText(MainActivity.this, obj.GetAdr()+obj.Getval()+obj.Getfaren(), Toast.LENGTH_SHORT).show();
                }

                if (ordernum == 2) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, CheckActivity.class);
                    String name = getName(id);
                    intent.putExtra("info",name+","+ id);
                    startActivity(intent);
                }

                if (ordernum == 3) {
                    final String delId = id;
////
                    MyDialog dlg = new MyDialog(MainActivity.this);
                    dlg.setOnClickBtn(new MyDialog.IClick() {
                        @Override
                        public void OnClickBtn(Boolean isOK) {

                            //数据库删除
                            String url = "http://115.29.136.148:3333/HandlerDelByID.ashx?id="+delId;
                            Log.d("res",url);
                            // Log.d("res", netUrl);
                            httpHelper http = new httpHelper();
                            http.StringHttp(mRequestQueue, url);
                            http.GetResEvent.addCusListener(new CusEventListener() {
                                @Override
                                public void fireCusEvent(CusEvent e) {
                                    EventSourceObject eObject = (EventSourceObject) e.getSource();
                                    String res = eObject.getString();
                                    if(res.equals("1"))
                                    {
                                        //刷新
                                        for(int i = 0;i<data.size();i++)
                                        {
                                            if(data.get(i).get("id") == delId)
                                            {
                                                data.remove(i);
                                                setSoure(data);

                                                for(int j = 0;j<data.size();j++)
                                                {
                                                    data.get(j).put("num",String.valueOf(j+1));
                                                }

                                                break;
                                            }
                                        }
                                        Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                    }else    Toast.makeText(MainActivity.this, "删除不成功，请联系系统管理员！", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                }
            }
        });

        mSlideListView.setAdapter(mAdapter);

    }

    void initMarckClk()
    {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                try {
                    String info = marker.getExtraInfo().get("info").toString();
                    String[] strs = info.split(",");
                    MyDlgIncome myDlgIncome = new MyDlgIncome(MainActivity.this, "详细信息");
                    myDlgIncome.init(R.layout.activity_incomeadd);
                    myDlgIncome.initData(strs, strs[4]);
                    final  String myid = strs[4];
                    final  String name = strs[1];
                    myDlgIncome.setOkBtnClick(new MyDlg.IDlgClick() {
                        @Override
                        public void OnClickBtn(Boolean isOK) {
//                            Intent intent = new Intent();
//                            intent.setClass(MainActivity.this, CheckActivity.class);
//                            String name = getName(myid);
//                            intent.putExtra("info",name+","+ myid);
//                            startActivity(intent);
                        }
                    });
                }
                catch (Exception e)
                {}
                return true;
            }
        });
    }

    CObj getRandomFactory()
    {
        CObj  o;

        Random r = new Random();

        int n = r.nextInt(list.size());

        o = list.get(n);

        return  o;
    }

    void initHttp()
    {
        HashMap<String,String> datamap = new HashMap<String, String>();
        datamap.put("sdeIP","115.29.136.148");
        datamap.put("sdeUser","sde");
        datamap.put("sdeName", "sde");
        datamap.put("sdePwd","sde");
        datamap.put("tbName", "SDE.sdfactory");
        datamap.put("fieldNameList","a3,a4,lon,lat,yan,a1,a6");
        datamap.put("f", "pjson");

        String url = "http://115.29.136.148:6080/arcgis/rest/services/smallregion/MapServer/exts/QueryData/sampleOperation";

        // String url = "http://192.168.1.101:6080/arcgis/rest/services/smallRegion/MapServer/exts/QueryData/sampleOperation";
        String netUrl = comm.UrlFormat(url,datamap);
        // Log.d("res",netUrl);
        httpHelper http = new httpHelper();
        http.StringHttp(mRequestQueue,netUrl);
        http.GetResEvent.addCusListener(new CusEventListener() {
            @Override
            public void fireCusEvent(CusEvent e) {
                EventSourceObject eObject = (EventSourceObject) e.getSource();
                String res = eObject.getString();
                String[] strs = res.split(";");

                //Log.d("res",res);
                double dis1dGREE = 89252;
                for(int i = 0;i<strs.length;i++)
                {
                    try {


                        String[] str2 = strs[i].split(",");
                        CObj obj = new CObj(str2[3],str2[2],str2[4],str2[5],str2[6],str2[0],str2[1]);
                        list.add(obj);
                        double lon = Double.valueOf(obj.lon);
                        double lat = Double.valueOf(obj.lat);
                        LatLng   p = new LatLng(lat,lon);
                        double dis = dis1dGREE*Math.sqrt((lon - lon0) * (lon - lon0) + (lat - lat0) * (lat - lat0));
                        // Log.d("res",String.valueOf(dis));
                        AddMarker2(p, dis,obj);

//                    OverlayOptions textOption = new TextOptions()
//                            .bgColor(0xAAFFFF00)
//                            .fontSize(24)
//                            .fontColor(0xFFFF00FF)
//                            .text(obj.Adr)
//                            .position(p);
////在地图上添加该文字对象并显示
//                    mBaiduMap.addOverlay(textOption);
                    }
                    catch (Exception exp)
                    {}
                }

                if(res.equals(httpHelper.errorTip)) {
                    Toast.makeText(MainActivity.this,"未能连接网络", Toast.LENGTH_SHORT).show();
                    return;//无结果的
                }

                //???????????????????????
                initFactory();
            }
        });
    }

    void initHttpZuomuniao()
    {
//        HashMap<String,String> datamap = new HashMap<String, String>();
//        datamap.put("sdeIP","115.29.136.148");
//        datamap.put("sdeUser","sde");
//        datamap.put("sdeName", "sde");
//        datamap.put("sdePwd","sde");
//        datamap.put("tbName", "SDE.sdfactory");
//        datamap.put("fieldNameList","a3,a4,lon,lat,yan,a1,a6");
//        datamap.put("f", "pjson");

        String url = "http://115.29.136.148:5555/handle/DBHandler.ashx?action=GetRows&tableName=SD_ZhuoMuNiao&mode=detailed";

        // String url = "http://192.168.1.101:6080/arcgis/rest/services/smallRegion/MapServer/exts/QueryData/sampleOperation";
//        String netUrl = comm.UrlFormat(url,datamap);
        // Log.d("res",netUrl);
        httpHelper http = new httpHelper();
        http.StringHttp(mRequestQueue,url);
        http.GetResEvent.addCusListener(new CusEventListener() {
            @Override
            public void fireCusEvent(CusEvent e) {
                EventSourceObject eObject = (EventSourceObject) e.getSource();
                String res = eObject.getString();
                String[] strs = res.split(";");

                String[] str1 = res.split(";");
                for(int i = 0;i<str1.length;i++) {
                    String[] str2 = str1[i].split(",");
                        CObj obj = new CObj(str2[3],"","","","",str2[12],str2[2].replace(".000000",""));
                        list.add(obj);
                }

                //???????????????????????
                initFactory();
            }
        });
    }

   public class CObj {
        private String Adr= "";;
        private String faren = "";
        private String lon = "";
        private String lat = "";
        private String val = "";
        private String id = "";
        private String name = "";

        public CObj() {
            Adr = "";
            faren = "";
            lon = "";
            lat = "";
            val = "";
        }

        public CObj(String _Adr,String _faren,String _lon,String _lat,String _val,String _id,String _name) {
            Adr = _Adr.trim();
            faren = _faren.trim();
            lon = _lon.trim();
            lat = _lat.trim();
            val = _val.trim();
            id = _id.trim();
            name = _name.trim();
        }
        public String Getname() {
            return name;
        }
        public String Getid() {
            return id;
        }
        public String GetAdr() {
            return Adr;
        }

        public String Getfaren() {
            return faren;
        }

        public String Getlat() {
            return lat;
        }

        public String Getlon() {
            return lon;
        }

        public String Getval() {
            return val;
        }
    }


    void  initTopbar()
    {


    }



    void  initLbs()
    {
        mLocationClient = new LocationClient(getApplicationContext());
        MyLocationListener mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        InitLocation();
        mLocationClient.start();
    }

    void  initMap() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap(); //获取地图控制??
        mBaiduMap.setMyLocationEnabled(true);

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            public void onMapLoaded() {

                //初始化要放开
                initLbs();

                //ceshi
                initHttp();
                //initHttpZuomuniao();

                isLBS = false;
                if(oLocation.latitude>0) {
                    mapTo(oLocation);
                    AddMarker(oLocation);
                }
                else {
                    mapTo(cenpt);
                    AddMarker(cenpt);
                }
            }
        });
//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                    mBaiduMap.clear();
//                    AddMarker(latLng);
//                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
//            }
//            @Override
//            public boolean onMapPoiClick(MapPoi mapPoi) {
//                return false;
//            }
//        });
        mMapView.setVisibility(View.INVISIBLE);
        InVisibleZoomControlss();

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams _layoutParamImageWidth = new LinearLayout.LayoutParams(
                comm.dipTopx(this, 100),//宽度
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    //隐藏放大缩小
    void  InVisibleZoomControlss()
    {
        int childCount = mMapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        zoom.setVisibility(View.GONE);
    }

    private void InitLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        int span=1000*60;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信??
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            logMsg(sb.toString());
            Log.i("BaiduLocationApiDem", sb.toString());

            initMap();
            if(!isLBS) {
                cenpt = new LatLng(location.getLatitude(), location.getLongitude());
                mapTo(cenpt);
            }
            else  {mapTo(new LatLng(location.getLatitude(), location.getLongitude())); isLBS = false;}

            lon0 = cenpt.longitude;
            lat0 = cenpt.latitude;

            //mBaiduMap.clear();//标注前先删除
            AddMarker(cenpt);
            mLocationClient.stop();
        }
    }
    LatLng cenpt;
    void mapTo(LatLng geo)
    {
        //定义地图状???
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(geo)
                .zoom(15)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状???
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mMapView.setVisibility(View.VISIBLE);
    }

    void AddMarker(LatLng pnt)
    {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.nav);


//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(pnt)
                .icon(bitmap);
//在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option);
    }

    void AddMarker2(LatLng pnt,double dis,CObj obj)
    {
        //构建Marker图标
        BitmapDescriptor bitmap = null;
        if(dis<3000) bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.loc);
        else  if(dis>=3000&&dis<5000) bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.loc);
        else bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.loc);

//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(pnt)
                .icon(bitmap);
//在地图上添加Marker，并显示

        Marker  marker = (Marker)  mBaiduMap.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", obj.name+","+obj.faren+","+obj.Adr+","+obj.val+","+obj.id);
        marker.setExtraInfo(bundle);
    }

    /**
     * 显示请求字符??
     * @param str
     */
    public void logMsg(String str) {
//        try {
//            if (mLocationResult != null)
//                mLocationResult.setText(str);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管??
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管??
        if(isPause) {
            mMapView.onResume();
            isPause = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管??
        mMapView.onPause();
        isPause = true;
    }

    @Override
    protected void  onStop()
    {
        super.onStop();
        if(mLocationClient!=null)
            mLocationClient.stop();
    }
    long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出!", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                SysApplication.getInstance().exit();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        //lockLongPressKey = true;
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return true;
    }

}
