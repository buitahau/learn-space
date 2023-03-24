package hau.kute.dojo.service.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import hau.kute.dojo.config.ApplicationProperties;
import hau.kute.dojo.config.properties.KeycloakProperties;
import hau.kute.dojo.dto.auth.AuthDto;
import hau.kute.dojo.dto.auth.JWTDto;
import hau.kute.dojo.exception.DojoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class KeyCloakService {

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public JWTDto auth(AuthDto authDto) {
        HttpHeaders httpHeaders = buildAuthHeader();
        MultiValueMap<String, String> requestBody = buildAuthRequestBody(authDto);
        var entity = new HttpEntity<>(requestBody, httpHeaders);
        try {
            ResponseEntity<KeyCloakAuthResponse> authResponse =
                    restTemplate.exchange(applicationProperties.getKeycloak().getAuthUri(), HttpMethod.POST, entity,
                            KeyCloakAuthResponse.class);
            return convert(Objects.requireNonNull(authResponse.getBody()));
        } catch (Exception e) {
            throw new DojoException("401", "Invalid user name or password");
        }
    }

    private HttpHeaders buildAuthHeader() {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }

    private MultiValueMap<String, String> buildAuthRequestBody(AuthDto authDto) {
        KeycloakProperties keycloakProperties = applicationProperties.getKeycloak();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", keycloakProperties.getClientId());
        requestBody.add("username", authDto.getUsername());
        requestBody.add("password", authDto.getPassword());
        requestBody.add("grant_type", keycloakProperties.getGrantType());
        requestBody.add("client_secret", keycloakProperties.getClientSecret());
        requestBody.add("scope", keycloakProperties.getScope());
        return requestBody;
    }

    private JWTDto convert(KeyCloakAuthResponse keyCloakAuthResponse) {
        JWTDto jwtDto = new JWTDto();
        jwtDto.setAccessToken(keyCloakAuthResponse.getAccessToken());
        return jwtDto;
    }

    static class KeyCloakAuthResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private long expiresIm;

        @JsonProperty("refresh_expires_in")
        private long refreshExpiresIn;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("id_token")
        private String idToken;

        @JsonProperty("not-before-policy")
        private long notBeforePolicy;

        @JsonProperty("session_state")
        private String sessionState;

        @JsonProperty("scope")
        private String scope;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public long getExpiresIm() {
            return expiresIm;
        }

        public void setExpiresIm(long expiresIm) {
            this.expiresIm = expiresIm;
        }

        public long getRefreshExpiresIn() {
            return refreshExpiresIn;
        }

        public void setRefreshExpiresIn(long refreshExpiresIn) {
            this.refreshExpiresIn = refreshExpiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public long getNotBeforePolicy() {
            return notBeforePolicy;
        }

        public void setNotBeforePolicy(long notBeforePolicy) {
            this.notBeforePolicy = notBeforePolicy;
        }

        public String getSessionState() {
            return sessionState;
        }

        public void setSessionState(String sessionState) {
            this.sessionState = sessionState;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }
}
