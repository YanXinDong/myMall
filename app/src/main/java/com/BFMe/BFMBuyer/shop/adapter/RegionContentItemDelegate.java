package com.BFMe.BFMBuyer.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.RegionFiltrateBean;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.shop.activity.ShopListActivity;

/**
 * Description：店铺地区content
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 16:03
 */
public class RegionContentItemDelegate implements ItemViewDelegate<BaseTypeBean> {

    private Context context;
    public RegionContentItemDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_content_360_50_bg_ffffff;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("content");
    }

    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        final Object data = baseTypeBean.getData();
        if(data instanceof RegionFiltrateBean.AreaDataBean.ConuntryBean) {//国家、地区
            RegionFiltrateBean.AreaDataBean.ConuntryBean country = (RegionFiltrateBean.AreaDataBean.ConuntryBean) data;
            holder.setText(R.id.tv_content,country.getAreaName());
        }else {//洲际馆
            RegionFiltrateBean.AreaDataBean.IntercontinentalBean intercontinentalBean = (RegionFiltrateBean.AreaDataBean.IntercontinentalBean) data;
            holder.setText(R.id.tv_content,intercontinentalBean.getAreaName());
        }

        holder.setOnClickListener(R.id.ll_item_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopListActivity.class);
                if(data instanceof RegionFiltrateBean.AreaDataBean.ConuntryBean) {//国家、地区
                    RegionFiltrateBean.AreaDataBean.ConuntryBean country = (RegionFiltrateBean.AreaDataBean.ConuntryBean) data;

                    intent.putExtra("TitleName",country.getAreaName());
                    intent.putExtra("AreaId",country.getAreaId());

                }else {//洲际馆
                    RegionFiltrateBean.AreaDataBean.IntercontinentalBean intercontinentalBean = (RegionFiltrateBean.AreaDataBean.IntercontinentalBean) data;
                    intent.putExtra("TitleName",intercontinentalBean.getAreaName());
                    intent.putExtra("IntercontinentalId",intercontinentalBean.getAreaId());
                }

                context.startActivity(intent);
            }
        });

    }
}
