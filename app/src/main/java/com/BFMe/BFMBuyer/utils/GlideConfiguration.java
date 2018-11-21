package com.BFMe.BFMBuyer.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Description：解决Glide过度压缩图片导致背景图发青
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/19 12:42
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
