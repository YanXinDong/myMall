package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/11 14:48
 */
public class ShopFiltrateBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ShopTypeName : 自营店铺
         * Value : 1
         */

        private List<ShopTypesBean> shopTypes;
        /**
         * AreaId : 1
         * AreaName : 日本
         */

        private List<AreasListBean> areasList;

        public List<ShopTypesBean> getShopTypes() {
            return shopTypes;
        }

        public void setShopTypes(List<ShopTypesBean> shopTypes) {
            this.shopTypes = shopTypes;
        }

        public List<AreasListBean> getAreasList() {
            return areasList;
        }

        public void setAreasList(List<AreasListBean> areasList) {
            this.areasList = areasList;
        }

        public static class ShopTypesBean {
            private String ShopTypeName;
            private int Value;
            private boolean isSelected;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public String getShopTypeName() {
                return ShopTypeName;
            }

            public void setShopTypeName(String ShopTypeName) {
                this.ShopTypeName = ShopTypeName;
            }

            public int getValue() {
                return Value;
            }

            public void setValue(int Value) {
                this.Value = Value;
            }
        }

        public static class AreasListBean {
            private int AreaId;
            private String AreaName;
            private boolean isSelected;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }
            public int getAreaId() {
                return AreaId;
            }

            public void setAreaId(int AreaId) {
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
