package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description:  结算界面获取购物车列表
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/8/15 20:06
 */
public class AccountBean {
    /**
     * AddressId : 14050
     * CartItems : [{"CartItems":[{"CartItemId":2709,"Count":1,"ImgUrl":"http://img01.bfme.com/Storage/Shop/399/Products/371/1_50.png","Ismyself":false,"Name":" Shiseido资生堂六角眉笔","Price":22,"ProductId":371,"SkuId":"371_0_0_0","Taxation":"0.00"}],"FreeFreight":0,"Freight":10,"ShopId":399,"ShopLogo":"","ShopName":"七七香港留学狗","Taxation":0}]
     */

    private int AddressId;
    private boolean HasHSCode;
    /**
     * CartItems : [{"CartItemId":2709,"Count":1,"ImgUrl":"http://img01.bfme.com/Storage/Shop/399/Products/371/1_50.png","Ismyself":false,"Name":" Shiseido资生堂六角眉笔","Price":22,"ProductId":371,"SkuId":"371_0_0_0","Taxation":"0.00"}]
     * FreeFreight : 0.0
     * Freight : 10
     * ShopId : 399
     * ShopLogo :
     * ShopName : 七七香港留学狗
     * Taxation : 0.0
     */

    private List<CartItemsDatas> CartItems;

    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int AddressId) {
        this.AddressId = AddressId;
    }

    public boolean isHasHSCode() {
        return HasHSCode;
    }

    public void setHasHSCode(boolean hasHSCode) {
        HasHSCode = hasHSCode;
    }

    public List<CartItemsDatas> getCartItems() {
        return CartItems;
    }

    public void setCartItems(List<CartItemsDatas> CartItems) {
        this.CartItems = CartItems;
    }

    public static class CartItemsDatas {
        private double FreeFreight;
        private double Freight;
        private int ShopId;
        private String ShopLogo;
        private String ShopName;
        private double Taxation;
        /**
         * CartItemId : 2709
         * Count : 1
         * ImgUrl : http://img01.bfme.com/Storage/Shop/399/Products/371/1_50.png
         * Ismyself : false
         * Name :  Shiseido资生堂六角眉笔
         * Price : 22.0
         * ProductId : 371
         * SkuId : 371_0_0_0
         * Taxation : 0.00
         */

        private List<CartItems> CartItems;

        public double getFreeFreight() {
            return FreeFreight;
        }

        public void setFreeFreight(double FreeFreight) {
            this.FreeFreight = FreeFreight;
        }

        public double getFreight() {
            return Freight;
        }

        public void setFreight(double Freight) {
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

        public double getTaxation() {
            return Taxation;
        }

        public void setTaxation(double Taxation) {
            this.Taxation = Taxation;
        }

        public List<CartItems> getCartItems() {
            return CartItems;
        }

        public void setCartItems(List<CartItems> CartItems) {
            this.CartItems = CartItems;
        }

        public static class CartItems {
            private int CartItemId;
            private int Count;
            private String ImgUrl;
            private boolean Ismyself;
            private String Name;
            private double Price;
            private int ProductId;
            private String SkuId;
            private double Taxation;
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

            public double getTaxation() {
                return Taxation;
            }

            public void setTaxation(double Taxation) {
                this.Taxation = Taxation;
            }
        }
    }
//    public String                    AddressId;
//    public ArrayList<CartItemsDatas> CartItems;
//
//    public class CartItemsDatas {
//        public String                    FreeFreight;
//        public String                    Freight;
//        public String                    ShopId;
//        public String                    ShopLogo;
//        public String                    ShopName;
//        public String                    Taxation;
//        public ArrayList<CartItems> CartItems;
//
//        @Override
//        public String toString() {
//            return "CartItemsDatas{" +
//                    "FreeFreight='" + FreeFreight + '\'' +
//                    ", Freight='" + Freight + '\'' +
//                    ", ShopId='" + ShopId + '\'' +
//                    ", ShopLogo='" + ShopLogo + '\'' +
//                    ", ShopName='" + ShopName + '\'' +
//                    ", Taxation='" + Taxation + '\'' +
//                    ", CartItems=" + CartItems +
//                    '}';
//        }
//
//        public class CartItems{
//           public String CartItemId;
//            public String Count;
//            public String ImgUrl;
//            public String Ismyself;
//            public String Name;
//            public String Price;
//            public String ProductId;
//            public String SkuId;
//            public String Taxation;
//
//            @Override
//            public String toString() {
//                return "CartItems{" +
//                        "CartItemId='" + CartItemId + '\'' +
//                        ", Count='" + Count + '\'' +
//                        ", ImgUrl='" + ImgUrl + '\'' +
//                        ", Ismyself='" + Ismyself + '\'' +
//                        ", Name='" + Name + '\'' +
//                        ", Price='" + Price + '\'' +
//                        ", ProductId='" + ProductId + '\'' +
//                        ", SkuId='" + SkuId + '\'' +
//                        ", Taxation='" + Taxation + '\'' +
//                        '}';
//            }
//        }
//    }


}
