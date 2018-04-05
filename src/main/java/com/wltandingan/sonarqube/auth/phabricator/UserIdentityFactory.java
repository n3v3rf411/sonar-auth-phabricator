package com.wltandingan.sonarqube.auth.phabricator;

import com.wltandingan.sonarqube.auth.phabricator.models.UserWhoami;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.authentication.UserIdentity;

/**
 * Converts Phabricator JSON responses to {@link UserIdentity}
 *
 * @author Willie Loyd Tandingan
 * @since 1.0.0
 */
@ServerSide
public class UserIdentityFactory {

    UserIdentity create(UserWhoami gsonUser) {
        UserIdentity.Builder builder = builder(gsonUser);
        builder.setEmail(gsonUser.getPrimaryEmail());
        return builder.build();
    }

    private UserIdentity.Builder builder(UserWhoami gsonUser) {
        return UserIdentity.builder()
                .setProviderLogin(gsonUser.getUserName())
                .setLogin(generateLogin(gsonUser))
                .setName(generateName(gsonUser));
    }

    private String generateLogin(UserWhoami gsonUser) {
        return gsonUser.getUserName();
    }

    private static String generateName(UserWhoami gson) {
        String name = gson.getRealName();
        return name == null || name.isEmpty() ? gson.getUserName() : name;
    }
}
