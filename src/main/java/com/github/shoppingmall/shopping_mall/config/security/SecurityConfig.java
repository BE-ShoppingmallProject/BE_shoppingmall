package com.github.shoppingmall.shopping_mall.config.security;


import com.github.shoppingmall.shopping_mall.web.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(h->h.frameOptions(f->f.sameOrigin()))
                .csrf((c)->c.disable())
                .httpBasic(h->h.disable())
                .formLogin(f->f.disable())
//                .oauth2Login(o->o.loginPage("/api/account/login"))
                .rememberMe(r->r.disable())
//                .cors(c->{
//                    c.configurationSource(corsConfigurationSource());
//                })
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e->{
                    e.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                    e.accessDeniedHandler(new CustomAccessDeniedHandler());
                })
                .authorizeRequests(a ->
                        a
                                .requestMatchers("/resources/static/**", "/api/sign/*").permitAll()
                                .requestMatchers("/admin/**", "/api/account/set-super-user","/api/seller/*").hasAnyRole("ADMIN","SELLER")
                                .requestMatchers("/api/sign/logout").hasAnyRole("ADMIN", "NORMAL", "SELLER")
                                .requestMatchers("/api/account/**", "/api/items/**", "/api/cart/**", "/api/cartItem/**", "/api/post/**", "/api/posts/**", "/api/delete/**").hasAnyRole("ADMIN", "NORMAL", "SELLER")

                )
                .logout(l -> {
                    l.logoutUrl("/api/sign/logout"); // 로그아웃을 수행할 URL
                    l.logoutSuccessHandler((rq, rs, a) -> {}); // 로그아웃 성공 시의 동작 (비워둠)
                    l.invalidateHttpSession(true);
                    l.deleteCookies("JSESSIONID");
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



}
