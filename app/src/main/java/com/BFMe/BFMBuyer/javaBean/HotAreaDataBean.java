package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：店铺页面推荐地区和洲际名称
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/5 12:29
 */
public class HotAreaDataBean {

    private AreasBean areas;

    public AreasBean getAreas() {
        return areas;
    }

    public void setAreas(AreasBean areas) {
        this.areas = areas;
    }

    public static class AreasBean {
        /**
         * CountryAreaDate : /Date(1447039231000-0000)/
         * CountryAreaName : 香港
         * CountryAreaNum : 00852
         * CountryAreaOrder : 8
         * FId : 0
         * Id : 3
         * ImgPath : http://img01.baifomi.com//temp/image/20170303/6362415601103710936284505.jpg
         * IntercontinentalId : 24
         * Sort : 1
         */

        private List<AreaListBean> areaList;
        /**
         * CreateTime : /Date(1493884597000-0000)/
         * Id : 24
         * ImgPath : http://img01.baifomi.com//Storage/Plat/temp/22782c9d6cc442a2.jpg
         * IntercontinentalName : 亚洲
         * IsDel : 0
         * Sort : 1
         */

        private List<InterListBean> interList;

        public List<AreaListBean> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<AreaListBean> areaList) {
            this.areaList = areaList;
        }

        public List<InterListBean> getInterList() {
            return interList;
        }

        public void setInterList(List<InterListBean> interList) {
            this.interList = interList;
        }

        public static class AreaListBean {
            private String CountryAreaDate;
            private String CountryAreaName;
            private String CountryAreaNum;
            private int CountryAreaOrder;
            private int FId;
            private int Id;
            private String ImgPath;
            private int IntercontinentalId;
            private int Sort;

            public String getCountryAreaDate() {
                return CountryAreaDate;
            }

            public void setCountryAreaDate(String CountryAreaDate) {
                this.CountryAreaDate = CountryAreaDate;
            }

            public String getCountryAreaName() {
                return CountryAreaName;
            }

            public void setCountryAreaName(String CountryAreaName) {
                this.CountryAreaName = CountryAreaName;
            }

            public String getCountryAreaNum() {
                return CountryAreaNum;
            }

            public void setCountryAreaNum(String CountryAreaNum) {
                this.CountryAreaNum = CountryAreaNum;
            }

            public int getCountryAreaOrder() {
                return CountryAreaOrder;
            }

            public void setCountryAreaOrder(int CountryAreaOrder) {
                this.CountryAreaOrder = CountryAreaOrder;
            }

            public int getFId() {
                return FId;
            }

            public void setFId(int FId) {
                this.FId = FId;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getImgPath() {
                return ImgPath;
            }

            public void setImgPath(String ImgPath) {
                this.ImgPath = ImgPath;
            }

            public int getIntercontinentalId() {
                return IntercontinentalId;
            }

            public void setIntercontinentalId(int IntercontinentalId) {
                this.IntercontinentalId = IntercontinentalId;
            }

            public int getSort() {
                return Sort;
            }

            public void setSort(int Sort) {
                this.Sort = Sort;
            }
        }

        public static class InterListBean {
            private String CreateTime;
            private int Id;
            private String ImgPath;
            private String IntercontinentalName;
            private int IsDel;
            private int Sort;

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getImgPath() {
                return ImgPath;
            }

            public void setImgPath(String ImgPath) {
                this.ImgPath = ImgPath;
            }

            public String getIntercontinentalName() {
                return IntercontinentalName;
            }

            public void setIntercontinentalName(String IntercontinentalName) {
                this.IntercontinentalName = IntercontinentalName;
            }

            public int getIsDel() {
                return IsDel;
            }

            public void setIsDel(int IsDel) {
                this.IsDel = IsDel;
            }

            public int getSort() {
                return Sort;
            }

            public void setSort(int Sort) {
                this.Sort = Sort;
            }
        }
    }
}
