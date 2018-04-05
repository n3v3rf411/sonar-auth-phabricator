# Phabricator Authentication Plugin for SonarQube #

This plugin enables user authentication and Single Sign-On via [Phabricator](https://www.phacility.com/).

This was based on the [Bitbucket Authentication Plugin for SonarQube](https://github.com/SonarQubeCommunity/sonar-auth-bitbucket).

## Installation ##
1. Install the plugin into the *SONARQUBE_HOME/extensions/plugins* directory
1. Restart the SonarQube server

## Configuration ##
1. In Phabricator, create an OAuth application :
  1. Set "phabricator.show-prototypes" to true
  2. Go to "More Applications" -> "Administration" -> "OAuth Server" -> "Create OAuth Server"
  3. Name: Something like "SonarQube"
  4. Redirect URI: SonarQube_URL/oauth2/callback/phabricator
2. In SonarQube :
  1. Go to "Administration" -> "Configuration" -> "General Settings" -> "Security" -> "Phabricator"
  2. Set the "Enabled" property to true
  3. Set the "OAuth Application Client PHID" from the value provided by the Phabricator OAuth Application Client PHID
  4. Set the "OAuth Application Secret" from the value provided by the Phabricator OAuth Application Secret
  5. Set the "Phabricator Host" to the URL of your Phabricator server
3. Go to the login form, a new button "Log in with Phabricator" allow users to connect to SonarQube with their Phabricator accounts.

> Note: Only HTTPS is supported
> * SonarQube must be publicly accessible through HTTPS only
> * The property 'sonar.core.serverBaseURL' must be set to this public HTTPS URL

## General Configuration ##

Property | Description | Default value
---------| ----------- | -------------
sonar.auth.phabricator.allowUsersToSignUp|Allow new users to authenticate. When set to 'false', only existing users will be able to authenticate to the server|true
sonar.auth.phabricator.clientId.secured|Client PHID provided by Phabricator when registering the application|None
sonar.auth.phabricator.clientSecret.secured|Application Secret provided by Phabricator when registering the application|None
sonar.auth.phabricator.enabled|Enable Phabricator users to login. Value is ignored if Client PHID and Application are not defined|false
sonar.auth.phabricator.hostUrl|Phabricator server URL|https://secure.phabricator.com

## License ##

This project is licensed under the terms of the MIT license.
