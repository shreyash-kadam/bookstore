
package com.shreyashkadam.bookstore.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/admin/**");
    }

    // Custom Admin Interceptor
    public HandlerInterceptor adminInterceptor() {

        return new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) throws Exception {

                HttpSession session = request.getSession(false);

                // Not logged in → redirect to login
                if (session == null || session.getAttribute("loggedUser") == null) {
                    response.sendRedirect("/login");
                    return false;
                }

                Object role = session.getAttribute("role");

                // Role must be ADMIN
                if (role == null || !"ADMIN".equals(role.toString())) {
                    response.sendRedirect("/books");
                    return false;
                }

                return true; // Allow access
            }
        };
    }
}
