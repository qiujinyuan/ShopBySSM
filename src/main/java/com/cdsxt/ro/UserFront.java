package com.cdsxt.ro;

/**
 * 用户
 */

public class UserFront {

    private Integer uid;
    private String username;
    private String password;
    private String name;
    private Integer state;
    private String securityEmail;
    private String securityPhone;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getSecurityEmail() {
        return securityEmail;
    }

    public void setSecurityEmail(String securityEmail) {
        this.securityEmail = securityEmail;
    }

    public String getSecurityPhone() {
        return securityPhone;
    }

    public void setSecurityPhone(String securityPhone) {
        this.securityPhone = securityPhone;
    }
}
