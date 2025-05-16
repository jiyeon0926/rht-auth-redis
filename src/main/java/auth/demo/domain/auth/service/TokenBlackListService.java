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

    // Access 토큰을 블랙리스트에 저장하여 관리
    public void saveAccessToken(String accessToken) {
        long now = new Date().getTime();
        Claims claims = jwtProvider.getClaims(accessToken);
        Date expiration = claims.getExpiration();
        long remainExpiration = expiration.getTime() - now;

        // Access 토큰이 아직 유효하다면 블랙리스트에 저장
        if (remainExpiration > 0) {
            TokenBlackList blackList = TokenBlackList.builder()
                    .accessToken(accessToken)
                    .ttl(remainExpiration)
                    .build();

            tokenBlackListRepository.save(blackList);
        }
    }

    public boolean validBlackList(String accessToken) {
        return tokenBlackListRepository.findByAccessToken(accessToken).isPresent();
    }
}
