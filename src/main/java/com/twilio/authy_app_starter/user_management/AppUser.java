package com.twilio.authy_app_starter.user_management;

import jakarta.persistence.*;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String userId;
    private String username;
    private String password;
    private boolean i2FAEnabled;
    private String _2faFactorSid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isI2FAEnabled() {
        return i2FAEnabled;
    }

    public void setI2FAEnabled(boolean i2FAEnabled) {
        this.i2FAEnabled = i2FAEnabled;
    }

    public String get_2faFactorSid() {
        return _2faFactorSid;
    }

    public void set_2faFactorSid(String _2faFactorSid) {
        this._2faFactorSid = _2faFactorSid;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", i2FAEnabled=" + i2FAEnabled +
                ", _2faFactorSid='" + _2faFactorSid + '\'' +
                '}';
    }
}
