package com.projects.paymentsduedemo.users;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    public String getAuthenticatedUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
