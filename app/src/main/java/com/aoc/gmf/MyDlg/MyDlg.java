package com.aoc.gmf.MyDlg;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aoc.gmf.R;

/**
 * Created by giser on 2015/9/8.
 */
public class MyDlg implements IMyDlg {

 Activity activity;
 AlertDialog alertDialog;
 IDlgClick iDlgClick;
 String title;
public LinearLayout layout;

 public void setOnDlgClick(IDlgClick iDlgClick) {
  this.iDlgClick = iDlgClick;
 }

 public MyDlg(Activity activity,String title) {
  this.activity = activity;
  this.title = title;
 }

 public AlertDialog getAlertDialog()
 {
   return  this.alertDialog;
 }

 @Override
 public void init(int contentView) {
  layout = (LinearLayout) activity
          .getLayoutInflater().inflate(R.layout.mydlg, null);

  LinearLayout layoutContentView = (LinearLayout) activity
          .getLayoutInflater().inflate(contentView, null);
  LinearLayout contentLay = (LinearLayout)layout.findViewById(R.id.mydlg_content);
  contentLay.addView(layoutContentView);

  TextView tvTitle = (TextView) layout.findViewById(R.id.mydlg_title);
  tvTitle.setText(this.title);

  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
  alertDialog = builder.create();
  alertDialog.show();
  alertDialog.getWindow().setContentView(layout);
  alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
  if(contentView==R.layout.list)
  alertDialog.getWindow().setLayout(1500, 800);

  Button btnCanel = (Button) layout.findViewById(R.id.btnCanel_dialog);

  btnCanel.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
    alertDialog.dismiss();
   }
  });
 }

 public void  initData(String[] strs,String id)
 {

 }

 @Override
 public void setOkBtnClick(IDlgClick iDlgClick) {
    this.iDlgClick = iDlgClick;
 }

 @Override
 public boolean checkInput() {
  return true;
 }

 public interface IDlgClick {
  public void OnClickBtn(Boolean isOK);
 }

}
