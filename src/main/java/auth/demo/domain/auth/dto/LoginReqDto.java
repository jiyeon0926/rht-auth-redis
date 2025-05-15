package auth.demo.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginReqDto {

    private String email;
    private String password;
}
