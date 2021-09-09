package com.challenge.deviget.mines.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiKeyPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String HEADER_NAME = "X-API-KEY";
    private static final String PRINCIPAL_NAME = "API-KEY";

    public ApiKeyPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return PRINCIPAL_NAME;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        request.setAttribute(SecurityConstants.USERNAME, authResult.getName());
        super.successfulAuthentication(request, response, authResult);
    }
}
