package com.BFMe.BFMBuyer.main.adapter.ugc;

import android.app.Activity;

import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.ugc.bean.TopicList;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/14 11:34
 */
public class UGCHomeAdapter extends MultiItemTypeAdapter<TopicList.TopicsListBean> {

    private List<TopicList.TopicsListBean> datas;
    public UGCHomeAdapter(Activity activity, List<TopicList.TopicsListBean> datas) {
        super(activity, datas);
        this.datas = datas;
        addItemViewDelegate(10, new UGCTopicListAdapter(activity));
    }

    public void cleanData() {
        datas.clear();
    }

    public void addData(List<TopicList.TopicsListBean> mTopicsList) {
        addData(0,mTopicsList);
    }

    public void addData(int position,List<TopicList.TopicsListBean> data) {
        if(data != null && data.size() > 0 ) {
            datas.addAll(position,data);
            notifyDataSetChanged();
        }
    }
}
