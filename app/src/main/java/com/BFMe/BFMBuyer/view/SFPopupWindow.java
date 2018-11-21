package com.BFMe.BFMBuyer.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by BFMe.miao on 2018/2/2.
 */

public class SFPopupWindow extends PopupWindow{
    public SFPopupWindow(View contentView, int width, int height, boolean b) {
        super(contentView, width, height,b);
    }

    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}
