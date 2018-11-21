package com.BFMe.BFMBuyer.commodity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.SectionAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.commodity.activity.CommodityFiltrateActivity;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/18 13:22
 */
public class CommodityFiltrateAdapter extends SectionAdapter {
    private LayoutInflater mLayoutInflater;
    private List<String> title;
    private SearchCommodityBean.DataBean.FacetResultsBean mData;
    private CommodityFiltrateActivity mActivity;

    public CommodityFiltrateAdapter(CommodityFiltrateActivity activity, SearchCommodityBean.DataBean.FacetResultsBean data) {
        mActivity = activity;
        mData = data;
        title = new ArrayList<>();
        title.add(mActivity.getString(R.string.brand));
        title.add(mActivity.getString(R.string.country_region));
        title.add(mActivity.getString(R.string.classify));
        mLayoutInflater = LayoutInflater.from(mActivity);

        setupPosition();//调用父类方法从新计算数据
    }

    @Override
    protected boolean hasHeader() {
        return true;
    }

    @Override
    protected int getSectionCount() {
        return title.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int itemCountSize = 0;
        switch (section) {
            case 0:
                itemCountSize = mData.getBnf().size();
                break;
            case 1:
                itemCountSize = mData.getCtryf().size();
                break;
            case 2:
                itemCountSize = mData.getTcnf().size();
                break;

        }
        return itemCountSize;
    }

    @Override
    protected ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_commodity_filtrate_head, parent, false);
        return new ViewHolder(parent.getContext(), inflate);
    }

    @Override
    protected ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_shop_filtrate_head, parent, false);
        return new ViewHolder(parent.getContext(), inflate);
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_shop_filtrate_body, parent, false);
        return new ViewHolder(parent.getContext(), inflate);
    }

    @Override
    protected void onBindHeaderViewHolder(final ViewHolder holder) {

        mActivity.setGetPrices(new CommodityFiltrateActivity.GetPrices() {
            @Override
            public Map<String, String> getPrice() {
                String maxPrice = holder.getText(R.id.et_max_price);
                String minPrice = holder.getText(R.id.et_min_price);
                Map<String,String> price = new HashMap<>();
                price.put("maxPrice",maxPrice);
                price.put("minPrice",minPrice);

                return price;
            }
        });
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ViewHolder holder, int section) {
//        if (section == 0) {
//            holder.setVisible(R.id.look_all, true);
//        } else {
//            holder.setVisible(R.id.look_all, false);
//        }
        holder.setText(R.id.tv_title, title.get(section));
    }

    @Override
    protected void onBindItemViewHolder(final ViewHolder holder, final int section, final int position) {

        switch (section) {
            case 0:
                SearchCommodityBean.DataBean.FacetResultsBean.BnfBean bnf = mData.getBnf().get(position);
                bindView(holder, bnf.getName(), bnf.isSelected());
                break;
            case 1:
                SearchCommodityBean.DataBean.FacetResultsBean.CtryfBean ctryf = mData.getCtryf().get(position);
                bindView(holder, ctryf.getName(), ctryf.isSelected());
                break;
            case 2:
                SearchCommodityBean.DataBean.FacetResultsBean.TcnfBean tcnf = mData.getTcnf().get(position);
                bindView(holder, tcnf.getName(), tcnf.isSelected());
                break;
        }

        holder.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterSelected(section,position);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    /**
     * 改变筛选条件的选中状态
     * @param section
     * @param position
     */
    private void alterSelected(int section, int position) {
        switch (section) {
            case 0:
                SearchCommodityBean.DataBean.FacetResultsBean.BnfBean bnf = mData.getBnf().get(position);
                if (bnf.isSelected()) {
                    bnf.setSelected(false);
                } else {
                    if (validationMaxSelected()) {
                        Toast.makeText(mActivity, "最多五个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bnf.setSelected(true);
                }

                break;
            case 1:
                SearchCommodityBean.DataBean.FacetResultsBean.CtryfBean ctryf = mData.getCtryf().get(position);
                if (ctryf.isSelected()) {
                    ctryf.setSelected(false);
                } else {
                    if (validationMaxSelected()) {
                        Toast.makeText(mActivity, "最多五个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ctryf.setSelected(true);
                }
                break;
            case 2:
                SearchCommodityBean.DataBean.FacetResultsBean.TcnfBean tcnf = mData.getTcnf().get(position);
                if (tcnf.isSelected()) {
                    tcnf.setSelected(false);
                } else {
                    if (validationMaxSelected()) {
                        Toast.makeText(mActivity, "最多五个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tcnf.setSelected(true);
                }
                break;
        }
    }

    private boolean validationMaxSelected() {
        List<SearchCommodityBean.DataBean.FacetResultsBean.BnfBean> bnf = mData.getBnf();
        List<SearchCommodityBean.DataBean.FacetResultsBean.CtryfBean> ctryf = mData.getCtryf();
        List<SearchCommodityBean.DataBean.FacetResultsBean.TcnfBean> tcnf = mData.getTcnf();

        int number = 0;

        for(int i = 0; i < bnf.size(); i++) {
            if (bnf.get(i).isSelected()) {
                number++;
            }
        }
        for(int i = 0; i < ctryf.size(); i++) {
            if (ctryf.get(i).isSelected()) {
                number++;
            }
        }
        for(int i = 0; i < tcnf.size(); i++) {
            if (tcnf.get(i).isSelected()) {
                number++;
            }
        }


        return number >= 5;
    }

    private void bindView(ViewHolder holder, String name, boolean isSelected) {
        holder.setText(R.id.tv_content, name);
        if (isSelected) {
            holder.setBackgroundRes(R.id.tv_content, R.color.blue_CDDFFB);
        } else {
            holder.setBackgroundRes(R.id.tv_content, R.color.translucence_gray_fafafafa);
        }
    }

}
