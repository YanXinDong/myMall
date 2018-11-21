package com.BFMe.BFMBuyer.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Description：粘性嵌套滚动
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/3/16 14:49
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　  　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class StickyNavLayout extends FrameLayout implements NestedScrollingParent {

    public StickyNavLayout(Context context) {
        this(context, null);
    }

    public StickyNavLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    private Scroller scroller;
    private View foldView, rollView;
    private int foldWidth, foldHeight, rollHeight;

    //1). 重写onFinishInflate(): 得到menu子View
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        foldView = getChildAt(0);
        rollView = getChildAt(1);
    }


    //2). 重写onMeasure(), 得到其子View的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        foldWidth = foldView.getMeasuredWidth();
        foldHeight = foldView.getMeasuredHeight();
        rollHeight = rollView.getMeasuredHeight();
    }


    //3). 重写onLayout(), 对子View进行重新布局
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.e("TAG", "foldWidth==" + foldWidth);
        Log.e("TAG", "foldHeight==" + foldHeight);
        Log.e("TAG", "rollHeight==" + rollHeight);

        rollView.layout(0, foldHeight, foldWidth, rollHeight + foldHeight);
    }

    private int lastX, downX, downY, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastX = eventX;
                downY = lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = eventY - lastY;
                //滚动视图
                int toScrollY = getScrollY() - dy;
                //限制范围[0,menuWidth]
                if (toScrollY < 0) {
                    toScrollY = 0;
                } else if (toScrollY > rollHeight) {
                    toScrollY = rollHeight;
                }

                //整体滚动以达到下部view覆盖上部view，同时上部view缓缓滚动
                //父布局整体正常滚动
                scrollTo(0, toScrollY);

                lastX = eventX;
                lastY = eventY;
                break;
            case MotionEvent.ACTION_UP:
                //得到总的偏移量
                int totalScrollY = getScrollY();
                if (totalScrollY < foldHeight / 2) {//当滚动的偏移量小于需要折叠view的1/3时
                    openMenu();//不折叠
                } else {
                    closeMenu();//折叠
                }
                break;
        }

        return true;
    }

    /**
     * 平滑打开折叠view -->(menuWidth,0)
     */
    public void openMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), 0, -getScrollY(),500);
        invalidate();

        if (onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }

    /**
     * 平滑关闭折叠view --->(0, 0)
     */
    public void closeMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), 0, foldHeight - getScrollY(),1000);
        invalidate();

        if (onStateChangeListener != null) {
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
        }
    }

    //----------------------------滑动相关--------------------------------

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < foldHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= foldHeight) return false;
        fling((int) velocityY);
        return true;
    }

    public void fling(int velocityY) {
        Log.e("TAG", "velocity=="+velocityY);
        scroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, foldHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > foldHeight) {
            y = foldHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
        //被嵌套的只滚动父类的一半 (因为父布局滚动时是带动子view一起滚动的，因此要减去一半的偏移量，以达到目的)
        foldView.scrollTo(0, - y / 2);
    }

    private int locationT;//view偏移量(0,490)
    private boolean isFlip = false;

    /**
     * 滚动监听  做一些你想做的事
     * @param l 变化后的X轴位置
     * @param t 变化后的Y轴的位置
     * @param oldl 原先的X轴的位置
     * @param oldt 原先的Y轴的位置
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        locationT = t;

    }

    /**
     * 利用此方法监听 子view滑动完成
     * @param child
     */
    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        Log.e("TAG", "locationT=="+locationT);

        if(isFlip) {
            if(locationT >= foldHeight || locationT <= 0) {
                return;
            }else if(locationT >= foldHeight / 2) {
                closeMenu();//折叠
            }else{
                openMenu();//不折叠
            }
            Log.e("TAG", "isFlip=="+isFlip);
            isFlip = false;
        }else {
            isFlip = true;
        }
    }

    //----------------------------------结束--------------------------------


    public interface OnStateChangeListener {
        public void onOpen(StickyNavLayout layout); //当layout打开时调用

        public void onClose(StickyNavLayout layout);//当layout关闭时调用

        public void onDown(StickyNavLayout layout); //当layout被按下时调用

    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
}
