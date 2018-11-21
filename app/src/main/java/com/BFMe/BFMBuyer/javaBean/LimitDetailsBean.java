package com.BFMe.BFMBuyer.javaBean;

import java.io.Serializable;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 11:42
 */
public class LimitDetailsBean implements Serializable{

    /**
     * EndTime : 20170525094546
     * FreightStr : 卖家承担运费
     * HaveCode : true
     * Id : 122
     * ImagePath : http://192.168.1.149:8088/Storage/Shop/769/Products/1075
     * LimitStock : 98
     * MarketPrice : 1.0
     * MaxSaleCount : 5
     * MinSalePrice : 100.0
     * ProductId : 1075
     * ProductName : 测试看看
     * SaleCount : 1
     * ShopId : 769
     * ShopName : 陈浩南的小店
     * StartTime : 20161202000000
     * Tax : 0
     * Title : 限时折扣
     */

    private DataBean Data;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean implements Serializable{
        private String EndTime;
        private String FreightStr;
        private boolean HaveCode;
        private int Id;
        private String ImagePath;
        private int LimitStock;
        private double MarketPrice;
        private int MaxSaleCount;
        private double MinSalePrice;
        private int ProductId;
        private String ProductName;
        private int SaleCount;
        private int ShopId;
        private String ShopName;
        private String StartTime;
        private double Tax;
        private String Title;

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getFreightStr() {
            return FreightStr;
        }

        public void setFreightStr(String FreightStr) {
            this.FreightStr = FreightStr;
        }

        public boolean isHaveCode() {
            return HaveCode;
        }

        public void setHaveCode(boolean HaveCode) {
            this.HaveCode = HaveCode;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public int getLimitStock() {
            return LimitStock;
        }

        public void setLimitStock(int LimitStock) {
            this.LimitStock = LimitStock;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double MarketPrice) {
            this.MarketPrice = MarketPrice;
        }

        public int getMaxSaleCount() {
            return MaxSaleCount;
        }

        public void setMaxSaleCount(int MaxSaleCount) {
            this.MaxSaleCount = MaxSaleCount;
        }

        public double getMinSalePrice() {
            return MinSalePrice;
        }

        public void setMinSalePrice(double MinSalePrice) {
            this.MinSalePrice = MinSalePrice;
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

        public int getSaleCount() {
            return SaleCount;
        }

        public void setSaleCount(int SaleCount) {
            this.SaleCount = SaleCount;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
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

        public double getTax() {
            return Tax;
        }

        public void setTax(double Tax) {
            this.Tax = Tax;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "EndTime='" + EndTime + '\'' +
                    ", FreightStr='" + FreightStr + '\'' +
                    ", HaveCode=" + HaveCode +
                    ", Id=" + Id +
                    ", ImagePath='" + ImagePath + '\'' +
                    ", LimitStock=" + LimitStock +
                    ", MarketPrice=" + MarketPrice +
                    ", MaxSaleCount=" + MaxSaleCount +
                    ", MinSalePrice=" + MinSalePrice +
                    ", ProductId=" + ProductId +
                    ", ProductName='" + ProductName + '\'' +
                    ", SaleCount=" + SaleCount +
                    ", ShopId=" + ShopId +
                    ", ShopName='" + ShopName + '\'' +
                    ", StartTime='" + StartTime + '\'' +
                    ", Tax='" + Tax + '\'' +
                    ", Title='" + Title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LimitDetailsBean{" +
                "Data=" + Data +
                '}';
    }
}
