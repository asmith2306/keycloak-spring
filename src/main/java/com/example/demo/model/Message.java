package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private String sender;

    public Message(Long id, String content) {
        this.content = content;
    }
}

