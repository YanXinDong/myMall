package com.BFMe.BFMBuyer.javaBean;

/**
 * Created by BFMe.miao on 2018/3/1.
 */

public class BankInfoBean {


    /**
     * data : {"BankName":"滙豐銀行","Account":"848-741377-001","AccountName":"BFM International Company Limited"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * BankName : 滙豐銀行
         * Account : 848-741377-001
         * AccountName : BFM International Company Limited
         */

        private String BankName;
        private String Account;
        private String AccountName;

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String BankName) {
            this.BankName = BankName;
        }

        public String getAccount() {
            return Account;
        }

        public void setAccount(String Account) {
            this.Account = Account;
        }

        public String getAccountName() {
            return AccountName;
        }

        public void setAccountName(String AccountName) {
            this.AccountName = AccountName;
        }
    }
}
