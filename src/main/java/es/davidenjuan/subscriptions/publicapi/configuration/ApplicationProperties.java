package es.davidenjuan.subscriptions.publicapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Jwt jwt;

    private InternalApi internalApi;

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public InternalApi getInternalApi() {
        return internalApi;
    }

    public void setInternalApi(InternalApi internalApi) {
        this.internalApi = internalApi;
    }

    public static class Jwt {

        private String base64Secret;

        private int tokenValiditySeconds;

        public String getBase64Secret() {
            return base64Secret;
        }

        public void setBase64Secret(String base64Secret) {
            this.base64Secret = base64Secret;
        }

        public int getTokenValiditySeconds() {
            return tokenValiditySeconds;
        }

        public void setTokenValiditySeconds(int tokenValiditySeconds) {
            this.tokenValiditySeconds = tokenValiditySeconds;
        }
    }

    public static class InternalApi {

        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
