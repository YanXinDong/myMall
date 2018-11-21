package com.BFMe.BFMBuyer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Description：重写RecyclerView点击事件
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/3/13 14:42
 */
public class MyRecyclerView extends RecyclerView{
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int lastX = 0;
    private int lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取当前触摸的绝对坐标
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 上一次离开时的坐标
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                // 两次的偏移量
                int offsetX = rawX - lastX;
                int offsetY = rawY - lastY;
                if(Math.abs(offsetX) < 10 && Math.abs(offsetY) < 10) {
                    onBannerClickListener.onBannerClick();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private OnBannerClickListener onBannerClickListener;
    public interface OnBannerClickListener{
        void onBannerClick();
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
