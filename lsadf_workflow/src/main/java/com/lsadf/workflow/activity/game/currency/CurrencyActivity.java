package com.lsadf.workflow.activity.game.currency;

import com.lsadf.core.domain.game.currency.Currency;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CurrencyActivity {
  @ActivityMethod
  Currency getCurrency(String gameSaveId);

  @ActivityMethod
  void updateCurrency(String gameSaveId, Currency currency);
}
