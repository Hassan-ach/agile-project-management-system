package com.ensa.agile.infrastructure.persistence.service;
import java.util.List;

public enum FetchField {

    ALL,
    PRODUCT,
    MEMBERS,

    SPRINTS,
    SPRINTS_MEMBERS,

    EPICS,

    USER_STORIES,

    TASKS;

    public static boolean has(List<String> fields, FetchField f) {
        return fields != null && fields.contains(f.name());
    }

    public static boolean isAll(List<String> fields) {
        return fields != null && fields.contains(ALL.name());
    }
}
