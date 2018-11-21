package com.BFMe.BFMBuyer.main.presenter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/19 15:58
 */
public abstract class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        myAfterTextChanged(s);
    }

    public abstract void myAfterTextChanged(Editable s);
}
