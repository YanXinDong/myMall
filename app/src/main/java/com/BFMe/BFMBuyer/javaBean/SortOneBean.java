package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class SortOneBean {

    /**
     * Name : 图书
     * Id : 1
     * Icon : http://img.bfme.com/Storage/Plat/Category/201411280221592666310.jpg
     * SubCategory : [{"Name":"教育","Id":"75","Icon":"","SubCategory":[{"Id":"570","Name":"测试","Icon":""},{"Id":"79","Name":"教材","Icon":""},{"Id":"78","Name":"考试","Icon":""},{"Id":"77","Name":"外语学习","Icon":""}]},{"Name":"少儿","Id":"4","Icon":"","SubCategory":[{"Id":"73","Name":"儿童教育","Icon":""},{"Id":"39","Name":"少儿英语","Icon":""},{"Id":"38","Name":"动漫/卡通","Icon":""}]},{"Name":"电子书","Id":"2","Icon":"","SubCategory":[{"Id":"619","Name":"加一个","Icon":""},{"Id":"29","Name":"小说","Icon":""},{"Id":"618","Name":"1","Icon":""},{"Id":"33","Name":"数字杂志","Icon":""},{"Id":"30","Name":"多媒体图书","Icon":""}]},{"Name":"文学","Id":"3","Icon":"","SubCategory":[{"Id":"615","Name":"123123","Icon":""},{"Id":"36","Name":"名家作品","Icon":""},{"Id":"35","Name":"世界名著","Icon":""},{"Id":"34","Name":"诗歌词曲","Icon":""}]},{"Name":"人文社科","Id":"76","Icon":"","SubCategory":[{"Id":"82","Name":"历史","Icon":""},{"Id":"81","Name":"法律","Icon":""},{"Id":"80","Name":"社会科学","Icon":""}]}]
     */

    private List<CategoryBean> Category;

    public List<CategoryBean> getCategory() {
        return Category;
    }

    public void setCategory(List<CategoryBean> Category) {
        this.Category = Category;
    }

    public static class CategoryBean {
        private String Name;
        private String Id;
        private String Icon;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        /**
         * Name : 教育
         * Id : 75
         * Icon :
         * SubCategory : [{"Id":"570","Name":"测试","Icon":""},{"Id":"79","Name":"教材","Icon":""},{"Id":"78","Name":"考试","Icon":""},{"Id":"77","Name":"外语学习","Icon":""}]
         */

        private List<SubCategoryBeanOne> SubCategory;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String Icon) {
            this.Icon = Icon;
        }

        public List<SubCategoryBeanOne> getSubCategory() {
            return SubCategory;
        }

        public void setSubCategory(List<SubCategoryBeanOne> SubCategory) {
            this.SubCategory = SubCategory;
        }

        public static class SubCategoryBeanOne {
            private String Name;
            private String Id;
            private String Icon;
            /**
             * Id : 570
             * Name : 测试
             * Icon :
             */

            private List<SubCategoryBeanTwo> SubCategory;

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getIcon() {
                return Icon;
            }

            public void setIcon(String Icon) {
                this.Icon = Icon;
            }

            public List<SubCategoryBeanTwo> getSubCategory() {
                return SubCategory;
            }

            public void setSubCategory(List<SubCategoryBeanTwo> SubCategory) {
                this.SubCategory = SubCategory;
            }

            public static class SubCategoryBeanTwo {
                private String Id;
                private String Name;
                private String Icon;

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

                public String getIcon() {
                    return Icon;
                }

                public void setIcon(String Icon) {
                    this.Icon = Icon;
                }

                @Override
                public String toString() {
                    return "SubCategoryBeanTwo{" +
                            "Id='" + Id + '\'' +
                            ", Name='" + Name + '\'' +
                            ", Icon='" + Icon + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "SubCategoryBeanOne{" +
                        "Name='" + Name + '\'' +
                        ", Id='" + Id + '\'' +
                        ", Icon='" + Icon + '\'' +
                        ", SubCategory=" + SubCategory +
                        '}';
            }
        }
    }


}
