package com.yen.androbe.object;

public class Product extends JsonBean{

    public String product_id;
    public String price;
    public String special_price;
    public String name;
    public String thumb;
    public String model;
    public String quantity;
    public String stock;
    public String stock_status;
    public String short_description;
    public String category;

    public Product(String product_id, String name, String price, String thumb) {
        this.product_id = product_id;
        this.price = price;
        this.name = name;
        this.thumb = thumb;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
