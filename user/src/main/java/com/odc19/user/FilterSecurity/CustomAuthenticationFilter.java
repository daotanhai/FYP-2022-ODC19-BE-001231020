package com.odc19.user.FilterSecurity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc19.user.Repository.readRepository.UserReadRepository;
import com.odc19.user.entity.RoleEntity;
import com.odc19.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final UserReadRepository userRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserReadRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("UserName is: " + userName);
        log.info("Password is: " + password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        UserEntity userEntity = userRepository.getUserEntityAndRolesByUserName(user.getUsername());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withClaim("userName", user.getUsername())
                .withClaim("userId", userEntity.getUserId())
//                10 * 60 * 1000 = 10min
                .withExpiresAt(new Date(System.currentTimeMillis() + 525600L * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
//        refresh token for user when the old token 10min is expired > not let user to login again to access resources(APIs)
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
/*
        response.setHeader("access_token", access_token);K
        response.setHeader("refresh_token", refresh_token);
*/

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        tokens.put("fullName", userEntity.getFullName());
        tokens.put("email", userEntity.getEmail());
        RoleConverter roleConverter = new RoleConverter();
        List<String> roleList = roleConverter.convertRoleEntityToRoleName(userEntity.getRoleEntities());
        String delim = "-";
        tokens.put("roles", String.join(delim,roleList));
        log.info("Access token: \n" + access_token);
        log.info("Refresh token: \n" + refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Getter
    @Setter
    private static class RoleConverter {
        private String roleName;
        private List<String> convertRoleEntityToRoleName(Set<RoleEntity> roleEntitySet) {
            List<String> roleNames = new ArrayList<>();
            for (RoleEntity role : roleEntitySet) {
                roleName = role.getRoleName();
                roleNames.add(roleName);
            }
            return roleNames;
        }
    }
}
