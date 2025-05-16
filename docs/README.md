# 📁 프로젝트 구조
```
─src
    ├─main
    │  ├─generated
    │  ├─java
    │  │  └─auth
    │  │      └─demo
    │  │          │  DemoApplication.java
    │  │          │  
    │  │          ├─domain
    │  │          │  ├─auth
    │  │          │  │  ├─controller
    │  │          │  │  │      AuthController.java
    │  │          │  │  │      
    │  │          │  │  ├─dto
    │  │          │  │  │      LoginReqDto.java
    │  │          │  │  │      LoginResDto.java
    │  │          │  │  │      RefreshReqDto.java
    │  │          │  │  │      TokenDto.java
    │  │          │  │  │      
    │  │          │  │  ├─entity
    │  │          │  │  │      RefreshToken.java
    │  │          │  │  │      TokenBlackList.java
    │  │          │  │  │      
    │  │          │  │  ├─repository
    │  │          │  │  │      RefreshTokenRepository.java
    │  │          │  │  │      TokenBlackListRepository.java
    │  │          │  │  │      
    │  │          │  │  └─service
    │  │          │  │          AuthService.java
    │  │          │  │          RefreshTokenService.java
    │  │          │  │          TokenBlackListService.java
    │  │          │  │          
    │  │          │  ├─test
    │  │          │  │  └─controller
    │  │          │  │          TestController.java
    │  │          │  │          
    │  │          │  └─user
    │  │          │      ├─controller
    │  │          │      │      AdminController.java
    │  │          │      │      UserController.java
    │  │          │      │      
    │  │          │      ├─dto
    │  │          │      │      SignupReqDto.java
    │  │          │      │      SignupResDto.java
    │  │          │      │      
    │  │          │      ├─entity
    │  │          │      │      User.java
    │  │          │      │      
    │  │          │      ├─repository
    │  │          │      │      UserRepository.java
    │  │          │      │      
    │  │          │      └─service
    │  │          │              AdminService.java
    │  │          │              UserService.java
    │  │          │              
    │  │          └─global
    │  │              ├─auth
    │  │              │  │  UserDetailsImpl.java
    │  │              │  │  UserDetailsServiceImpl.java
    │  │              │  │  
    │  │              │  ├─handler
    │  │              │  │      DelegatedAccessDeniedHandler.java
    │  │              │  │      DelegatedAuthenticationEntryPoint.java
    │  │              │  │      
    │  │              │  └─jwt
    │  │              │          JwtAuthFilter.java
    │  │              │          JwtProvider.java
    │  │              │          
    │  │              ├─common
    │  │              │  ├─entity
    │  │              │  │      BaseEntity.java
    │  │              │  │      
    │  │              │  └─enums
    │  │              │          AuthenticationScheme.java
    │  │              │          UserRole.java
    │  │              │          
    │  │              ├─config
    │  │              │      RedisConfig.java
    │  │              │      SecurityConfig.java
    │  │              │      WebConfig.java
    │  │              │      
    │  │              └─exception
    │  │                  └─handler
    │  │                          GlobalExceptionHandler.java
    │  │                          
    │  └─resources
    │      │  application.yml
    │      │  
    │      ├─static
    │      └─templates
    └─test
        └─java
            └─auth
                └─demo
                        DemoApplicationTests.java

```