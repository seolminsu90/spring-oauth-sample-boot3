#### Spring OAuth2 Sample

`2023-05-16`에 릴리즈된 최신 버전 기준의 Spring Oauth stack의 예시

- spring-boot 3.1
- spring-boot-starter-oauth2-authorization-server 1.1.x (Boot)
- spring-boot-starter-oauth2-client 3.1.x (Boot)

#### TODO?

- (하는중) spring-cloud-gateway 연동 (oauth-client)
  - 단일 게이트웨이 인증 처리
  - 서킷브레이커/폴백처리/캐싱 등등
  - 라우트 DB화
- spring-cloud-eureka 연동 (api-resource-server)
  - 리소스 서버 관리
  - spring-cloud-config 별도 구축 처리
- (완료) 유저 DB 연동
  - 메모리 유저에서 실제 데이터로 처리
- (완료) openldap 연동
  - 공통 유저로서의 처리
  - keycloak 은 자체가 oauth server 대용이 되는것 같으므로 PASS
 
#### 최종 그림

api-client (프론트엔드) -> api-gateway (공통게이트웨이) -> authorization-server (인증) / resource-server (자원)


[참조][ref]

[ref]: https://docs.spring.io/spring-authorization-server/docs/current/reference/html/index.html
