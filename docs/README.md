# 🛠️ 기술 스택
- Java 21
- Spring Boot 3.4.5 Version
- JPA
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
  - 보안 강화 및 인증 무력화 목적
  - Access Token이 유효한 상태에서 탈취될 가능성을 생각
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

# 🤔 고민

## 1️⃣ Access Token을 Redis에 저장할 경우 (Blacklist)
로그아웃된 유효한 Access Token을 즉시 무력화 시킬 수 있다. <br>
하지만 TTL로 지정한 시간이 지나기 전까지 Redis에 남아있기 때문에 사용자 수가 많아지면 메모리 사용량이 커지고, 성능 이슈가 발생할 수도 있다. <br>
Blacklist 방법을 사용하지 않으려면 Access Token의 유효 시간을 최대한 짧게 설정하고, 토큰을 재발급 받도록 유도하는 방법을 사용해야 한다.
- ex) Access Token : 10 ~ 15분

## 2️⃣ Refresh Token만 Redis에 저장할 경우
Access Token이 만료됐을 때, 새로운 Access Token을 재발급 받기 위해 Refresh Token을 저장하여 관리한다. <br>
Blacklist를 사용하지 않고, 로그아웃 기능을 구현하기 위해서는 Access Token을 재발급 받지 못하도록 Refresh Token을 삭제한다. <br>
Refresh Token을 삭제해도 이미 발급된 Access Token이 유효할 수 있기 때문에 탈취 당하지 않도록 즉시 무력화 시키려면 Blacklist 방법을 고려할 수 있다.
