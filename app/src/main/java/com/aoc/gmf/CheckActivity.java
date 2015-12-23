package com.aoc.gmf;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aoc.gmf.MyEvent.CusEvent;
import com.aoc.gmf.MyEvent.CusEventListener;
import com.aoc.gmf.MyEvent.EventSourceObject;
import com.aoc.gmf.MyUI.MyLay;
import com.aoc.gmf.common.comm;
import com.aoc.gmf.entity.CItem;
import com.aoc.gmf.http.httpHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CheckActivity extends Activity {

    EditText edit_etxtCheckFname ;
    EditText edit_etxtCheckDate;

    Activity activity;
    Spinner mSpinner1;
    Spinner mSpinner2;
    Spinner mSpinner3;
    Spinner mSpinner4;
    Spinner mSpinner5;
    Button btn;
    EditText  edit_PlaceType1;
    EditText  edit_PlaceType2;
    EditText  edit_PlaceType3;
    EditText  edit_PlaceType4;
    EditText  edit_PlaceType5;

    EditText  txtjilu1;
    EditText  txtjilu2;
    EditText  txtjilu3;
    EditText  txtjilu4;
    EditText  txtjilu5;
    String info;
    RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_check);
            mRequestQueue = Volley.newRequestQueue(this);

            info = getIntent().getStringExtra("info");
            String name = info.split(",")[0];
            String id = info.split(",")[1];
            initData(name,id);

            LinearLayout LinearLayoutback_topbar = (LinearLayout)findViewById(R.id.LinearLayoutback_topbar);
            LinearLayoutback_topbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception exp)
        {
            Log.d("res",exp.getMessage());
        }
    }

    public void initData(String name, String id) {
        final  String  myid = id;
        Button  btnQuickReg = (Button)CheckActivity.this.findViewById(R.id.btnQuickReg);
        btnQuickReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button  login = (Button)CheckActivity.this.findViewById(R.id.login2222);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                str  += edit_PlaceType1.getText().toString()+"$"+txtjilu1.getText().toString()+"@";
                str  += edit_PlaceType2.getText().toString()+"$"+txtjilu2.getText().toString()+"@";
                str  += edit_PlaceType3.getText().toString()+"$"+txtjilu3.getText().toString()+"@";
                str  += edit_PlaceType4.getText().toString()+"$"+txtjilu4.getText().toString()+"@";
                str  += edit_PlaceType5.getText().toString()+"$"+txtjilu5.getText().toString()+"@";
                EditText  edit_etxtJl = (EditText)CheckActivity.this.findViewById(R.id.edit_etxtJl);
                String  checkDate = edit_etxtCheckDate.getText().toString();
                String  factoryID = edit_etxtCheckFname.getText().toString();
                String  userID = myid;
                String  zenrenID = "temp";
                String  jianchajilu =edit_etxtJl.getText().toString();
                String  cunzaiwenti = str;
                String  fangkui = "temp";

                HashMap<String,String> datamap = new HashMap<String, String>();

                if(factoryID.length()<1)  factoryID = "temp";
                datamap.put("factoryID",factoryID);
                if(userID.length()<1)  userID = "temp";
                datamap.put("userID",userID);
                if(zenrenID.length()<1)  zenrenID = "temp";
                datamap.put("zenrenID",zenrenID);
                if(jianchajilu.length()<1)  jianchajilu = "temp";
                datamap.put("jianchajilu",jianchajilu);
                if(cunzaiwenti.length()<1)  cunzaiwenti = "temp";
                datamap.put("cunzaiwenti",cunzaiwenti);
                if(fangkui.length()<1)  fangkui = "temp";
                datamap.put("fangkui",fangkui);

                //?????????
                String url = "http://115.29.136.148:3333/HandlerJianchabiao.ashx";
                String netUrl = comm.UrlFormat(url, datamap);
                Log.d("res", netUrl);
                httpHelper http = new httpHelper();
                http.StringHttp(mRequestQueue, netUrl);
                http.GetResEvent.addCusListener(new CusEventListener() {
                    @Override
                    public void fireCusEvent(CusEvent e) {
                        EventSourceObject eObject = (EventSourceObject) e.getSource();
                        String res = eObject.getString();

                        if (res.equals(httpHelper.errorTip)) {
                            Toast.makeText(activity, "????????????", Toast.LENGTH_SHORT).show();
                            return;//??????
                        }
                        else {
                            Toast.makeText(activity, res, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSpinner1 = (Spinner) CheckActivity.this.findViewById(R.id.spinner_place1);
        mSpinner2 = (Spinner) CheckActivity.this.findViewById(R.id.spinner_place2);
        mSpinner3 = (Spinner) CheckActivity.this.findViewById(R.id.spinner_place3);
        mSpinner4 = (Spinner) CheckActivity.this.findViewById(R.id.spinner_place4);
        mSpinner5 = (Spinner) CheckActivity.this.findViewById(R.id.spinner_place5);

        txtjilu1 = (EditText)CheckActivity.this.findViewById(R.id.txtjilu1);
        txtjilu2 = (EditText)CheckActivity.this.findViewById(R.id.txtjilu2);
        txtjilu3 = (EditText)CheckActivity.this.findViewById(R.id.txtjilu3);
        txtjilu4 = (EditText)CheckActivity.this.findViewById(R.id.txtjilu4);
        txtjilu5 = (EditText)CheckActivity.this.findViewById(R.id.txtjilu5);

        edit_PlaceType1 = (EditText)CheckActivity.this.findViewById(R.id.edit_PlaceType1);
        edit_PlaceType2 = (EditText)CheckActivity.this.findViewById(R.id.edit_PlaceType2);
        edit_PlaceType3 = (EditText)CheckActivity.this.findViewById(R.id.edit_PlaceType3);
        edit_PlaceType4 = (EditText)CheckActivity.this.findViewById(R.id.edit_PlaceType4);
        edit_PlaceType5 = (EditText)CheckActivity.this.findViewById(R.id.edit_PlaceType5);

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String typeid = ((CItem) mSpinner1.getSelectedItem()).GetID();
                String name = ((CItem) mSpinner1.getSelectedItem()).GetValue();

                edit_PlaceType1.setText(name);
                edit_PlaceType1.setTag(typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String typeid = ((CItem) mSpinner2.getSelectedItem()).GetID();
                String name = ((CItem) mSpinner2.getSelectedItem()).GetValue();

                edit_PlaceType2.setText(name);
                edit_PlaceType2.setTag(typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        mSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String typeid = ((CItem) mSpinner3.getSelectedItem()).GetID();
                String name = ((CItem) mSpinner3.getSelectedItem()).GetValue();

                edit_PlaceType3.setText(name);
                edit_PlaceType3.setTag(typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        mSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String typeid = ((CItem) mSpinner4.getSelectedItem()).GetID();
                String name = ((CItem) mSpinner4.getSelectedItem()).GetValue();

                edit_PlaceType4.setText(name);
                edit_PlaceType4.setTag(typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        mSpinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String typeid = ((CItem) mSpinner5.getSelectedItem()).GetID();
                String name = ((CItem) mSpinner5.getSelectedItem()).GetValue();

                edit_PlaceType5.setText(name);
                edit_PlaceType5.setTag(typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        ArrayList<CItem> lst = new ArrayList<CItem>();
        CItem item1 = new CItem("1", "市发改委");lst.add(item1);
        CItem item2 = new CItem("2", "市经信委");lst.add(item2);
        CItem item3 = new CItem("3", "市国土局");lst.add(item3);
        CItem item4 = new CItem("4", "市公安局");lst.add(item4);
        CItem item5 = new CItem("5", "市建委");lst.add(item5);
        CItem item6 = new CItem("6", "市城管局");lst.add(item6);
        CItem item7 = new CItem("7", "市环保局");lst.add(item7);
        CItem item8 = new CItem("8", "市市政工业局");lst.add(item8);
        CItem item9 = new CItem("9", "市园林局");lst.add(item9);
        CItem item10 = new CItem("10", "城投集团");lst.add(item10);
        CItem item11 = new CItem("11", "西投集团");lst.add(item11);
        CItem item12 = new CItem("12", "旧投集团");lst.add(item12);
        CItem item13 = new CItem("13", "滨投集团");lst.add(item13);
        CItem item14 = new CItem("14", "历下区");lst.add(item14);
        CItem item15 = new CItem("15", "市中区");lst.add(item15);
        CItem item16 = new CItem("16", "槐荫区");lst.add(item16);
        CItem item17 = new CItem("17", "天桥区");lst.add(item17);
        CItem item18 = new CItem("18", "历城区");lst.add(item18);
        CItem item19 = new CItem("19", "高新区");lst.add(item19);
        CItem item20 = new CItem("20", "平阴县");lst.add(item20);
        CItem item21 = new CItem("21", "济阳县");lst.add(item21);
        CItem item22 = new CItem("22", "商河县");lst.add(item22);
        CItem item23 = new CItem("23", "章丘市");lst.add(item23);

        // ????Adapter??????????
        ArrayAdapter<CItem> adapter1 = new ArrayAdapter<CItem>(CheckActivity.this, android.R.layout.simple_spinner_item, lst);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //?? Adapter?????
        mSpinner1.setAdapter(adapter1);

        // ????Adapter??????????
        ArrayAdapter<CItem> adapter2 = new ArrayAdapter<CItem>(CheckActivity.this, android.R.layout.simple_spinner_item, lst);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //?? Adapter?????
        mSpinner2.setAdapter(adapter2);

        // ????Adapter??????????
        ArrayAdapter<CItem> adapter3 = new ArrayAdapter<CItem>(CheckActivity.this, android.R.layout.simple_spinner_item, lst);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //?? Adapter?????
        mSpinner3.setAdapter(adapter3);

        // ????Adapter??????????
        ArrayAdapter<CItem> adapter4 = new ArrayAdapter<CItem>(CheckActivity.this, android.R.layout.simple_spinner_item, lst);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //?? Adapter?????
        mSpinner4.setAdapter(adapter4);

        // ????Adapter??????????
        ArrayAdapter<CItem> adapter5 = new ArrayAdapter<CItem>(CheckActivity.this, android.R.layout.simple_spinner_item, lst);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //?? Adapter?????
        mSpinner5.setAdapter(adapter5);
        //initUI();

        if (name == null) {
            // initInfo("");
        } else {
            try {
                edit_etxtCheckFname = (EditText) CheckActivity.this.findViewById(R.id.edit_etxtCheckFname);
                edit_etxtCheckDate = (EditText) CheckActivity.this.findViewById(R.id.edit_etxtCheckDate);
                edit_etxtCheckFname.setText(name);
                // edit_etxtCheckFname.setText("1");

                MyLay lay = (MyLay)CheckActivity.this.findViewById(R.id.layselect_palace1);

                lay.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mSpinner1.performClick();
                        return true;
                    }
                });

                MyLay  lay2 = (MyLay)CheckActivity.this.findViewById(R.id.layselect_palace2);

                lay2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mSpinner2.performClick();
                        return true;
                    }
                });

                MyLay  lay3 = (MyLay)CheckActivity.this.findViewById(R.id.layselect_palace3);

                lay3.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mSpinner3.performClick();
                        return true;
                    }
                });

                MyLay  lay4 = (MyLay)CheckActivity.this.findViewById(R.id.layselect_palace4);

                lay4.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mSpinner4.performClick();
                        return true;
                    }
                });

                MyLay  lay5 = (MyLay)CheckActivity.this.findViewById(R.id.layselect_palace5);

                lay5.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mSpinner5.performClick();
                        return true;
                    }
                });

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
                Date curDate = new Date(System.currentTimeMillis());//?????????
                String str = formatter.format(curDate);
                edit_etxtCheckDate.setText(str);
            }
            catch (Exception exp)
            {
                Log.d(("res"),exp.getMessage());
            }

        }
    }
}
