package com.ensa.agile.application.product.usecase;

import com.ensa.agile.application.common.request.RemoveRequest;
import com.ensa.agile.application.common.response.RemoveResponse;
import com.ensa.agile.application.common.usecase.RemoveUseCase;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class RemoveDeveloperUseCase extends RemoveUseCase {
    public RemoveDeveloperUseCase(ITransactionalWrapper tr,
                                  ProjectMemberRepository pm,
                                  UserRepository ur) {
        super(tr, pm, ur);
    }

    @Override
    public RemoveResponse execute(RemoveRequest request) {

        return super.removeUserWithRole(request, RoleType.DEVELOPER);
    }
}
