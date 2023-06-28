package com.apiclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class ClientRegistrationConfig {

    // https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/jc.html JAVA CONFIG EXAMPLE
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.sampleClientRegistrationOidc(), this.sampleClientRegistrationAuthorizationCode());
    }

    private ClientRegistration sampleClientRegistrationOidc() {
        return ClientRegistrations.fromIssuerLocation("http://auth-server:9000")
                .registrationId("sample-oidc")
                .clientId("sample")
                .clientSecret("sample")
                .clientName("sample-oidc")
                .scope("openid", "profile")
                .build();
    }

    private ClientRegistration sampleClientRegistrationAuthorizationCode() {
        return ClientRegistrations.fromIssuerLocation("http://auth-server:9000")
                .registrationId("sample-authorization-code")
                .clientId("sample")
                .clientSecret("sample")
                .clientName("sample-authorization-code")
                .scope("openid", "profile") // 기타 다른 리소스 접근 시의 필요 롤에 대한 커스터마이징 처리
                .build();
    }

    @Bean
    public OAuth2AuthorizedClientManager defaultAuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .build();

        // HttpServletRequest 컨텍스트 범위 내에서 사용
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        // Background service 등, clientCredentials 방식의 서비스 시스템 레벨에서의 사용
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }


    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(
            OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }


}
