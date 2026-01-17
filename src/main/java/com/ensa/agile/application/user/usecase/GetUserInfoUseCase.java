package com.ensa.agile.application.user.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.user.mapper.UserInfoResponseMapper;
import com.ensa.agile.application.user.response.UserInfoResponse;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;

@Component
public class GetUserInfoUseCase extends BaseUseCase<String, UserInfoResponse> {

    private final UserRepository userRepository;
    private final ICurrentUserService currentUserService;

    public GetUserInfoUseCase(
        UserRepository userRepository,
        ICurrentUserService currentUserService,
                              ITransactionalWrapper transactionalWrapper) {

        super(transactionalWrapper);
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public UserInfoResponse execute(String email) {
        if(email == null || email.isEmpty()) {
             User currentUser = currentUserService.getCurrentUser();
             return UserInfoResponseMapper.toResponse(currentUser);
        }
        User user = userRepository.findByEmail(email);
        return UserInfoResponseMapper.toResponse(user);
    }
}
