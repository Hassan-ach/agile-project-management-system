package com.ensa.agile.domain.product.member;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestProjectMemberFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import org.junit.jupiter.api.Test;

public class ProjectMemberTest {

    @Test
    void shouldCreateProjectMember_whenAllFieldsValid() {
        User user = TestUserFactory.validUser();
        ProductBackLog product = TestProductBackLogFactory.validProduct();

        assertDoesNotThrow(()
                               -> ProjectMember.builder()
                                      .user(user)
                                      .productBackLog(product)
                                      .role(RoleType.DEVELOPER)
                                      .status(MemberStatus.INVITED)
                                      .build());
    }

    @Test
    void shouldFail_whenUserIsNull() {
        ProductBackLog product = TestProductBackLogFactory.validProduct();

        assertThrows(ValidationException.class,
                     ()
                         -> ProjectMember.builder()
                                .user(null)
                                .productBackLog(product)
                                .role(RoleType.DEVELOPER)
                                .status(MemberStatus.INVITED)
                                .build());
    }

    @Test
    void shouldActivateMember_whenInvited() {
        ProjectMember member = TestProjectMemberFactory.validInvitedMember();

        member.activate();

        assertEquals(MemberStatus.ACTIVE, member.getStatus());
    }

    @Test
    void shouldFailActivation_whenNotInvited() {
        ProjectMember member = TestProjectMemberFactory.validActiveMember();

        assertThrows(ValidationException.class, member::activate);
    }

    @Test
    void shouldFail_whenSettingSameRole() {
        ProjectMember member = TestProjectMemberFactory.validActiveMember();
        assertThrows(ValidationException.class,
                     () -> member.setDeveloperRole());
    }

    @Test
    void shouldUpdateRoleAndStatusSuccessfully() {
        ProjectMember member = TestProjectMemberFactory.validActiveMember();

        assertDoesNotThrow(() -> {
            member.setScrumMasterRole();
            member.deactivate();
        });

        assertEquals(RoleType.SCRUM_MASTER, member.getRole());
        assertEquals(MemberStatus.INACTIVE, member.getStatus());
    }
}
