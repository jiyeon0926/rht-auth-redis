# 🛠️ 기술 스택
- Java 21
- Spring Boot 3.4.5 Version
- Spring Security
- JWT
- Redis
- MySQL

# 💡 기능
- 일반 사용자 회원가입
- 관리자 회원가입
- 로그인
- 로그아웃
- Token 재발급

# 🧩 설계

## 1️⃣ ERD
![rht-auth-redis](https://github.com/user-attachments/assets/3da24304-c514-41a6-90ee-58471b7b018f)

## 2️⃣ API 명세서
👉 [Notion API 명세서](https://www.notion.so/JWT-1f2e22e7e413805ab06ac854faa19a8b)

<details>
  <summary>인증/인가 확인 테스트용 API</summary>
  <br>
  
  |테스트|Method|URL|상태코드|응답 성공 메시지|
  |---|---|---|---|---|
  |인증|GET|/auth/check|200 OK <br> 401 Unauthorized|인증된 사용자입니다.|
  |사용자 권한|GET|/users/check|200 OK <br> 401 Unauthorized <br> 403 Forbidden|사용자와 관리자 모두 접근할 수 있습니다.|
  |관리자 권한|GET|/admins/check|200 OK <br> 401 Unauthorized <br> 403 Forbidden|관리자만 접근할 수 있습니다.|

</details>

## 3️⃣ Token 관리
- Redis에 저장하여 관리
- Refresh Token과 Access Token을 분리하여 저장

### Refresh Token
- 로그인할 때, Refresh Token을 Redis에 저장
  - Key : 사용자 이메일
    -  ex) refreshToken:email@naver.com
  - Value : Refresh Token과 만료 시간을 함께 저장
- Token 재발급 시, 저장된 Refresh Token을 요청 Body에 담아 전송하여 새로운 Access Token과 Refresh Token을 함께 발급
  - Access Token 뿐만 아니라, Refresh Token도 갱신함으로써 보안을 강화할 수 있음
- 로그아웃 시, Refresh Token 삭제
 
### Blacklist
- 로그아웃 시, Access Token을 Blacklist로 Redis에 저장
  - Access Token이 유효한 상태에서 탈취될 가능성을 생각
  - 보안 강화 목적
  - Key : Access Token
  - Value : 남은 만료 시간
- TTL로 지정한 시간이 지나면 Redis가 해당 데이터를 자동으로 삭제

# 📁 프로젝트 구조
<details>
  <summary>프로젝트 구조</summary>
  <br>
  
```
 src
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
</details>
