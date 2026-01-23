package com.ensa.agile.application.product.response;

import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectMemberResponse {
    private UUID memberId;
    private String userEmail;
    private RoleType role;
    private MemberStatus status;

    private UUID invitedBy;
    private LocalDateTime invitationDate;
}
