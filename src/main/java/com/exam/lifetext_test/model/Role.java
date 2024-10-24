package com.exam.lifetext_test.model;

import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public enum Role {
    USER, ADMIN, MANAGER
}
