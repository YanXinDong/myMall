package com.BFMe.BFMBuyer.javaBean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/13.
 */
public class GoPayBean {

    /**
     * appid : wx495bc67e6e0acd3e
     * noncestr : 6EA9AB1BAA0EFB9E19094440C317E21B
     * package : Sign=WXPay
     * partnerid : 1339696001
     * prepayid : wx20160913142228a200c023ab0865441977
     * sign : E410971C4B279793DD6D932B81E03895
     * timestamp : 1473747749
     */

    private PayInfoBean PayInfo;

    public PayInfoBean getPayInfo() {
        return PayInfo;
    }

    public void setPayInfo(PayInfoBean PayInfo) {
        this.PayInfo = PayInfo;
    }

    public static class PayInfoBean {
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
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

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
