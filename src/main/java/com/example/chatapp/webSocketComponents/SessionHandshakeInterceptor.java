package com.example.chatapp.webSocketComponents;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@PropertySource("classpath:/properties/session.properties")
public class SessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Value("${httpSessionAttributeKey}")
    private String httpSessionAttributeKey;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);
            if (httpSession != null) {
                attributes.put(httpSessionAttributeKey,httpSession);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
