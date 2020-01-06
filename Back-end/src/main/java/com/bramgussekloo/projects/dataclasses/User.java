package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details from User")
public class User {

    @ApiModelProperty(notes = "Auto-assigned id of the user")
    private int id;

    @ApiModelProperty(notes = "The name of the user", required = true)
    private String user_name;

    @ApiModelProperty(notes = "The password. It will be encrypted after making it", required = true)
    private String password;

    @ApiModelProperty(notes = "Authority should be either \"ROLE_USER\" or \"ROLE_ADMIN\", Admin can make users and normal users can't", required = true)
    private String authority;

    @ApiModelProperty(notes = "Should be set to true if the user wants to use the features in the api.", required = true)
    private Boolean enabled;

    public User(int id, String user_name, String password, String authority, Boolean enabled) {
        this.id = id;
        this.password = password;
        this.user_name = user_name;
        this.authority = authority;
        this.enabled = enabled;
    }

    public User() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
