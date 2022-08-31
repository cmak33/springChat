package com.example.chatapp.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {
    @GeneratedValue
    @Id
    private Long id;
    @CreationTimestamp
    private Date creationDate;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<User> participants = new HashSet<>();
    @ManyToOne
    private User creator;
    @OneToMany(mappedBy = "chat",cascade = CascadeType.REMOVE)
    private List<ChatMessage> messages;
}
