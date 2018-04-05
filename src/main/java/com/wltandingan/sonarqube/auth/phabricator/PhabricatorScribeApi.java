package com.wltandingan.sonarqube.auth.phabricator;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.JsonTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import org.sonar.api.server.ServerSide;

import static com.github.scribejava.core.utils.OAuthEncoder.encode;

/**
 * @author Willie Loyd Tandingan
 * @since 1.0.0
 */
@ServerSide
public class PhabricatorScribeApi extends DefaultApi20 {

    private final PhabricatorSettings settings;

    public PhabricatorScribeApi(PhabricatorSettings settings) {
        this.settings = settings;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return settings.hostURL() + "oauthserver/token/";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return settings.hostURL() +
                "oauthserver/auth/?response_type=code&client_id=" + encode(config.getApiKey()) +
                "&redirect_uri=" + encode(config.getCallback());
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
