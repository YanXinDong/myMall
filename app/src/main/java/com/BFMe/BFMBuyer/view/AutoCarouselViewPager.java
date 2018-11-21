package com.BFMe.BFMBuyer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.BFMe.BFMBuyer.utils.logger.Logger;

/**
 * Description：自动轮播的ViewPager
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/1 16:20
 */
public class AutoCarouselViewPager extends ViewPager {

    private long mDelayMillis = 3000;//自动轮播间隔
    private boolean isDrag;//判断是否处于拖曳状态

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //handler收到 消息切换到下一个页面
            setCurrentItem(getCurrentItem() + 1);
            //继续发送延迟消息，形成递归
            handler.sendEmptyMessageDelayed(0, mDelayMillis);
        }
    };

    public AutoCarouselViewPager(Context context) {
        this(context, null);
    }

    public AutoCarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        Logger.e("AutoCarouselViewPager()");
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(0, mDelayMillis);
        }

        //添加实现后的页面滚动监听
        addOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 当view销毁时移除所有消息
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //先移除所有消息
        handler.removeCallbacksAndMessages(null);
        if (hasWindowFocus) {
            handler.sendEmptyMessageDelayed(0, mDelayMillis);
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            onPagerChangeListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                //移除消息
                handler.removeCallbacksAndMessages(null);
                isDrag = true;
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (isDrag) {
                    handler.sendEmptyMessageDelayed(1, mDelayMillis);
                    isDrag = false;
                }
            }
        }
    }


    /**
     * 暴露给外部一个接口，以方便改变指示器的数据
     */
    private OnPagerChangeListener onPagerChangeListener;

    public void setOnPagerChangeListener(OnPagerChangeListener onPagerChangeListener) {
        this.onPagerChangeListener = onPagerChangeListener;
    }


    public interface OnPagerChangeListener {
        void onPageSelected(int position);
    }
}
