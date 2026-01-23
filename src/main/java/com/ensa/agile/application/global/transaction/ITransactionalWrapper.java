package com.ensa.agile.application.global.transaction;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface ITransactionalWrapper {
  <T> T execute(ITransactionCallBack<T> callback);
}
