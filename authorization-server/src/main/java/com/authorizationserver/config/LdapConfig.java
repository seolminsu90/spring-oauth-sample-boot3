package com.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;

@Configuration
public class LdapConfig {
    @Bean
    public PasswordComparisonAuthenticator ldapAuthenticator(BaseLdapPathContextSource contextSource) {
        PasswordComparisonAuthenticator authenticator = new PasswordComparisonAuthenticator(contextSource);
        LdapUserSearch userSearch = new FilterBasedLdapUserSearch("ou=users", "(uid={0})", contextSource);
        authenticator.setPasswordAttributeName("userPassword"); // uid 필터링 후 검색하여 userPassword를 비교한다.
        authenticator.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // 실전에는 적당한 암호화 선택 필요
        authenticator.setUserSearch(userSearch);
        return authenticator;
    }

    @Bean
    public BindAuthenticator bindAuthenticator(BaseLdapPathContextSource contextSource) {
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        LdapUserSearch userSearch = new FilterBasedLdapUserSearch("ou=users", "(uid={0})", contextSource);
        authenticator.setUserSearch(userSearch);
        return authenticator;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator ldapAuthenticator) {
        return new LdapAuthenticationProvider(ldapAuthenticator); // default SSHA (Salted SHA)
    }

    @Bean
    public BaseLdapPathContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();

        contextSource.setUrl("ldap://localhost:389");
        contextSource.setBase("dc=mycompany,dc=com");
        contextSource.setUserDn("cn=admin,dc=mycompany,dc=com");
        contextSource.setPassword("admin");

        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
