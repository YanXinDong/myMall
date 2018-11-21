package com.BFMe.BFMBuyer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;


/**
 * Description：圆角imageview
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/12/8 11:18
 * <p>
 * ************************************************
 * **                  _oo0oo_                  **
 * **                 o8888888o                 **
 * **                 88" . "88                 **
 * **                 (| -_- |)                 **
 * **                 0\  =  /0                 **
 * **               ___/'---'\___               **
 * **            .' \\\|     |// '.             **
 * **           / \\\|||  :  |||// \\           **
 * **          / _ ||||| -:- |||||- \\          **
 * **          | |  \\\\  -  /// |   |          **
 * **          | \_|  ''\---/''  |_/ |          **
 * **          \  .-\__  '-'  __/-.  /          **
 * **        ___'. .'  /--.--\  '. .'___        **
 * **     ."" '<  '.___\_<|>_/___.' >'  "".     **
 * **    | | : '-  \'.;'\ _ /';.'/ - ' : | |    **
 * **    \  \ '_.   \_ __\ /__ _/   .-' /  /    **
 * **====='-.____'.___ \_____/___.-'____.-'=====**
 * **                  '=---='                  **
 * ***********************************************
 * **              佛祖保佑  镇类之宝             **
 * ***********************************************
 * 佛曰:
         写字楼里写字间，写字间里程序员；
         程序人员写程序，又拿程序换酒钱。
         酒醒只在网上坐，酒醉还来网下眠；
         酒醉酒醒日复日，网上网下年复年。
         但愿老死电脑间，不愿鞠躬老板前；
         奔驰宝马贵者趣，公交自行程序员。
         别人笑我忒疯癫，我笑自己命太贱；
         不见满街漂亮妹，哪个归得程序员？
 */
public class CircularImageView extends ImageView {

    public CircularImageView(Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        inital(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;

    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半径
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;
    private RectF mRoundRect;

    private void inital(Context context, AttributeSet attrs) {
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();

        //无锯齿
        mBitmapPaint.setAntiAlias(true);

        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView);

        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.CircularImageView_borderRadius, dp2px(BODER_RADIUS_DEFAULT));// 默认为10dp

        type = a.getInt(R.styleable.CircularImageView_type, TYPE_CIRCLE);// 默认为Circle

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            //取宽高的最小值
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            //设置测量过的宽高
            setMeasuredDimension(mWidth, mWidth);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas); 不可以写这个  会导致绘制出错
        if (getDrawable() == null) {
            return;
        }
        setUpShader();

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }
    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        //将drawable转换为bitmap
        Bitmap bmp = drawable2Bitmap(drawable);

        if (bmp == null) {
            return;
        }

        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;//比例
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        float dx = 0, dy = 0;

        if (type == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmpWidth, bmpHeight);
            scale = mWidth * 1.0f / bSize;

        } else if (type == TYPE_ROUND) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；
            // 缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(viewWidth * 1.0f / bmpWidth, viewHeight
                    * 1.0f / bmpHeight);
        }

        if (bmpWidth * viewHeight > viewWidth * bmpHeight) {
            dx = (viewWidth - bmpWidth * scale) * 0.5f;
        } else {
            dy = (viewHeight - bmpHeight * scale) * 0.5f;
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }
    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
//        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
    }


    /**
     * --------------------------状态的存储与恢复------------------
     * 当然了，如果内存不足，而恰好我们的Activity置于后台，不幸被重启，或者用户旋转屏幕造成Activity重启，
     * 我们的View应该也能尽可能的去保存自己的属性。
     * 状态保存什么用处呢？
     * 比如，现在一个的圆角大小是10dp，用户点击后变成50dp；
     * 当用户旋转以后，或者长时间置于后台以后，返回我们的Activity应该还是50dp；
     * 我们简单的存储一下，当前的type以及mBorderRadius
     */
    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }
    }
    //-------------------------------------------------------------------------

    //对外公布了两个方法，用于动态修改圆角大小和type
    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
