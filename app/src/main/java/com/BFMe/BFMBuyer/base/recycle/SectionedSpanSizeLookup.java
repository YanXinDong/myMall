package com.BFMe.BFMBuyer.base.recycle;

import android.support.v7.widget.GridLayoutManager;

/**
 * author: gyc
 * description:分组每行显示数量的管理类
 * time: create at 2017/7/7 23:19
 */

public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

    protected SectionAdapter adapter = null;
    protected GridLayoutManager layoutManager = null;

    public SectionedSpanSizeLookup(SectionAdapter adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter.hasHeader()) {//列表顶部有header
            if (position == 0) {
                return layoutManager.getSpanCount();
            } else if (position < adapter.getItemCount()) {
                if (adapter.isSectionHeaderPosition(position -1)) {
                    return layoutManager.getSpanCount();
                } else {
                    return 1;
                }
            } else {
                return layoutManager.getSpanCount();
            }
        } else {//列表顶部没有header
            if (position < adapter.getItemCount()) {
                if (adapter.isSectionHeaderPosition(position)) {
                    return layoutManager.getSpanCount();
                } else {
                    return 1;
                }
            } else {
                return layoutManager.getSpanCount();
            }
        }
    }
}
