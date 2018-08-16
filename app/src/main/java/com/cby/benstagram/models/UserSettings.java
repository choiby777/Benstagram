package com.cby.benstagram.models;

public class UserSettings {

    private User user;
    private UserAccountSettings setting;

    public UserSettings(User user, UserAccountSettings setting) {
        this.user = user;
        this.setting = setting;
    }

    public UserSettings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccountSettings getSetting() {
        return setting;
    }

    public void setSetting(UserAccountSettings setting) {
        this.setting = setting;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", setting=" + setting +
                '}';
    }
}
