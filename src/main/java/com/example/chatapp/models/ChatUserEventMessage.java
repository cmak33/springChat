package com.example.chatapp.models;

public record ChatUserEventMessage(String username, ChatUserEventType userEvent){
    public String createMessage(){
        return userEvent.formatMessage(username);
    }
}
