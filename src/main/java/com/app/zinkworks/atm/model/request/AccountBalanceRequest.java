package com.app.zinkworks.atm.model.request;

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
public class AccountBalanceRequest {
	
	@JsonProperty("AccountNumber")
    private int accountNumber;
		
    @JsonProperty("Pin")
    private int pin;

}
