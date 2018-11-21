package com.BFMe.BFMBuyer.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/14 16:55
 */
public class BaseViewPagerAdapter extends PagerAdapter {
    private List<String> mImageUrl = new ArrayList<>();
    private boolean isCarousel;
    private boolean mIsUGC;

    public BaseViewPagerAdapter(List<String> imageUrl) {
        if (imageUrl != null && imageUrl.size() > 0) {
            mImageUrl.clear();
            mImageUrl.addAll(imageUrl);
        }
    }

    public BaseViewPagerAdapter(List<String> imageUrl, boolean isUGC) {
        if (imageUrl != null && imageUrl.size() > 0) {
            mImageUrl.clear();
            mImageUrl.addAll(imageUrl);
        }

        mIsUGC = isUGC;
    }

    public void setCarousel(boolean carousel) {
        isCarousel = carousel;
    }

    @Override
    public int getCount() {
        return isCarousel ? Integer.MAX_VALUE : mImageUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        if (mIsUGC) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent viewParent = imageView.getParent();
        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent;
            parent.removeView(imageView);
        }
        if (mImageUrl.size() > 0) {

            if (mIsUGC) {
                Glide
                        .with(container.getContext())
                        .load(mImageUrl.get(position % mImageUrl.size()))
                        .fitCenter()
                        .dontTransform()
                        .into(imageView);
            } else {
                Glide
                        .with(container.getContext())
                        .load(mImageUrl.get(position % mImageUrl.size()))
                        .centerCrop()
                        .dontTransform()
                        .into(imageView);
            }

        }

        container.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position % mImageUrl.size());
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
