package com.ensa.agile.testfactory;

import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import java.util.UUID;

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
}
