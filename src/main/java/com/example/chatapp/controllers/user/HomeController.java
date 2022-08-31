package com.example.chatapp.controllers.user;

import com.example.chatapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("user",userService.receiveCurrentUser());
        return "user/home";
    }
}
