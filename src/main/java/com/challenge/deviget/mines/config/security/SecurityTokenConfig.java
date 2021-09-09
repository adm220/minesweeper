package com.challenge.deviget.mines.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityTokenConfig {

    private List<SecurityToken> tokens;

    public List<SecurityToken> getTokens() {
        return tokens;
    }

    public SecurityTokenConfig setTokens(List<SecurityToken> tokens) {
        this.tokens = tokens;
        return this;
    }

    public static class SecurityToken {
        private String nome;
        private String token;

        public String getNome() {
            return nome;
        }

        public SecurityToken setNome(String nome) {
            this.nome = nome;
            return this;
        }

        public String getToken() {
            return token;
        }

        public SecurityToken setToken(String token) {
            this.token = token;
            return this;
        }
    }

}
