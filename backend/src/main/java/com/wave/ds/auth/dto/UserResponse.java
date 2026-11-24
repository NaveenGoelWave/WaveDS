package com.wave.ds.auth.dto;

import com.wave.ds.auth.entity.Role;

public record UserResponse(Long id, String fullName, String email, Role role) {
}
