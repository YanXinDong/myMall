package com.BFMe.BFMBuyer.shop.adapter;

import android.content.Context;

import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

import java.util.List;

/**
 * Description：店铺地区选择adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 15:56
 */
public class RegionFiltrateAdapter extends MultiItemTypeAdapter<BaseTypeBean> {

    public RegionFiltrateAdapter(Context context, List<BaseTypeBean> datas) {
        super(context, datas);

        addItemViewDelegate(new RegionTitleItemDelegate());
        addItemViewDelegate(new RegionContentItemDelegate(context));

    }
}
