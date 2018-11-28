package com.cby.benstagram.models;

public class RecmmendUserInfo {
    private String userName;
    private String description;
    private String imageUrl;

    public RecmmendUserInfo(String userName, String description, String imageUrl) {
        this.userName = userName;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
