package com.odc19.user.PasswordEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderCustom {
    @Bean
    public PasswordEncoder getDecoder() {
        return new BCryptPasswordEncoder();
    }
}
