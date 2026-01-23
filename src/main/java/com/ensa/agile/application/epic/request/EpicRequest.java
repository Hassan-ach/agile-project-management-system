package com.ensa.agile.application.epic.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
public class EpicRequest {
    UUID epicId;

    public EpicRequest(EpicRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }
        this.epicId = req.getEpicId();
        validate();
    }

    public EpicRequest(UUID id) { this.epicId = id; }

    public void validate() {
        if (epicId == null) {
            throw new ValidationException("epicId cannot be null");
        }
    }
}
