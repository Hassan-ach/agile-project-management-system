package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.common.request.UpdateStatusRequest;
import com.ensa.agile.application.common.response.UpdateStatusResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.mapper.UserStoryHistoryResponseMapper;
import com.ensa.agile.application.story.response.UserStoryHistoryResponse;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.domain.story.repository.UserStoryHistoryRepository;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserStoryStatusUseCase
    extends BaseUseCase<UpdateStatusRequest<StoryStatus>,
                        UpdateStatusResponse<UserStoryHistoryResponse>> {

    private final UserStoryRepository userStoryRepository;
    private final UserStoryHistoryRepository userStoryHistoryRepository;

    public UpdateUserStoryStatusUseCase(ITransactionalWrapper tr,
                                        UserStoryRepository usr,
                                        UserStoryHistoryRepository ushr) {
        super(tr);
        this.userStoryRepository = usr;
        this.userStoryHistoryRepository = ushr;
    }

    @Override
    public UpdateStatusResponse<UserStoryHistoryResponse>
    execute(UpdateStatusRequest<StoryStatus> request) {
        UserStory story = this.userStoryRepository.findById(request.getId());

        UserStoryHistory status = story.getStatus();
        if (status.getStatus() == request.getStatus()) {
            return UpdateStatusResponse.<UserStoryHistoryResponse>builder()
                .status(UserStoryHistoryResponseMapper.toResponse(status))
                .updated(false)
                .message("Status is already " + request.getStatus().name())
                .build();
        }

        StoryStatus nextStatus = status.getNextStatus();

        if (nextStatus != request.getStatus()) {
            return UpdateStatusResponse.<UserStoryHistoryResponse>builder()
                .status(null)
                .updated(false)
                .message("Invalid status transition from " +
                         status.getStatus().name() + " to " +
                         request.getStatus().name())
                .build();
        }

        UserStoryHistory newStatus = UserStoryHistory.builder()
                                         .userStory(story)
                                         .status(request.getStatus())
                                         .note(request.getNote())
                                         .build();
        return UpdateStatusResponse.<UserStoryHistoryResponse>builder()
            .status(UserStoryHistoryResponseMapper.toResponse(
                this.userStoryHistoryRepository.save(newStatus)))
            .updated(true)
            .message("Status updated to " + request.getStatus().name())
            .build();
    }
}
