package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.member.SprintMemberJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestSprintMemberFactory {

    public static SprintMemberJpaEntity validJpaSprintMember(SprintBackLogJpaEntity sb, UserJpaEntity user, UserJpaEntity logedUser) {

        return SprintMemberJpaEntity.builder()
            .sprintBackLog(sb)
            .user(user)
            .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<SprintMemberJpaEntity> multipleJpaSprintMembers(SprintBackLogJpaEntity sb, UserJpaEntity user, UserJpaEntity logedUser, Integer count) {
        return java.util.stream.IntStream.range(0, count)
            .mapToObj(i -> validJpaSprintMember(sb, user, logedUser))
            .toList();
    }
    
}
