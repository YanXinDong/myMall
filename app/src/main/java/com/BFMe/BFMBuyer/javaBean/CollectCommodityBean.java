package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：喜欢的商品
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/22 10:35
 */
public class CollectCommodityBean {

    /**
     * Products : [{"Country":"","CountryImg":"http://img01.baifomi.com/lib/countries/flags/22.png","CurrentPage":1,"DateDate":"2017-05-09","Freight":0,"ImagePath":"http://img01.baifomi.com//Storage/Shop/728/Products/9816/a71222ca0c2ff865.jpg","ItemsPerPage":10,"MarketPrice":285,"MinSalePrice":270,"ProductId":9816,"ProductName":"Whoo后 拱辰享洗面奶泡沫洁面膏 保湿温和深层清洁洗面奶 180ml ","SellerBearFreight":true,"SellerBearTax":true,"ShopId":728,"Tax":"0","TotalItems":"1"}]
     * Total : 1
     */

    private int Total;
    /**
     * Country :
     * CountryImg : http://img01.baifomi.com/lib/countries/flags/22.png
     * CurrentPage : 1
     * DateDate : 2017-05-09
     * Freight : 0
     * ImagePath : http://img01.baifomi.com//Storage/Shop/728/Products/9816/a71222ca0c2ff865.jpg
     * ItemsPerPage : 10
     * MarketPrice : 285.0
     * MinSalePrice : 270.0
     * ProductId : 9816
     * ProductName : Whoo后 拱辰享洗面奶泡沫洁面膏 保湿温和深层清洁洗面奶 180ml
     * SellerBearFreight : true
     * SellerBearTax : true
     * ShopId : 728
     * Tax : 0
     * TotalItems : 1
     */

    private List<ProductsBean> Products;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsBean> Products) {
        this.Products = Products;
    }

    public static class ProductsBean {
        private String Country;
        private String CountryImg;
        private int CurrentPage;
        private String DateDate;
        private int Freight;
        private String ImagePath;
        private int ItemsPerPage;
        private double MarketPrice;
        private double MinSalePrice;
        private String ProductId;
        private String ProductName;
        private boolean SellerBearFreight;
        private boolean SellerBearTax;
        private String ShopId;
        private String Tax;
        private String TotalItems;

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

        public int getCurrentPage() {
            return CurrentPage;
        }

        public void setCurrentPage(int CurrentPage) {
            this.CurrentPage = CurrentPage;
        }

        public String getDateDate() {
            return DateDate;
        }

        public void setDateDate(String DateDate) {
            this.DateDate = DateDate;
        }

        public int getFreight() {
            return Freight;
        }

        public void setFreight(int Freight) {
            this.Freight = Freight;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public int getItemsPerPage() {
            return ItemsPerPage;
        }

        public void setItemsPerPage(int ItemsPerPage) {
            this.ItemsPerPage = ItemsPerPage;
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

        public boolean isSellerBearFreight() {
            return SellerBearFreight;
        }

        public void setSellerBearFreight(boolean SellerBearFreight) {
            this.SellerBearFreight = SellerBearFreight;
        }

        public boolean isSellerBearTax() {
            return SellerBearTax;
        }

        public void setSellerBearTax(boolean SellerBearTax) {
            this.SellerBearTax = SellerBearTax;
        }

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String ShopId) {
            this.ShopId = ShopId;
        }

        public String getTax() {
            return Tax;
        }

        public void setTax(String Tax) {
            this.Tax = Tax;
        }

        public String getTotalItems() {
            return TotalItems;
        }

        public void setTotalItems(String TotalItems) {
            this.TotalItems = TotalItems;
        }
    }
}
