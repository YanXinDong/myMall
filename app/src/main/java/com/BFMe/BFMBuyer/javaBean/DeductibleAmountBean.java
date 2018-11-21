package com.BFMe.BFMBuyer.javaBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class DeductibleAmountBean {

    /**
     * IntegralPerMoney : 0.0
     * UserIntegrals : 0.0
     */

    private DeductibleAmount DeductibleAmount;

    public DeductibleAmount getDeductibleAmount() {
        return DeductibleAmount;
    }

    public void setDeductibleAmount(DeductibleAmount DeductibleAmount) {
        this.DeductibleAmount = DeductibleAmount;
    }

    public static class DeductibleAmount {
        private double IntegralPerMoney;
        private double UserIntegrals;

        public double getIntegralPerMoney() {
            return IntegralPerMoney;
        }

        public void setIntegralPerMoney(double IntegralPerMoney) {
            this.IntegralPerMoney = IntegralPerMoney;
        }

        public double getUserIntegrals() {
            return UserIntegrals;
        }

        public void setUserIntegrals(double UserIntegrals) {
            this.UserIntegrals = UserIntegrals;
        }
    }
}
