package com.aoc.gmf.MyUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aoc.gmf.R;

public class MyDialog {

    Activity activity;
    AlertDialog  alertDialog;

    IClick iClick;

    public MyDialog(Activity activity,String title,String tip,String noTip) {
        this.activity = activity;
        init(title,tip,noTip);
    }

    public MyDialog(Activity activity) {
        this.activity = activity;
        init("","","");
    }

    public MyDialog(Activity activity,String title) {
        this.activity = activity;
        init(title,"","");
    }

    public void  setOnClickBtn(IClick iClick)
    {
        this.iClick = iClick;
    }

    void  init(String title,String tip,String noTip) {
        LinearLayout layout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog, null);
        TextView tvTitle = (TextView)layout.findViewById(R.id.textView_dialog);

        if(title.length()>0)
        tvTitle.setText(title+"?");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(layout);
        //alertDialog.getWindow().setLayout(480, 340);

        Button btnOk = (Button)layout.findViewById(R.id.btnOk_dialog);

        if(tip.length()>0)
        btnOk.setText(tip);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClick.OnClickBtn(true);
                alertDialog.dismiss();
            }
        });

        Button btnCanel = (Button)layout.findViewById(R.id.btnCanel_dialog);

        if(noTip.length()>0)
        btnCanel.setText(noTip);

        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iClick.OnClickBtn(false);
                alertDialog.dismiss();
            }
        });
    }

    public interface IClick
    {
        public void OnClickBtn(Boolean isOK);
    }
}

