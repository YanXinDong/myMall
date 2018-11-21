package com.BFMe.BFMBuyer.javaBean;

/**
 * Created by Administrator on 2016/9/18.
 */
public class RefundProductsBean {

    /**
     * MacRefundGoodCount : 1
     * MaxRefundAmount : 12.0
     * Mobile : 1888888888
     * OrderItem : {"Color":"白色","CostPrice":22,"ImgPath":"http://img.bfme.com//Storage/Shop/520/Products/1208/a6ec4ea5cd13870e_100.jpg","OrderId":2000000000001476,"OrderItemId":2559,"ProductId":1208,"ProductName":"郝艳同测试双SKU","Quantity":1,"RealTotalPrice":12,"SalePrice":12,"ShopId":520,"Size":"XL","SkuId":"1208_1_1_0"}
     * ServiceFee : 3.0
     * UserName : tiger21-xm
     */

    private int MacRefundGoodCount;
    private double MaxRefundAmount;
    private String Mobile;
    /**
     * Color : 白色
     * CostPrice : 22.0
     * ImgPath : http://img.bfme.com//Storage/Shop/520/Products/1208/a6ec4ea5cd13870e_100.jpg
     * OrderId : 2000000000001476
     * OrderItemId : 2559
     * ProductId : 1208
     * ProductName : 郝艳同测试双SKU
     * Quantity : 1
     * RealTotalPrice : 12.0
     * SalePrice : 12.0
     * ShopId : 520
     * Size : XL
     * SkuId : 1208_1_1_0
     */

    private OrderItemBean OrderItem;
    private double ServiceFee;
    private String UserName;

    public int getMacRefundGoodCount() {
        return MacRefundGoodCount;
    }

    public void setMacRefundGoodCount(int MacRefundGoodCount) {
        this.MacRefundGoodCount = MacRefundGoodCount;
    }

    public double getMaxRefundAmount() {
        return MaxRefundAmount;
    }

    public void setMaxRefundAmount(double MaxRefundAmount) {
        this.MaxRefundAmount = MaxRefundAmount;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public OrderItemBean getOrderItem() {
        return OrderItem;
    }

    public void setOrderItem(OrderItemBean OrderItem) {
        this.OrderItem = OrderItem;
    }

    public double getServiceFee() {
        return ServiceFee;
    }

    public void setServiceFee(double ServiceFee) {
        this.ServiceFee = ServiceFee;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public static class OrderItemBean {
        private String Color;
        private double CostPrice;
        private String ImgPath;
        private long OrderId;
        private int OrderItemId;
        private int ProductId;
        private String ProductName;
        private int Quantity;
        private double RealTotalPrice;
        private double SalePrice;
        private int ShopId;
        private String Size;
        private String SkuId;

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

        public String getImgPath() {
            return ImgPath;
        }

        public void setImgPath(String ImgPath) {
            this.ImgPath = ImgPath;
        }

        public long getOrderId() {
            return OrderId;
        }

        public void setOrderId(long OrderId) {
            this.OrderId = OrderId;
        }

        public int getOrderItemId() {
            return OrderItemId;
        }

        public void setOrderItemId(int OrderItemId) {
            this.OrderItemId = OrderItemId;
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

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int Quantity) {
            this.Quantity = Quantity;
        }

        public double getRealTotalPrice() {
            return RealTotalPrice;
        }

        public void setRealTotalPrice(double RealTotalPrice) {
            this.RealTotalPrice = RealTotalPrice;
        }

        public double getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(double SalePrice) {
            this.SalePrice = SalePrice;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
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
    }
}
