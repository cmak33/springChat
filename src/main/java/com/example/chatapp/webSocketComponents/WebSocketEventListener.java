package com.example.chatapp.webSocketComponents;

import com.example.chatapp.models.ChatUserEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessagingTemplate messageSendingOperations;

    @EventListener
    public void handleWebSocketConnectionListener(SessionConnectedEvent connectedEvent){
        receiveUsernameFromEvent(connectedEvent).ifPresent(username->
            messageSendingOperations.convertAndSend("/chat/event",ChatUserEventType.ONLINE.formatMessage(username)));
    }

    @EventListener
    public void handleWebSocketConnectionListener(SessionDisconnectEvent disconnectEvent){
        receiveUsernameFromEvent(disconnectEvent).ifPresent(username->
                messageSendingOperations.convertAndSend("/chat/event",ChatUserEventType.OFFLINE.formatMessage(username)));
    }

    private Optional<String> receiveUsernameFromEvent(AbstractSubProtocolEvent event){
        Optional<String> username;
        if(event.getUser() != null){
            username = Optional.of(event.getUser().getName());
        } else{
            username = Optional.empty();
        }
        return username;
    }
}
