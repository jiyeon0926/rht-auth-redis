package auth.demo.domain.auth.service;

import auth.demo.domain.auth.dto.LoginResDto;
import auth.demo.domain.auth.dto.TokenDto;
import auth.demo.domain.user.entity.User;
import auth.demo.domain.user.repository.UserRepository;
import auth.demo.global.auth.jwt.JwtProvider;
import auth.demo.global.common.enums.AuthenticationScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlackListService tokenBlackListService;

    // 로그인
    public LoginResDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        validPassword(password, user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), accessToken, refreshToken);
    }

    public void logout(String accessToken) {
        String email = jwtProvider.getUsername(accessToken);

        tokenBlackListService.saveAccessToken(accessToken, email);
        refreshTokenService.deleteRefreshToken(email);
    }

    // Refresh Token을 사용해 Access Token 재발급
    public TokenDto refresh(String refreshToken) {
        String email = jwtProvider.getUsername(refreshToken);
        refreshTokenService.validRefreshToken(email); // Key(email) 조회하여 검증

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(user.getRole().name())));

        String accessToken = jwtProvider.generateAccessToken(authenticationToken);

        // 기존 Refresh Token 삭제, 새 Refresh Token 발급 및 저장
        refreshTokenService.deleteRefreshToken(email);
        String newRefreshToken = jwtProvider.generateRefreshToken(authenticationToken);
        refreshTokenService.saveRefreshToken(newRefreshToken, email);

        return new TokenDto(accessToken, newRefreshToken);
    }

    private void validPassword(String rawPassword, String encodedPassword) {
        boolean notValid = !passwordEncoder.matches(rawPassword, encodedPassword);

        if (notValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }
    }
}
