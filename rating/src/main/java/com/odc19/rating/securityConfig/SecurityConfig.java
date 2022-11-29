package com.odc19.rating.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS).and();
        http.authorizeRequests()
//                notification API
                /*.antMatchers(HttpMethod.GET, "/api/v1/medical-shop/list/{postCode}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/medical-shop/list","/api/v1/medical-shop/{medicalShopId}").permitAll()*/
                .antMatchers( "/api/v1/rating/**").permitAll()
                .anyRequest().authenticated().and().cors().disable();
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
