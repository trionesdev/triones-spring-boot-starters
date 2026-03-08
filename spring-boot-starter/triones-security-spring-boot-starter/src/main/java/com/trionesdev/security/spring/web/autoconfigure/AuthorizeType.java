package com.trionesdev.security.spring.web.autoconfigure;

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
