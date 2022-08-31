package com.example.chatapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class ChatMessage {
    @GeneratedValue
    @Id
    private Long id;
    @ManyToOne
    private Chat chat;
    @ManyToOne
    private User creator;
    @NotBlank
    private String message;
}
