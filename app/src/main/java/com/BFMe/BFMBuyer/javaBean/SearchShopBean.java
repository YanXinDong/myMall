package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class SearchShopBean {


    /**
     * Total : 15
     * Products : [{"Id":"1700","ShopId":616,"ProductName":"NICE BABY婴儿纸尿裤男女宝宝<em>尿不湿<\/em>M中码44片包邮【5-10kg】","ImagePath":"http://img01.bfme.com/Storage/Shop/616/Products/1700","MarketPrice":75,"MinSalePrice":63,"Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"1701","ShopId":616,"ProductName":"NICE BABY婴儿纸尿裤男女宝宝<em>尿不湿<\/em>S小码50片包邮【3-6kg】","ImagePath":"http://img01.bfme.com/Storage/Shop/616/Products/1701","MarketPrice":75,"MinSalePrice":63,"Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"1702","ShopId":616,"ProductName":"NICE BABY婴儿纸尿裤男女宝宝<em>尿不湿<\/em>XL加大码36片包邮【13公斤以上】","ImagePath":"http://img01.bfme.com/Storage/Shop/616/Products/1702","MarketPrice":105,"MinSalePrice":63,"Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"387","ShopId":411,"ProductName":"日本本土花王纸尿裤/<em>尿不湿<\/em>NB60/S54/M42四联包","ImagePath":"http://img01.bfme.com/Storage/Shop/411/Products/387","MarketPrice":410,"MinSalePrice":425,"Country":"其他","CountryLogo":"http://img01.bfme.com/lib/countries/logo/0.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"391","ShopId":411,"ProductName":"日本本土大王纸尿裤/<em>尿不湿<\/em>NB114/S104/M84/L68/XL52 三联包 ","ImagePath":"http://img01.bfme.com/Storage/Shop/411/Products/391","MarketPrice":480,"MinSalePrice":487,"Country":"其他","CountryLogo":"http://img01.bfme.com/lib/countries/logo/0.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"402","ShopId":410,"ProductName":"日本进口Merries花王<em>尿不湿<\/em>M号74片","ImagePath":"http://img01.bfme.com/Storage/Shop/410/Products/402","MarketPrice":150,"MinSalePrice":100,"Country":"其他","CountryLogo":"http://img01.bfme.com/lib/countries/logo/0.jpg","Freight":"","Tax":"","IsFreeFreight":"False","IsFreeTax":"False"},{"Id":"493","ShopId":431,"ProductName":"满199包邮还免代购费！日本进口花王Merries纸尿裤S82宝宝婴儿专用<em>尿不湿<\/em>","ImagePath":"http://img01.bfme.com/Storage/Shop/431/Products/493","MarketPrice":188,"MinSalePrice":154,"Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":"False","IsFreeTax":"True"},{"Id":"1775","ShopId":384,"ProductName":"日本进口花王<em>尿不湿<\/em>XL号50片X1包（12 ~ 22kg）","ImagePath":"http://img01.bfme.com/Storage/Shop/384/Products/1775","MarketPrice":280,"MinSalePrice":215,"Country":"其他","CountryLogo":"http://img01.bfme.com/lib/countries/logo/0.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"1776","ShopId":384,"ProductName":"日本进口花王<em>尿不湿<\/em>L号56片X2包（9 ~ 14kg）","ImagePath":"http://img01.bfme.com/Storage/Shop/384/Products/1776","MarketPrice":450,"MinSalePrice":308,"Country":"其他","CountryLogo":"http://img01.bfme.com/lib/countries/logo/0.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"},{"Id":"1778","ShopId":384,"ProductName":"日本进口花王<em>尿不湿<\/em>M号76片X1包（6~11ｋｇ）","ImagePath":"http://img01.bfme.com/Storage/Shop/384/Products/1778","MarketPrice":230,"MinSalePrice":202,"Country":"其他","CountryLogo":"http://img01.bfme.com/lib/countries/logo/0.jpg","Freight":"","Tax":"","IsFreeFreight":"True","IsFreeTax":"True"}]
     */

    private int Total;
    /**
     * Id : 1700
     * ShopId : 616
     * ProductName : NICE BABY婴儿纸尿裤男女宝宝<em>尿不湿</em>M中码44片包邮【5-10kg】
     * ImagePath : http://img01.bfme.com/Storage/Shop/616/Products/1700
     * MarketPrice : 75
     * MinSalePrice : 63
     * Country : 香港
     * CountryLogo : http://img01.bfme.com/lib/countries/logo/3.jpg
     * Freight :
     * Tax :
     * IsFreeFreight : True
     * IsFreeTax : True
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
        private String Id;
        private String ShopId;
        private String ProductName;
        private String ImagePath;
        private double MarketPrice;
        private double MinSalePrice;
        private String Country;
        private String CountryLogo;
        private String Freight;
        private String Tax;
        private String IsFreeFreight;
        private String IsFreeTax;
        private String ProductId;
        private String SaleCounts;
        private String ShopName;

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getSaleCounts() {
            return SaleCounts;
        }

        public void setSaleCounts(String saleCounts) {
            SaleCounts = saleCounts;
        }

        public String getProductId() {
            return ProductId;
        }

        public void setProductId(String productId) {
            ProductId = productId;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String ShopId) {
            this.ShopId = ShopId;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
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

        public String getTax() {
            return Tax;
        }

        public void setTax(String Tax) {
            this.Tax = Tax;
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

        @Override
        public String toString() {
            return "ProductsBean{" +
                    "Id='" + Id + '\'' +
                    ", ShopId=" + ShopId +
                    ", ProductName='" + ProductName + '\'' +
                    ", ImagePath='" + ImagePath + '\'' +
                    ", MarketPrice=" + MarketPrice +
                    ", MinSalePrice=" + MinSalePrice +
                    ", Country='" + Country + '\'' +
                    ", CountryLogo='" + CountryLogo + '\'' +
                    ", Freight='" + Freight + '\'' +
                    ", Tax='" + Tax + '\'' +
                    ", IsFreeFreight='" + IsFreeFreight + '\'' +
                    ", IsFreeTax='" + IsFreeTax + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchShopBean{" +
                "Total=" + Total +
                ", Products=" + Products +
                '}';
    }
}
