package com.ensa.agile.infrastructure.persistence.jpa.global.transaction;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.ensa.agile.application.global.transaction.ITransactionCallBack;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.domain.global.exception.DataBaseTransactionException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TransactionWrapper implements ITransactionalWrapper {

    private final TransactionTemplate transactionTemplate;

    public <T> T execute(ITransactionCallBack<T> callBack) {
        return transactionTemplate.execute(status -> {
            try {
                return callBack.execution();
            }catch (DataIntegrityViolationException e) {
                status.setRollbackOnly();
                throw new DataBaseTransactionException("Data integrity violation occurred during transaction.", e);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new DataBaseTransactionException("An unexpected error occurred during transaction.", e);
            }
        });
    }
}
