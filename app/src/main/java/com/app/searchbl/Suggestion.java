package com.app.searchbl;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("psize")
    @Expose
    private Integer psize;
    @SerializedName("word")
    @Expose
    private List<String> word = null;
    @SerializedName("category")
    @Expose
    private List<Category> category = null;
    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("history")
    @Expose
    private List<Object> history = null;
    @SerializedName("keyword")
    @Expose
    private List<Object> keyword = null;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("time")
    @Expose
    private String time;

    public Integer getPsize() {
        return psize;
    }

    public void setPsize(Integer psize) {
        this.psize = psize;
    }

    public List<String> getWord() {
        return word;
    }

    public void setWord(List<String> word) {
        this.word = word;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Object> getHistory() {
        return history;
    }

    public void setHistory(List<Object> history) {
        this.history = history;
    }

    public List<Object> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<Object> keyword) {
        this.keyword = keyword;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}