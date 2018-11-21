package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description
 * Copyright    Copyright (c) 2016
 * Company     白富美(北京)网络科技有限公司
 * Author       闫信董
 * Date       2016/8/24 17 //57
 */
public class OrderBean {

    /**
     * Id : 2000000000001480
     * Image : http://www.bfme.com//Storage/Shop/520/Products/1207/407260781eeaeaa1_100.jpg
     * OrderCommentInfo : 0
     * OrderDate : 2017-02-27 17:59:18
     * OrderStatus : Finish
     * OrderStatusDesc : 已完成
     * OrderTotalAmount : 70.0
     * OverDate : 2017-02-28 05:59:18
     * Price : 12.0
     * ProductCount : 4
     * ProductId : 1207
     * ProductList : [{"BuyNum":3,"CostPrice":11,"Image":"http://www.bfme.com//Storage/Shop/520/Products/1207/407260781eeaeaa1_100.jpg","IsLimit":false,"LimitEndDate":"","Price":11,"ProductId":1207,"ProductName":"郝艳同测试单SKU","SkuId":"1207_1_0_0"},{"BuyNum":1,"CostPrice":12,"Image":"http://www.bfme.com//Storage/Shop/520/Products/1207/407260781eeaeaa1_100.jpg","IsLimit":false,"LimitEndDate":"","Price":12,"ProductId":1207,"ProductName":"郝艳同测试单SKU","SkuId":"1207_3_0_0"}]
     * ProductName : 郝艳同测试单SKU
     * ShopId : 520
     * ShopName : 某宝28-店铺
     */

    private List<OrdersBean> Orders;

    public List<OrdersBean> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrdersBean> Orders) {
        this.Orders = Orders;
    }

    public static class OrdersBean {
        private boolean HasHSCode;
        private String Id;
        private String Image;
        private String OrderCommentInfo;
        private String OrderDate;
        private String OrderStatus;
        private String OrderStatusDesc;
        private String OrderTotalAmount;
        private String OverDate;
        private double Price;
        private int ProductCount;
        private int ProductId;
        private String ProductName;
        private String ShopId;
        private String ShopName;
        private boolean IsLaterWaitReceiving;


        /**
         * BuyNum : 3
         * CostPrice : 11.0
         * Image : http://www.bfme.com//Storage/Shop/520/Products/1207/407260781eeaeaa1_100.jpg
         * IsLimit : false
         * LimitEndDate :
         * Price : 11.0
         * ProductId : 1207
         * ProductName : 郝艳同测试单SKU
         * SkuId : 1207_1_0_0
         */

        private List<ProductListBean> ProductList;

        public boolean isHasHSCode() {
            return HasHSCode;
        }

        public void setHasHSCode(boolean hasHSCode) {
            HasHSCode = hasHSCode;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public String getOrderCommentInfo() {
            return OrderCommentInfo;
        }

        public void setOrderCommentInfo(String OrderCommentInfo) {
            this.OrderCommentInfo = OrderCommentInfo;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String OrderDate) {
            this.OrderDate = OrderDate;
        }

        public String getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(String OrderStatus) {
            this.OrderStatus = OrderStatus;
        }

        public String getOrderStatusDesc() {
            return OrderStatusDesc;
        }

        public void setOrderStatusDesc(String OrderStatusDesc) {
            this.OrderStatusDesc = OrderStatusDesc;
        }

        public String getOrderTotalAmount() {
            return OrderTotalAmount;
        }

        public void setOrderTotalAmount(String OrderTotalAmount) {
            this.OrderTotalAmount = OrderTotalAmount;
        }

        public String getOverDate() {
            return OverDate;
        }

        public void setOverDate(String OverDate) {
            this.OverDate = OverDate;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public int getProductCount() {
            return ProductCount;
        }

        public void setProductCount(int ProductCount) {
            this.ProductCount = ProductCount;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String ShopId) {
            this.ShopId = ShopId;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public List<ProductListBean> getProductList() {
            return ProductList;
        }

        public void setProductList(List<ProductListBean> ProductList) {
            this.ProductList = ProductList;
        }

        public boolean isLaterWaitReceiving() {
            return IsLaterWaitReceiving;
        }

        public void setLaterWaitReceiving(boolean laterWaitReceiving) {
            IsLaterWaitReceiving = laterWaitReceiving;
        }

        public static class ProductListBean {
            private int BuyNum;
            private double CostPrice;
            private String Image;
            private boolean IsHSCode;
            private boolean IsLimit;
            private String LimitEndDate;
            private double Price;
            private String ProductId;
            private String ProductName;
            private String SkuId;

            public int getBuyNum() {
                return BuyNum;
            }

            public void setBuyNum(int BuyNum) {
                this.BuyNum = BuyNum;
            }

            public double getCostPrice() {
                return CostPrice;
            }

            public void setCostPrice(double CostPrice) {
                this.CostPrice = CostPrice;
            }

            public String getImage() {
                return Image;
            }

            public void setImage(String Image) {
                this.Image = Image;
            }

            public boolean isHSCode() {
                return IsHSCode;
            }

            public void setHSCode(boolean HSCode) {
                IsHSCode = HSCode;
            }

            public boolean isLimit() {
                return IsLimit;
            }

            public void setLimit(boolean limit) {
                IsLimit = limit;
            }

            public boolean isIsLimit() {
                return IsLimit;
            }

            public void setIsLimit(boolean IsLimit) {
                this.IsLimit = IsLimit;
            }

            public String getLimitEndDate() {
                return LimitEndDate;
            }

            public void setLimitEndDate(String LimitEndDate) {
                this.LimitEndDate = LimitEndDate;
            }

            public double getPrice() {
                return Price;
            }

            public void setPrice(double Price) {
                this.Price = Price;
            }

            public String getProductId() {
                return ProductId;
            }

            public void setProductId(String ProductId) {
                this.ProductId = ProductId;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String ProductName) {
                this.ProductName = ProductName;
            }

            public String getSkuId() {
                return SkuId;
            }

            public void setSkuId(String SkuId) {
                this.SkuId = SkuId;
            }
        }
    }
}
