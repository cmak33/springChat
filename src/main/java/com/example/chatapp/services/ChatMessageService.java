package com.example.chatapp.services;

import com.example.chatapp.models.Chat;
import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;
import com.example.chatapp.repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public void saveByMessage(User creator, Chat chat, String message){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setChat(chat);
        chatMessage.setCreator(creator);
        chatMessageRepository.save(chatMessage);
    }
}
