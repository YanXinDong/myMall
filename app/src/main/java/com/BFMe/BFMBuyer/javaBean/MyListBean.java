package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：我的页面列表数据
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/19 15:35
 */
public class MyListBean {

    private List<ListData> listData;

    public static class ListData{
        private int imgPath;
        private String name;
        private String info;
        private boolean isGoShop;

        public int getImgPath() {
            return imgPath;
        }

        public void setImgPath(int imgPath) {
            this.imgPath = imgPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public boolean isGoShop() {
            return isGoShop;
        }

        public void setGoShop(boolean goShop) {
            isGoShop = goShop;
        }
    }

    public List<ListData> getListData() {
        return listData;
    }

    public void setListData(List<ListData> listData) {
        this.listData = listData;
    }
}
