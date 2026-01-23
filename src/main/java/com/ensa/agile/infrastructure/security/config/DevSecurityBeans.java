package com.ensa.agile.infrastructure.security.config;

import com.ensa.agile.application.user.security.IPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class DevSecurityBeans {

    @Bean
    public IPasswordEncoder passwordEncoderImpl() {
        return new IPasswordEncoder() {
            @Override
            public String encode(final CharSequence pass) {
                return pass.toString();
            }
            @Override
            public boolean matches(final CharSequence rawPass,
                                   final String encodedPass) {
                return rawPass.toString().equals(encodedPass);
            }
        };
    }
}
