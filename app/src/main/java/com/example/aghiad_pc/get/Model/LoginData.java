package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("user")
    private String user;
    @SerializedName("password")
    private String password;

    public LoginData(String email, String user) {
        this.user = user;
        this.password = password;
    }

    /**
     *
     * @return
     * The email
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The email
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
