package com.ensa.agile.application.sprint.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SprintMemberResponse {
    private UUID id;
    private String userEmail;

    private UUID invitedBy;
    private LocalDateTime joinedAt;
}
