package com.greenfox.dramacsoport.petclinicbackend.models;

import org.springframework.security.core.GrantedAuthority;

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

    /**
     * By default, the user roles are stored with a ROLE_ prefix as a
     * GrantedAuthority (e.g. ROLE_USER).
     *
     * @param roleAsAuthority a user role stored as a GrantedAuthority object
     * @return a Role enum object, if it can be identified.
     */
    public static Role getRole(GrantedAuthority roleAsAuthority) {
        String rolePrefix = "ROLE_";
        String firstAuthorityName = roleAsAuthority.getAuthority();
        String roleNameAsString = firstAuthorityName.substring(rolePrefix.length());
        return fromString(roleNameAsString);
    }
}
