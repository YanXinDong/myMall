package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/19 19:08
 */
public class RefundListBean {

    /**
     * Models : [{"Amount":679,"Applicant":"bfme_1607261105577731","ApplyDate":"/Date(1469519080000-0000)/","ContactCellPhone":"13501035445","EnabledRefundAmount":900,"Id":41,"IsReturn":false,"ManagerConfirmDate":"/Date(1469519080000-0000)/","ManagerConfirmStatus":"UnConfirm","ManagerConfirmStatusDesc":"待平台确认","OrderId":1100000000014291,"OrderItemId":14646,"OrderItemInfo":{"Color":"","OrderItemId":14646,"ProductId":2068,"ProductName":"rrerererere","ReturnQuantity":0,"Size":"","SkuId":"2068_0_0_0","ThumbnailsUrl":"http://img01.bfme.com/Storage/Shop/430/Products/2068/1_100.png","UserId":0,"Version":""},"Reason":"123456789","RefundMode":"OrderItemRefund","RefundModeDesc":"货品退款","RefundStatus":"待商家审核","RefundType":0,"ReturnQuantity":0,"SellerAuditDate":"/Date(1469519080000-0000)/","SellerAuditStatus":"WaitAudit","SellerAuditStatusDesc":"待商家审核","ShopId":430,"ShopName":"秃笔蓝波丸","StrApplyDate":"2016-07-26 15:44:40","StrBuyerDeliverDate":"","StrManagerConfirmDate":"2016-07-26 15:44:40","StrSellerAuditDate":"2016-07-26 15:44:40","StrSellerConfirmArrivalDate":"","UserId":14729}]
     * Total : 1
     */

    private int Total;
    /**
     * Amount : 679.0
     * Applicant : bfme_1607261105577731
     * ApplyDate : /Date(1469519080000-0000)/
     * ContactCellPhone : 13501035445
     * EnabledRefundAmount : 900.0
     * Id : 41
     * IsReturn : false
     * ManagerConfirmDate : /Date(1469519080000-0000)/
     * ManagerConfirmStatus : UnConfirm
     * ManagerConfirmStatusDesc : 待平台确认
     * OrderId : 1100000000014291
     * OrderItemId : 14646
     * OrderItemInfo : {"Color":"","OrderItemId":14646,"ProductId":2068,"ProductName":"rrerererere","ReturnQuantity":0,"Size":"","SkuId":"2068_0_0_0","ThumbnailsUrl":"http://img01.bfme.com/Storage/Shop/430/Products/2068/1_100.png","UserId":0,"Version":""}
     * Reason : 123456789
     * RefundMode : OrderItemRefund
     * RefundModeDesc : 货品退款
     * RefundStatus : 待商家审核
     * RefundType : 0
     * ReturnQuantity : 0
     * SellerAuditDate : /Date(1469519080000-0000)/
     * SellerAuditStatus : WaitAudit
     * SellerAuditStatusDesc : 待商家审核
     * ShopId : 430
     * ShopName : 秃笔蓝波丸
     * StrApplyDate : 2016-07-26 15:44:40
     * StrBuyerDeliverDate :
     * StrManagerConfirmDate : 2016-07-26 15:44:40
     * StrSellerAuditDate : 2016-07-26 15:44:40
     * StrSellerConfirmArrivalDate :
     * UserId : 14729
     */

    private List<ModelsBean> Models;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<ModelsBean> getModels() {
        return Models;
    }

    public void setModels(List<ModelsBean> Models) {
        this.Models = Models;
    }

    public static class ModelsBean {
        private double Amount;
        private String Applicant;
        private String ApplyDate;
        private String ContactCellPhone;
        private double EnabledRefundAmount;
        private int Id;
        private boolean IsReturn;
        private String ManagerConfirmDate;
        private String ManagerConfirmStatus;
        private String ManagerConfirmStatusDesc;
        private long OrderId;
        private int OrderItemId;
        /**
         * Color :
         * OrderItemId : 14646
         * ProductId : 2068
         * ProductName : rrerererere
         * ReturnQuantity : 0
         * Size :
         * SkuId : 2068_0_0_0
         * ThumbnailsUrl : http://img01.bfme.com/Storage/Shop/430/Products/2068/1_100.png
         * UserId : 0
         * Version :
         */

        private OrderItemInfoBean OrderItemInfo;
        private String Reason;
        private String RefundMode;
        private String RefundModeDesc;
        private String RefundStatus;
        private int RefundType;
        private int ReturnQuantity;
        private String SellerAuditDate;
        private String SellerAuditStatus;
        private String SellerAuditStatusDesc;
        private int ShopId;
        private String ShopName;
        private String StrApplyDate;
        private String StrBuyerDeliverDate;
        private String StrManagerConfirmDate;
        private String StrSellerAuditDate;
        private String StrSellerConfirmArrivalDate;
        private int UserId;

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public String getApplicant() {
            return Applicant;
        }

        public void setApplicant(String Applicant) {
            this.Applicant = Applicant;
        }

        public String getApplyDate() {
            return ApplyDate;
        }

        public void setApplyDate(String ApplyDate) {
            this.ApplyDate = ApplyDate;
        }

        public String getContactCellPhone() {
            return ContactCellPhone;
        }

        public void setContactCellPhone(String ContactCellPhone) {
            this.ContactCellPhone = ContactCellPhone;
        }

        public double getEnabledRefundAmount() {
            return EnabledRefundAmount;
        }

        public void setEnabledRefundAmount(double EnabledRefundAmount) {
            this.EnabledRefundAmount = EnabledRefundAmount;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public boolean isIsReturn() {
            return IsReturn;
        }

        public void setIsReturn(boolean IsReturn) {
            this.IsReturn = IsReturn;
        }

        public String getManagerConfirmDate() {
            return ManagerConfirmDate;
        }

        public void setManagerConfirmDate(String ManagerConfirmDate) {
            this.ManagerConfirmDate = ManagerConfirmDate;
        }

        public String getManagerConfirmStatus() {
            return ManagerConfirmStatus;
        }

        public void setManagerConfirmStatus(String ManagerConfirmStatus) {
            this.ManagerConfirmStatus = ManagerConfirmStatus;
        }

        public String getManagerConfirmStatusDesc() {
            return ManagerConfirmStatusDesc;
        }

        public void setManagerConfirmStatusDesc(String ManagerConfirmStatusDesc) {
            this.ManagerConfirmStatusDesc = ManagerConfirmStatusDesc;
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

        public String getRefundMode() {
            return RefundMode;
        }

        public void setRefundMode(String RefundMode) {
            this.RefundMode = RefundMode;
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

        public int getRefundType() {
            return RefundType;
        }

        public void setRefundType(int RefundType) {
            this.RefundType = RefundType;
        }

        public int getReturnQuantity() {
            return ReturnQuantity;
        }

        public void setReturnQuantity(int ReturnQuantity) {
            this.ReturnQuantity = ReturnQuantity;
        }

        public String getSellerAuditDate() {
            return SellerAuditDate;
        }

        public void setSellerAuditDate(String SellerAuditDate) {
            this.SellerAuditDate = SellerAuditDate;
        }

        public String getSellerAuditStatus() {
            return SellerAuditStatus;
        }

        public void setSellerAuditStatus(String SellerAuditStatus) {
            this.SellerAuditStatus = SellerAuditStatus;
        }

        public String getSellerAuditStatusDesc() {
            return SellerAuditStatusDesc;
        }

        public void setSellerAuditStatusDesc(String SellerAuditStatusDesc) {
            this.SellerAuditStatusDesc = SellerAuditStatusDesc;
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

        public String getStrBuyerDeliverDate() {
            return StrBuyerDeliverDate;
        }

        public void setStrBuyerDeliverDate(String StrBuyerDeliverDate) {
            this.StrBuyerDeliverDate = StrBuyerDeliverDate;
        }

        public String getStrManagerConfirmDate() {
            return StrManagerConfirmDate;
        }

        public void setStrManagerConfirmDate(String StrManagerConfirmDate) {
            this.StrManagerConfirmDate = StrManagerConfirmDate;
        }

        public String getStrSellerAuditDate() {
            return StrSellerAuditDate;
        }

        public void setStrSellerAuditDate(String StrSellerAuditDate) {
            this.StrSellerAuditDate = StrSellerAuditDate;
        }

        public String getStrSellerConfirmArrivalDate() {
            return StrSellerConfirmArrivalDate;
        }

        public void setStrSellerConfirmArrivalDate(String StrSellerConfirmArrivalDate) {
            this.StrSellerConfirmArrivalDate = StrSellerConfirmArrivalDate;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public static class OrderItemInfoBean {
            private String Color;
            private int OrderItemId;
            private int ProductId;
            private String ProductName;
            private int ReturnQuantity;
            private String Size;
            private String SkuId;
            private String ThumbnailsUrl;
            private int UserId;
            private String Version;

            public String getColor() {
                return Color;
            }

            public void setColor(String Color) {
                this.Color = Color;
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

            public int getReturnQuantity() {
                return ReturnQuantity;
            }

            public void setReturnQuantity(int ReturnQuantity) {
                this.ReturnQuantity = ReturnQuantity;
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

            public int getUserId() {
                return UserId;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public String getVersion() {
                return Version;
            }

            public void setVersion(String Version) {
                this.Version = Version;
            }
        }
    }
}
