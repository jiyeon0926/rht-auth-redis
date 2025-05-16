package auth.demo.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("refreshToken")
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String authKey; // 이메일(Key)

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    @Builder
    public RefreshToken(String authKey, String refreshToken, Long ttl) {
        this.authKey = authKey;
        this.refreshToken = refreshToken;
        this.ttl = ttl;
    }
}
