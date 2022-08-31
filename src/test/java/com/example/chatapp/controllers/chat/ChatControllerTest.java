package com.example.chatapp.controllers.chat;

import com.example.chatapp.models.Chat;
import com.example.chatapp.models.User;
import com.example.chatapp.services.ChatService;
import com.example.chatapp.services.HttpSessionService;
import com.example.chatapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = ChatController.class)
class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private ChatService chatService;
    @MockBean
    private HttpSessionService httpSessionService;

    @Test
    void chat_ChatFound() throws Exception{
        Chat chat = new Chat();
        chat.setId(1L);
        boolean isCreator = true;

        when(chatService.findById(chat.getId())).thenReturn(Optional.of(chat));
        when(chatService.isCurrentUserParticipantOfChat(chat)).thenReturn(true);
        when(chatService.isCurrentUserTheCreatorOfChat(chat.getId())).thenReturn(isCreator);

        String url = String.format("/chat/%d",chat.getId());

        mockMvc.perform(get(url).with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("chat",chat))
                .andExpect(model().attribute("isCreator",isCreator));
    }

    @Test
    void chat_ChatNotFound() throws Exception{
        Long id = 1L;

        when(chatService.findById(id)).thenReturn(Optional.empty());

        String url = String.format("/chat/%d",id);

        mockMvc.perform(get(url).with(user(new User())))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteChat_UserIsCreator_AllowDeletion() throws Exception{
        Long id = 1L;

        when(chatService.isCurrentUserTheCreatorOfChat(id)).thenReturn(true);

        String url = String.format("/chat/%d/delete",id);

        mockMvc.perform(post(url).with(csrf()).with(user(new User())))
                .andExpect(status().is3xxRedirection());

        verify(chatService).deleteById(id);
    }

    @Test
    void deleteChat_UserIsNotCreator_DontAllowDeletion() throws Exception{
        Long id = 1L;

        when(chatService.isCurrentUserTheCreatorOfChat(id)).thenReturn(false);

        String url = String.format("/chat/%d/delete",id);

        mockMvc.perform(post(url).with(csrf()).with(user(new User())))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void createChatPost() throws Exception{
        Chat chat = new Chat();
        chat.setName("chatName");

        when(chatService.saveNewChatByName(chat.getName())).thenReturn(chat);

        mockMvc.perform(post("/chat/create").with(csrf()).with(user(new User()))
                        .flashAttr("chatName",chat.getName()))
                .andExpect(status().is3xxRedirection());
    }
}