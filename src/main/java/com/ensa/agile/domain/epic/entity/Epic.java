package com.ensa.agile.domain.epic.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class Epic extends BaseDomainEntity {

    private String title;
    private String description;
    private ProductBackLog productBackLog;
    private List<UserStory> userStories;

    protected Epic(EpicBuilder<?, ?> b) {
        super(b);
        this.title = b.title;
        this.description = b.description;
        this.productBackLog = b.productBackLog;
        this.userStories =
            b.userStories != null ? b.userStories : new ArrayList<>();

        validate();
    }

    public void updateMetadata(String title, String description) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        this.validate();
    }

    public void validate() {
        if (title == null || title.isEmpty()) {
            throw new ValidationException("Epic title cannot be null or empty");
        }

        if (description == null || description.isEmpty()) {
            throw new ValidationException(
                "Epic description cannot be null or empty");
        }
    }
}
