package com.aoc.gmf.controlHelp;

import android.text.Editable;
import android.text.TextWatcher;


/**
 * Created by giser on 2015/9/7.
 */
public class MyEditChangeListener implements TextWatcher {

    public interface ITextChanged
    {
        public  void  onTextChanged();
    }

    ITextChanged iTextChanged;

    public void setTextChanged(ITextChanged  iTextChanged)
    {
        this.iTextChanged = iTextChanged;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            iTextChanged.onTextChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
