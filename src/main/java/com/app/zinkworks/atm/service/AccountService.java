package com.app.zinkworks.atm.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.app.zinkworks.atm.exception.AtmException;
import com.app.zinkworks.atm.model.request.AccountBalanceRequest;
import com.app.zinkworks.atm.model.request.RollbackWithdrawRequest;
import com.app.zinkworks.atm.model.request.WithdrawAmountRequest;
import com.app.zinkworks.atm.model.response.BalanceResponse;
import com.app.zinkworks.atm.model.response.WithdrawAccountBalanceResponse;

@Service
public class AccountService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${zinkworks.account.url}")
	private String accountServiceURL;
	
	
	public WithdrawAccountBalanceResponse withdrawAmount(WithdrawAmountRequest withdrawAmountRequest) throws AtmException, JSONException {		
        ResponseEntity<WithdrawAccountBalanceResponse> response = null;
        
        try {
        	response = restTemplate.exchange(accountServiceURL + "account/withdrawal", HttpMethod.POST, new HttpEntity<>(withdrawAmountRequest), WithdrawAccountBalanceResponse.class);
        } catch (HttpClientErrorException ex) {         	   
        	JSONObject jsonObj = new JSONObject(ex.getResponseBodyAsString());
        	throw new AtmException(jsonObj.get("message").toString());
        }
		
		return response.getBody(); 
	}
	
	public BalanceResponse rollbackWithdrawal(RollbackWithdrawRequest rollbackWithdrawRequest) throws AtmException, JSONException {		
        ResponseEntity<BalanceResponse> response = null;
        try {
        	response = restTemplate.exchange(accountServiceURL + "account/rollback/withdrawal", HttpMethod.POST, new HttpEntity<>(rollbackWithdrawRequest), BalanceResponse.class);
        } catch (HttpClientErrorException ex) {         	   
        	JSONObject jsonObj = new JSONObject(ex.getResponseBodyAsString());
        	throw new AtmException(jsonObj.get("message").toString());
        }
		return response.getBody(); 
	}
	
	public BalanceResponse getAccountBalance(AccountBalanceRequest accountBalanceRequest)  throws AtmException, JSONException {		
        ResponseEntity<BalanceResponse> response = null;	
        try {
        	response = restTemplate.exchange(accountServiceURL + "account/balance", HttpMethod.POST, new HttpEntity<>(accountBalanceRequest), BalanceResponse.class);
        } catch (HttpClientErrorException ex) {         	   
        	JSONObject jsonObj = new JSONObject(ex.getResponseBodyAsString());
        	throw new AtmException(jsonObj.get("message").toString());
        }
		return response.getBody(); 
	}

}
