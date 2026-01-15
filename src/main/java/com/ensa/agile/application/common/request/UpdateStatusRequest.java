package com.ensa.agile.application.common.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateStatusRequest<T> {
    private final String id;
    private final String note;
    private final T status;

    public UpdateStatusRequest(String id, String note, T status) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("");
        }
        if (status == null) {
            throw new ValidationException("");
        }
        if (note != null && note.length() > 500) {
            throw new ValidationException("note cannot exceed 500 characters");
        }
        this.id = id;
        this.status = status;
        this.note = note;
    }
}
