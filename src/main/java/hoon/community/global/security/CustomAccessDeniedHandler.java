package hoon.community.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String referer = request.getHeader("REFERER");

        if(request.getRequestURI().split("/")[1].equals("api")) {
            response.sendRedirect("/exception/access-denied");
        } else {
            if(referer != null) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect("/");
            }
        }

    }
}
