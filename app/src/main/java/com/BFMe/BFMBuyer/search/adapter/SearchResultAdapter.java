package com.BFMe.BFMBuyer.search.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;
import com.BFMe.BFMBuyer.javaBean.ShopListData;
import com.BFMe.BFMBuyer.main.model.CommodityListItemModel;
import com.BFMe.BFMBuyer.main.model.ShopListItemModel;
import com.BFMe.BFMBuyer.main.model.TopicListItemModel;
import com.BFMe.BFMBuyer.main.presenter.CommodityListItemOnClickListener;
import com.BFMe.BFMBuyer.main.presenter.ShopListItemOnClickListener;
import com.BFMe.BFMBuyer.main.presenter.TopicListItemOnClickListener;
import com.BFMe.BFMBuyer.search.activity.SearchResultActivity;
import com.BFMe.BFMBuyer.ugc.bean.TopicList;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.BFMe.BFMBuyer.R.id.rl_recommend_topic;

/**
 * Description：搜索结果adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/27 12:19
 */
public class SearchResultAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private List<T> mDataLists = new ArrayList<>();
    private static final int TOPIC = 0; //话题
    private static final int SHOP = 1; //店铺
    private static final int COMMODITY = 2;//商品
    public static int STATE = TOPIC;//用于控制加载不同布局的状态
    private SearchResultActivity activity;
    private TopicListItemOnClickListener topicListItemOnClickListener;
    private ShopListItemOnClickListener shopListItemOnClickListener;
    private CommodityListItemOnClickListener commodityListItemOnClickListener;
    public SearchResultAdapter(SearchResultActivity activity, List<T> dataLists) {
        this.activity = activity;
        topicListItemOnClickListener = new TopicListItemModel(activity);
        shopListItemOnClickListener = new ShopListItemModel(activity);
        commodityListItemOnClickListener = new CommodityListItemModel(activity);
        if (dataLists != null && dataLists.size() > 0) {
            mDataLists.clear();
            mDataLists.addAll(dataLists);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (STATE == TOPIC) {
            return TOPIC;
        } else if (STATE == SHOP) {
            return SHOP;
        } else if (STATE == COMMODITY) {
            return COMMODITY;
        } else {
            return TOPIC;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case TOPIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ucg_recommend_topic_list, parent, false);
                viewHolder = new TopicViewHolder(view, parent.getContext());
                break;
            case SHOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_list, parent, false);
                viewHolder = new ShopViewHolder(parent.getContext(), view);
                break;
            case COMMODITY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
                viewHolder = new CommodityViewHolder(parent.getContext(), view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TOPIC:
                TopicViewHolder topicViewHolder = (TopicViewHolder) holder;
                topicViewHolder.setData(position);
                break;
            case SHOP:
                ShopViewHolder shopViewHolder = (ShopViewHolder) holder;
                shopViewHolder.setData(position);
                break;
            case COMMODITY:
                CommodityViewHolder commodityViewHolder = (CommodityViewHolder) holder;
                commodityViewHolder.setData(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataLists.size();
    }

    private class TopicViewHolder extends ViewHolder {
        private TopicViewHolder(View itemView, Context context) {
            super(context, itemView);
        }

        public void setData(int position) {
            T t = mDataLists.get(position);
            if (t instanceof TopicList.TopicsListBean) {
                TopicList.TopicsListBean data = (TopicList.TopicsListBean) t;

                Drawable drawable = activity.getResources().getDrawable(R.drawable.collect_select);
                drawable.setBounds(0, 0, UiUtils.dip2px(18), UiUtils.dip2px(18));
                CheckBox cb_collect = getView(R.id.cb_collect);
                cb_collect.setCompoundDrawables(drawable, null, null, null);

                setImageGlide(R.id.iv_icon, data.getImageUrl());
                setText(R.id.tv_info, data.getContent());
                setImageError(R.id.civ_head_icon, data.getUserImage());
                setText(R.id.tv_user_name, data.getUserName());
                if (data.getIsPrase() == 1) {
                    cb_collect.setChecked(true);
                } else {
                    cb_collect.setChecked(false);
                }

                cb_collect.setText(data.getParseCount() + "");

                setListener(data);
            }
        }

        private void setListener(final TopicList.TopicsListBean data) {
            setOnClickListener(rl_recommend_topic, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topicListItemOnClickListener.detailsOnClick(data.getId());
                }
            });

            setOnClickListener(R.id.civ_head_icon, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topicListItemOnClickListener.userTopicOnClick(data.getEncryptUserId());
                }
            });

            setOnClickListener(R.id.cb_collect, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topicListItemOnClickListener.topicLikeOnClick(v, data);
                }
            });
        }
    }

    private class ShopViewHolder extends ViewHolder {

        private Context context;
        private ShopViewHolder(Context context, View itemView) {
            super(context, itemView);
            this.context = context;
        }

        public void setData(int position) {
            T t = mDataLists.get(position);
            if (t instanceof ShopListData.DataBean) {
                ShopListData.DataBean data = (ShopListData.DataBean) t;

                setImageGlideCircular(R.id.iv_shop_img, data.getImageUrl());
                setText(R.id.tv_shop_name, data.getShopName());

                if (data.getArea() == null || data.getArea().isEmpty()) {
                    setText(R.id.tv_shop_county, context.getString(R.string.rest));
                } else {
                    setText(R.id.tv_shop_county, data.getArea());
                }

                if (data.getDescription() == null || data.getDescription().isEmpty()) {
                    setText(R.id.tv_shop_detail, context.getString(R.string.shop_info_hint));
                } else {
                    setText(R.id.tv_shop_detail, data.getDescription());
                }

                setListener(data);
            }
        }

        private void setListener(final ShopListData.DataBean data) {
            setOnClickListener(R.id.rl_shop_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopListItemOnClickListener.onItemClick(data.getId());
                }
            });
        }

    }


    private class CommodityViewHolder extends ViewHolder {

        private CommodityViewHolder(Context context, View itemView) {
            super(context, itemView);
        }

        public void setData(int position) {
            T t = mDataLists.get(position);
            if (t instanceof SearchCommodityBean.DataBean.ProductsBean) {
                SearchCommodityBean.DataBean.ProductsBean data = (SearchCommodityBean.DataBean.ProductsBean) t;
                setImageGlide(R.id.iv_product_icon, data.getImagePath());
                setText(R.id.tv_product_name, data.getProductName());
                setText(R.id.tv_product_price, MyApplication.getContext().getResources().getString(R.string.money_type)+Utils.doubleSave2(data.getMinSalePrice()));
                setImageGlide(R.id.iv_address, data.getCountryLogo());

                setListener(data);
            }

        }

        private void setListener(final SearchCommodityBean.DataBean.ProductsBean data) {
            setOnClickListener(R.id.rl_subjectproduct, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commodityListItemOnClickListener.onItemClick(data.getId());
                }
            });
        }
    }


    public void cleanData() {
        mDataLists.clear();
        notifyDataSetChanged();
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {
        mDataLists.addAll(position, datas);
        notifyDataSetChanged();
    }
}
