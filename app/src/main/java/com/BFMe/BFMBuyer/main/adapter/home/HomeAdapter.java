package com.BFMe.BFMBuyer.main.adapter.home;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

import java.util.List;

/**
 * Description：首页adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/14 18:55
 */
public class HomeAdapter extends MultiItemTypeAdapter<BaseTypeBean> {

    private List<BaseTypeBean> mDatas;

    public HomeAdapter(Context context, List<BaseTypeBean> datas) {
        super(context, datas);
        mDatas = datas;
        CarouselItemDelegate carouselItemDelegate = new CarouselItemDelegate();
        HotTopicItemDelegate hotTopicItemDelegate = new HotTopicItemDelegate();
        AllTopicIItemDelegate allTopicIItemDelegate = new AllTopicIItemDelegate();
        AllLimitedItemDelegate allLimitedItemDelegate = new AllLimitedItemDelegate();
        LimitedItemDelegate limitedItemDelegate = new LimitedItemDelegate();
        BannerItemDelegate bannerItemDelegate = new BannerItemDelegate();
        HotProductsTitleItemDelegate hotProductsTitleItemDelegate = new HotProductsTitleItemDelegate();
        HotProductsItemDelegate hotProductsItemDelegate = new HotProductsItemDelegate();
        AllLimitedItemDelegate2 allLimitedItemDelegate2 = new AllLimitedItemDelegate2();


        addItemViewDelegate(10, carouselItemDelegate);
        addItemViewDelegate(20, hotTopicItemDelegate);
        addItemViewDelegate(30, allTopicIItemDelegate);
        addItemViewDelegate(40, allLimitedItemDelegate);
        addItemViewDelegate(50, limitedItemDelegate);
        addItemViewDelegate(55, allLimitedItemDelegate2);
        addItemViewDelegate(60, bannerItemDelegate);
        addItemViewDelegate(80, hotProductsTitleItemDelegate);
        addItemViewDelegate(70, hotProductsItemDelegate);

        setListeners(carouselItemDelegate,
                hotTopicItemDelegate,
                allTopicIItemDelegate,
                limitedItemDelegate,
                bannerItemDelegate,
                hotProductsTitleItemDelegate,
                hotProductsItemDelegate,allLimitedItemDelegate2);
    }

    private void setListeners(CarouselItemDelegate carouselItemDelegate,
                              final HotTopicItemDelegate hotTopicItemDelegate,
                              AllTopicIItemDelegate allTopicIItemDelegate,
                              LimitedItemDelegate limitedItemDelegate,
                              BannerItemDelegate bannerItemDelegate,
                              HotProductsTitleItemDelegate hotProductsTitleItemDelegate,
                              HotProductsItemDelegate hotProductsItemDelegate, AllLimitedItemDelegate2 allLimitedItemDelegate2) {
        carouselItemDelegate.setBannerOnItemClickListener(new CarouselItemDelegate.BannerOnItemClickListener() {
            @Override
            public void itemOnClick(int type, String url,String imageUrl) {
                homeItemOnClickListener.bannerItemOnClick(type, url,imageUrl);
            }
        });

        hotTopicItemDelegate.setTopicCategoryOnItemClickListener(new HotTopicItemDelegate.TopicCategoryOnItemClickListener() {
            @Override
            public void onItemClick(String topicId) {
                homeItemOnClickListener.topicCategoryOnItemClick(topicId);
            }
        });

        allTopicIItemDelegate.setAllTopicCategoryOnClickListener(new AllTopicIItemDelegate.AllTopicCategoryOnClickListener() {
            @Override
            public void allTopicOnClick() {
                homeItemOnClickListener.allTopicOnClick();
            }
        });

        limitedItemDelegate.setLimitedItemOnClickListener(new LimitedItemDelegate.LimitedItemOnClickListener() {
            @Override
            public void onLimitedItemClick(String productId) {
                homeItemOnClickListener.onLimitedItemClick(productId);
            }
        });
        bannerItemDelegate.setBannerOnItemClickListener(new BannerItemDelegate.BannerOnItemClickListener() {
            @Override
            public void itemOnClick(int type, String url,String imageUrl) {
                homeItemOnClickListener.bannerItemOnClick(type, url,imageUrl);
            }
        });

        hotProductsTitleItemDelegate.setAllProductsOnClickListener(new HotProductsTitleItemDelegate.AllProductsOnClickListener() {
            @Override
            public void allProductsOnClick() {
                homeItemOnClickListener.allProductsOnClick();
            }
        });
        hotProductsItemDelegate.setHotProductsItemOnClickListener(new HotProductsItemDelegate.HotProductsItemOnClickListener() {
            @Override
            public void onHotProductsItemClick(String productId) {
                homeItemOnClickListener.onHotProductsItemClick(productId);
            }
        });

        allLimitedItemDelegate2.setAllLimitedOnClickListener(new AllLimitedItemDelegate2.AllLimitedOnClickListener2() {
            @Override
            public void allLimitedOnClick() {
                homeItemOnClickListener.allLimitedOnClick();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = 1;
                    if (position <= mDatas.size() && position != 0) {
                        itemViewType = getItemViewType(position - 1);
                    }
                    switch (itemViewType) {
                        case 10:
                        case 30:
                        case 40:
                        case 55:
                        case 60:
                        case 80:
                            return gridManager.getSpanCount();
                        case 20:
                        case 50:
                        case 70:
                            return 1;
                        default:
                            return gridManager.getSpanCount();
                    }
                }
            });
        }

    }

    private HomeItemOnClickListener homeItemOnClickListener;

    public void cleanData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void addData(List<BaseTypeBean> data) {
        addData(0,data);
    }
    public void addData(int position,List<BaseTypeBean> data) {
        if (data != null && data.size() > 0) {
            mDatas.addAll(position,data);
//            notifyDataSetChanged();
            notifyItemRangeInserted(position+1,data.size());
        }
    }

    public interface HomeItemOnClickListener {
        void bannerItemOnClick(int type, String url,String imageUrl);

        void topicCategoryOnItemClick(String topicCategoryId);

        void allTopicOnClick();

        void allLimitedOnClick();

        void onLimitedItemClick(String productId);

        void allProductsOnClick();

        void onHotProductsItemClick(String productId);
    }

    public void setHomeItemOnClickListener(HomeItemOnClickListener homeItemOnClickListener) {
        this.homeItemOnClickListener = homeItemOnClickListener;
    }
}
