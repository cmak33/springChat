package com.example.chatapp.services;

import com.example.chatapp.models.Chat;
import com.example.chatapp.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/properties/session.properties")
public class HttpSessionService {
    private final ChatRepository chatRepository;
    @Value("${chatIdSessionAttributeKey}")
    private String chatIdSessionAttributeKey;
    @Value("${httpSessionAttributeKey}")
    private String httpSessionAttributeKey;

    public void putChatIdInHttpSession(Long id){
        HttpSession session = receiveHttpSession();
        if(session!=null) {
            session.setAttribute(chatIdSessionAttributeKey, id);
        }
    }

    private HttpSession receiveHttpSession(){
        ServletRequestAttributes attributes = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
        if(attributes!=null){
            return attributes.getRequest().getSession();
        } else{
            return null;
        }
    }

    public Optional<Chat> receiveChatFromWebsocketSession(Map<String,Object> sessionAttributes){
        Optional<Chat> chat;
        HttpSession session = receiveHttpSessionFromWebSession(sessionAttributes);
        if(hasKey(session, chatIdSessionAttributeKey)){
            Long chatId = (Long)session.getAttribute(chatIdSessionAttributeKey);
            chat = chatRepository.findById(chatId);
        } else {
            chat = Optional.empty();
        }
        return chat;
    }

    private HttpSession receiveHttpSessionFromWebSession(Map<String,Object> sessionAttributes){
        return (HttpSession)sessionAttributes.get(httpSessionAttributeKey);
    }

    private boolean hasKey(HttpSession session,String key){
        return session.getAttribute(key) != null;
    }
}
