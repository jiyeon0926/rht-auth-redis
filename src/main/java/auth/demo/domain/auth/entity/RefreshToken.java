package auth.demo.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash("refreshToken")
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Indexed
    private String refreshToken;

    private String authKey;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    @Builder
    public RefreshToken(String refreshToken, String authKey, Long ttl) {
        this.refreshToken = refreshToken;
        this.authKey = authKey;
        this.ttl = ttl;
    }
}
