package com.BFMe.BFMBuyer.javaBean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PayBean {


    /**
     * appid : wx495bc67e6e0acd3e
     * partnerid : 1339696001
     * prepayid : wx20160906110916a8cf3c3ce30537230900
     * package : Sign=WXPay
     * noncestr : 4E0928DE075538C593FBDABB0C5EF2C3
     * timestamp : 1473131358
     * sign : F2FFB505121A79595FF1DCB055F9961A
     */

    private WxPayBean WxPay;
    /**
     * Data : [1100000000014306]
     * WxPay : {"appid":"wx495bc67e6e0acd3e","partnerid":"1339696001","prepayid":"wx20160906110916a8cf3c3ce30537230900","package":"Sign=WXPay","noncestr":"4E0928DE075538C593FBDABB0C5EF2C3","timestamp":"1473131358","sign":"F2FFB505121A79595FF1DCB055F9961A"}
     */

    private List<Long> Data;

    public WxPayBean getWxPay() {
        return WxPay;
    }

    public void setWxPay(WxPayBean WxPay) {
        this.WxPay = WxPay;
    }

    public List<Long> getData() {
        return Data;
    }

    public void setData(List<Long> Data) {
        this.Data = Data;
    }

    public static class WxPayBean {
        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
