package com.vdx.BTR.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        System.out.println("== LOGIN SUCCESS ==");
        authentication.getAuthorities().forEach(a ->
                System.out.println("AUTH: " + a.getAuthority())
        );

        boolean isApprover = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_APPROVER"));

        System.out.println("isApprover = " + isApprover);

        if (isApprover) {
            response.sendRedirect("/approver/dashboard");
        } else {
            response.sendRedirect("/btr/dashboard");
        }
    }
}
