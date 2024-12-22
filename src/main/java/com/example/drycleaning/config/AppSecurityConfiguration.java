package com.example.drycleaning.config;

import com.example.drycleaning.models.UserRoles;
import com.example.drycleaning.repositories.UserRepository;
import com.example.drycleaning.services.impl.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AppSecurityConfiguration {
    private UserRepository userRepository;

    public AppSecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                                authorizeHttpRequests.
                                        requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                        .permitAll()
                                        .requestMatchers("/favicon.ico").permitAll()
                                        .requestMatchers("/error","/auth/logout").permitAll()
                                        .requestMatchers("/","/branches", "/products", "/services", "/auth/login", "/auth/register", "/auth/login-error").permitAll().
                                        requestMatchers("/profile","/profile/edit", "/profile/orders","/profile/review","/user/order", "/user/order/create","/user/order/edit","/user/order/editBranch", "/user/order/create").authenticated().
                                        requestMatchers("/").authenticated().
                                        requestMatchers("/admin/branches", "/admin/orders", "/admin/orders/edit", "/admin/products", "/admin/services","/admin/users" ).hasAnyRole(UserRoles.MODERATOR.name(), UserRoles.ADMIN.name()).
                                        requestMatchers("/admin/branches/create", "/admin/branches/edit", "/admin/branches/delete","/admin/products/create", "/admin/products/edit","/admin/products/delete","/admin/services/create","/admin/services/edit","/admin/services/delete", "/admin/users/edit","/admin/users/delete" ).hasRole(UserRoles.ADMIN.name()).
                                        anyRequest().authenticated()
                )
                .formLogin(
                        (formLogin) ->//описываем форму логина
                                formLogin.
                                        loginPage("/auth/login").
                                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                                        defaultSuccessUrl("/").
                                        failureForwardUrl("/auth/login-error")
                )
                .logout((logout) ->
                        logout.logoutUrl("/auth/logout").
                                logoutSuccessUrl("/").
                                invalidateHttpSession(true)
                ).securityContext(
                        securityContext -> securityContext.
                                securityContextRepository(securityContextRepository)
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() { return new AppUserDetailsService(userRepository); }
}
