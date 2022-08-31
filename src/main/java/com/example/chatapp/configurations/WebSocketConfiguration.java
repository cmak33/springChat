package com.example.chatapp.configurations;

import com.example.chatapp.webSocketComponents.SessionHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry endpointRegistry){
        endpointRegistry.addEndpoint("/endpoint").addInterceptors(handshakeInterceptor()).withSockJS();
    }

    @Bean
    public SessionHandshakeInterceptor handshakeInterceptor(){
        return new SessionHandshakeInterceptor();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry brokerRegistry){
        brokerRegistry.setApplicationDestinationPrefixes("/application");
        brokerRegistry.enableSimpleBroker("/chat");
    }
}
