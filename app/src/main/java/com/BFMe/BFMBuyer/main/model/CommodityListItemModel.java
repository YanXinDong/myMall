package com.BFMe.BFMBuyer.main.model;

import android.app.Activity;
import android.content.Intent;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.main.presenter.CommodityListItemOnClickListener;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/19 11:00
 */
public class CommodityListItemModel implements CommodityListItemOnClickListener {

    private Activity activity;
    public CommodityListItemModel(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(activity,ProducetDetailsActivity.class);
        intent.putExtra("productId",id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);;
    }
}
