package com.cby.benstagram.models;

public class CommentUser {

    private Comment comment;
    private UserAccountSettings userAccountSettings;

    public CommentUser() {
    }

    public CommentUser(Comment comment, UserAccountSettings userAccountSettings) {
        this.comment = comment;
        this.userAccountSettings = userAccountSettings;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public UserAccountSettings getUserAccountSettings() {
        return userAccountSettings;
    }

    public void setUserAccountSettings(UserAccountSettings userAccountSettings) {
        this.userAccountSettings = userAccountSettings;
    }

    @Override
    public String toString() {
        return "CommentUser{" +
                "comment=" + comment +
                ", userAccountSettings=" + userAccountSettings +
                '}';
    }
}
