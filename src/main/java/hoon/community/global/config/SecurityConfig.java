package hoon.community.global.config;

import hoon.community.domain.sign.service.TokenHelper;
import hoon.community.domain.sign.service.TokenService;
import hoon.community.global.security.CustomAccessDeniedHandler;
import hoon.community.global.security.CustomAuthenticationEntryPoint;
import hoon.community.global.security.CustomUserDetailsService;
import hoon.community.global.security.JwtAuthenticationFilter;
import hoon.community.global.security.oauth.CustomOAuth2UserService;
import hoon.community.global.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> {
            web.ignoring()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .antMatchers(
                            "/css/**", "/img/**", "/fonts/**", "/plugin/**", "/scripts/**", "/favicon.ico", "/resources/**", "/error", "/swagger-ui/**","/swagger-resources/**","/v3/api-docs/**", "**/favicon.ico"
                    )
                    .mvcMatchers(
                            "/exception/**"
                    );
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/image/**").permitAll()

                .antMatchers(HttpMethod.POST, "/api/sign-in", "/api/sign-up", "/api/refresh-token", "/api/duplicate-email-check").permitAll()

                .antMatchers(HttpMethod.DELETE, "/api/members/{id}/**").access("@memberGuard.check(#id)")
                .antMatchers(HttpMethod.GET, "/api/members/details").authenticated()
                .antMatchers(HttpMethod.GET, "/api/members").permitAll()
                .antMatchers(HttpMethod.POST, "/api/members/modify/password").authenticated()


                .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                .antMatchers(HttpMethod.POST, "/api/posts/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
//                        .antMatchers(HttpMethod.GET, "/api/posts/**").access("@postGuard.check(#id)")
                .antMatchers(HttpMethod.PATCH, "/api/posts/{id}").access("@postGuard.check(#id)")
                .antMatchers(HttpMethod.DELETE, "/api/posts/{id}").access("@postGuard.check(#id)")

                .antMatchers(HttpMethod.GET, "/api/comments/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/comments").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/comments/{id}").access("@commentGuard.check(#id)")

                .antMatchers(HttpMethod.GET, "/posts/create/**").authenticated()
                .antMatchers(HttpMethod.GET, "/posts/update/{id}").access("@postGuard.check(#id)")
                .antMatchers(HttpMethod.GET, "/posts/{id}").permitAll()

                .antMatchers(HttpMethod.GET, "/members/details", "/members/modify/**").authenticated()
                .antMatchers(HttpMethod.GET, "/members/list").permitAll()
                .antMatchers(HttpMethod.POST, "/members/modify/password").authenticated()
                .antMatchers(HttpMethod.POST, "/members/modify/roles/**").permitAll()


                .antMatchers("/auth/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()

                .anyRequest().hasAnyRole("ROLE_ADMIN")
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                .oauth2Login()
                    .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
//                .successHandler(authenticationSuccessHandler)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(accessTokenHelper, userDetailsService), UsernamePasswordAuthenticationFilter.class);




        return http.build();
    }
}

