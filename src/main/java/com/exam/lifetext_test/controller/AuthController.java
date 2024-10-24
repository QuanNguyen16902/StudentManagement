package com.exam.lifetext_test.controller;

import com.exam.lifetext_test.model.InvalidToken;
import com.exam.lifetext_test.model.Role;
import com.exam.lifetext_test.model.User;
import com.exam.lifetext_test.payload.request.LoginRequest;
import com.exam.lifetext_test.payload.request.RegisterRequest;
import com.exam.lifetext_test.payload.response.JwtResponse;
import com.exam.lifetext_test.payload.response.MessageResponse;
import com.exam.lifetext_test.repository.UserRepository;
import com.exam.lifetext_test.security.jwt.JwtUtils;
import com.exam.lifetext_test.service.InvalidTokenService;
import com.exam.lifetext_test.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private InvalidTokenService invalidTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Thực hiện xác thực với authenticationManager
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Tạo JWT token
            String jwt = jwtUtils.generateJwtToken(userDetails);
            String role = userDetails.getAuthorities().toString();

            // Trả về thông tin JWT và refresh token
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), role));
        } catch (BadCredentialsException e) {
            // Lỗi khi thông tin đăng nhập không chính xác
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc mật khẩu không đúng");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi không mong muốn xảy ra");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: Tên người dùng đã tồn tại"));
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()));

        String strRole = registerRequest.getRole();
        Role role;

        if (strRole == null || strRole.isEmpty()) {
            role = Role.USER;
        } else {
            try {
                role = Role.valueOf(strRole.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new RuntimeException("Lỗi: Vai trò không hợp lệ"));
            }
        }
        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Đăng ký thành công"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        // Lấy thông tin xác thực từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Người dùng không hợp lệ."));
        }

        // Xóa bối cảnh xác thực
        String jwt = authHeader.substring(7); // Loại bỏ "Bearer " từ token
        // Thêm token vào danh sách đã thu hồi
        invalidTokenService.revokeToken(jwt);

//        SecurityContextHolder.clearContext();

        // Trả về thông báo đăng xuất thành công
        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
    }


}
