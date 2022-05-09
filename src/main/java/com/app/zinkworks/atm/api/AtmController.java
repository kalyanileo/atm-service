package com.app.zinkworks.atm.api;

import java.util.List;

import javax.validation.Valid;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.zinkworks.atm.exception.AtmException;
import com.app.zinkworks.atm.model.request.AccountBalanceRequest;
import com.app.zinkworks.atm.model.request.WithdrawAmountRequest;
import com.app.zinkworks.atm.model.response.BalanceResponse;
import com.app.zinkworks.atm.model.response.CurrencyResponse;
import com.app.zinkworks.atm.model.response.WithdrawAmountResponse;
import com.app.zinkworks.atm.service.AtmService;

@RequestMapping("/v1")
@RestController
public class AtmController {
	
	@Autowired
	private AtmService atmService;
	
	
	@PostMapping(value="/atm/withdrawal", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<WithdrawAmountResponse>
        withdrawCash(@Valid @RequestBody WithdrawAmountRequest withdrawAmountRequest) throws NumberFormatException, AtmException, JSONException {
    
            return atmService.withdrawMoney(withdrawAmountRequest);            
        
    }
	
	@PostMapping(value="/atm/account/balance", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<BalanceResponse> fetchAccountBalance(@Valid @RequestBody AccountBalanceRequest account) throws AtmException, JSONException {        
        
            return atmService.getAccountBalance(account);
        
    }




}
