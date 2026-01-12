package com.ensa.agile.domain.product.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
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

    private final User user;
    private final ProductBackLog productBackLog;
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
        if (role != null) {
            this.role = role;
        }
        if (status != null) {
            this.status = status;
        }
        this.validate();
    }

    public void validate() {
        if (this.user == null) {
            throw new ValidationException(
                "Project member must be associated with a user.");
        }
        if (this.productBackLog == null) {
            throw new ValidationException(
                "Project member must be associated with a product backlog.");
        }
        if (this.role == null) {
            throw new ValidationException(
                "Project member must have a role assigned.");
        }
        if (this.status == null) {
            throw new ValidationException(
                "Project member must have a status assigned.");
        }
    }

    public void activate() {
        if (this.user == null) {
            throw new ValidationException(
                "Cannot activate a project member without a user.");
        }
        if (this.status != MemberStatus.INVITED ||
            this.status == MemberStatus.ACTIVE) {
            throw new ValidationException(
                "Can only activate a project member who is in INVITED status.");
        }
        this.status = MemberStatus.ACTIVE;
        return;
    }

    public void deactivate() {
        if (this.user == null) {
            throw new ValidationException(
                "Cannot deactivate a project member without a user.");
        }
        if (this.status != MemberStatus.ACTIVE) {
            throw new ValidationException("Can only deactivate a project "
                                          + "member who is in ACTIVE status.");
        }
        this.status = MemberStatus.INACTIVE;
    }

    public void setProducOwnerRole() {
        if (this.role == RoleType.PRODUCT_OWNER) {
            throw new ValidationException(
                "Project member is already a Product Owner.");
        }
        this.role = RoleType.PRODUCT_OWNER;
    }

    public void setScrumMasterRole() {
        if (this.role == RoleType.SCRUM_MASTER) {
            throw new ValidationException(
                "Project member is already a Scrum Master.");
        }
        this.role = RoleType.SCRUM_MASTER;
    }

    public void setDeveloperRole() {
        if (this.role == RoleType.DEVELOPER) {
            throw new ValidationException(
                "Project member is already a Developer.");
        }
        this.role = RoleType.DEVELOPER;
    }
}
