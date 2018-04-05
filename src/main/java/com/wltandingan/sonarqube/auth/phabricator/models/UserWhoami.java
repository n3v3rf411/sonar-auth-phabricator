package com.wltandingan.sonarqube.auth.phabricator.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Willie Loyd Tandingan
 * @since 1.0.0
 */
public class UserWhoami extends ConduitResponse<UserWhoami> {

    @SerializedName("userName")
    private String userName;

    @SerializedName("realName")
    private String realName;

    @SerializedName("primaryEmail")
    private String primaryEmail;

    public UserWhoami() {}

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
        return realName;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

}
