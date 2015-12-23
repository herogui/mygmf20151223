package com.aoc.gmf.MyDlg;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aoc.gmf.CheckActivity;
import com.aoc.gmf.MainActivity;
import com.aoc.gmf.MyEvent.CusEvent;
import com.aoc.gmf.MyEvent.CusEventListener;
import com.aoc.gmf.MyEvent.EventSourceObject;
import com.aoc.gmf.MyUI.MyDialog;
import com.aoc.gmf.MyUI.MyLay;
import com.aoc.gmf.MyUI.MySlideAdapter;
import com.aoc.gmf.R;
import com.aoc.gmf.controlHelp.MyEditChangeListener;
import com.aoc.gmf.entity.CItem;
import com.aoc.gmf.http.httpHelper;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.roamer.slidelistview.SlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giser on 2015/9/8.
 */
public class MyDlgLIST extends MyDlg {

    EditText income_etxtmoney;
    EditText income_etxtmoney2;
    EditText income_etxtmoney3;
    EditText income_etxtmoney4;
    EditText income_txttype;
    EditText income_etxtdate;
    EditText income_etxtother;

    SlideListView listview_item;

    public List<Map<String, String>> dataList;
    Activity activity;
    Spinner mSpinner;

    int typeChangeNum = 0;//记录第几次触发选择

    public MyDlgLIST(Activity activity, String title) {
        super(activity, title);
        this.activity = activity;
        mRequestQueue =  Volley.newRequestQueue(activity);
        dataList  = new ArrayList<Map<String, String>>();
    }

    void  GetData()
    {
        String url = "http://115.29.136.148:5555/handle/DBHandler.ashx?action=GetRows&tableName=SD_ZhuoMuNiao&mode=detailed";

        // Log.d("res", netUrl);
        httpHelper http = new httpHelper();
        http.StringHttp(mRequestQueue, url);
        http.GetResEvent.addCusListener(new CusEventListener() {
            @Override
            public void fireCusEvent(CusEvent e) {
                EventSourceObject eObject = (EventSourceObject) e.getSource();
                String res = eObject.getString();
                String[] str1 = res.split(";");
                for(int i = 0;i<str1.length;i++) {
                    String[] str2 = str1[i].split(",");

                    Map<String, String> dataMap3 = new HashMap<String, String>();
                    dataMap3.put("id",str2[12].replace(".000000",""));
                    dataMap3.put("num", String.valueOf(dataList.size() + 1));
                    dataMap3.put("name", str2[2]);
                    dataList.add(dataMap3);
                }
                setSoureList(dataList);
            }
        });
    }
    Spinner   mSpinner1;
    public void initData(String[] strs, String id) {
        try {
            mSpinner1 = (Spinner) activity.findViewById(R.id.spinner_place9);

            ArrayList<CItem> lst = new ArrayList<CItem>();
            CItem item1 = new CItem("1", "历下区");lst.add(item1);
            CItem item2 = new CItem("2", "市中区");lst.add(item2);
            CItem item3 = new CItem("3", "槐荫区");lst.add(item3);
            CItem item4 = new CItem("4", "天桥区");lst.add(item4);
            CItem item5 = new CItem("5", "历城区");lst.add(item5);
            CItem item6 = new CItem("6", "历城区");lst.add(item6);
            CItem item7 = new CItem("7", "平阴县");lst.add(item7);
            CItem item8 = new CItem("8", "济阳县");lst.add(item8);
            CItem item9 = new CItem("9", "商河县");lst.add(item9);
            CItem item10 = new CItem("10", "章丘市");lst.add(item10);
            ArrayAdapter<CItem> adapter1 = new ArrayAdapter<CItem>(activity, android.R.layout.simple_spinner_item, lst);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //?? Adapter?????
            mSpinner1.setAdapter(adapter1);

            listview_item = (SlideListView) layout.findViewById(R.id.listview_item2);
            listview_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    View[] views = (View[]) view.getTag();
                    TextView tv = ((TextView) views[0]);
                    TextView tv2 = ((TextView) views[1]);
                    final  String myid =tv.getTag().toString();
                    final  String myname =tv2.getText().toString();


                    //导入数据库
                    String url = "http://115.29.136.148:3333/Handler1.ashx?ischeck=0&userid="+((MainActivity)activity).user+"&factoryId="+myid;

                    // Log.d("res", netUrl);
                    httpHelper http = new httpHelper();
                    http.StringHttp(mRequestQueue, url);
                    http.GetResEvent.addCusListener(new CusEventListener() {
                        @Override
                        public void fireCusEvent(CusEvent e) {
                            EventSourceObject eObject = (EventSourceObject) e.getSource();
                            String res = eObject.getString();

                            if (res.equals(httpHelper.errorTip)) {
                                Toast.makeText(activity, "未能连接网络", Toast.LENGTH_SHORT).show();
                                return;//无结果的
                            }
                            else {
                                Map<String, String> dataMap3 = new HashMap<String, String>();
                                dataMap3.put("id",myid);
                                dataMap3.put("num", String.valueOf( ((MainActivity)activity). data.size() + 1));
                                dataMap3.put("adr", myname);
                                ((MainActivity)activity).data.add(dataMap3);
                                setSoure(((MainActivity)activity).data);
                                Toast.makeText(activity, "添加成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        catch(Exception EXP)
        {
            Log.d("res",EXP.getMessage());
        }
        GetData();

       // initUI();

//        if (strs == null) {
//            initInfo("");
//        } else {
//            income_etxtmoney.setTag(id);
//            income_etxtmoney.setText(strs[0]);
//            income_etxtmoney2.setText(strs[1]);
//            income_etxtmoney3.setText(strs[2]);
//            income_etxtmoney4.setText(strs[3]+"立方米");
//        }
    }

    void setSoure(final List<Map<String, String>> datas) {
        final String[] menunames = new String[]{"详细","检查","删除"};
        ((MainActivity)activity).mAdapter = new MySlideAdapter(activity, datas, R.layout.activity_incomeitem,
                new String[]{"num", "adr"},
                new int[]{R.id.txtdata1_income,
                        R.id.txtdata2_income,
                },
                menunames, "id");
        ((MainActivity)activity). mAdapter.setOnBtnClick(new MySlideAdapter.IBtnClick() {
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
                MainActivity.CObj obj = null;
                for(int j = 0;j<((MainActivity)activity).list.size();j++)
                {
                    if(((MainActivity)activity).list.get(j).Getid().equals(id))
                    {
                        obj = ((MainActivity)activity).list.get(j);
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
                    MyDlgLIST myDlgIncome = new MyDlgLIST(activity, "详细信息");
                    myDlgIncome.init(R.layout.activity_incomeadd);
                    myDlgIncome.initData(str, id);
                    myDlgIncome.setOkBtnClick(new IDlgClick() {
                        @Override
                        public void OnClickBtn(Boolean isOK) {
                            //  initInfo(pagesize,0);
                        }
                    });
                    //  Toast.makeText(MainActivity.this, obj.GetAdr()+obj.Getval()+obj.Getfaren(), Toast.LENGTH_SHORT).show();
                }

                if (ordernum == 2) {
                    Intent intent = new Intent();
                    intent.setClass(activity, CheckActivity.class);
                    String name = ((MainActivity)activity).getName(id);
                    intent.putExtra("info",name+","+ id);
                    activity.startActivity(intent);
                }

                if (ordernum == 3) {
                    final String delId = id;
////
                    MyDialog dlg = new MyDialog(activity);
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
                                        for(int i = 0;i< ((MainActivity)activity).data.size();i++)
                                        {
                                            if( ((MainActivity)activity).data.get(i).get("id") == delId)
                                            {
                                                ((MainActivity)activity).data.remove(i);
                                                setSoure( ((MainActivity)activity).data);

                                                for(int j = 0;j< ((MainActivity)activity).data.size();j++)
                                                {
                                                    ((MainActivity)activity).data.get(j).put("num",String.valueOf(j+1));
                                                }

                                                break;
                                            }
                                        }
                                        Toast.makeText(activity, "删除成功！", Toast.LENGTH_SHORT).show();
                                    }else    Toast.makeText(activity, "删除不成功，请联系系统管理员！", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                }
            }
        });

        ((MainActivity)activity).mSlideListView.setAdapter( ((MainActivity)activity).mAdapter);
    }

    void setSoureList(final List<Map<String, String>> datas) {
        final String[] menunames = new String[]{"详细","检查"};
        ((MainActivity)activity).mAdapter = new MySlideAdapter(activity, datas, R.layout.activity_incomeitem,
                new String[]{"num", "name"},
                new int[]{R.id.txtdata1_income,
                        R.id.txtdata2_income,
                },
                menunames, "id");
        ((MainActivity)activity). mAdapter.setOnBtnClick(new MySlideAdapter.IBtnClick() {
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
                MainActivity.CObj obj = null;
                for(int j = 0;j<((MainActivity)activity).list.size();j++)
                {
                    if(((MainActivity)activity).list.get(j).Getid().equals(id))
                    {
                        obj = ((MainActivity)activity).list.get(j);
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
                    MyDlgLIST myDlgIncome = new MyDlgLIST(activity, "详细信息");
                    myDlgIncome.init(R.layout.activity_incomeadd);
                    myDlgIncome.initData(str, id);
                    myDlgIncome.setOkBtnClick(new IDlgClick() {
                        @Override
                        public void OnClickBtn(Boolean isOK) {
                            //  initInfo(pagesize,0);
                        }
                    });
                    //  Toast.makeText(MainActivity.this, obj.GetAdr()+obj.Getval()+obj.Getfaren(), Toast.LENGTH_SHORT).show();
                }

                if (ordernum == 2) {
                    Intent intent = new Intent();
                    intent.setClass(activity, CheckActivity.class);
                    String name = ((MainActivity)activity).getName(id);
                    intent.putExtra("info",name+","+ id);
                    activity.startActivity(intent);
                }

                if (ordernum == 3) {
                    final String delId = id;
////
                    MyDialog dlg = new MyDialog(activity);
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
                                        for(int i = 0;i< ((MainActivity)activity).data.size();i++)
                                        {
                                            if( ((MainActivity)activity).data.get(i).get("id") == delId)
                                            {
                                                ((MainActivity)activity).data.remove(i);
                                                setSoure( ((MainActivity)activity).data);

                                                for(int j = 0;j< ((MainActivity)activity).data.size();j++)
                                                {
                                                    ((MainActivity)activity).data.get(j).put("num",String.valueOf(j+1));
                                                }

                                                break;
                                            }
                                        }
                                        Toast.makeText(activity, "删除成功！", Toast.LENGTH_SHORT).show();
                                    }else    Toast.makeText(activity, "删除不成功，请联系系统管理员！", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                }
            }
        });

        listview_item.setAdapter( ((MainActivity)activity).mAdapter);
    }
    public boolean checkInput() {


        return true;
    }
    RequestQueue mRequestQueue;
    void initUI() {
        income_etxtmoney = (EditText)layout.findViewById(R.id.income_etxtmoney);
        income_etxtmoney2 = (EditText)layout.findViewById(R.id.income_etxtmoney2);
        income_etxtmoney3 = (EditText)layout.findViewById(R.id.income_etxtmoney3);
        income_etxtmoney4 = (EditText)layout.findViewById(R.id.income_etxtmoney4);
        income_etxtdate = (EditText)layout.findViewById(R.id.income_etxtmoney4);
        income_txttype = (EditText)layout.findViewById(R.id.income_txttype);
        income_etxtother = (EditText)layout.findViewById(R.id.income_etxtother);

        Button btnOk = (Button) this.layout.findViewById(R.id.btnOk_dlg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(activity, CheckActivity.class);
//                //String name = getName(myid);
//                intent.putExtra("info", income_etxtmoney.getText().toString()+ "," + income_etxtmoney.getTag());
//                activity.startActivity(intent);
                //导入数据库
                String url = "http://115.29.136.148:3333/Handler1.ashx?ischeck=0&userid="+((MainActivity)activity).user+"&factoryId="+ income_etxtmoney.getTag();
                mRequestQueue =  Volley.newRequestQueue(activity);
                // Log.d("res", netUrl);
                httpHelper http = new httpHelper();
                http.StringHttp(mRequestQueue, url);
                http.GetResEvent.addCusListener(new CusEventListener() {
                    @Override
                    public void fireCusEvent(CusEvent e) {
                        EventSourceObject eObject = (EventSourceObject) e.getSource();
                        String res = eObject.getString();

                        if (res.equals(httpHelper.errorTip)) {
                            Toast.makeText(activity, "未能连接网络", Toast.LENGTH_SHORT).show();
                            return;//无结果的
                        }
                        else {
                            Map<String, String> dataMap3 = new HashMap<String, String>();
                            dataMap3.put("id", income_etxtmoney.getTag().toString());
                            dataMap3.put("num", String.valueOf(((MainActivity)activity).data.size() + 1));
                            dataMap3.put("adr", income_etxtmoney.getText().toString());
                            ((MainActivity)activity).data.add(dataMap3);
                            setSoure(((MainActivity)activity).data);
                            alertDialog.dismiss();
                            Toast.makeText(activity, "添加成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSpinner = (Spinner) this.layout.findViewById(R.id.spinner_incomeadd);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                typeChangeNum++;
                if (typeChangeNum == 1) return;
                ;

                String typeid = ((CItem) mSpinner.getSelectedItem()).GetID();
                String name = ((CItem) mSpinner.getSelectedItem()).GetValue();

                income_txttype.setText(name);
                income_txttype.setTag(typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        final MyLay place_txttype= (MyLay) this.layout.findViewById(R.id.income_lay_type);
        place_txttype.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSpinner.performClick();
                return true;
            }
        });

        final  LinearLayout LinearLayoutincome_other = (LinearLayout)this.layout.findViewById(R.id.LinearLayoutincome_other);
        MyEditChangeListener myEditChangeListener = new MyEditChangeListener();
        income_txttype.addTextChangedListener(myEditChangeListener);
        myEditChangeListener.setTextChanged(new MyEditChangeListener.ITextChanged() {
            @Override
            public void onTextChanged() {
                String mydate = income_txttype.getText().toString();
                if(mydate.trim().equals("其它"))
                {
                    LinearLayoutincome_other.setVisibility(View.VISIBLE);
                }
                else {
                    LinearLayoutincome_other.setVisibility(View.GONE);
                }
            }
        });
    }

    void  initInfo(final String typeName)
    {

    }
}
