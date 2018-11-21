package com.BFMe.BFMBuyer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Description：重写ImageView实现 圆角
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/3 12:41
 */

public class RoundCornerImageView extends ImageView {

    public RoundCornerImageView(Context context) {
        super(context);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int height = this.getHeight();
        int width = this.getWidth();
        path.addRoundRect(new RectF(0, 0, width, height), 20.0f, 20.0f, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}
