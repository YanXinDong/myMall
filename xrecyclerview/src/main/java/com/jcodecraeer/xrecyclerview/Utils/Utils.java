package com.jcodecraeer.xrecyclerview.Utils;

import android.content.Context;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/14 16:10
 */
public class Utils {
    /**
     * dp到px
     * @param dp
     * @return
     */
    public static int dip2px(int dp,Context context) {
        return (int) (context.getResources().getDisplayMetrics().density
                * dp + 0.5);
    }

    /**
     * px到Dp
     * @param px
     * @return
     */
    public static int px2dp(int px,Context context) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
