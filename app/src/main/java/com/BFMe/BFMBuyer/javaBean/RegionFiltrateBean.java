package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：店铺地区bean
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 15:43
 */
public class RegionFiltrateBean {

    private AreaDataBean areaData;

    public AreaDataBean getAreaData() {
        return areaData;
    }

    public void setAreaData(AreaDataBean areaData) {
        this.areaData = areaData;
    }

    public static class AreaDataBean {
        /**
         * AreaChar : R
         * AreaId : 1
         * AreaName : 日本
         */

        private List<ConuntryBean> Conuntry;
        /**
         * AreaId : 24
         * AreaName : 亚洲
         */

        private List<IntercontinentalBean> Intercontinental;

        public List<ConuntryBean> getConuntry() {
            return Conuntry;
        }

        public void setConuntry(List<ConuntryBean> Conuntry) {
            this.Conuntry = Conuntry;
        }

        public List<IntercontinentalBean> getIntercontinental() {
            return Intercontinental;
        }

        public void setIntercontinental(List<IntercontinentalBean> Intercontinental) {
            this.Intercontinental = Intercontinental;
        }

        public static class ConuntryBean {
            private String AreaChar;
            private String AreaId;
            private String AreaName;

            public String getAreaChar() {
                return AreaChar;
            }

            public void setAreaChar(String AreaChar) {
                this.AreaChar = AreaChar;
            }

            public String getAreaId() {
                return AreaId;
            }

            public void setAreaId(String AreaId) {
                this.AreaId = AreaId;
            }

            public String getAreaName() {
                return AreaName;
            }

            public void setAreaName(String AreaName) {
                this.AreaName = AreaName;
            }
        }

        public static class IntercontinentalBean {
            private String AreaId;
            private String AreaName;

            public String getAreaId() {
                return AreaId;
            }

            public void setAreaId(String AreaId) {
                this.AreaId = AreaId;
            }

            public String getAreaName() {
                return AreaName;
            }

            public void setAreaName(String AreaName) {
                this.AreaName = AreaName;
            }
        }
    }
}
