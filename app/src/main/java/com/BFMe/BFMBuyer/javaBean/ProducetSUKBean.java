package com.BFMe.BFMBuyer.javaBean;

/**
 * Created by Administrator on 2016/10/13.
 * 商品sku信息
 */
public class ProducetSUKBean {


    /**
     * Id : 371_0_0_0
     * ProductId : 371
     * Stock : 94
     * SalePrice : 22.00
     * Tax : 11.00
     */

    private String Id;
    private String ProductId;
    private String Stock;
    private String SalePrice;
    private String Tax;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String Stock) {
        this.Stock = Stock;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String SalePrice) {
        this.SalePrice = SalePrice;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String Tax) {
        this.Tax = Tax;
    }
}
