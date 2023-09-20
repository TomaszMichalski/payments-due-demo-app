package com.projects.paymentsduedemo.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.projects.paymentsduedemo.consts.TestConsts.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedUserServiceTest {
    private AuthenticatedUserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthenticatedUserService();
    }

    @Test
    void shouldGetAuthenticatedUserId() {
        // given
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(USER_ID, null));

        // when
        String result = underTest.getAuthenticatedUserId();

        // then
        assertThat(result).isEqualTo(USER_ID);
    }
}
