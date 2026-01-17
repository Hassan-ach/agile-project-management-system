package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.infrastructure.persistence.jpa.epic.EpicJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestEpicFactory {

    public static Epic validEpic() {
        return Epic.builder()
            .title("Valid Epic Title")
            .description("Valid Epic Description")
            .productBackLog(TestProductBackLogFactory.validProduct())
            .build();
    }

    public static EpicJpaEntity validJpaEpic(ProductBackLogJpaEntity pb, UserJpaEntity logedUser){
        return EpicJpaEntity.builder()
            .title("Valid Epic Title")
            .description("Valid Epic Description")
            .productBackLog(pb)
        .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<EpicJpaEntity> multipleJpaEpics(ProductBackLogJpaEntity pb, UserJpaEntity logedUser, Integer count) {

        return IntStream.range(0, count)
            .mapToObj(i -> validJpaEpic(pb, logedUser))
            .toList();

    }
}
