package com.challenge.deviget.mines.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SessionCreationPolicy stateless = SessionCreationPolicy.STATELESS;
        http.csrf().disable().authorizeRequests().anyRequest().authenticated().and()
                .addFilter(new ApiKeyPreAuthenticationFilter(authenticationManager())).sessionManagement().sessionCreationPolicy(stateless)
                .sessionFixation().none().and().exceptionHandling().authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/metrics/**", "/health/**", "/prometheus/**", "/api-docs","/v2/api-docs",
                "/configuration/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/configuration/security",
                "/swagger-ui.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(preAuthenticationProvider());
    }

    @Bean
    protected AuthenticationManager preAuthAuthenticationManager() {

        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(apiKeyService);

        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(preAuthProvider);

        return new ProviderManager(providers);
    }

    private AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(apiKeyService);
        return provider;
    }

}
