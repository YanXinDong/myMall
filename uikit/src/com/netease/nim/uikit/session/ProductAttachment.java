package com.netease.nim.uikit.session;

import com.netease.nimlib.r.c;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONObject;

/**
 * Created by BFMe.miao on 2018/3/13.
 */

public class ProductAttachment implements MsgAttachment {
    private String img;
    private String des;
    private String price;

    public ProductAttachment(String img, String des, String price) {
        this.img = img;
        this.des = des;
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public void fromJson(String var1) {
        JSONObject var2 = c.a(var1);
        this.img = c.e(var2, "img");
        this.des = c.e(var2, "des");
        this.price = c.e(var2, "price");
    }

    public String toJson(boolean var1) {
        JSONObject var3 = new JSONObject();

        try {
            var3.put("img", this.img);
            var3.put("des", this.des);
            var3.put("price", this.price);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return var3.toString();
    }
}
