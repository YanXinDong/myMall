package com.netease.nim.uikit.session.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.session.ProductAttachment;

/**
 * Created by BFMe.miao on 2018/3/13.
 */

public class MsgViewHolderProduct extends MsgViewHolderBase{

    private ImageView iv_product;
    private TextView tv_des;
    private TextView tv_price;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_product;
    }

    @Override
    protected void inflateContentView() {
        iv_product = (ImageView) view.findViewById(R.id.iv_product);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        tv_price = (TextView) view.findViewById(R.id.tv_price);


    }

    @Override
    protected void bindContentView() {
        ProductAttachment productAttachment = (ProductAttachment) message.getAttachment();
        tv_des.setText(productAttachment.getDes());
        tv_price.setText(productAttachment.getPrice());
        Glide.with(NimUIKit.getContext()).load(productAttachment.getImg()).error(R.drawable.nim_image_default).placeholder(R.drawable.nim_image_default).into(iv_product);

    }
}
