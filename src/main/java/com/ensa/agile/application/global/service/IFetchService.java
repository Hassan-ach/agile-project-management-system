package com.ensa.agile.application.global.service;

import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface IFetchService {
    ProductBackLogResponse getResponse(ProductBackLogGetRequest req);
    SprintBackLogResponse getResponse(SprintBackLogGetRequest req);
    EpicResponse getResponse(EpicGetRequest req);
    UserStoryResponse getResponse(UserStoryGetRequest req);
}
