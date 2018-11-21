package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：积分明细
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/18 12:21
 */
public class GradeBean {

    /**
     * TotalItems : 10
     * ItemsPerPage : 10
     * CurrentPage : 1
     * TotalPages : 1
     */

    private PageInfoBean PageInfo;
    /**
     * Id : 4015
     * UserName : 160906162757182875
     * RecordDate : 2016-09-18
     * Integral : 50
     * TypeDesc : 每日登录
     */

    private List<MemberIntegralRecordBean> MemberIntegralRecord;

    public PageInfoBean getPageInfo() {
        return PageInfo;
    }

    public void setPageInfo(PageInfoBean PageInfo) {
        this.PageInfo = PageInfo;
    }

    public List<MemberIntegralRecordBean> getMemberIntegralRecord() {
        return MemberIntegralRecord;
    }

    public void setMemberIntegralRecord(List<MemberIntegralRecordBean> MemberIntegralRecord) {
        this.MemberIntegralRecord = MemberIntegralRecord;
    }

    public static class PageInfoBean {
        private int TotalItems;
        private int ItemsPerPage;
        private int CurrentPage;
        private int TotalPages;

        public int getTotalItems() {
            return TotalItems;
        }

        public void setTotalItems(int TotalItems) {
            this.TotalItems = TotalItems;
        }

        public int getItemsPerPage() {
            return ItemsPerPage;
        }

        public void setItemsPerPage(int ItemsPerPage) {
            this.ItemsPerPage = ItemsPerPage;
        }

        public int getCurrentPage() {
            return CurrentPage;
        }

        public void setCurrentPage(int CurrentPage) {
            this.CurrentPage = CurrentPage;
        }

        public int getTotalPages() {
            return TotalPages;
        }

        public void setTotalPages(int TotalPages) {
            this.TotalPages = TotalPages;
        }
    }

    public static class MemberIntegralRecordBean {
        private int Id;
        private String UserName;
        private String RecordDate;
        private int Integral;
        private String TypeDesc;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getRecordDate() {
            return RecordDate;
        }

        public void setRecordDate(String RecordDate) {
            this.RecordDate = RecordDate;
        }

        public int getIntegral() {
            return Integral;
        }

        public void setIntegral(int Integral) {
            this.Integral = Integral;
        }

        public String getTypeDesc() {
            return TypeDesc;
        }

        public void setTypeDesc(String TypeDesc) {
            this.TypeDesc = TypeDesc;
        }
    }
}
