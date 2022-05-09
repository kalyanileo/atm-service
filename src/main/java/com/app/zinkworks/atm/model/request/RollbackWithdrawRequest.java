package com.app.zinkworks.atm.model.request;

import java.math.BigDecimal;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RollbackWithdrawRequest {
	
	@NonNull
	@JsonProperty("Amount")
    private BigDecimal amount;
	
	@NonNull
    @JsonProperty("AccountNumber")
    private int accountNumber;
	
	@NonNull
	@JsonProperty("Overdraft")	
    private BigDecimal overdraft;
	


}
