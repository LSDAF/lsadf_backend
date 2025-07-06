package com.lsadf.workflow.activity.game.currency;

import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.domain.game.currency.Currency;

public class CurrencyActivityImpl implements CurrencyActivity {

  private final CurrencyService currencyService;

  public CurrencyActivityImpl(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  @Override
  public Currency getCurrency(String gameSaveId) {
    return currencyService.getCurrency(gameSaveId);
  }

  @Override
  public void updateCurrency(String gameSaveId, Currency currency) {
    currencyService.saveCurrency(gameSaveId, currency, true);
  }
}
