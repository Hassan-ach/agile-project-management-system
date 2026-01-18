package com.ensa.agile.application.task.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.task.repository.TaskRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;

@Component
public class GetUserTasksUseCase extends BaseUseCase<String,List<TaskResponse>>{

    private final ICurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public GetUserTasksUseCase(ITransactionalWrapper tr,
        UserRepository userRepository,
        ICurrentUserService getCurrentUserUseCase,
                                      TaskRepository taskRepository) {

        super(tr);
        this.currentUserService = getCurrentUserUseCase;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> execute(String email) {

        User user = null;

        if(email != null && !email.isEmpty()){
            user = userRepository.findByEmail(email);
        }else{
            user = currentUserService.getCurrentUser();
        }

        var tasks = taskRepository.findAllByAssigneeId(user.getId());

        return tasks.stream().map(TaskResponseMapper::toResponse).toList();
    }
}
