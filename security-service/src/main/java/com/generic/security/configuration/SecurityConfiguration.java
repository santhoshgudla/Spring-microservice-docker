package com.generic.security.configuration;

import com.generic.security.constant.Role;
import com.generic.security.filter.CustomAuthentication;
import com.generic.security.filter.CustomAuthorization;
import com.generic.security.repository.UserRepository;
import com.generic.security.service.SessionService;
import com.generic.security.service.UserPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserPrincipalService userPrincipalService;

    private UserRepository userRepository;

    private SessionService sessionService;

    public SecurityConfiguration(UserPrincipalService userPrincipalService, UserRepository userRepository,
                                 SessionService sessionService) {
        this.userPrincipalService = userPrincipalService;
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // remove csrf and state in session because in jwt we do not need them
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new CustomAuthentication(authenticationManager(),
                        this.sessionService, this.userRepository))
                .addFilter(new CustomAuthorization(authenticationManager(),
                        this.userRepository, this.sessionService))
                .authorizeRequests()
                // configure access rules
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/users").permitAll()
                .antMatchers("/provider/*").hasRole("PROVIDER")
                .antMatchers("/admin/*").hasRole(Role.ADMIN.name())
                .anyRequest().authenticated()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}