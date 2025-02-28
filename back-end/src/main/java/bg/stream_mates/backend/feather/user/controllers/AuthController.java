package bg.stream_mates.backend.feather.user.controllers;

import bg.stream_mates.backend.feather.user.models.dtos.LoginRequest;
import bg.stream_mates.backend.feather.user.models.dtos.RegisterRequest;
import bg.stream_mates.backend.feather.user.models.entities.User;
import bg.stream_mates.backend.feather.user.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        return authService.register(registerRequest, response);
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        List<Object> data = authService.login(loginRequest, response);
        User user = (User) data.get(1);
        return user;
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println();
        authService.logout(request, response);
        return ResponseEntity.ok(true);
    }
};
