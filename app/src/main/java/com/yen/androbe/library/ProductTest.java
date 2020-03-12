package com.yen.androbe.library;

public class ProductTest {

    protected String name;
    protected String image;
    protected String price;

    public ProductTest(String name, String image, String price) {
        this.name = name;
        this.image = image;
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
