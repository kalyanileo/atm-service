package com.app.zinkworks.atm.mapper;

import com.app.zinkworks.atm.model.Currency;
import com.app.zinkworks.atm.model.response.CurrencyResponse;

public class CurrencyMapper {
	public static CurrencyResponse toCurrency(Currency currency) {
        return CurrencyResponse.builder()
                .quantity(currency.getQuantity())
                .currency(currency.getCurrency())
                .build();
    }

}
