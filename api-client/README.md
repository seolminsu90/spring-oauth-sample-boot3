#### 로그아웃 이슈

ClientRegistration을 Java-config로 생성해서 그런건지 모르겠지만 로그아웃이 제대로 되지 않는 문제가 발생했다.   
Client에서 OidcClientInitiatedLogoutSuccessHandler를 등록해서 oidc oauth Logout이 동작하도록 설정했는데 요청 진행이 제대로 되지 않았다.   
요청의 흐름을 디버그 하던 중, Authorization Provider로 부터 end_session_endpoint를 제공받아야 하는데, 정상적으로 제공받지 못하고 있었다.(설정이 누락된 것이겠지만)   

***OidcClientInitiatedLogoutSuccessHandler.class***
```
private URI endSessionEndpoint(ClientRegistration clientRegistration) {
    if (clientRegistration != null) {
        ProviderDetails providerDetails = clientRegistration.getProviderDetails();
        Object endSessionEndpoint = providerDetails.getConfigurationMetadata().get("end_session_endpoint"); // 값이 없음
        if (endSessionEndpoint != null) {
            return URI.create(endSessionEndpoint.toString());
        }
    }
    return null;
}
```

아래와 같은 형태의 Java config로 ClientRegistration을 등록하고 있는데, 별도 설정이 없으면 메타 정보가 비어서 동작하는 것을 확인하고
별도의 추가 정보를 제공했다.

***SecurityConfig.conf***
``` 
private ClientRegistration sampleClientRegistrationOidc() {
    // 추가된 메타데이터 정보
    Map<String, Object> configurationMetadata = new LinkedHashMap<>();
    configurationMetadata.put("end_session_endpoint", "http://auth-server:9000/connect/logout");
    
    return ClientRegistration.withRegistrationId("sample-oidc")
            .clientId("sample")
            .clientSecret("sample")
            .clientName("sample-oidc")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:8080/login/oauth2/code/sample-oidc") // {baseUrl}/login/oauth2/code/{registrationId}
            .scope("openid", "profile")
            .providerConfigurationMetadata(configurationMetadata)
            .authorizationUri("http://auth-server:9000/oauth2/authorize")
            .tokenUri("http://auth-server:9000/oauth2/token")
            .jwkSetUri("http://auth-server:9000/oauth2/jwks")
            .build();
}
```

위의 설정을 추가로 제공하니, `postLogoutRedirectUri`도 정상적으로 동작하여 redirect 되는 것을 확인할 수 있었다.      
Logout redirect 자체가 최근에 나온 기능이기도 하고 Java config로 된 레퍼런스가 거의 없어서 직접 찾아야 했다.

> `https://github.com/spring-projects/spring-authorization-server/issues/266` 5월 릴리즈(1.1)에 추가된 듯 하다.

아마도 yml 기반 ClientRegistration에서는 issuer 기반으로 자동 설정되는 듯 하다.   

**그렇다면 Java config에서도 자동 설정이 있을 것이다..**

찾아보니 있다.   

`ClientRegistrations` class의 `fromIssuerLocation`을 이용하면 된다.   
추가로 필수 제공 값을 줘서 Build 하면 위에서 설정하는 방법보다 편리하게 설정이 가능하다. 내부적으로는 기본적으로 Provider가 제공하는 `AuthorizationServerSettings` +@ 정보를 가져오는 것 같다.   

***ClientRegistration 예시***
```
ClientRegistration clientRegistration = ClientRegistrations.fromIssuerLocation("http://auth-server:9000")
    .registrationId("sample-oidc")
    .clientId("sample")
    .clientSecret("sample")
    .clientName("sample-oidc")
    .scope("openid", "profile")
    .build();
```


[Reference][ref]

[ref]: https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html