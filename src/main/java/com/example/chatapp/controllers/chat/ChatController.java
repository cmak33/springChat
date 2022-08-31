package com.example.chatapp.controllers.chat;

import com.example.chatapp.models.*;
import com.example.chatapp.services.ChatService;
import com.example.chatapp.services.HttpSessionService;
import com.example.chatapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;
    private final HttpSessionService httpSessionService;

    @GetMapping("/{id}")
    public String chat(@PathVariable Long id, Model model){
        Optional<Chat> chat = chatService.findById(id);
        if(chat.isPresent() && chatService.isCurrentUserParticipantOfChat(chat.get())){
            httpSessionService.putChatIdInHttpSession(chat.get().getId());
            model.addAttribute("chat",chat.get());
            model.addAttribute("isCreator",chatService.isCurrentUserTheCreatorOfChat(chat.get().getId()));
            return "chat/chat";
        } else{
            return "redirect:/home";
        }
    }

    @GetMapping("/list")
    public String chatList(Model model){
        Long id = userService.receiveCurrentUserId();
        Optional<User> user = userService.findUserById(id);
        return user.map(userValue->{
            model.addAttribute("chats",userValue.getChats());
            return "chat/list";
        }).orElse("redirect:/home");
    }

    @PostMapping("/{id}/delete")
    public String deleteChat(@PathVariable Long id){
        boolean isCreator = chatService.isCurrentUserTheCreatorOfChat(id);
        if(isCreator){
            chatService.deleteById(id);
            return "redirect:/home";
        } else{
            return String.format("redirect:/chat/%d",id);
        }
    }

    @PostMapping("/create")
    public String createChatPost(@ModelAttribute("chatName") String chatName){
        Chat chat = chatService.saveNewChatByName(chatName);
        return String.format("redirect:/chat/%d",chat.getId());
    }
}
