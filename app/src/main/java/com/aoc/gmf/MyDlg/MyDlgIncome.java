package com.aoc.gmf.MyDlg;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.aoc.gmf.MyApp;
import com.aoc.gmf.MyEvent.CusEvent;
import com.aoc.gmf.MyEvent.CusEventListener;
import com.aoc.gmf.MyEvent.EventSourceObject;

import com.aoc.gmf.MyUI.MyDialog;
import com.aoc.gmf.MyUI.MyLay;
import com.aoc.gmf.MyUI.MySlideAdapter;
import com.aoc.gmf.R;
import com.aoc.gmf.common.DateHelper;
import com.aoc.gmf.common.comm;
import com.aoc.gmf.controlHelp.MyEditChangeListener;
import com.aoc.gmf.entity.CItem;
import com.aoc.gmf.http.httpHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giser on 2015/9/8.
 */
public class MyDlgIncome extends MyDlg {

    EditText income_etxtmoney;
    EditText income_etxtmoney2;
    EditText income_etxtmoney3;
    EditText income_etxtmoney4;
    EditText income_txttype;
    EditText income_etxtdate;
    EditText income_etxtother;


    Activity activity;
    Spinner mSpinner;

    int typeChangeNum = 0;//记录第几次触发选择

    public MyDlgIncome(Activity activity, String title) {
        super(activity, title);
        this.activity = activity;

    }

    public void initData(String[] strs, String id) {
        initUI();

        if (strs == null) {
            initInfo("");
        } else {
            income_etxtmoney.setTag(id);
            income_etxtmoney.setText(strs[0]);
            income_etxtmoney2.setText(strs[1]);
            income_etxtmoney3.setText(strs[2]);
            income_etxtmoney4.setText(strs[3]+"立方米");

        }
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
                    MyDlgIncome  myDlgIncome = new MyDlgIncome(activity, "详细信息");
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
    public boolean checkInput() {
//        String money = income_etxtmoney.getText().toString();
//        String type = income_txttype.getText().toString();
//        String txtdate = income_etxtdate.getText().toString();
//        String other = income_etxtother.getText().toString();
//
//        if (money.length() < 1) {
//            Toast.makeText(activity, "请输入发生金额！", Toast.LENGTH_SHORT).show();
//            income_etxtmoney.setFocusable(true);
//            return false;
//        }
//
//        if (txtdate.length() < 1) {
//            Toast.makeText(activity, "请选择发生日期！", Toast.LENGTH_SHORT).show();
//            income_etxtdate.setFocusable(true);
//            return false;
//        }
//
//        if (type.length() < 1) {
//            Toast.makeText(activity, "请选择项目类型！", Toast.LENGTH_SHORT).show();
//            income_txttype.setFocusable(true);
//            return false;
//        }
//
//        if(type.equals("其它"))
//            if (other.length() < 1) {
//                Toast.makeText(activity, "请输入其它！", Toast.LENGTH_SHORT).show();
//                income_etxtother.setFocusable(true);
//                return false;
//            }

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

//        final  MyLay income_lay_date = (MyLay)this.layout.findViewById(R.id.income_lay_date);
//        income_lay_date.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        income_lay_date.setBackgroundColor(activity.getResources().getColor(R.color.listviewSelect));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        income_lay_date.setBackgroundColor(activity.getResources().getColor(R.color.contentBack));
//
//                        String strdate = "";
//                        if(income_etxtdate.getText().toString().length()>0)
//                        {
//                            Date myDate = DateHelper.strToDate(income_etxtdate.getText().toString().replace("/","-"));
//                            SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日");
//                            strdate   =    formatter.format(myDate.getTime());
//                        }
//                        else  strdate = DateHelper.getNowDayStr();
//
//                        DateTimePickDialogUtilDay dateTimePicKDialog = new DateTimePickDialogUtilDay(
//                                activity, strdate);
//                        dateTimePicKDialog.dateTimePicKDialogJustDay(income_etxtdate);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    void  initInfo(final String typeName)
    {

    }
}
