package com.wltandingan.sonarqube.auth.phabricator;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Willie Loyd Tandingan
 * @since 1.0.0
 */
@ServerSide
public class PhabricatorSettings {

    private static final String CONSUMER_KEY = "sonar.auth.phabricator.clientId.secured";
    private static final String CONSUMER_SECRET = "sonar.auth.phabricator.clientSecret.secured";
    private static final String ENABLED = "sonar.auth.phabricator.enabled";
    private static final String ALLOW_USERS_TO_SIGN_UP = "sonar.auth.phabricator.allowUsersToSignUp";
    private static final String HOST_URL = "sonar.auth.phabricator.hostUrl";
    private static final String DEFAULT_HOST_URL = "https://secure.phabricator.com/";

    private static final String CATEGORY = "security";
    private static final String SUBCATEGORY = "phabricator";

    private final Settings settings;

    public PhabricatorSettings(Settings settings) {
        this.settings = settings;
    }

    @CheckForNull
    String clientId() {
        return settings.getString(CONSUMER_KEY);
    }

    @CheckForNull
    String clientSecret() {
        return settings.getString(CONSUMER_SECRET);
    }

    boolean isEnabled() {
        return settings.getBoolean(ENABLED) && clientId() != null && clientSecret() != null;
    }

    boolean allowUsersToSignUp() {
        return settings.getBoolean(ALLOW_USERS_TO_SIGN_UP);
    }

    String hostURL() {
        String url = settings.getString(HOST_URL);
        if (url == null) {
            url = DEFAULT_HOST_URL;
        }
        return urlWithEndingSlash(url);
    }

    private static String urlWithEndingSlash(String url) {
        if (!url.endsWith("/")) {
            return url + "/";
        }
        return url;
    }

    public static List<PropertyDefinition> definitions() {
        int index = 1;
        return Arrays.asList(
                PropertyDefinition.builder(ENABLED)
                        .name("Enabled")
                        .description("Enable Phabricator users to login. Value is ignored if client ID and secret are not defined.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(PropertyType.BOOLEAN)
                        .defaultValue(String.valueOf(false))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CONSUMER_KEY)
                        .name("OAuth Application Client PHID")
                        .description("Client PHID provided by Phabricator when registering the application.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CONSUMER_SECRET)
                        .name("OAuth Application Secret")
                        .description("Application secret provided by Phabricator.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(ALLOW_USERS_TO_SIGN_UP)
                        .name("Allow users to sign-up")
                        .description("Allow new users to authenticate. When set to 'false', only existing users will be able to authenticate to the server.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(PropertyType.BOOLEAN)
                        .defaultValue(String.valueOf(true))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(HOST_URL)
                        .name("Phabricator Host")
                        .description("Configurable Phabricator url.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .defaultValue(DEFAULT_HOST_URL)
                        .index(index)
                        .build());
    }

}
