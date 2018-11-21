package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/12/11 15:40
 */
public class RecommendCommoditiesBean {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Country : 香港
         * CountryLogo : http://img01.baifomi.com/lib/countries/flags/3.png
         * Freight :
         * Id : 1422
         * ImagePath : http://192.168.0.50:8888//Storage/Shop/520/Products/1422/d140c986f3c52d4f_350.jpg
         * IsFreeFreight : False
         * IsFreeTax : False
         * MarketPrice : 0
         * MinSalePrice : 88
         * ProductName : 小韦测试多规格商品
         * SaleCounts : 0
         * ShopId : 520
         * ShopName : 某宝28-店铺
         * Tax :
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
            private String CountryLogo;
            private String Freight;
            private String Id;
            private String ImagePath;
            private String IsFreeFreight;
            private String IsFreeTax;
            private double MarketPrice;
            private double MinSalePrice;
            private String ProductName;
            private int SaleCounts;
            private int ShopId;
            private String ShopName;
            private String Tax;

            public String getCountry() {
                return Country;
            }

            public void setCountry(String Country) {
                this.Country = Country;
            }

            public String getCountryLogo() {
                return CountryLogo;
            }

            public void setCountryLogo(String CountryLogo) {
                this.CountryLogo = CountryLogo;
            }

            public String getFreight() {
                return Freight;
            }

            public void setFreight(String Freight) {
                this.Freight = Freight;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String ImagePath) {
                this.ImagePath = ImagePath;
            }

            public String getIsFreeFreight() {
                return IsFreeFreight;
            }

            public void setIsFreeFreight(String IsFreeFreight) {
                this.IsFreeFreight = IsFreeFreight;
            }

            public String getIsFreeTax() {
                return IsFreeTax;
            }

            public void setIsFreeTax(String IsFreeTax) {
                this.IsFreeTax = IsFreeTax;
            }

            public double getMarketPrice() {
                return MarketPrice;
            }

            public void setMarketPrice(double MarketPrice) {
                this.MarketPrice = MarketPrice;
            }

            public double getMinSalePrice() {
                return MinSalePrice;
            }

            public void setMinSalePrice(double MinSalePrice) {
                this.MinSalePrice = MinSalePrice;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String ProductName) {
                this.ProductName = ProductName;
            }

            public int getSaleCounts() {
                return SaleCounts;
            }

            public void setSaleCounts(int SaleCounts) {
                this.SaleCounts = SaleCounts;
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

            public String getTax() {
                return Tax;
            }

            public void setTax(String Tax) {
                this.Tax = Tax;
            }
        }
    }
}
