package com.odc19.user.SecurityConfig;

import com.odc19.user.FilterSecurity.CustomAuthenticationFilter;
import com.odc19.user.FilterSecurity.CustomAuthorizationFilter;
import com.odc19.user.PasswordEncoder.PasswordEncoderCustom;
import com.odc19.user.Repository.readRepository.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserReadRepository userRepository;

    @Autowired
    PasswordEncoderCustom passwordService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordService.getDecoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Collections.singletonList("Bearer "));*/
//        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), userRepository);
//        custom login api url
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/user/login");
        http.cors().and().csrf().disable();
//        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS).and();
//      this line will accept some url that can be public without authentication
        http.authorizeRequests()
                .antMatchers("/v3/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-ui/**").permitAll()
                .antMatchers("/api/v1/user/login/**", "/api/v1/user/refresh-token/**", "/api/v1/user/new", "/api/v1/user/password/reset","/api/v1/user/list/shipper", "/api/v1/user/{id}","/api/v1/user/new/shipper").permitAll()
//                user API
                .antMatchers(HttpMethod.GET, "/api/v1/user/list/**").hasAnyAuthority("Admin")

//                .anyRequest().authenticated().and().cors().configurationSource(request -> corsConfiguration);
                  .anyRequest().authenticated().and().cors().disable();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }
}
