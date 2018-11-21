package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 * 订单内容的bean类
 */
public class OrdersBean {

    private int PlatformType;//平台类型
    private List<OrdersData> Orders;//订单列表
    private long RecieveAddressId;//收货地址
    private String UserId;//用户id
    private long Intergal;//积分数量
    private String LimitId ;

    public String getLimitId() {
        return LimitId;
    }

    public void setLimitId(String limitId) {
        LimitId = limitId;
    }

    public int getPlatformType() {
        return PlatformType;
    }

    public void setPlatformType(int platformType) {
        PlatformType = platformType;
    }

    public List<OrdersData> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrdersData> orders) {
        Orders = orders;
    }

    public long getRecieveAddressId() {
        return RecieveAddressId;
    }

    public void setRecieveAddressId(long recieveAddressId) {
        RecieveAddressId = recieveAddressId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public long getIntergal() {
        return Intergal;
    }

    public void setIntergal(long intergal) {
        Intergal = intergal;
    }

    @Override
    public String toString() {
        return "OrdersBean{" +
                "PlatformType=" + PlatformType +
                ", Orders=" + Orders +
                ", RecieveAddressId=" + RecieveAddressId +
                ", UserId='" + UserId + '\'' +
                ", Intergal=" + Intergal +
                '}';
    }

    public static class OrdersData{
        private int [] CartItemIds;//购物车id
        private String Remark;//备注

        public int[] getCartItemIds() {
            return CartItemIds;
        }

        public void setCartItemIds(int[] cartItemIds) {
            CartItemIds = cartItemIds;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        @Override
        public String toString() {
            return "OrdersData{" +
                    "CartItemIds=" + CartItemIds +
                    ", Remark='" + Remark + '\'' +
                    '}';
        }
    }


}
