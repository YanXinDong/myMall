package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 * 退款原因
 */
public class RefundReasonBean {

    /**
     * Belong : RefundCash
     * Id : 1
     * ReasonDesc : 缺货
     * ReasonId : 1
     * ReasonName : LackGoods
     * SellerBear : true
     */

    private List<ReasonBean> Reason;

    public List<ReasonBean> getReason() {
        return Reason;
    }

    public void setReason(List<ReasonBean> Reason) {
        this.Reason = Reason;
    }

    public static class ReasonBean {
        private String Belong;
        private int Id;
        private String ReasonDesc;
        private int ReasonId;
        private String ReasonName;
        private boolean SellerBear;

        public String getBelong() {
            return Belong;
        }

        public void setBelong(String Belong) {
            this.Belong = Belong;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getReasonDesc() {
            return ReasonDesc;
        }

        public void setReasonDesc(String ReasonDesc) {
            this.ReasonDesc = ReasonDesc;
        }

        public int getReasonId() {
            return ReasonId;
        }

        public void setReasonId(int ReasonId) {
            this.ReasonId = ReasonId;
        }

        public String getReasonName() {
            return ReasonName;
        }

        public void setReasonName(String ReasonName) {
            this.ReasonName = ReasonName;
        }

        public boolean isSellerBear() {
            return SellerBear;
        }

        public void setSellerBear(boolean SellerBear) {
            this.SellerBear = SellerBear;
        }
    }
}
