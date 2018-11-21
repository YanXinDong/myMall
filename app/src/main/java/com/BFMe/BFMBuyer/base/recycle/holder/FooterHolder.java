package com.BFMe.BFMBuyer.base.recycle.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * author: gyc
 * description:上拉加载的footer
 * time: create at 2017/7/7 20:50
 */

public class FooterHolder extends RecyclerView.ViewHolder {
    public TextView tvFooter;

    public FooterHolder(View itemView) {
        super(itemView);
        initView(itemView.getContext());
    }

    private void initView(Context context) {
        tvFooter = new TextView(context);
    }
}
