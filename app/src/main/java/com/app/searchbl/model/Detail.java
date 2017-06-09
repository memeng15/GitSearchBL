package com.app.searchbl.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("products")
    @Expose
    private List<DetailPrd> products = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DetailPrd> getProducts() {
        return products;
    }

    public void setProducts(List<DetailPrd> products) {
        this.products = products;
    }

}