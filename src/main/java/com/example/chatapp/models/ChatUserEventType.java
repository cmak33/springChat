package com.example.chatapp.models;

import java.text.MessageFormat;

public enum ChatUserEventType {
    LEAVE("{0} has left the chat"),JOIN("{0} has joined the chat"),
    ONLINE("{0} is online now"),OFFLINE("{0} is offline now");

    private final MessageFormat eventMessageFormat;

    ChatUserEventType(String messageFormat){
        this.eventMessageFormat = new MessageFormat(messageFormat);
    }

    public String formatMessage(String username){
        return eventMessageFormat.format(username);
    }
}
