package com.wave.ds.auth.service;

import com.wave.ds.auth.dto.AuthResponse;
import com.wave.ds.auth.dto.LoginRequest;
import com.wave.ds.auth.dto.SignupRequest;
import com.wave.ds.auth.dto.UserResponse;
import com.wave.ds.auth.entity.AppUser;
import com.wave.ds.auth.entity.Role;
import com.wave.ds.auth.repository.AppUserRepository;
import com.wave.ds.auth.security.AuthProperties;
import com.wave.ds.auth.security.JwtService;
import com.wave.ds.common.exception.ConflictException;
import com.wave.ds.common.exception.ForbiddenException;
import com.wave.ds.common.exception.UnauthorizedException;
import java.util.Locale;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String BAD_CREDENTIALS = "Invalid email or password";

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthProperties properties;

    /** Compared against when the email is unknown, so unknown and wrong-password logins take similar time. */
    private final String dummyHash;

    public AuthService(AppUserRepository repository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthProperties properties) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.properties = properties;
        this.dummyHash = passwordEncoder.encode("not-a-real-password");
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        long existingUsers = repository.count();
        if (!properties.signupEnabled() && existingUsers > 0) {
            throw new ForbiddenException("Sign-up is disabled. Ask an administrator to create your account.");
        }

        String email = normalize(request.email());
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("An account with this email already exists");
        }

        // The very first account becomes the administrator; everyone after that is a regular user.
        Role role = existingUsers == 0 ? Role.ADMIN : Role.USER;
        AppUser user = new AppUser(email, passwordEncoder.encode(request.password()),
                request.fullName().trim(), role);
        try {
            user = repository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            // Two sign-ups with the same email at the same instant: the unique index decides.
            throw new ConflictException("An account with this email already exists");
        }
        return respond(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        AppUser user = repository.findByEmailIgnoreCase(normalize(request.email())).orElse(null);
        boolean passwordMatches = passwordEncoder.matches(
                request.password(), user != null ? user.getPasswordHash() : dummyHash);
        if (user == null || !passwordMatches || !user.isEnabled()) {
            throw new UnauthorizedException(BAD_CREDENTIALS);
        }
        return respond(user);
    }

    @Transactional(readOnly = true)
    public UserResponse me(Long userId) {
        AppUser user = repository.findById(userId)
                .filter(AppUser::isEnabled)
                .orElseThrow(() -> new UnauthorizedException("Account no longer exists"));
        return toResponse(user);
    }

    private AuthResponse respond(AppUser user) {
        String token = jwtService.generate(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, "Bearer", jwtService.expiresInSeconds(), toResponse(user));
    }

    private static UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
