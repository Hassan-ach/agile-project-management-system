package com.ensa.agile.domain.product.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProjectMember extends BaseDomainEntity {

    private User user;
    private ProductBackLog productBackLog;
    private RoleType role;
    private MemberStatus status;

    protected ProjectMember(ProjectMemberBuilder<?, ?> b) {
        super(b);
        this.user = b.user;
        this.productBackLog = b.productBackLog;
        this.role = b.role;
        this.status = b.status;

        validate();
    }

    public void updateMetadata(RoleType role, MemberStatus status) {
        this.role = role != null ? role : this.role;
        this.status = status != null ? status : this.status;
        this.validate();
    }

    public void validate() {
        ValidationUtil.requireNonNull(user, "project member user");
        ValidationUtil.requireNonNull(productBackLog,
                                      "project member product backlog");
    }

    public void activate() {
        ValidationUtil.requireNonNull(user, "project member user");

        this.status = ValidationUtil.requireStateTransition(
            this.status, MemberStatus.ACTIVE, "project member status", (s) -> {
                return s == MemberStatus.INACTIVE || s == MemberStatus.INVITED;
            });
    }

    public void deactivate() {
        ValidationUtil.requireNonNull(user, "project member user");

        this.status = ValidationUtil.requireStateTransition(
            this.status, MemberStatus.ACTIVE, "project member status",
            (s) -> { return s == MemberStatus.ACTIVE; });
    }

    public void setProducOwnerRole() {
        this.role =
            ValidationUtil.require(this.role, "project member role", (r) -> {
                return r != RoleType.PRODUCT_OWNER;
            }, "Project member is already a Product Owner.");
    }

    public void setScrumMasterRole() {
        this.role =
            ValidationUtil.require(this.role, "project member role", (r) -> {
                return r != RoleType.SCRUM_MASTER;
            }, "Project member is already a Scrum Master.");
    }

    public void setDeveloperRole() {
        this.role =
            ValidationUtil.require(this.role, "project member role", (r) -> {
                return r != RoleType.DEVELOPER;
            }, "Project member is already a Developer.");
    }
}
