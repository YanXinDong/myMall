package com.BFMe.BFMBuyer.javaBean;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/27 17:18
 */
public class ShopDetailBean  {

    /**
     * BaseLogo :
     * CountryLogo : http://img01.baifomi.com/lib/countries/logo/.jpg
     * Description : 我的Q22WODE 2我的世界222222222
     * GuaranteeSeller : {"BZMJ":"该卖家已经缴纳BUY FOR ME 保证金请放心购物","HWJK":"卖家保证所有商品均来自海外，BUY FOR ME海淘会 定期抽查店铺商品，保障商品品质","SFYZ":{"Address":"","Contacts":"","DefaultWords":"该卖家由BUY FOR ME审核认证","Phone":""},"ZYMJ":"BUY FOR ME海淘值得信赖的认证星级会员"}
     * IsAttention : false
     * IsBestSeller : false
     * IsGuaranteeGoods : false
     * IsGuaranteeSeller : true
     * IsIdentityVerification : true
     * IsQualityGoods : true
     * Logo : http://192.168.1.149:8088/Storage/Shop/602/ImageAd/a3b751b2628b2b85.jpg
     * NumOfFavor : 54
     * ProducCount : 22
     * SellerIMID : bfme_8b183a55e23c47bb9bd16d375
     * ShareLink : http://aaa.bfme.com/1/602
     * ShopId : 602
     * ShopName : tiger28-dp
     * ThisMonthViewNum : 5324
     * TotalBalance : 50000.00
     * UserName : tiger28
     */

    private String BaseLogo;
    private String CountryLogo;
    private String Description;
    /**
     * BZMJ : 该卖家已经缴纳BUY FOR ME 保证金请放心购物
     * HWJK : 卖家保证所有商品均来自海外，BUY FOR ME海淘会 定期抽查店铺商品，保障商品品质
     * SFYZ : {"Address":"","Contacts":"","DefaultWords":"该卖家由BUY FOR ME审核认证","Phone":""}
     * ZYMJ : BUY FOR ME海淘值得信赖的认证星级会员
     */

    private GuaranteeSellerBean GuaranteeSeller;
    private boolean IsAttention;
    private boolean IsBestSeller;
    private boolean IsGuaranteeGoods;
    private boolean IsGuaranteeSeller;
    private boolean IsIdentityVerification;
    private boolean IsQualityGoods;
    private String Logo;
    private String NumOfFavor;
    private String ProducCount;
    private String SellerIMID;
    private String ShareLink;
    private int ShopId;
    private String ShopName;
    private String ThisMonthViewNum;
    private String TotalBalance;
    private String UserName;

    public String getBaseLogo() {
        return BaseLogo;
    }

    public void setBaseLogo(String BaseLogo) {
        this.BaseLogo = BaseLogo;
    }

    public String getCountryLogo() {
        return CountryLogo;
    }

    public void setCountryLogo(String CountryLogo) {
        this.CountryLogo = CountryLogo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public GuaranteeSellerBean getGuaranteeSeller() {
        return GuaranteeSeller;
    }

    public void setGuaranteeSeller(GuaranteeSellerBean GuaranteeSeller) {
        this.GuaranteeSeller = GuaranteeSeller;
    }

    public boolean isIsAttention() {
        return IsAttention;
    }

    public void setIsAttention(boolean IsAttention) {
        this.IsAttention = IsAttention;
    }

    public boolean isIsBestSeller() {
        return IsBestSeller;
    }

    public void setIsBestSeller(boolean IsBestSeller) {
        this.IsBestSeller = IsBestSeller;
    }

    public boolean isIsGuaranteeGoods() {
        return IsGuaranteeGoods;
    }

    public void setIsGuaranteeGoods(boolean IsGuaranteeGoods) {
        this.IsGuaranteeGoods = IsGuaranteeGoods;
    }

    public boolean isIsGuaranteeSeller() {
        return IsGuaranteeSeller;
    }

    public void setIsGuaranteeSeller(boolean IsGuaranteeSeller) {
        this.IsGuaranteeSeller = IsGuaranteeSeller;
    }

    public boolean isIsIdentityVerification() {
        return IsIdentityVerification;
    }

    public void setIsIdentityVerification(boolean IsIdentityVerification) {
        this.IsIdentityVerification = IsIdentityVerification;
    }

    public boolean isIsQualityGoods() {
        return IsQualityGoods;
    }

    public void setIsQualityGoods(boolean IsQualityGoods) {
        this.IsQualityGoods = IsQualityGoods;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public String getNumOfFavor() {
        return NumOfFavor;
    }

    public void setNumOfFavor(String NumOfFavor) {
        this.NumOfFavor = NumOfFavor;
    }

    public String getProducCount() {
        return ProducCount;
    }

    public void setProducCount(String ProducCount) {
        this.ProducCount = ProducCount;
    }

    public String getSellerIMID() {
        return SellerIMID;
    }

    public void setSellerIMID(String SellerIMID) {
        this.SellerIMID = SellerIMID;
    }

    public String getShareLink() {
        return ShareLink;
    }

    public void setShareLink(String ShareLink) {
        this.ShareLink = ShareLink;
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

    public String getThisMonthViewNum() {
        return ThisMonthViewNum;
    }

    public void setThisMonthViewNum(String ThisMonthViewNum) {
        this.ThisMonthViewNum = ThisMonthViewNum;
    }

    public String getTotalBalance() {
        return TotalBalance;
    }

    public void setTotalBalance(String TotalBalance) {
        this.TotalBalance = TotalBalance;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public static class GuaranteeSellerBean {
        private String BZMJ;
        private String HWJK;
        /**
         * Address :
         * Contacts :
         * DefaultWords : 该卖家由BUY FOR ME审核认证
         * Phone :
         */

        private SFYZBean SFYZ;
        private String ZYMJ;

        public String getBZMJ() {
            return BZMJ;
        }

        public void setBZMJ(String BZMJ) {
            this.BZMJ = BZMJ;
        }

        public String getHWJK() {
            return HWJK;
        }

        public void setHWJK(String HWJK) {
            this.HWJK = HWJK;
        }

        public SFYZBean getSFYZ() {
            return SFYZ;
        }

        public void setSFYZ(SFYZBean SFYZ) {
            this.SFYZ = SFYZ;
        }

        public String getZYMJ() {
            return ZYMJ;
        }

        public void setZYMJ(String ZYMJ) {
            this.ZYMJ = ZYMJ;
        }

        public static class SFYZBean {
            private String Address;
            private String Contacts;
            private String DefaultWords;
            private String Name;
            private String Phone;

            public String getAddress() {
                return Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public String getContacts() {
                return Contacts;
            }

            public void setContacts(String Contacts) {
                this.Contacts = Contacts;
            }

            public String getDefaultWords() {
                return DefaultWords;
            }

            public void setDefaultWords(String DefaultWords) {
                this.DefaultWords = DefaultWords;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getPhone() {
                return Phone;
            }

            public void setPhone(String Phone) {
                this.Phone = Phone;
            }
        }
    }
}
