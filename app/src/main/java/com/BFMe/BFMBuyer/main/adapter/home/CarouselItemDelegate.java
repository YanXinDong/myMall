package com.BFMe.BFMBuyer.main.adapter.home;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.BaseViewPagerAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.main.bean.HomeData;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.AutoCarouselViewPager;

import java.util.ArrayList;
import java.util.List;

import static com.BFMe.BFMBuyer.R.id.tv_number;

/**
 * Description：首页轮播图
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/15 11:26
 */
public class CarouselItemDelegate implements ItemViewDelegate<BaseTypeBean> {

    private BaseViewPagerAdapter adapter;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_viewpager;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("Carousel");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        AutoCarouselViewPager viewPager = holder.getView(R.id.vp_image);
        final List<HomeData.HomeDataBean.TopBannerBean> topBanner = (List<HomeData.HomeDataBean.TopBannerBean>) baseTypeBean.getData();
        final List<String> imageUrl = new ArrayList<>();

        for (int i = 0; i < topBanner.size(); i++) {
            imageUrl.add(topBanner.get(i).getImageUrl());
        }

        if (adapter == null && imageUrl.size() > 0) {//防止重复创建
            Logger.e("11111111111111");
            if (imageUrl.size() > 0) {
                holder.setText(tv_number, 1 + "/" + imageUrl.size());
            }
            adapter = new BaseViewPagerAdapter(imageUrl);
            adapter.setCarousel(true);//设置为无限翻页
            viewPager.setAdapter(adapter);
            viewPager.setOnPagerChangeListener(new AutoCarouselViewPager.OnPagerChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    holder.setText(tv_number, position % imageUrl.size() + 1 + "/" + imageUrl.size());
                }
            });

            adapter.setOnItemClickListener(new BaseViewPagerAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    //如果url最后一个“/”后是数字 则这个数字就是店铺id
                    int currentItem = position % imageUrl.size();
                    HomeData.HomeDataBean.TopBannerBean data = topBanner.get(currentItem);
                    int type = data.getType();//1:专题   2：话题
                    String url = data.getUrl();
                    bannerOnItemClickListener.itemOnClick(type,url,imageUrl.get(currentItem));
                }
            });
        }
    }

    private BannerOnItemClickListener bannerOnItemClickListener;
    public interface BannerOnItemClickListener{
        void itemOnClick(int type,String url,String imageUrl);
    }

    public void setBannerOnItemClickListener(BannerOnItemClickListener bannerOnItemClickListener) {
        this.bannerOnItemClickListener = bannerOnItemClickListener;
    }
}
