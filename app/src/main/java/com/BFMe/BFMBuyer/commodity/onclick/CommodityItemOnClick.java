package com.BFMe.BFMBuyer.commodity.onclick;

import android.content.Intent;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.CommodityListActivity;

/**
 * Description：商品列表点击事件
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/18 20:12
 */
public class CommodityItemOnClick implements View.OnClickListener {

    private CommodityListActivity mActivity;
    private String id;
    public CommodityItemOnClick(CommodityListActivity activity, String id) {
        mActivity = activity;
        this.id = id;
    }

    @Override
    public void onClick(View v) {

    }
}
