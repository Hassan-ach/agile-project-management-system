package com.ensa.agile.application.sprint.request;

import com.ensa.agile.application.common.request.GetRequest;
import java.util.UUID;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SprintBackLogGetRequest extends GetRequest {

    public SprintBackLogGetRequest(UUID id, String with) { super(id, with); }
}
