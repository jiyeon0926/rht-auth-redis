package auth.demo.domain.auth.repository;

import auth.demo.domain.auth.entity.TokenBlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlackListRepository extends CrudRepository<TokenBlackList, String> {

    Optional<TokenBlackList> findByAccessToken(String accessToken);
}
