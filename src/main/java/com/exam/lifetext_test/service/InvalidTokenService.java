package com.exam.lifetext_test.service;

import com.exam.lifetext_test.model.InvalidToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InvalidTokenService {
    private final Set<InvalidToken> invalidTokens =new HashSet<>();

    // Thêm token vào danh sách không hợp lệ
    public void revokeToken(String token) {
        invalidTokens.add(new InvalidToken(token));
    }

    // Kiểm tra xem token có bị thu hồi
    public boolean isTokenRevoked(String token) {
        return invalidTokens.stream().anyMatch(invalidToken -> invalidToken.getToken().equals(token));
    }
}
