package com.example.chatapp.services;

import com.example.chatapp.models.Chat;
import com.example.chatapp.models.User;
import com.example.chatapp.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    public void saveChat(Chat chat){
        chatRepository.save(chat);
    }

    public Chat saveNewChatByName(String chatName){
        Chat chat = new Chat();
        chat.setName(chatName);
        User user = userService.receiveCurrentUser();
        chat.getParticipants().add(user);
        chat.setCreator(user);
        saveChat(chat);
        return chat;
    }

    public Optional<Chat> findById(Long id){
        return chatRepository.findById(id);
    }

    public void deleteById(Long id){
        chatRepository.deleteById(id);
    }

    public boolean isCurrentUserTheCreatorOfChat(Long chatId){
        boolean isCreator;
        Optional<Chat> chat = chatRepository.findById(chatId);
        isCreator = chat.map(value -> value.getCreator().getId().equals(userService.receiveCurrentUserId())).orElse(false);
        return isCreator;
    }

    public boolean isCurrentUserParticipantOfChat(Chat chat){
        Long userId = userService.receiveCurrentUserId();
        return chat.getParticipants().stream().anyMatch(user->user.getId().equals(userId));
    }

    public void deleteIfHasNoParticipantsOtherwiseSave(Chat chat){
        if(chat.getParticipants().isEmpty()){
            deleteById(chat.getId());
        } else{
            saveChat(chat);
        }
    }

    public void removeParticipantById(Chat chat,Long userId){
        chat.getParticipants().removeIf(value->value.getId().equals(userId));
    }
}
