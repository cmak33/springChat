package com.example.chatapp.controllers.chat;

import com.example.chatapp.models.*;
import com.example.chatapp.services.ChatMessageService;
import com.example.chatapp.services.ChatService;
import com.example.chatapp.services.UserService;
import com.example.chatapp.services.HttpSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final UserService userService;
    private final ChatService chatService;
    private final ChatMessageService chatMessageService;
    private final HttpSessionService webSocketHttpSessionService;

    @MessageMapping("/chat.addByUsername")
    @SendTo("/chat/event")
    public String joinChat(@Payload String username,SimpMessageHeaderAccessor headerAccessor){
        Optional<User> user = userService.findByUsername(username);
        return user.map(userValue->createMessage(userValue,headerAccessor)).orElse(null);
    }

    private String createMessage(User user, SimpMessageHeaderAccessor headerAccessor){
        Optional<Chat> chat = webSocketHttpSessionService.receiveChatFromWebsocketSession(headerAccessor.getSessionAttributes());
        return chat.map(chatValue->{
            chatValue.getParticipants().add(user);
            chatService.saveChat(chatValue);
            return ChatUserEventType.JOIN.formatMessage(user.getUsername());
        }).orElse(null);
    }

    @MessageMapping("/chat.leave")
    @SendTo("/chat/event")
    public String leaveChat(SimpMessageHeaderAccessor messageHeaderAccessor){
        Optional<User> user = findUserByHeaderAccessor(messageHeaderAccessor);
        return user.map(userValue->{
            Optional<Chat> chat = webSocketHttpSessionService.receiveChatFromWebsocketSession(messageHeaderAccessor.getSessionAttributes());
            chat.ifPresent(chatValue->{
                chatService.removeParticipantById(chatValue,userValue.getId());
                chatService.deleteIfHasNoParticipantsOtherwiseSave(chatValue);
            });
            return ChatUserEventType.LEAVE.formatMessage(userValue.getUsername());
        }).orElse(null);
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/chat/message")
    public ChatMessageDTO sendMessage(@Payload String message, SimpMessageHeaderAccessor messageHeaderAccessor){
        Optional<User> user = findUserByHeaderAccessor(messageHeaderAccessor);
        return user.map(userValue->{
            Optional<Chat> chat = webSocketHttpSessionService.receiveChatFromWebsocketSession(messageHeaderAccessor.getSessionAttributes());
            return chat.map(chatValue->{
                chatMessageService.saveByMessage(userValue,chatValue,message);
                return new ChatMessageDTO(userValue.getUsername(),message);
            }).orElse(null);
        }).orElse(null);
    }

    private Optional<User> findUserByHeaderAccessor(SimpMessageHeaderAccessor headerAccessor){
        Optional<User> user;
        if(headerAccessor.getUser() != null){
            String username = headerAccessor.getUser().getName();
            user = userService.findByUsername(username);
        } else{
            user = Optional.empty();
        }
        return user;
    }



}
