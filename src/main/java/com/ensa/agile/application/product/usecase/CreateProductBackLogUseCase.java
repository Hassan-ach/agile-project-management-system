package com.ensa.agile.application.product.usecase;

import com.ensa.agile.application.global.service.ICurrentUser;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.product.mapper.ProductBackLogResponseMapper;
import com.ensa.agile.application.product.request.ProductBackLogCreateRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CreateProductBackLogUseCase
    extends BaseUseCase<ProductBackLogCreateRequest, ProductBackLogResponse> {

    private final ProductBackLogRepository productBackLogRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ICurrentUser currentUserService;

    public CreateProductBackLogUseCase(
        ProductBackLogRepository productBackLogRepository,
        ProjectMemberRepository projectMemberRepository,
        ICurrentUser currentUserService,
        ITransactionalWrapper transactionalWrapper) {

        super(transactionalWrapper);
        this.productBackLogRepository = productBackLogRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.currentUserService = currentUserService;
    }

    public ProductBackLogResponse execute(ProductBackLogCreateRequest request) {

        ProductBackLog pb = ProductBackLog.builder()
                                .name(request.getName())
                                .description(request.getDescription())
                                .build();
        ProductBackLog pbSaved = productBackLogRepository.save(pb);

        User u = currentUserService.getCurrentUser();

        ProjectMember pm = ProjectMember.builder()
                               .user(u)
                               .productBackLog(pbSaved)
                               .role(RoleType.PRODUCT_OWNER)
                               .status(MemberStatus.ACTIVE)
                               .build();

        projectMemberRepository.save(pm);

        ProductBackLogResponse response =
            ProductBackLogResponseMapper.toResponse(pbSaved);
        return response;
    }
}
