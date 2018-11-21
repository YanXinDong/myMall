package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 16:34
 */
public class SubjectListBean {

    /**
     * Country : 法国
     * CountryImg : http://img01.bfme.com/lib/countries/flags/4.png
     * Id : 664
     * Image : http://img01.bfme.com/Storage/Shop/442/Products/664/1_350.png
     * MarketPrice : 3400.0
     * Name : MK女包michael kors真皮笑脸包MK铆钉斜挎耳朵包
     * Price : 2800.0
     * ShopId : 442
     */

    private List<ProductsBean> Products;

    public List<ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsBean> Products) {
        this.Products = Products;
    }

    public static class ProductsBean {
        private String Country;
        private String CountryImg;
        private int Id;
        private String Image;
        private int IsSeckill;
        private double MarketPrice;
        private String Name;
        private double Price;
        private int ShopId;

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

        public String getCountryImg() {
            return CountryImg;
        }

        public void setCountryImg(String CountryImg) {
            this.CountryImg = CountryImg;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public int isSeckill() {
            return IsSeckill;
        }

        public void setSeckill(int seckill) {
            IsSeckill = seckill;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double MarketPrice) {
            this.MarketPrice = MarketPrice;
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

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
        }
    }
}
