package project.northDev.springredditclone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.northDev.springredditclone.dto.AuthenticationResponse;
import project.northDev.springredditclone.dto.LoginRequest;
import project.northDev.springredditclone.dto.RefreshTokenRequest;
import project.northDev.springredditclone.dto.RegisterRequest;
import project.northDev.springredditclone.service.AuthService;
import project.northDev.springredditclone.service.RefreshTokenService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor

public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity<>(
                "User Registration successful", HttpStatus.OK
        );
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verify(token);
        return new ResponseEntity<>(
                "Account Activated Successful", HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshtoken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }
}
