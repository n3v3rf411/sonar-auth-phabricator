package com.wltandingan.sonarqube.auth.phabricator;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.wltandingan.sonarqube.auth.phabricator.models.ConduitResponse;
import com.wltandingan.sonarqube.auth.phabricator.models.UserWhoami;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.authentication.Display;
import org.sonar.api.server.authentication.OAuth2IdentityProvider;
import org.sonar.api.server.authentication.UserIdentity;

import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;
import static com.github.scribejava.core.utils.OAuthEncoder.encode;

/**
 * @author Willie Loyd Tandingan
 * @since 1.0.0
 */
@ServerSide
public class PhabricatorIdentityProvider implements OAuth2IdentityProvider {

    private static final String KEY = "phabricator";
    private static final Token EMPTY_TOKEN = null;

    private final PhabricatorSettings settings;
    private final UserIdentityFactory userIdentityFactory;
    private final PhabricatorScribeApi scribeApi;

    public PhabricatorIdentityProvider(PhabricatorSettings settings, UserIdentityFactory userIdentityFactory, PhabricatorScribeApi scribeApi) {
        this.settings = settings;
        this.userIdentityFactory = userIdentityFactory;
        this.scribeApi = scribeApi;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getName() {
        return "Phabricator";
    }

    @Override
    public Display getDisplay() {
        return Display.builder()
                // URL of src/main/resources/static/phabricator.svg at runtime
                .setIconPath("/static/authphabricator/phabricator.svg")
                .setBackgroundColor("#205081")
                .build();
    }

    @Override
    public boolean isEnabled() {
        return settings.isEnabled();
    }

    @Override
    public boolean allowsUsersToSignUp() {
        return settings.allowUsersToSignUp();
    }

    @Override
    public void init(InitContext context) {
        final OAuthService scribe = newScribeBuilder(context)
                .build();
        final String url = scribe.getAuthorizationUrl(EMPTY_TOKEN);
        context.redirectTo(url);
    }

    @Override
    public void callback(final CallbackContext context) {
        final HttpServletRequest request = context.getRequest();
        final OAuthService scribe = newScribeBuilder(context).build();
        final String oAuthVerifier = request.getParameter("code");
        final Token accessToken = scribe.getAccessToken(EMPTY_TOKEN, new Verifier(oAuthVerifier));

        final UserWhoami gsonUser = requestUser(scribe, accessToken);
        final UserIdentity userIdentity = userIdentityFactory.create(gsonUser);
        context.authenticate(userIdentity);
        context.redirectToRequestedPage();
    }

    private UserWhoami requestUser(final OAuthService scribe, final Token accessToken) {
        final OAuthRequest userRequest = new OAuthRequest(Verb.GET,
                settings.hostURL() + "api/user.whoami?access_token=" + encode(accessToken.getToken()),
                scribe);
        final Response userResponse = userRequest.send();

        if (!userResponse.isSuccessful()) {
            throw new IllegalStateException(format("Can not get Phabricator user profile. HTTP code: %s, response: %s",
                    userResponse.getCode(), userResponse.getBody()));
        }
        final String userResponseBody = userResponse.getBody();
        return ConduitResponse.parse(UserWhoami.class, userResponseBody).getResult();
    }

    private ServiceBuilder newScribeBuilder(final OAuth2IdentityProvider.OAuth2Context context) {
        if (!isEnabled()) {
            throw new IllegalStateException("Phabricator authentication is disabled");
        }
        return new ServiceBuilder()
                .provider(scribeApi)
                .apiKey(settings.clientId())
                .apiSecret(settings.clientSecret())
                .grantType("authorization_code")
                .callback(context.getCallbackUrl());
    }
}
