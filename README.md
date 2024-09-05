# jwt-login-sample
springsecurity+JWT 기반의 로그인 기능 샘플

## JWT library
### Java JWT 
* github : https://github.com/jwtk/jjwt
* 선택의 이유
    * 스타수가 많다 -> 10.2k
    * 구글링시에 많이 걸린다 (많은 레퍼런스)

## 요건 
* access token으로 인증하고, refresh token으로 연장
* argument resolver를 이용해서 controller에서 jwt관련 로직이 없도록 함 (관심사분리)
* interceptor를 활용한 간단하게 인증을 처리해보고 스프링시큐리티로의 변화과정을 거칠 예정
* 기능하나하나 만들면서 테스트코드를 작성한다