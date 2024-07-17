package com.greenfox.dramacsoport.petclinicbackend.dtos;

import java.util.Objects;

public final class RegisterRequestDTO {
    private final String username;
    private final String email;
    private final String password;

    public RegisterRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RegisterRequestDTO) obj;
        return Objects.equals(this.username, that.username) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }

    @Override
    public String toString() {
        return "RegisterRequestDTO[" +
                "username=" + username + ", " +
                "email=" + email + ", " +
                "password=" + password + ']';
    }

}
