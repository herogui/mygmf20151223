package com.aoc.gmf.MyUI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aoc.gmf.R;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import java.util.List;
import java.util.Map;


public class MySlideAdapter extends SlideBaseAdapter {
    protected  List<Map<String, String>>  mData;
    private int contentView;
    protected String[] from;
    protected int[] to;
    Context context;
    int resource;

    int rightMenuNum = 3;

    String[] menuNames;
    String idField;

    public interface  IBtnClick
    {
        public void SetOnBtnClick(int ordernum, View v);
    }

    IBtnClick iBtnClick;


    public MySlideAdapter(Context context,  List<Map<String, String>> data,int resource,
                            String[] from, int[] to,String idField) {
        super(context);
        this.context = context;
        mData = data;
        this.contentView = resource;
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.idField = idField;
    }

    //rightMenuNum  >0 <4
    public MySlideAdapter(Context context,  List<Map<String, String>> data,int resource,
                          String[] from, int[] to,int rightMenuNum,String idField) {
        super(context);
        this.context = context;
        mData = data;
        this.contentView = resource;
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.rightMenuNum = rightMenuNum;
        this.idField = idField;
    }

    public MySlideAdapter(Context context,  List<Map<String, String>> data,int resource,
                          String[] from, int[] to,String[] menuNames,String idField) {
        super(context);
        this.context = context;
        mData = data;
        this.contentView = resource;
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.rightMenuNum = menuNames.length;
        this.menuNames = menuNames;
        this.idField = idField;
    }

    @Override
    public SlideMode getSlideModeInPosition(int position) {
        return super.getSlideModeInPosition(position);
    }

    @Override
    public int getFrontViewId(int position) {
        return this.contentView;
    }

    @Override
    public int getLeftBackViewId(int position) {
        return R.layout.row_left_back_view;
    }

    @Override
    public int getRightBackViewId(int position) {
        return R.layout.row_right_back_view;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = createConvertView(position);

            View[] views = new View[to.length+this.rightMenuNum];
            for (int i = 0; i < to.length; i++) {
                View tv = v.findViewById(to[i]);
                views[i] = tv;
        }
//            //right slideView
            setRightSlide(views,v);

            v.setTag(views);
        }
        View[] holders = (View[]) v.getTag();
        int len = holders.length;
        for (int i = 0; i < len-this.rightMenuNum; i++) {
            ((TextView) holders[i]).setText(this.mData.get(position).get(from[i]).toString());
            ((TextView) holders[i]).setTag(this.mData.get(position).get(this.idField).toString());
        }

        setClickListener(holders);

        return v;
    }

    public void setRightMunuNum(int num)
    {
        this.rightMenuNum = num;
    }

    public int getRightMenuNum()
    {
        return this.rightMenuNum;
    }

    public void setRightSlide( View[] views,View v)
    {
        //right slideView
        View btn1 = v.findViewById(R.id.btn1);
        View btn2 = v.findViewById(R.id.btn2);
        View btn3 = v.findViewById(R.id.btn3);

        if(this.rightMenuNum>0) {
            views[to.length] = btn1;
            if(menuNames!=null)  ((Button)btn1).setText(menuNames[0]);
        }
        if(this.rightMenuNum>1) {
            views[to.length + 1] = btn2;
            if(menuNames!=null)  ((Button)btn2).setText(menuNames[1]);
        }
        else
        {
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
        }

        if(this.rightMenuNum>2) {
            views[to.length + 2] = btn3;
            if(menuNames!=null)  ((Button)btn3).setText(menuNames[2]);
        }
        else
        {
            btn3.setVisibility(View.GONE);
        }
    }

    public void  setOnBtnClick(IBtnClick iBtnClick)
    {
this.iBtnClick = iBtnClick;
    }

    public void setClickListener(View[] holders)
    {
        if(this.rightMenuNum<1) return;
        Button btn1 = (Button)holders[to.length];
        btn1.setTag(holders);//供点击编辑使用
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iBtnClick.SetOnBtnClick(1,v);
            }
        });

        if(rightMenuNum<2) return;;

        Button btn2 = (Button)holders[to.length+1];
        btn2.setTag(holders);//供点击编辑使用
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iBtnClick.SetOnBtnClick(2,v);
            }
        });

        if(rightMenuNum<3) return;;
        Button btn3 = (Button)holders[to.length+2];
        btn3.setTag(holders);//供点击编辑使用
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iBtnClick.SetOnBtnClick(3,v);
            }
        });
    }
}