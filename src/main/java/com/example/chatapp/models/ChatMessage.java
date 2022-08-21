package com.example.chatapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Embeddable
public class ChatMessage {
    @GeneratedValue
    @Id
    private Long id;
    @ManyToOne
    private User creator;
    @NotBlank
    private String message;
}
