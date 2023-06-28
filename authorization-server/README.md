#### device_code 를 이용한 인증 에시

제한된 디스플레이 또는 입력 장치가 있는 디바이스에서 인증을 수행해야 할 때 사용된다. 예를 들어, 텔레비전, 게임 콘솔, IoT 장치 등이 이에 해당한다.   
디바이스에서 인증 서버로 device_code 요청 후 별도의 웹(모바일웹) 환경의 인증 서버에 device_code를 입력하면 디바이스에서 토큰을 발급할 수 있게 된다.   
이때 디바이스는 polling을 수행하고 있다가, code 검증 완료 시 자동으로 access_token을 받도록 하면 된다.
아래는 curl을 통한 디바이스 인증 예시로, 실제 디바이스에선 별도로 구현해야한다. (공식 홈페이지 예제에선 웹 클라이언트 자체를 디바이스로 하여 인증하는 예시가 제공되어있다.)

**Device code 발급**

```bash
curl -v -XPOST -d "client_id=device-sample&scope=sample.role" auth-server:9000/oauth2/device_authorization
```

> scope는 그냥 아무거나 넣어놨다. 필요에 의해 바꾸면 된다.

**Device code 응답**

```json
{
  "user_code": "KVHH-SJBP",
  "device_code": "d-8uMughTh8nvQ94fjjOVQ4dY5FKDGKaNxPBP_p9Ifh1vEYolxDpZBXFFGKiwsA2GGMOl8mrFyJGKn6un96Dg84V5SAPhaBCQnSQgHfoIED1Ia0o24ETgFdHAJFgncPT",
  "verification_uri_complete": "http://auth-server:9000/activate?user_code=KVHH-SJBP",
  "verification_uri": "http://auth-server:9000/activate",
  "expires_in": 300
}
```

|name| desc                        |
|---|-----------------------------|
|user_code| 랜덤 고유 코드                    |
|device_code| Access_token 발급을 위한 디바이스코드  |
|verification_uri_complete| 로그인/인증완료 처리URL              |
|verification_uri| 코드 입력 웹 경로                  |
|expires_in| 만료 시간                       |

verification_uri 사이트에서 직접 코드(user_code)를 입력하거나 (사이트는 별도 구현해야한다.)   
verification_uri_complete를 통해 로그인/완료 처리를 바로 할 수있다.

**Access_token 발급**

이제 인증이 완료되었다면 device_code를 통해 access_token을 발급할 수 있다.

```bash
curl -v -XPOST -d "grant_type=urn:ietf:params:oauth:grant-type:device_code&device_code=[[device_code]]&client_id=device-sample" auth-server:9000/oauth2/token
```

> 각각 상황에 맞게 값을 입력한다.

**Access_token 응답**

```json
{
  "access_token": "RETURN_USER_JWT_TOKEN",
  "refresh_token": "RETURN_REFRESH_TOKEN",
  "scope": "sample.role",
  "token_type": "Bearer",
  "expires_in": 300
}
```

> 아래의 인증과 다르게 refresh_token까지 받아오는 것을 알 수 있다.

#### client_credentials 을 이용한 서비스 어플리케이션에서 자원(API server) 직접 요청

Background Service, Batch, Api To Api 서비스등 에서는 `client_credentials`를 이용하여 자원을 요청한다.   
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

#### Authorization server openid spec 확인

http://auth-server:9000/.well-known/openid-configuration