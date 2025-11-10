# jwt-login-sample
심플하게 interceptor 기반의 JWT 인증 처리 샘플

## 빠른 시작
- **필수**: JDK 21, Gradle 8.8 이상.
- 빌드 & 테스트: `./gradlew clean build`
- 애플리케이션 실행: `./gradlew bootRun` → `GET /echo-login-user` 엔드포인트로 토큰 검증 결과를 확인.

## 프로젝트 구조
```
src/main/java/kjstyle/jwtloginsample
 ├─ config            # JwtProperties, WebConfig
 ├─ interceptor       # AuthenticationInterceptor (JWT 가드)
 ├─ jwt               # JwtUtil, JwtInterceptorHelper 등 토큰 유틸
 ├─ auth              # LoginUser, ArgumentResolver
 └─ sample            # /echo-login-user 샘플 컨트롤러
src/test/java/kjstyle/jwtloginsample
 ├─ common            # BaseTest, BaseMockMvcTest
 ├─ jwt               # JwtUtil 단위 테스트
 └─ sample            # MockMvc 기반 통합 테스트
```

## JWT 처리 흐름
1. **AuthenticationInterceptor**가 요청마다 액세스 토큰을 헤더→쿠키 순으로 추출.
2. 만료되지 않았다면 **JwtUtil**이 사용자 정보를 복원해 `request`에 `loginUser`로 저장.
3. 만료된 경우 **리프레시 토큰**을 `X-Refresh-Token` 헤더나 쿠키에서 읽어 새 액세스 토큰을 발급하고 `X-New-Access-Token` 헤더로 반환.
4. **LoginUserArgumentResolver**가 컨트롤러 파라미터에 `LoginUser`를 주입하여 비즈니스 코드에서 JWT 세부 로직을 몰라도 되도록 구성.

## 설정
`src/main/resources/application.yml`의 `spring.security` 블록으로 시크릿과 만료 시간을 제어합니다.
```yaml
spring:
  security:
    secret: dgdE7unlvo0lzoEzDtCbjcBH90062kVW
    expiration: 86400000       # 액세스 토큰 (ms)
    refresh-expiration: 604800000 # 리프레시 토큰 (ms)
```
배포 환경에서는 환경변수/외부 프로퍼티로 값을 덮어써서 민감정보를 코드 밖으로 분리하세요.

## 테스트
- `./gradlew test`는 JUnit5 + SpringBootTest 기반 테스트를 실행합니다.
- `JwtUtilTest`는 토큰 생성/만료/타입 검증을 커버하고, `SampleControllerTest`는 만료 토큰 재발급 시나리오까지 검증합니다.

## 사용 라이브러리
### Java JWT
* github : https://github.com/jwtk/jjwt
* 선택의 이유
    * 스타수가 많다 -> 10.2k
    * 구글링시에 많이 걸린다 (많은 레퍼런스)

## 요건
* argument resolver를 이용해서 controller에서 jwt관련 로직이 없도록 함 (관심사분리)
* interceptor를 활용한 간단하게 인증을 처리
* 기능하나하나 만들면서 테스트코드를 작성한다
