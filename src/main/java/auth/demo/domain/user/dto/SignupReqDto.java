package auth.demo.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupReqDto {

    private String email;
    private String password;
    private String name;
}
