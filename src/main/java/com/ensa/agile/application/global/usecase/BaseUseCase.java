package com.ensa.agile.application.global.usecase;

import com.ensa.agile.application.global.transaction.ITransactionCallBack;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.domain.global.annotation.Loggable;
import lombok.RequiredArgsConstructor;
@Loggable
@RequiredArgsConstructor
public abstract class BaseUseCase<T, D> implements IBaseUseCase<T, D> {
    private final ITransactionalWrapper transactionalWrapper;

    public D executeTransactionally(T data) {
        return transactionalWrapper.execute(new ITransactionCallBack<D>() {
            @Override
            public D execution() {
                return execute(data);
            }
        });
    }
}
