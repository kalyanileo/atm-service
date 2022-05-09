package com.app.zinkworks.atm.model.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@NonNull
public class WithdrawAmountRequest {
	
	@NonNull
	@JsonProperty("Amount")
    private BigDecimal amount;
	
	@NonNull
    @JsonProperty("AccountNumber")
    private int accountNumber;
	
	@NonNull
    @JsonProperty("Pin")
    private int pin;

}
