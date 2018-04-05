package com.wltandingan.sonarqube.auth.phabricator;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.SonarPlugin;

/**
 * @author Willie Loyd Tandingan
 * @since 1.0.0
 */
public class AuthPhabricatorPlugin extends SonarPlugin {
    @Override
    public List getExtensions() {
        List extensions = new ArrayList();
        extensions.add(PhabricatorSettings.class);
        extensions.add(UserIdentityFactory.class);
        extensions.add(PhabricatorIdentityProvider.class);
        extensions.add(PhabricatorScribeApi.class);
        extensions.addAll(PhabricatorSettings.definitions());
        return extensions;
    }
}
