package com.cby.benstagram.models;

import java.util.ArrayList;

public class MainFeedData {
    private String headerTitle;
    private ArrayList<SingleItemModel> allItemsInSection;
    private Photo photo;

    public enum DataType{
        Photo,
        RecommendUsers,
    }

    public MainFeedData(DataType dataType) {

    }

    public MainFeedData(DataType dataType , Photo photo) {
        this.photo = photo;
    }

    public MainFeedData(String headerTitle, ArrayList<SingleItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
