package com.cby.benstagram.models;

import java.util.Map;

public class ChatRoom {

    private String roomId;
    private String last_message;
    private String roomTitle;
    private Map<String,Boolean> userIds;

    public ChatRoom() {
    }

    public ChatRoom(String roomId, String last_message, String roomTitle, Map<String, Boolean> userIds) {
        this.roomId = roomId;
        this.last_message = last_message;
        this.roomTitle = roomTitle;
        this.userIds = userIds;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomId='" + roomId + '\'' +
                ", last_message='" + last_message + '\'' +
                ", roomTitle='" + roomTitle + '\'' +
                ", userIds=" + userIds +
                '}';
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public Map<String, Boolean> getUserIds() {
        return userIds;
    }

    public void setUserIds(Map<String, Boolean> userIds) {
        this.userIds = userIds;
    }
}
