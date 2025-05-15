package auth.demo.domain.auth.service;

import auth.demo.domain.auth.entity.TokenBlackList;
import auth.demo.domain.auth.repository.TokenBlackListRepository;
import auth.demo.global.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;
    private final JwtProvider jwtProvider;

    public void saveAccessToken(String accessToken, String email) {
        long now = new Date().getTime();
        Claims claims = jwtProvider.getClaims(accessToken);
        Date expiration = claims.getExpiration();
        long remainExpiration = expiration.getTime() - now;

        if (remainExpiration > 0) {
            TokenBlackList blackList = TokenBlackList.builder()
                    .accessToken(accessToken)
                    .authKey(email)
                    .ttl(remainExpiration)
                    .build();

            tokenBlackListRepository.save(blackList);
        }
    }

    public boolean validBlackList(String accessToken) {
        return tokenBlackListRepository.findByAccessToken(accessToken).isPresent();
    }
}
