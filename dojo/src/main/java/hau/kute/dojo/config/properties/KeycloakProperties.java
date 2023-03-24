package hau.kute.dojo.config.properties;

public class KeycloakProperties {

    private String baseUri;

    private String clientId;

    private String clientSecret;

    private String grantType;

    private String scope;

    private String authUri;

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthUri() {
        return this.baseUri + this.authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }
}
