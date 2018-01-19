package com.cdsxt.ro;

import java.util.Objects;

/**
 * 后台用户
 */

public class UserBack {

    private Integer uid;
    private String name;
    private String username; // 账号
    private String password;
    private Boolean online;

    public UserBack(Integer uid, String username, Boolean online) {
        this.uid = uid;
        this.username = username;
        this.online = online;
    }

    public UserBack() {
    }

    // 账号和id 做为唯一标识
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBack userBack = (UserBack) o;
        return Objects.equals(uid, userBack.uid) &&
                Objects.equals(username, userBack.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, username);
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
