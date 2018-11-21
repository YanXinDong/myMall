package com.BFMe.BFMBuyer.main.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/12 19:20
 */
public class ClassifyBean {

    /**
     * Icon : http://img01.baifomi.com/areas/mobile/categories/20170912/temp/mzxs.png
     * Id : 580
     * Name :
     */

    private List<CategoryBean> Category;

    public List<CategoryBean> getCategory() {
        return Category;
    }

    public void setCategory(List<CategoryBean> Category) {
        this.Category = Category;
    }

    public static class CategoryBean {
        private String Icon;
        private int Id;
        private String Name;

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String Icon) {
            this.Icon = Icon;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
