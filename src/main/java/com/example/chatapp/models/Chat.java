package com.example.chatapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {
    @GeneratedValue
    @Id
    private Long id;
    private String name;
    @ManyToMany
    private Set<User> participants;
    @ElementCollection
    private List<ChatMessage> messages;
}
