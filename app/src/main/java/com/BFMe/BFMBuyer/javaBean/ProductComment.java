package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 * 评价bean
 */
public class ProductComment {

    private List<ProductCommentData> Productcomment;

    public List<ProductCommentData> getProductcomment() {
        return Productcomment;
    }

    public void setProductcomment(List<ProductCommentData> productcomment) {
        Productcomment = productcomment;
    }

    public static class ProductCommentData{
        private String subOrderId;
        private int star;
        private String Content;
        private String Pics;

        public String getSubOrderId() {
            return subOrderId;
        }

        public void setSubOrderId(String subOrderId) {
            this.subOrderId = subOrderId;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getPics() {
            return Pics;
        }

        public void setPics(String pics) {
            Pics = pics;
        }

        @Override
        public String toString() {
            return "ProductCommentData{" +
                    "subOrderId='" + subOrderId + '\'' +
                    ", star=" + star +
                    ", Content='" + Content + '\'' +
                    ", Pics='" + Pics + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ProductComment{" +
                "Productcomment=" + Productcomment +
                '}';
    }
}
