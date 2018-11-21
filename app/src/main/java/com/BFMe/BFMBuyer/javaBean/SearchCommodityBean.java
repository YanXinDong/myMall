package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/15 17:47
 */
public class SearchCommodityBean {

    /**
     * FacetResults : {"bnf":[{"Count":1,"Name":"Bfme"},{"Count":1,"Name":"HKC"}],"ctryf":[{"Count":2,"Name":"其他"}],"tcnf":[{"Count":1,"Name":"电饭煲"},{"Count":1,"Name":"运动跟踪器"}]}
     * Products : [{"Country":"其他","CountryLogo":"http://img01.baifomi.com/lib/countries/flags/0.png","Freight":"","Id":"908","ImagePath":"http://img01.bfme_350.com/Storage/Shop/602/Products/908","IsFreeFreight":"False","IsFreeTax":"False","MarketPrice":0.1,"MinSalePrice":0.05,"ProductName":"测~试~退~款~","SaleCounts":2,"ShopId":602,"ShopName":"tiger28-dp","Tax":""},{"Country":"其他","CountryLogo":"http://img01.baifomi.com/lib/countries/flags/0.png","Freight":"","Id":"994","ImagePath":"http://192.168.1.149/Storage/Shop/602/Products/994/1_350.png","IsFreeFreight":"False","IsFreeTax":"False","MarketPrice":90,"MinSalePrice":80,"ProductName":"HK~物~流~发~货~测~试~","SaleCounts":0,"ShopId":602,"ShopName":"tiger28-dp","Tax":""}]
     * Total : 2
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private FacetResultsBean FacetResults;
        private int Total;
        /**
         * Country : 其他
         * CountryLogo : http://img01.baifomi.com/lib/countries/flags/0.png
         * Freight :
         * Id : 908
         * ImagePath : http://img01.bfme_350.com/Storage/Shop/602/Products/908
         * IsFreeFreight : False
         * IsFreeTax : False
         * MarketPrice : 0.1
         * MinSalePrice : 0.05
         * ProductName : 测~试~退~款~
         * SaleCounts : 2
         * ShopId : 602
         * ShopName : tiger28-dp
         * Tax :
         */

        private List<ProductsBean> Products;

        public FacetResultsBean getFacetResults() {
            return FacetResults;
        }

        public void setFacetResults(FacetResultsBean FacetResults) {
            this.FacetResults = FacetResults;
        }

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

        public static class FacetResultsBean{
            /**
             * Count : 1
             * Name : Bfme
             */

            private List<BnfBean> bnf;
            /**
             * Count : 2
             * Name : 其他
             */

            private List<CtryfBean> ctryf;
            /**
             * Count : 1
             * Name : 电饭煲
             */

            private List<TcnfBean> tcnf;


            public List<BnfBean> getBnf() {
                return bnf;
            }

            public void setBnf(List<BnfBean> bnf) {
                this.bnf = bnf;
            }

            public List<CtryfBean> getCtryf() {
                return ctryf;
            }

            public void setCtryf(List<CtryfBean> ctryf) {
                this.ctryf = ctryf;
            }

            public List<TcnfBean> getTcnf() {
                return tcnf;
            }

            public void setTcnf(List<TcnfBean> tcnf) {
                this.tcnf = tcnf;
            }

            public static class BnfBean {
                private int Count;
                private String Name;
                private boolean isSelected;

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }

                public int getCount() {
                    return Count;
                }

                public void setCount(int Count) {
                    this.Count = Count;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }
            }

            public static class CtryfBean {
                private int Count;
                private String Name;
                private boolean isSelected;

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }
                public int getCount() {
                    return Count;
                }

                public void setCount(int Count) {
                    this.Count = Count;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }
            }

            public static class TcnfBean {
                private int Count;
                private String Name;
                private boolean isSelected;

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }
                public int getCount() {
                    return Count;
                }

                public void setCount(int Count) {
                    this.Count = Count;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }
            }
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
