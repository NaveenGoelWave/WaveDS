package com.wave.ds.auth.security;

import com.wave.ds.auth.entity.Role;

/** The authenticated caller, rebuilt from the JWT on every request (no database lookup). */
public record AuthUser(Long id, String email, Role role) {
}
