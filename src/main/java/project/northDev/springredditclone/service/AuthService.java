package project.northDev.springredditclone.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.northDev.springredditclone.dto.AuthenticationResponse;
import project.northDev.springredditclone.dto.RefreshTokenRequest;
import project.northDev.springredditclone.exceptions.SpringRedditException;
import project.northDev.springredditclone.model.NotificationEmail;
import project.northDev.springredditclone.model.User;
import project.northDev.springredditclone.model.VerificationToken;
import project.northDev.springredditclone.dto.LoginRequest;
import project.northDev.springredditclone.dto.RegisterRequest;
import project.northDev.springredditclone.repository.UserRepository;
import project.northDev.springredditclone.repository.VerificationTokenRepository;
import project.northDev.springredditclone.security.JwtProvider;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

   // @Autowired user to inject bean to service
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUserName());
        user.setMail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please active your account", user.getMail(),
                "Thank you for signing up to Spring Reddit "+
                "please click on the below url to activate your account "+
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;

    }

    public void verify(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new SpringRedditException("Token Invalid!1"));
        fetchUserAndEnable(verificationToken.get());
    }
    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found with name "+username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);//check user login or not login
        String token = jwtProvider.generateToken(authentication);
        return  AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(()-> new SpringRedditException("User with name "+ principal.getUsername()+" not found!!"));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
         refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
         String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
         return AuthenticationResponse.builder()
                 .authenticationToken(token)
                 .refreshToken(refreshTokenRequest.getRefreshToken())
                 .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                 .username(refreshTokenRequest.getUserName())
                 .build();
    }
}
