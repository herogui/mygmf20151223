package com.aoc.gmf.MyDlg;

/**
 * Created by giser on 2015/9/8.
 */
public interface IMyDlg {
    public void init(int contentView);
    public void setOkBtnClick(MyDlg.IDlgClick iDlgClick);
    public boolean checkInput();
    public void  initData(String[] strs, String id);
}
