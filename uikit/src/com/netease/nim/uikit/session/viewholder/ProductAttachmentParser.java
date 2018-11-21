package com.netease.nim.uikit.session.viewholder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.session.ProductAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by BFMe.miao on 2018/3/14.
 */

public class ProductAttachmentParser  implements MsgAttachmentParser {
    @Override
    public MsgAttachment parse(String json) {
        ProductAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            String img = object.getString("img");
            String des = object.getString("des");
            String price = object.getString("price");
            attachment = new ProductAttachment(img,des,price);

            if (attachment != null) {
                attachment.fromJson(object.toString());
            }
        } catch (Exception e) {

        }
        return attachment;
    }
}
