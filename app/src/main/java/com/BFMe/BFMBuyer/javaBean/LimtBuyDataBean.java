package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/22 14:54
 */
public class LimtBuyDataBean {


    /**
     * Products : [{"AuditStatus":"Ongoing","CancelReson":"","CategoryName":"服装","EndTime":"2016-09-25 00:00:00","Id":129,"ImagePath":["http://img01.bfme.com/Storage/Shop/689/Products/2119/1.png"],"LimitStock":2132,"MaxSaleCount":54,"MinPrice":6,"Price":4,"ProductAd":"fewfefwefefevdefe","ProductId":2119,"ProductName":"fewfew","RecentMonthPrice":6,"SaleCount":0,"ShopId":689,"ShopName":"淘淘","StartTime":"2016-09-18 00:00:00","Title":"限时折扣"},{"AuditStatus":"Ongoing","CancelReson":"","CategoryName":"服装","EndTime":"2016-09-30 10:05:14","Id":128,"ImagePath":["http://img01.bfme.com/Storage/Shop/689/Products/2120/1.png"],"LimitStock":2120,"MaxSaleCount":32,"MinPrice":8,"Price":7,"ProductAd":"fewfwww","ProductId":2120,"ProductName":"dew","RecentMonthPrice":8,"SaleCount":0,"ShopId":689,"ShopName":"淘淘","StartTime":"2016-09-09 10:50:00","Title":"限时折扣"},{"AuditStatus":"Ongoing","CancelReson":"","CategoryName":"美妆彩妆","EndTime":"2016-09-25 11:55:06","Id":125,"ImagePath":["http://img01.bfme.com/Storage/Shop/689/Products/2123/1.png"],"LimitStock":21213,"MaxSaleCount":33,"MinPrice":43,"Price":1,"ProductAd":"fewfr3qr32r32fedddddddd","ProductId":2123,"ProductName":"dew","RecentMonthPrice":43,"SaleCount":0,"ShopId":689,"ShopName":"淘淘","StartTime":"2016-09-09 00:00:00","Title":"限时折扣"}]
     * Total : 3
     */

    private int Total;
    /**
     * AuditStatus : Ongoing
     * CancelReson :
     * CategoryName : 服装
     * EndTime : 2016-09-25 00:00:00
     * Id : 129
     * ImagePath : ["http://img01.bfme.com/Storage/Shop/689/Products/2119/1.png"]
     * LimitStock : 2132
     * MaxSaleCount : 54
     * MinPrice : 6.0
     * Price : 4.0
     * ProductAd : fewfefwefefevdefe
     * ProductId : 2119
     * ProductName : fewfew
     * RecentMonthPrice : 6.0
     * SaleCount : 0
     * ShopId : 689
     * ShopName : 淘淘
     * StartTime : 2016-09-18 00:00:00
     * Title : 限时折扣
     */

    private List<ProductsBean> Products;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsBean> Products) {
        this.Products = Products;
    }

    public static class ProductsBean {
        private String AuditStatus;
        private String CancelReson;
        private String CategoryName;
        private String EndTime;
        private int Id;
        private int LimitStock;
        private int MaxSaleCount;
        private double MinPrice;
        private double Price;
        private String ProductAd;
        private int ProductId;
        private String ProductName;
        private double RecentMonthPrice;
        private int SaleCount;
        private String ShopId;
        private String ShopName;
        private String StartTime;
        private String Title;
        private List<String> ImagePath;

        public String getAuditStatus() {
            return AuditStatus;
        }

        public void setAuditStatus(String AuditStatus) {
            this.AuditStatus = AuditStatus;
        }

        public String getCancelReson() {
            return CancelReson;
        }

        public void setCancelReson(String CancelReson) {
            this.CancelReson = CancelReson;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String CategoryName) {
            this.CategoryName = CategoryName;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getLimitStock() {
            return LimitStock;
        }

        public void setLimitStock(int LimitStock) {
            this.LimitStock = LimitStock;
        }

        public int getMaxSaleCount() {
            return MaxSaleCount;
        }

        public void setMaxSaleCount(int MaxSaleCount) {
            this.MaxSaleCount = MaxSaleCount;
        }

        public double getMinPrice() {
            return MinPrice;
        }

        public void setMinPrice(double MinPrice) {
            this.MinPrice = MinPrice;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public String getProductAd() {
            return ProductAd;
        }

        public void setProductAd(String ProductAd) {
            this.ProductAd = ProductAd;
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

        public double getRecentMonthPrice() {
            return RecentMonthPrice;
        }

        public void setRecentMonthPrice(double RecentMonthPrice) {
            this.RecentMonthPrice = RecentMonthPrice;
        }

        public int getSaleCount() {
            return SaleCount;
        }

        public void setSaleCount(int SaleCount) {
            this.SaleCount = SaleCount;
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

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public List<String> getImagePath() {
            return ImagePath;
        }

        public void setImagePath(List<String> ImagePath) {
            this.ImagePath = ImagePath;
        }
    }
}


