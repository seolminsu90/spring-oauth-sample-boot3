#### Spring OAuth2 Sample

`2023-05-16`에 릴리즈된 최신 버전 기준의 Spring Oauth stack의 예시

- spring-boot 3.1
- spring-boot-starter-oauth2-authorization-server 1.1.x (Boot)
- spring-boot-starter-oauth2-client 3.1.x (Boot)

#### TODO?

- spring-cloud-gateway 연동 (oauth-client)
  - 단일 게이트웨이 인증 처리
- spring-cloud-eureka 연동 (api-resource-server)
  - 리소스 서버 관리
- (완료) 유저 DB 연동
  - 메모리 유저에서 실제 데이터로 처리
- (완료) keycloak / openldap 연동
  - 공통 유저로서의 처리

[참조][ref]

[ref]: https://docs.spring.io/spring-authorization-server/docs/current/reference/html/index.html