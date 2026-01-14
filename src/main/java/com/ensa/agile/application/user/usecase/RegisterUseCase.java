package com.ensa.agile.application.user.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.user.exception.EmailAlreadyUsedException;
import com.ensa.agile.application.user.mapper.AuthenticationResponseMapper;
import com.ensa.agile.application.user.mapper.RegisterRequestMapper;
import com.ensa.agile.application.user.request.RegisterRequest;
import com.ensa.agile.application.user.response.AuthenticationResponse;
import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.application.user.security.service.IAuthenticationService;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class RegisterUseCase
    extends BaseUseCase<RegisterRequest, AuthenticationResponse> {

    private final UserRepository userRepository;
    private final IPasswordEncoder passwordEncoder;
    private final IAuthenticationService authenticationService;

    public RegisterUseCase(UserRepository userRepository,
                           IPasswordEncoder passwordEncoder,
                           IAuthenticationService authenticationService,
                           ITransactionalWrapper transactionalWrapper) {
        super(transactionalWrapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public AuthenticationResponse execute(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        String hashedPassword =
            passwordEncoder.encode(registerRequest.getPassword());

        User u = RegisterRequestMapper.toUser(registerRequest, hashedPassword);

        userRepository.save(u);

        // for now, we return an empty refresh token
        return AuthenticationResponseMapper.toResponse(
            authenticationService.generateToken(u.getEmail()), "");
    }
}
