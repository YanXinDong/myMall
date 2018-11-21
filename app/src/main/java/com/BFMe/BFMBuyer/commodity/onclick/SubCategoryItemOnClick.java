package com.BFMe.BFMBuyer.commodity.onclick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.BFMe.BFMBuyer.commodity.activity.CommodityListActivity;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/18 19:45
 */
public class SubCategoryItemOnClick implements View.OnClickListener {

    private Context context;
    private String name;
    private String id;
    public SubCategoryItemOnClick(Context context, String name, String id) {
        this.context = context;
        this.name = name;
        this.id = id;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, CommodityListActivity.class);
        intent.putExtra("subCategoryName",name);
        context.startActivity(intent);
    }
}
