package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：店铺区域列表
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/11 15:21
 */
public class CountryAreaBean {


    /**
     * CountryAreas : [{"CountryAreaName":"日本","FId":0,"Id":1},{"CountryAreaName":"香港","FId":0,"Id":3},{"CountryAreaName":"法国","FId":0,"Id":4},{"CountryAreaName":"意大利","FId":0,"Id":5},{"CountryAreaName":"德国","FId":0,"Id":6},{"CountryAreaName":"美国","FId":0,"Id":7},{"CountryAreaName":"英国","FId":0,"Id":8},{"CountryAreaName":"瑞典","FId":0,"Id":9},{"CountryAreaName":"瑞士","FId":0,"Id":10},{"CountryAreaName":"澳大利亚","FId":0,"Id":11},{"CountryAreaName":"新西兰","FId":0,"Id":12},{"CountryAreaName":"丹麦","FId":0,"Id":13},{"CountryAreaName":"挪威","FId":0,"Id":14},{"CountryAreaName":"奥地利","FId":0,"Id":15},{"CountryAreaName":"荷兰","FId":0,"Id":16},{"CountryAreaName":"比利时","FId":0,"Id":17},{"CountryAreaName":"西班牙","FId":0,"Id":18},{"CountryAreaName":"台湾","FId":0,"Id":20},{"CountryAreaName":"韩国","FId":0,"Id":22},{"CountryAreaName":"中国","FId":0,"Id":23}]
     * Total : 20
     */

    private int Total;
    /**
     * CountryAreaName : 日本
     * FId : 0
     * Id : 1
     */

    private List<CountryAreasBean> CountryAreas;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<CountryAreasBean> getCountryAreas() {
        return CountryAreas;
    }

    public void setCountryAreas(List<CountryAreasBean> CountryAreas) {
        this.CountryAreas = CountryAreas;
    }

    public static class CountryAreasBean {
        private String CountryAreaName;
        private int FId;
        private int Id;

        public String getCountryAreaName() {
            return CountryAreaName;
        }

        public void setCountryAreaName(String CountryAreaName) {
            this.CountryAreaName = CountryAreaName;
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
    }
}
