package com.quick.localmarketprice;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String name;
    private String price;
    private int imageResource;

    public ProductModel(String name, String price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}
