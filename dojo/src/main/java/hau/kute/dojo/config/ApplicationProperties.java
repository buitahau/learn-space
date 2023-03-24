package hau.kute.dojo.config;

import hau.kute.dojo.config.properties.KeycloakProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private KeycloakProperties keycloak;

    public KeycloakProperties getKeycloak() {
        return keycloak;
    }

    public void setKeycloak(KeycloakProperties keycloak) {
        this.keycloak = keycloak;
    }
}
