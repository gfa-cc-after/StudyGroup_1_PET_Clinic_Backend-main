package com.greenfox.dramacsoport.petclinicbackend.errors;

/**
 * This exception should be used when a role cannot be identified from a String (e.g when extracting from a token)
 */
public class UnIdentifiableRoleException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code Role cannot be retrieved!} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UnIdentifiableRoleException() {
        super("Role cannot be retrieved!");
    }
}
