package com.trionesdev.spring.boot.security.autoconfigure;

public enum AuthorizeType {
    permitAll,
    denyAll,
    authenticated,
    anonymous,
    hasVariable,
    hasAuthority,
    hasAnyAuthority,
    hasRole,
    hasAnyRole
}
