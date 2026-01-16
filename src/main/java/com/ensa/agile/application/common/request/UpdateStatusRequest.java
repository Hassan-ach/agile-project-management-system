package com.ensa.agile.application.common.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateStatusRequest<T> {
    private final UUID id;
    private final String note;
    private final T status;

    public UpdateStatusRequest(UUID id, String note, T status) {
        if (id == null || !ValidationUtil.isValidUUID(id)) {
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
