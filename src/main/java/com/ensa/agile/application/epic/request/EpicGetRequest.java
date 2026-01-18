package com.ensa.agile.application.epic.request;

import com.ensa.agile.application.common.request.GetRequest;
import java.util.UUID;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class EpicGetRequest extends GetRequest {

    public EpicGetRequest(UUID id, String with) {
        super(id, with);
    }
}
