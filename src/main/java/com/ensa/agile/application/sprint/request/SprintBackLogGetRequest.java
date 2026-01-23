package com.ensa.agile.application.sprint.request;

import java.util.UUID;

import com.ensa.agile.application.common.request.GetRequest;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class SprintBackLogGetRequest extends GetRequest {

    public SprintBackLogGetRequest(UUID id, String with) { super(id, with); }
}
