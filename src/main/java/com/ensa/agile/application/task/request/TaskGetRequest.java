package com.ensa.agile.application.task.request;

import com.ensa.agile.application.common.request.GetRequest;
import java.util.UUID;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TaskGetRequest extends GetRequest {
    public TaskGetRequest(UUID id, String with) { super(id, with); }
}
