package com.BFMe.BFMBuyer.search.adapter;

import android.content.Context;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;

import java.util.List;

/**
 * Description：搜索联想列表adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/26 16:43
 */
public class SearchThinkAdapter extends CommonAdapter<String> {


    public SearchThinkAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_content,s);
    }

}
