package com.challenge.deviget.mines.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ApiKeyService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private SecurityTokenConfig securityTokenConfig;

    @Autowired
    public ApiKeyService(SecurityTokenConfig securityTokenConfig) {
        this.securityTokenConfig = securityTokenConfig;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String tokenSecret = token.getCredentials().toString();

        SecurityTokenConfig.SecurityToken securityToken = findSecurityToken(tokenSecret);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(securityToken.getNome());

        return new User(securityToken.getNome(), tokenSecret, authorities);
    }

    private SecurityTokenConfig.SecurityToken findSecurityToken(String tokenSecret) throws UsernameNotFoundException {
        return securityTokenConfig.getTokens().stream()
                .filter(securityToken -> securityToken.getToken().equals(tokenSecret))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Invalid API-KEY"));
    }
}
