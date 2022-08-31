package com.example.chatapp.models;

public enum ChatUserEventType {
    LEAVE("%s has left the chat"),JOIN("%s has joined the chat"),
    ONLINE("%s is online now"),OFFLINE("%s is offline now");

    private final String eventMessageFormat;

    ChatUserEventType(String messageFormat){
        this.eventMessageFormat = messageFormat;
    }

    public String formatMessage(String username){
        return String.format(eventMessageFormat,username);
    }
}
