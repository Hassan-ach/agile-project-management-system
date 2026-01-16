package com.ensa.agile.application.product.request;

import java.util.UUID;

import com.ensa.agile.application.common.request.GetRequest;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ProductBackLogGetRequest extends GetRequest {
    public ProductBackLogGetRequest(UUID id, String with) { super(id, with); }
}
