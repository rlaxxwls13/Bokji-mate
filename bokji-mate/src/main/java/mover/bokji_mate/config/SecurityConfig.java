package mover.bokji_mate.config;

import lombok.RequiredArgsConstructor;
import mover.bokji_mate.Service.RedisService;
import mover.bokji_mate.jwt.JwtTokenProvider;
import mover.bokji_mate.jwt.JwtVerificationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("members/sign-in").permitAll()
                        //.requestMatchers("members/sign-up").permitAll()
                        .requestMatchers("members/test").hasRole("USER")
                        //.requestMatchers("members/test").permitAll()
                        .requestMatchers("members/sign-out").hasRole("USER")
                        .requestMatchers("calendar/**").hasRole("USER")
                        .requestMatchers("policy/scrap/**").hasRole("USER")
                        .requestMatchers("policy/recommendation").hasRole("USER")
                        .requestMatchers("members/edit").hasRole("USER")
                        .requestMatchers("members/update-password").hasRole("USER")
                        .anyRequest().permitAll())
                        //.anyRequest().authenticated())
                .addFilterBefore(new JwtVerificationFilter(jwtTokenProvider, redisService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
