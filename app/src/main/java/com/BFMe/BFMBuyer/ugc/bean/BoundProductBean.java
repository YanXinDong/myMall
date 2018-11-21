package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：关联商品的bean
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/15 17:47
 */
public class BoundProductBean {

    /**
     * Id : 379
     * ImageUrl : http://img01.bfme.com/Storage/Shop/407/Products/379/1_100.png
     * ProductName : 嘉娜宝KATE三色立体眉粉
     * SalePrice : 79.0
     */

    private List<OrdersListBean> OrdersList;

    public List<OrdersListBean> getOrdersList() {
        return OrdersList;
    }

    public void setOrdersList(List<OrdersListBean> OrdersList) {
        this.OrdersList = OrdersList;
    }

    public static class OrdersListBean {
        private String Id;
        private String ImageUrl;
        private String ProductName;
        private String SalePrice;
        private String ShopName;

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(String SalePrice) {
            this.SalePrice = SalePrice;
        }

        public OrdersListBean(String id, String imageUrl, String productName, String salePrice,String shopName) {
            Id = id;
            ImageUrl = imageUrl;
            ProductName = productName;
            SalePrice = salePrice;
            ShopName = shopName;
        }

        public OrdersListBean() {
        }
    }

    public BoundProductBean(List<OrdersListBean> ordersList) {
        OrdersList = ordersList;
    }
}
