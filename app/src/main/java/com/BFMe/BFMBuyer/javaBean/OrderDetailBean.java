package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description: 订单详情
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/8/30 19:53
 */
public class OrderDetailBean {

    /**
     * Address : dfgfg
     * CellPhone : 111111111111
     * ExpressInfo : [{"IsReady":true,"StateDate":"2017-02-27 16:39:09","StateDesc":"提交订单"},{"IsReady":false,"StateDate":"","StateDesc":"商品出库"},{"IsReady":false,"StateDate":"","StateDesc":"海外仓库已收货"},{"IsReady":false,"StateDate":"","StateDesc":"等待收货"},{"IsReady":false,"StateDate":"","StateDesc":"完成"}]
     * Freight : 20.0
     * Id : 2000000000001478
     * IntegralDiscount : 0.0
     * OrderItemInfo : [{"Color":"黑色","CostPrice":23,"HasRefundApply":false,"Id":2561,"MarketPrice":0,"OrderId":2000000000001478,"ProductId":1208,"ProductName":"郝艳同测试双SKU","Quantity":1,"SalePrice":12,"Size":"XL","SkuId":"1208_2_2_0","ThumbnailsUrl":"http://img.bfme.com//Storage/Shop/520/Products/1208/a6ec4ea5cd13870e_100.jpg"}]
     * OrderTotalAmount : 32.0
     * PaymentTypeName :
     * ProductTotalAmount : 12.0
     * RegionFullName : 新疆 克孜勒苏 阿合奇县
     * SellerIMUser : bfme_408dfdab2e544b55b199f5ac9
     * SendMethodName :
     * ShipTo : 645
     * ShopId : 520
     * ShopName : 某宝28-店铺
     * StrOrderDate : 2017-02-27 16:39:09
     * StrPayDate :
     * Tax : 0.0
     * UserRemark :
     */

    private String Address;
    private String CellPhone;
    private double Freight;
    private String Id;
    private double IntegralDiscount;
    private double OrderTotalAmount;
    private String PaymentTypeName;
    private double ProductTotalAmount;
    private String RegionFullName;
    private String SellerIMUser;
    private String SendMethodName;
    private String ShipTo;
    private String ShopId;
    private String ShopName;
    private String StrOrderDate;
    private String StrPayDate;
    private double Tax;
    private String UserRemark;
    private boolean HasHSCode;
    /**
     * IsReady : true
     * StateDate : 2017-02-27 16:39:09
     * StateDesc : 提交订单
     */

    private List<ExpressInfoData> ExpressInfo;
    /**
     * Color : 黑色
     * CostPrice : 23.0
     * HasRefundApply : false
     * Id : 2561
     * MarketPrice : 0
     * OrderId : 2000000000001478
     * ProductId : 1208
     * ProductName : 郝艳同测试双SKU
     * Quantity : 1
     * SalePrice : 12.0
     * Size : XL
     * SkuId : 1208_2_2_0
     * ThumbnailsUrl : http://img.bfme.com//Storage/Shop/520/Products/1208/a6ec4ea5cd13870e_100.jpg
     */

    private List<OrderItemInfoBean> OrderItemInfo;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getCellPhone() {
        return CellPhone;
    }

    public void setCellPhone(String CellPhone) {
        this.CellPhone = CellPhone;
    }

    public double getFreight() {
        return Freight;
    }

    public void setFreight(double Freight) {
        this.Freight = Freight;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public double getIntegralDiscount() {
        return IntegralDiscount;
    }

    public void setIntegralDiscount(double IntegralDiscount) {
        this.IntegralDiscount = IntegralDiscount;
    }

    public double getOrderTotalAmount() {
        return OrderTotalAmount;
    }

    public void setOrderTotalAmount(double OrderTotalAmount) {
        this.OrderTotalAmount = OrderTotalAmount;
    }

    public String getPaymentTypeName() {
        return PaymentTypeName;
    }

    public void setPaymentTypeName(String PaymentTypeName) {
        this.PaymentTypeName = PaymentTypeName;
    }

    public double getProductTotalAmount() {
        return ProductTotalAmount;
    }

    public void setProductTotalAmount(double ProductTotalAmount) {
        this.ProductTotalAmount = ProductTotalAmount;
    }

    public String getRegionFullName() {
        return RegionFullName;
    }

    public void setRegionFullName(String RegionFullName) {
        this.RegionFullName = RegionFullName;
    }

    public String getSellerIMUser() {
        return SellerIMUser;
    }

    public void setSellerIMUser(String SellerIMUser) {
        this.SellerIMUser = SellerIMUser;
    }

    public String getSendMethodName() {
        return SendMethodName;
    }

    public void setSendMethodName(String SendMethodName) {
        this.SendMethodName = SendMethodName;
    }

    public String getShipTo() {
        return ShipTo;
    }

    public void setShipTo(String ShipTo) {
        this.ShipTo = ShipTo;
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

    public String getStrOrderDate() {
        return StrOrderDate;
    }

    public void setStrOrderDate(String StrOrderDate) {
        this.StrOrderDate = StrOrderDate;
    }

    public String getStrPayDate() {
        return StrPayDate;
    }

    public void setStrPayDate(String StrPayDate) {
        this.StrPayDate = StrPayDate;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double Tax) {
        this.Tax = Tax;
    }

    public String getUserRemark() {
        return UserRemark;
    }

    public void setUserRemark(String UserRemark) {
        this.UserRemark = UserRemark;
    }

    public boolean isHasHSCode() {
        return HasHSCode;
    }

    public void setHasHSCode(boolean hasHSCode) {
        HasHSCode = hasHSCode;
    }

    public List<ExpressInfoData> getExpressInfo() {
        return ExpressInfo;
    }

    public void setExpressInfo(List<ExpressInfoData> ExpressInfo) {
        this.ExpressInfo = ExpressInfo;
    }

    public List<OrderItemInfoBean> getOrderItemInfo() {
        return OrderItemInfo;
    }

    public void setOrderItemInfo(List<OrderItemInfoBean> OrderItemInfo) {
        this.OrderItemInfo = OrderItemInfo;
    }

    public static class ExpressInfoData {
        private boolean IsReady;
        private String StateDate;
        private String StateDesc;

        public boolean isIsReady() {
            return IsReady;
        }

        public void setIsReady(boolean IsReady) {
            this.IsReady = IsReady;
        }

        public String getStateDate() {
            return StateDate;
        }

        public void setStateDate(String StateDate) {
            this.StateDate = StateDate;
        }

        public String getStateDesc() {
            return StateDesc;
        }

        public void setStateDesc(String StateDesc) {
            this.StateDesc = StateDesc;
        }
    }

    public static class OrderItemInfoBean {
        private String Color;
        private double CostPrice;
        private boolean HasRefundApply;
        private String Id;
        private int MarketPrice;
        private String OrderId;
        private String ProductId;
        private String ProductName;
        private int Quantity;
        private double SalePrice;
        private String Size;
        private String SkuId;
        private String ThumbnailsUrl;

        public String getColor() {
            return Color;
        }

        public void setColor(String Color) {
            this.Color = Color;
        }

        public double getCostPrice() {
            return CostPrice;
        }

        public void setCostPrice(double CostPrice) {
            this.CostPrice = CostPrice;
        }

        public boolean isHasRefundApply() {
            return HasRefundApply;
        }

        public void setHasRefundApply(boolean HasRefundApply) {
            this.HasRefundApply = HasRefundApply;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public int getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(int MarketPrice) {
            this.MarketPrice = MarketPrice;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String OrderId) {
            this.OrderId = OrderId;
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

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int Quantity) {
            this.Quantity = Quantity;
        }

        public double getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(double SalePrice) {
            this.SalePrice = SalePrice;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
        }

        public String getSkuId() {
            return SkuId;
        }

        public void setSkuId(String SkuId) {
            this.SkuId = SkuId;
        }

        public String getThumbnailsUrl() {
            return ThumbnailsUrl;
        }

        public void setThumbnailsUrl(String ThumbnailsUrl) {
            this.ThumbnailsUrl = ThumbnailsUrl;
        }
    }
}
