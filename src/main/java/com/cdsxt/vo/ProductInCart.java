package com.cdsxt.vo;

public class ProductInCart {

    // "pid":40,"image":"nan_yifu_04.jpg","shopPrice":288,"count":1,"pname"
    private Integer pid;
    private String image;
    private Double shopPrice;
    private Integer count;
    private String pname;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    @Override
    public String toString() {
        return "ProductInCart{" +
                "pid=" + pid +
                ", image='" + image + '\'' +
                ", shopPrice=" + shopPrice +
                ", count=" + count +
                ", pname='" + pname + '\'' +
                '}';
    }
}
