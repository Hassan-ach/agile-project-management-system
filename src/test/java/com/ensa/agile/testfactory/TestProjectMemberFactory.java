package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.member.ProjectMemberJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestProjectMemberFactory {

    public static ProjectMember validActiveMember() {
        return newProjectMember(RoleType.DEVELOPER, MemberStatus.ACTIVE);
    }

    public static ProjectMember validInvitedMember() {
        return newProjectMember(RoleType.DEVELOPER, MemberStatus.INVITED);
    }

    public static ProjectMember validInactiveMember() {
        return newProjectMember(RoleType.DEVELOPER, MemberStatus.INACTIVE);
    }

    private static ProjectMember newProjectMember(RoleType role,
                                                  MemberStatus status) {
        return ProjectMember.builder()
            .id(UUID.randomUUID())
            .user(TestUserFactory.validUser())
            .productBackLog(TestProductBackLogFactory.validProduct())
            .role(role)
            .status(status)
            .build();
    }

    public static ProjectMemberJpaEntity validJpaProjectMember(RoleType role,
                                                        MemberStatus status,ProductBackLogJpaEntity pb, UserJpaEntity user, UserJpaEntity logedUser) {
        return ProjectMemberJpaEntity.builder()
            .user(user)
            .productBackLog(pb)
            .role(role)
            .status(status)
        .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<ProjectMemberJpaEntity> multipleJpaProjectMembers(RoleType role,
                                                        MemberStatus status,ProductBackLogJpaEntity pb, UserJpaEntity user, UserJpaEntity logedUser, Integer count) {
        return IntStream.range(0, count)
            .mapToObj(i -> validJpaProjectMember(role, status, pb, user, logedUser))
            .toList();
    }
}
