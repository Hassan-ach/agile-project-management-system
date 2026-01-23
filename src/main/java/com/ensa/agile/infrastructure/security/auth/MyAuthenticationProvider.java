package com.ensa.agile.infrastructure.security.auth;

import com.ensa.agile.application.user.exception.AuthenticationFailureException;
import com.ensa.agile.application.user.exception.InvalidCredentialsException;
import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final IPasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication)
        throws AuthenticationFailureException {

        String email = authentication.getName();
        User u = userRepository.findByEmail(email);
        String rawPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(rawPassword, u.getPassword())) {
            throw new InvalidCredentialsException();
        }
        List<GrantedAuthority> authorities =
            u.getAuthorities()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(u, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(
            authentication);
    }
}
