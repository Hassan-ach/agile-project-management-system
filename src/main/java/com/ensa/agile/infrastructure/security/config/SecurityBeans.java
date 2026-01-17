package com.ensa.agile.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.infrastructure.security.utils.PasswordEncoderImpl;

@Configuration
@Profile("!dev")
public class SecurityBeans {

    @Bean
    public IPasswordEncoder passwordEncoderImpl(){
        return new PasswordEncoderImpl(new BCryptPasswordEncoder());
    }

}
