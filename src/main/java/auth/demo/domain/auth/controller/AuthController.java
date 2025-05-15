package auth.demo.domain.auth.controller;

import auth.demo.domain.auth.dto.LoginReqDto;
import auth.demo.domain.auth.dto.LoginResDto;
import auth.demo.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto login = authService.login(loginReqDto.getEmail(), loginReqDto.getPassword());

        return new ResponseEntity<>(login, HttpStatus.OK);
    }
}
