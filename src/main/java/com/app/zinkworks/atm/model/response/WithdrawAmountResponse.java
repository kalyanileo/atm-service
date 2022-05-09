package com.app.zinkworks.atm.model.response;

import java.math.BigDecimal;
import java.util.List;

import com.app.zinkworks.atm.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WithdrawAmountResponse {
	
	@JsonProperty("CurrencyList")
	private List<CurrencyResponse> currencyList;
	
	@JsonProperty("AvailableBalance")
	private BigDecimal availableBalance;
	
	@JsonProperty("AvailableOverdraft")
	private BigDecimal availableOverdraft;
}
