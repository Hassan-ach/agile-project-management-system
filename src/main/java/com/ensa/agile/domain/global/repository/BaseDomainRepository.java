package com.ensa.agile.domain.global.repository;

import com.ensa.agile.domain.global.annotation.Loggable;
import java.util.List;

@Loggable
public interface BaseDomainRepository<T, ID> {
    T save(T entity);

    T findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    boolean existsById(ID id);
}
