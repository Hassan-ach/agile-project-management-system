package com.ensa.agile.application.common.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class InviteResponse {
    private final String message;
    private final UUID inviteId;
    private final boolean success;
}
