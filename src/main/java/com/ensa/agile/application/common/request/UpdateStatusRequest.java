package com.ensa.agile.application.common.request;

import java.util.UUID;

import com.ensa.agile.domain.global.exception.ValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UpdateStatusRequest<T> {
    private UUID id;
    private String note;
    private T status;

    public UpdateStatusRequest(UUID id, String note, T status) {
        this.id = id;
        this.status = status;
        this.note = note;
        validate();
    }

    void validate() {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (status == null) {
            throw new ValidationException("Status cannot be null");
        }
        if (note != null && note.length() > 500) {
            throw new ValidationException("note cannot exceed 500 characters");
        }
    }
}
