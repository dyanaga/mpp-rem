package com.dianagrigore.rem.dto;

import javax.validation.constraints.NotBlank;

public class LoginUserDto {
    @NotBlank(message = "Login id is missing!")
    private String loginId;
    @NotBlank(message = "Password was not given!")
    private String password;

    @Override
    public int hashCode() {
        int result = getLoginId() != null ? getLoginId().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoginUserDto that = (LoginUserDto) o;

        if (getLoginId() != null ? !getLoginId().equals(that.getLoginId()) : that.getLoginId() != null) {
            return false;
        }
        return getPassword() != null ? getPassword().equals(that.getPassword()) : that.getPassword() == null;
    }

    public String getLoginId() {
        return loginId;
    }

    public LoginUserDto setLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"LoginUserDto\", " +
                "\"loginId\":" + (loginId == null ? "null" : "\"" + loginId + "\"") + ", " +
                "\"password\":" + (password == null ? "null" : "\"" + password + "\"") +
                "}";
    }
}
