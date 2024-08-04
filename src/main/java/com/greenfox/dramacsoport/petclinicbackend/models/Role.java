package com.greenfox.dramacsoport.petclinicbackend.models;

public enum Role {
    USER, ADMIN, VET;

    public static Role fromString(String role) throws IllegalArgumentException {
        return switch (role) {
            case "USER" -> USER;
            case "ADMIN" -> ADMIN;
            case "VET" -> VET;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}
