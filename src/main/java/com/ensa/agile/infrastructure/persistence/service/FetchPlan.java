package com.ensa.agile.infrastructure.persistence.service;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class FetchPlan {

    boolean product;
    boolean members;
    boolean sprints;
    boolean sprintMembers;
    boolean stories;
    boolean epics;
    boolean Tasks;

    public static FetchPlan resolve(List<String> fetchFields) {
        FetchPlan p = new FetchPlan();
        if (fetchFields == null) {
            return p;
        }

        fetchFields = fetchFields.stream().map(String::toUpperCase).toList();

        if (FetchField.isAll(fetchFields)) {
            p.product = p.members = p.sprints = p.sprintMembers = true;
            p.epics = p.stories = p.Tasks = true;
            return p;
        }
        p.product = FetchField.has(fetchFields, FetchField.PRODUCT);
        p.members = FetchField.has(fetchFields, FetchField.MEMBERS);

        p.sprints = FetchField.has(fetchFields, FetchField.SPRINTS);
        p.sprintMembers =
            FetchField.has(fetchFields, FetchField.SPRINTS_MEMBERS);

        p.epics = FetchField.has(fetchFields, FetchField.EPICS);

        p.stories = FetchField.has(fetchFields, FetchField.USER_STORIES);

        p.Tasks = FetchField.has(fetchFields, FetchField.TASKS) && p.stories;

        return p;
    }
}
