package com.cby.benstagram.models;

public class MessageListItem {

    private ChattingMessage messageInfo;
    private User userInfo;

    public MessageListItem() {
    }

    public MessageListItem(ChattingMessage messageInfo, User userInfo) {
        this.messageInfo = messageInfo;
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "MessageListItem{" +
                "messageInfo=" + messageInfo +
                ", userInfo=" + userInfo +
                '}';
    }

    public ChattingMessage getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(ChattingMessage messageInfo) {
        this.messageInfo = messageInfo;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
