package auth.demo.domain.auth.service;

import auth.demo.domain.auth.entity.RefreshToken;
import auth.demo.domain.auth.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Getter
    @Value("${jwt.refresh-expiry-millis}")
    private long refreshExpiryMillis;

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String refreshToken, String email) {
        RefreshToken refresh = RefreshToken.builder()
                .refreshToken(refreshToken)
                .authKey(email)
                .ttl(refreshExpiryMillis)
                .build();

        refreshTokenRepository.save(refresh);
    }

    public void deleteRefreshToken(String email) {
        Optional<RefreshToken> authKey = refreshTokenRepository.findByAuthKey(email);

        if (authKey.isPresent()) {
            refreshTokenRepository.delete(authKey.get());
        }
    }

    public void validRefreshToken(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token 입니다."));
    }
}
