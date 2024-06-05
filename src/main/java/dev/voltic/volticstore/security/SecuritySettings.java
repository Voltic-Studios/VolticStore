package dev.voltic.volticstore.security;

import dev.voltic.volticstore.security.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecuritySettings {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                request -> {
                    request.requestMatchers("/", "/about", "/products/**", "/login", "/login/**", "/register", "/register/**", "/static/**").permitAll();
                    request.requestMatchers("/dashboard", "/dashboard/**").hasAnyAuthority("ADMIN");
                    request.requestMatchers("/panel", "/panel/**", "/cart", "/api/**").authenticated();
                }
        ).formLogin(
                login -> {
                    login.loginPage("/login");
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                    login.defaultSuccessUrl("/");
                    login.successForwardUrl("/");
                }
        ).logout(
                logout -> {
                    logout.logoutRequestMatcher(request -> request.getServletPath().equals("/logout"));
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessUrl("/");
                }
        ).exceptionHandling(
                exception -> {
                    exception.accessDeniedPage("/403");
                }
        ).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
