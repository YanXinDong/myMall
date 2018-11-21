package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/13 12:04
 */
public class ShopBean {


    /**
     * Id : 0
     * ProductId : 0
     * SaleCounts : 0
     * ShopId : 364
     * ShopName : 香港屈臣氏
     * ImagePath : http://img01.bfme.com/Storage/Shop/364/ImageAd/201602161008207231610.jpg
     * Address : 国际个人
     * Description :
     */


        private int Id;
        private int ProductId;
        private int SaleCounts;
        private int ShopId;
        private String ShopName;
        private String ImagePath;
        private String Address;
        private String Description;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public int getSaleCounts() {
            return SaleCounts;
        }

        public void setSaleCounts(int SaleCounts) {
            this.SaleCounts = SaleCounts;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

    public ShopBean() {
    }

    public ShopBean(int id, int productId, int saleCounts, int shopId, String shopName, String imagePath, String address, String description) {
        Id = id;
        ProductId = productId;
        SaleCounts = saleCounts;
        ShopId = shopId;
        ShopName = shopName;
        ImagePath = imagePath;
        Address = address;
        Description = description;
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "Id=" + Id +
                ", ProductId=" + ProductId +
                ", SaleCounts=" + SaleCounts +
                ", ShopId=" + ShopId +
                ", ShopName='" + ShopName + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                ", Address='" + Address + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }
}
