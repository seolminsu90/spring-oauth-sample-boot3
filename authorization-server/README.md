#### client_credentials 을 이용한 서비스 어플리케이션에서 자원(API server) 직접 요청

Web Browser 기반에서 동작하지 않는 서비스 (ex)Background Service, Batch, Api To Api 서비스등) 에서는 `client_credentials`를 이용하여 자원을 요청한다.   
client_credentials의 경우 `authorization_code` 발급이 생략되고 바로 `access_token`을 발급받게 된다.   
아래는 curl예시이며 클라이언트는 WebClient 요청을 별도로 구현해야한다.   

**Access_token 발급**

```bash
curl -v -XPOST -H'Content-Type:application/x-www-form-urlencoded' auth-server:9000/oauth2/token?grant_type=client_credentials -u'sample:sample'
```

**Access_token 응답**

```json
{
  "access_token": "RETURN_USER_JWT_TOKEN",
  "token_type": "Bearer",
  "expires_in": 300
}
```

**자원 서버(8090) 요청**

```bash
curl -v -XGET -H'Content-type:application/json' -H'Authorization:Bearer {{access_token}}' auth-server:8090/sample
```

**자원 서버(8090) 응답**

```text
Hello world !
```