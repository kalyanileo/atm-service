package com.app.zinkworks.atm.model.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WithdrawAccountBalanceResponse {
	
	@JsonProperty("AccountNumber")
	private int accountNumber;
	
	@JsonProperty("PrevOverdraft")
    private BigDecimal prevOverdraft;
	
	@JsonProperty("NewOverdraft")
    private BigDecimal newOverdraft;
	
	@JsonProperty("PrevBalance")
    private BigDecimal prevBalance;
	
	@JsonProperty("NewBalance")
    private BigDecimal newBalance;
	
	@JsonProperty("WithdrawAmount")
    private BigDecimal withdrawAmount;

}
