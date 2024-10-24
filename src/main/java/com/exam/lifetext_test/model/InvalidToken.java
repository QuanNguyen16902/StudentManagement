package com.exam.lifetext_test.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvalidToken {
    private String token;

    public InvalidToken(String token) {
        this.token = token;
    }
}
