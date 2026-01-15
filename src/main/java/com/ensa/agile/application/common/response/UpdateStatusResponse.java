package com.ensa.agile.application.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UpdateStatusResponse<T> {
    private T status;
    private boolean updated;
    private String message;
}
