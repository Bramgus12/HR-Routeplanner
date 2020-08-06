package com.bramgussekloo.projects.utilities;

import com.bramgussekloo.projects.exceptions.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.addHeader("content-type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        System.out.println("HTTP STATUS 401 - " + authEx.getMessage() + " - URI: " + request.getRequestURI() + " - Host: " + request.getRemoteHost());
        writer.print(objectMapper.writeValueAsString(new Error(401, "Unathorized", authEx.getMessage(), request.getRequestURI())));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("DeveloperStack");
        super.afterPropertiesSet();
    }

}