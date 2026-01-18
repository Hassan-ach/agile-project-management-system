package com.ensa.agile.application.epic.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class EpicRequest {
    UUID epicId;

    public EpicRequest(EpicRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }
        if (req.getEpicId() == null) {
            throw new ValidationException("epicId cannot be null");
        }

        this.epicId = req.getEpicId();
    }
}
