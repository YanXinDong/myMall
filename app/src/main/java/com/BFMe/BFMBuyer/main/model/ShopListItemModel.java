package com.BFMe.BFMBuyer.main.model;

import android.app.Activity;
import android.content.Intent;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.main.presenter.ShopListItemOnClickListener;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/19 10:54
 */
public class ShopListItemModel implements ShopListItemOnClickListener {
    private Activity activity;
    public ShopListItemModel(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(String shopId) {
        Intent intent = new Intent(activity, ShopHomeActivity.class);
        intent.putExtra("shopId", shopId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }
}
