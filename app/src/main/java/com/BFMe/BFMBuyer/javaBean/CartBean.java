package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description: 购物车的Javabean
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 闫信董
 * Date       : 2016/8/11 15:55
 */
public class CartBean {

    /**
     * CartItems : [{"CartItemId":13916,"Count":1,"ImgUrl":"http://img01.baifomi.com//Storage/Shop/553/Products/5742/1_220.png","Ismyself":false,"Name":"Four Loko  果味酒饮料  695ml","Price":73,"ProductId":5742,"SkuId":"5742_0_0_0","Taxation":"0"},{"CartItemId":13917,"Count":1,"ImgUrl":"http://img01.baifomi.com//Storage/Shop/553/Products/2476/1_220.png","Ismyself":false,"Name":"【香港sasa】矿物透亮洗面膏 120g","Price":47,"ProductId":2476,"SkuId":"2476_0_0_0","Taxation":"0"},{"CartItemId":13918,"Count":1,"ImgUrl":"http://img01.baifomi.com//Storage/Shop/553/Products/1688/1_220.png","Ismyself":false,"Name":"【香港卡莱美】日本佑天兰透明质酸黄金啫喱果冻面膜深层补水保湿滋润|面膜贴3片装","Price":59,"ProductId":1688,"SkuId":"1688_0_0_0","Taxation":"0"}]
     * FreeFreight : 199.0
     * Freight : 0
     * ShopId : 553
     * ShopLogo : http://img01.baifomi.com/lib/user/headimg/default-1.jpg
     * ShopName : 小安安专业香港代购
     * Taxation : 0
     */

    private List<CartItemsBean> CartItems;

    public List<CartItemsBean> getCartItems() {
        return CartItems;
    }

    public void setCartItems(List<CartItemsBean> CartItems) {
        this.CartItems = CartItems;
    }

    public static class CartItemsBean {
        private double FreeFreight;
        private int Freight;
        private int ShopId;
        private String ShopLogo;
        private String ShopName;
        private int Taxation;
        private boolean isChecked = false;
        /**
         * CartItemId : 13916
         * Count : 1
         * ImgUrl : http://img01.baifomi.com//Storage/Shop/553/Products/5742/1_220.png
         * Ismyself : false
         * Name : Four Loko  果味酒饮料  695ml
         * Price : 73.0
         * ProductId : 5742
         * SkuId : 5742_0_0_0
         * Taxation : 0
         */

        private List<CartSonItemsBean> CartItems;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public double getFreeFreight() {
            return FreeFreight;
        }

        public void setFreeFreight(double FreeFreight) {
            this.FreeFreight = FreeFreight;
        }

        public int getFreight() {
            return Freight;
        }

        public void setFreight(int Freight) {
            this.Freight = Freight;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
        }

        public String getShopLogo() {
            return ShopLogo;
        }

        public void setShopLogo(String ShopLogo) {
            this.ShopLogo = ShopLogo;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public int getTaxation() {
            return Taxation;
        }

        public void setTaxation(int Taxation) {
            this.Taxation = Taxation;
        }

        public List<CartSonItemsBean> getCartItems() {
            return CartItems;
        }

        public void setCartItems(List<CartSonItemsBean> CartItems) {
            this.CartItems = CartItems;
        }

        public static class CartSonItemsBean {
            private int CartItemId;
            private int Count;
            private String ImgUrl;
            private boolean Ismyself;
            private String Name;
            private double Price;
            private int ProductId;
            private String SkuId;
            private String Taxation;
            private boolean isChecked = false;
            private String Size;
            private String Color;

            public String getSize() {
                return Size;
            }

            public void setSize(String size) {
                Size = size;
            }

            public String getColor() {
                return Color;
            }

            public void setColor(String color) {
                Color = color;
            }

            public boolean ismyself() {
                return Ismyself;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public int getCartItemId() {
                return CartItemId;
            }

            public void setCartItemId(int CartItemId) {
                this.CartItemId = CartItemId;
            }

            public int getCount() {
                return Count;
            }

            public void setCount(int Count) {
                this.Count = Count;
            }

            public String getImgUrl() {
                return ImgUrl;
            }

            public void setImgUrl(String ImgUrl) {
                this.ImgUrl = ImgUrl;
            }

            public boolean isIsmyself() {
                return Ismyself;
            }

            public void setIsmyself(boolean Ismyself) {
                this.Ismyself = Ismyself;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public double getPrice() {
                return Price;
            }

            public void setPrice(double Price) {
                this.Price = Price;
            }

            public int getProductId() {
                return ProductId;
            }

            public void setProductId(int ProductId) {
                this.ProductId = ProductId;
            }

            public String getSkuId() {
                return SkuId;
            }

            public void setSkuId(String SkuId) {
                this.SkuId = SkuId;
            }

            public String getTaxation() {
                return Taxation;
            }

            public void setTaxation(String Taxation) {
                this.Taxation = Taxation;
            }
        }
    }
}
