package com.quick.localmarketprice;

import java.io.Serializable;
import java.util.List;

public class ItemModel implements Serializable {
    private String id;
    private String name;
    private String price;
    private String image;
    private List<MarketModel> markets;

    public ItemModel() {
    }

    public ItemModel(String id, String name, String price, String image, List<MarketModel> markets) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.markets = markets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<MarketModel> getMarkets() {
        return markets;
    }

    public void setMarkets(List<MarketModel> markets) {
        this.markets = markets;
    }
}
