package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/3 16:29
 */
public class LimitProductFreightAmountBean {

    /**
     * Freight : 卖家承担运费
     * Id : 0
     * IsExpiredShop : false
     * IsMyself : false
     * IsSellerAdminProdcut : false
     * Tax : 税费 ¥ 0
     */

    private DataBean Data;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private double Freight;
        private int Id;
        private boolean IsExpiredShop;
        private boolean IsMyself;
        private boolean IsSellerAdminProdcut;
        private double Tax;

        public double getFreight() {
            return Freight;
        }

        public void setFreight(double Freight) {
            this.Freight = Freight;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public boolean isIsExpiredShop() {
            return IsExpiredShop;
        }

        public void setIsExpiredShop(boolean IsExpiredShop) {
            this.IsExpiredShop = IsExpiredShop;
        }

        public boolean isIsMyself() {
            return IsMyself;
        }

        public void setIsMyself(boolean IsMyself) {
            this.IsMyself = IsMyself;
        }

        public boolean isIsSellerAdminProdcut() {
            return IsSellerAdminProdcut;
        }

        public void setIsSellerAdminProdcut(boolean IsSellerAdminProdcut) {
            this.IsSellerAdminProdcut = IsSellerAdminProdcut;
        }

        public double getTax() {
            return Tax;
        }

        public void setTax(double Tax) {
            this.Tax = Tax;
        }
    }
}
