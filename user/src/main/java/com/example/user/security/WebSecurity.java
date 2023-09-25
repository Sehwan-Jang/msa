package com.example.user.security;

import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurity {

    private final AuthenticationConfiguration authConfig;
    private final UserService userService;
    private final Environment environment;


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll());
        IpAddressMatcher hasIpAddress = new IpAddressMatcher("127.0.0.1");
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
                        .access((authentication, object) ->
                               new AuthorizationDecision(hasIpAddress.matches(object.getRequest()))
                        )
        ).addFilter(getAuthenticationFilter());

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager,
                userService, environment);
        return authenticationFilter;
    }
}
