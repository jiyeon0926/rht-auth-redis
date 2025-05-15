package auth.demo.domain.user.controller;

import auth.demo.domain.user.dto.SignupReqDto;
import auth.demo.domain.user.dto.SignupResDto;
import auth.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> userSignup(@RequestBody SignupReqDto signupReqDto) {
        SignupResDto userSignup = userService.userSignup(signupReqDto.getEmail(), signupReqDto.getPassword(), signupReqDto.getName());

        return new ResponseEntity<>(userSignup, HttpStatus.CREATED);
    }
}
