package com.cby.benstagram.models;

import java.util.Date;

public class ChattingMessage {

    private String messageId;
    private String messageType;
    private String messageText;
    private String userId;
    private String chattingRoomId;

    public ChattingMessage() {
    }

    public ChattingMessage(String messageId, String messageType, String messageText, String userId, String chattingRoomId) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.messageText = messageText;
        this.userId = userId;
        this.chattingRoomId = chattingRoomId;
    }

    @Override
    public String toString() {
        return "ChattingMessage{" +
                "messageId='" + messageId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", messageText='" + messageText + '\'' +
                ", userId='" + userId + '\'' +
                ", chattingRoomId='" + chattingRoomId + '\'' +
                '}';
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChattingRoomId() {
        return chattingRoomId;
    }

    public void setChattingRoomId(String chattingRoomId) {
        this.chattingRoomId = chattingRoomId;
    }
}
