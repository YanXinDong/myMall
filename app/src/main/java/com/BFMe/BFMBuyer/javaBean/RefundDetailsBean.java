package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/20 10:35
 */
public class RefundDetailsBean {


    /**
     * Amount : 0.01
     * BuyerBear : true
     * ExpressCompanyName : 测试物流
     * HandleDate :
     * Id : 353
     * Note : 商家收货时间为15个工作日(节假日、周末),请您耐心等待。
     * OrderId : 1000000000028575
     * OrderItemId : 29237
     * OrderItemInfo : {"OrderItemId":29237,"ProductId":9221,"ProductName":"迷人香水","ReturnQuantity":1,"SkuId":"9221_0_0_0","ThumbnailsUrl":"http://img01.baifomi.com//Storage/Shop/1051/Products/9221/cc38ee39a8495dce_100.jpg"}
     * Reason : 商品质量坏了
     * ReasonTypeDesc : 商品损坏(卖家承担服务费)
     * RefundModeDesc : 退货退款
     * RefundStatus : 待商家收货
     * SellerIMID : bfme_8323b0a144a8411fbde17e373
     * SellerRemark : 同意
     * ServiceFee : 0
     * ShipOrderNumber : 12345680
     * ShopId : 1051
     * ShopName : 竹溪阁
     * StrApplyDate : 2017-03-17 16:05:49
     * UserId : k1T6hEBdjdER+PQhXoTA25OapZOeIndynaOANsfc04tlyZRODGtOYRCVBoKcugqDgLp3LDSAdQqvIHPa9K+IvMTJUA0I7VI/qGnw8TOW5kg4yJRWvmI5uCqv866fbHJT0OdoKlvh1kFTyXLh+PwldEQt8zxsadk+kQVwpGFAJF9p+tmRpsMenLBjsAfSWo+VRPscAKev1PDGgZmY0wMlz4539hlxCDL3Cb9UaOZrFv22Bn6/xRuUmoq2OQiwoESOjegBbUH+QOVoLQ76hcQ5qSWgAfsAWpVUmikMBEXykp42MKzE5ZAs2JDoxVFGJDFHoPdwOidO0D5yEkgWHzxdig==
     * WYCloudId : bfme_8323b0a144a8411fbde17e373
     */

    private double Amount;
    private boolean BuyerBear;
    private String ExpressCompanyName;
    private String HandleDate;
    private int Id;
    private String Note;
    private long OrderId;
    private int OrderItemId;
    /**
     * OrderItemId : 29237
     * ProductId : 9221
     * ProductName : 迷人香水
     * ReturnQuantity : 1
     * SkuId : 9221_0_0_0
     * ThumbnailsUrl : http://img01.baifomi.com//Storage/Shop/1051/Products/9221/cc38ee39a8495dce_100.jpg
     */

    private OrderItemInfoBean OrderItemInfo;
    private String Reason;
    private String ReasonTypeDesc;
    private String RefundModeDesc;
    private String RefundStatus;
    private String SellerIMID;
    private String SellerRemark;
    private int ServiceFee;
    private String ShipOrderNumber;
    private int ShopId;
    private String ShopName;
    private String StrApplyDate;
    private String UserId;
    private String WYCloudId;

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public boolean isBuyerBear() {
        return BuyerBear;
    }

    public void setBuyerBear(boolean BuyerBear) {
        this.BuyerBear = BuyerBear;
    }

    public String getExpressCompanyName() {
        return ExpressCompanyName;
    }

    public void setExpressCompanyName(String ExpressCompanyName) {
        this.ExpressCompanyName = ExpressCompanyName;
    }

    public String getHandleDate() {
        return HandleDate;
    }

    public void setHandleDate(String HandleDate) {
        this.HandleDate = HandleDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
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

    public OrderItemInfoBean getOrderItemInfo() {
        return OrderItemInfo;
    }

    public void setOrderItemInfo(OrderItemInfoBean OrderItemInfo) {
        this.OrderItemInfo = OrderItemInfo;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    public String getReasonTypeDesc() {
        return ReasonTypeDesc;
    }

    public void setReasonTypeDesc(String ReasonTypeDesc) {
        this.ReasonTypeDesc = ReasonTypeDesc;
    }

    public String getRefundModeDesc() {
        return RefundModeDesc;
    }

    public void setRefundModeDesc(String RefundModeDesc) {
        this.RefundModeDesc = RefundModeDesc;
    }

    public String getRefundStatus() {
        return RefundStatus;
    }

    public void setRefundStatus(String RefundStatus) {
        this.RefundStatus = RefundStatus;
    }

    public String getSellerIMID() {
        return SellerIMID;
    }

    public void setSellerIMID(String SellerIMID) {
        this.SellerIMID = SellerIMID;
    }

    public String getSellerRemark() {
        return SellerRemark;
    }

    public void setSellerRemark(String SellerRemark) {
        this.SellerRemark = SellerRemark;
    }

    public int getServiceFee() {
        return ServiceFee;
    }

    public void setServiceFee(int ServiceFee) {
        this.ServiceFee = ServiceFee;
    }

    public String getShipOrderNumber() {
        return ShipOrderNumber;
    }

    public void setShipOrderNumber(String ShipOrderNumber) {
        this.ShipOrderNumber = ShipOrderNumber;
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

    public String getStrApplyDate() {
        return StrApplyDate;
    }

    public void setStrApplyDate(String StrApplyDate) {
        this.StrApplyDate = StrApplyDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getWYCloudId() {
        return WYCloudId;
    }

    public void setWYCloudId(String WYCloudId) {
        this.WYCloudId = WYCloudId;
    }

    public static class OrderItemInfoBean {
        private int OrderItemId;
        private int ProductId;
        private String ProductName;
        private int ReturnQuantity;
        private String SkuId;
        private String ThumbnailsUrl;

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

        public int getReturnQuantity() {
            return ReturnQuantity;
        }

        public void setReturnQuantity(int ReturnQuantity) {
            this.ReturnQuantity = ReturnQuantity;
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
