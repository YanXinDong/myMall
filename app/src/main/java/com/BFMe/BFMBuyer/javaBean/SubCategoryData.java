package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/12 19:46
 */
public class SubCategoryData {

    /**
     * Category : [{"Icon":"","Id":"581","Name":"彩妆","SubCategory":[{"Icon":"http://img.bfme.com/Storage/Plat/Category/201601221035125148700.jpg","Id":"582","Name":"卸妆"}]}]
     * ImgPath : http://img.bfme.com/Storage/Plat/Category/201509141454596661180.png
     */

    private String ImgPath;
    /**
     * Icon :
     * Id : 581
     * Name : 彩妆
     * SubCategory : [{"Icon":"http://img.bfme.com/Storage/Plat/Category/201601221035125148700.jpg","Id":"582","Name":"卸妆"}]
     */

    private List<CategoryBean> Category;

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String ImgPath) {
        this.ImgPath = ImgPath;
    }

    public List<CategoryBean> getCategory() {
        return Category;
    }

    public void setCategory(List<CategoryBean> Category) {
        this.Category = Category;
    }

    public static class CategoryBean {
        private String Icon;
        private String Id;
        private String Name;
        /**
         * Icon : http://img.bfme.com/Storage/Plat/Category/201601221035125148700.jpg
         * Id : 582
         * Name : 卸妆
         */

        private List<SubCategoryBean> SubCategory;

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String Icon) {
            this.Icon = Icon;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public List<SubCategoryBean> getSubCategory() {
            return SubCategory;
        }

        public void setSubCategory(List<SubCategoryBean> SubCategory) {
            this.SubCategory = SubCategory;
        }

        public static class SubCategoryBean {
            private String Icon;
            private String Id;
            private String Name;

            public String getIcon() {
                return Icon;
            }

            public void setIcon(String Icon) {
                this.Icon = Icon;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
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
}
