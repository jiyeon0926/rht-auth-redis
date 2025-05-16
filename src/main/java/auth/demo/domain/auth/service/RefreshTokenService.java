package auth.demo.domain.auth.service;

import auth.demo.domain.auth.entity.RefreshToken;
import auth.demo.domain.auth.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Getter
    @Value("${jwt.refresh-expiry-millis}")
    private long refreshExpiryMillis;

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String refreshToken, String email) {
        RefreshToken refresh = RefreshToken.builder()
                .authKey(email)
                .refreshToken(refreshToken)
                .ttl(refreshExpiryMillis)
                .build();

        refreshTokenRepository.save(refresh);
    }

    public void deleteRefreshToken(String email) {
        refreshTokenRepository.findByAuthKey(email)
                .ifPresent(refreshTokenRepository::delete);
    }

    public void validRefreshToken(String email, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByAuthKey(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token이 존재하지 않습니다."));

        if (!token.getRefreshToken().equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }
    }
}
