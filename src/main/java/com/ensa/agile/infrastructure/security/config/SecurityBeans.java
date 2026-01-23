package com.ensa.agile.infrastructure.security.config;

import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.infrastructure.security.utils.PasswordEncoderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Profile("!test")
public class SecurityBeans {

    @Bean
    public IPasswordEncoder passwordEncoderImpl() {
        return new PasswordEncoderImpl(new BCryptPasswordEncoder());
    }
}
