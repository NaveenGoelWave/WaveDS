package com.wave.ds.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wave.ds.auth.dto.AuthResponse;
import com.wave.ds.auth.dto.LoginRequest;
import com.wave.ds.auth.dto.SignupRequest;
import com.wave.ds.auth.entity.AppUser;
import com.wave.ds.auth.entity.Role;
import com.wave.ds.auth.repository.AppUserRepository;
import com.wave.ds.auth.security.AuthProperties;
import com.wave.ds.auth.security.JwtService;
import com.wave.ds.common.exception.ConflictException;
import com.wave.ds.common.exception.ForbiddenException;
import com.wave.ds.common.exception.UnauthorizedException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String SECRET = "unit-test-secret-unit-test-secret-1234";

    @Mock
    AppUserRepository repository;

    // Cost factor 4 keeps the tests fast; production uses the default.
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
    JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(new AuthProperties(SECRET, 60, true));
    }

    private AuthService service(boolean signupEnabled) {
        return new AuthService(repository, passwordEncoder, jwtService,
                new AuthProperties(SECRET, 60, signupEnabled));
    }

    private void saveAssignsId() {
        when(repository.saveAndFlush(any(AppUser.class))).thenAnswer(inv -> {
            AppUser user = inv.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 1L);
            return user;
        });
    }

    private final SignupRequest signup = new SignupRequest("  Pravar  ", "  Pravar@Example.COM ", "Secret123");

    @Test
    void firstSignupBecomesAdminAndStoresHashedPassword() {
        when(repository.count()).thenReturn(0L);
        when(repository.existsByEmailIgnoreCase("pravar@example.com")).thenReturn(false);
        saveAssignsId();

        AuthResponse response = service(true).signup(signup);

        ArgumentCaptor<AppUser> saved = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).saveAndFlush(saved.capture());
        assertThat(saved.getValue().getEmail()).isEqualTo("pravar@example.com");
        assertThat(saved.getValue().getFullName()).isEqualTo("Pravar");
        assertThat(saved.getValue().getPasswordHash()).isNotEqualTo("Secret123");
        assertThat(passwordEncoder.matches("Secret123", saved.getValue().getPasswordHash())).isTrue();
        assertThat(response.user().role()).isEqualTo(Role.ADMIN);
        assertThat(jwtService.parse(response.token())).isPresent();
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void laterSignupsAreRegularUsers() {
        when(repository.count()).thenReturn(3L);
        when(repository.existsByEmailIgnoreCase("pravar@example.com")).thenReturn(false);
        saveAssignsId();

        assertThat(service(true).signup(signup).user().role()).isEqualTo(Role.USER);
    }

    @Test
    void duplicateEmailIsRejected() {
        when(repository.count()).thenReturn(1L);
        when(repository.existsByEmailIgnoreCase("pravar@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service(true).signup(signup)).isInstanceOf(ConflictException.class);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void signupIsBlockedWhenDisabledAndUsersExist() {
        when(repository.count()).thenReturn(1L);

        assertThatThrownBy(() -> service(false).signup(signup)).isInstanceOf(ForbiddenException.class);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void loginSucceedsWithCorrectPassword() {
        AppUser user = new AppUser("pravar@example.com", passwordEncoder.encode("Secret123"), "Pravar", Role.USER);
        ReflectionTestUtils.setField(user, "id", 9L);
        when(repository.findByEmailIgnoreCase("pravar@example.com")).thenReturn(Optional.of(user));

        AuthResponse response = service(true).login(new LoginRequest(" Pravar@example.com", "Secret123"));

        assertThat(jwtService.parse(response.token()).orElseThrow().id()).isEqualTo(9L);
    }

    @Test
    void loginFailsWithWrongPassword() {
        AppUser user = new AppUser("pravar@example.com", passwordEncoder.encode("Secret123"), "Pravar", Role.USER);
        when(repository.findByEmailIgnoreCase("pravar@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service(true).login(new LoginRequest("pravar@example.com", "WrongPass1")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void loginFailsForUnknownEmailWithSameMessage() {
        when(repository.findByEmailIgnoreCase("nobody@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service(true).login(new LoginRequest("nobody@example.com", "Secret123")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or password");
    }
}
