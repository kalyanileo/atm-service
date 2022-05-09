package com.app.zinkworks.atm.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.zinkworks.atm.adapter.ResponseAdapter;
import com.app.zinkworks.atm.exception.AtmException;
import com.app.zinkworks.atm.mapper.CurrencyMapper;
import com.app.zinkworks.atm.model.Currency;
import com.app.zinkworks.atm.model.request.AccountBalanceRequest;
import com.app.zinkworks.atm.model.request.RollbackWithdrawRequest;
import com.app.zinkworks.atm.model.request.WithdrawAmountRequest;
import com.app.zinkworks.atm.model.response.BalanceResponse;
import com.app.zinkworks.atm.model.response.CurrencyResponse;
import com.app.zinkworks.atm.model.response.WithdrawAccountBalanceResponse;
import com.app.zinkworks.atm.model.response.WithdrawAmountResponse;
import com.app.zinkworks.atm.repository.CurrencyRepository;
import com.app.zinkworks.atm.utils.AtmUtility;


@Service
public class AtmService {
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Autowired
	private ResponseAdapter responseAdapter;
	
	@Autowired
	private AccountService accountService;
	
	@Transactional
	public ResponseEntity<WithdrawAmountResponse> withdrawMoney(WithdrawAmountRequest withdrawAmountRequest) throws AtmException, JSONException {
		
		List<CurrencyResponse> currencyNotes = null;		
		List<Currency> currencyList = currencyRepository.findAll();
		int amount =  withdrawAmountRequest.getAmount().intValue();
		int accountNumber = withdrawAmountRequest.getAccountNumber();
		
	    Map<Integer, CurrencyResponse> currencyMap
	                = currencyList.stream()
	                .map(currency -> CurrencyMapper.toCurrency(currency))
	                    .collect(Collectors.toMap(key -> key.getCurrency(), value -> value));
	     
	    currencyNotes = getCurrencyNotes(amount,currencyMap);	
	    
	    AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
	    											  .accountNumber(accountNumber)
	    											  .pin(withdrawAmountRequest.getPin())
	    											  .build();
	    											  
	    BalanceResponse balanceResponse = accountService.getAccountBalance(accountBalanceRequest);
		WithdrawAccountBalanceResponse details = accountService.withdrawAmount(withdrawAmountRequest);
		
		if(updateCurrency(amount, currencyMap)) {
	        
			WithdrawAmountResponse withdrawResponse = WithdrawAmountResponse.builder()                
	                								  .currencyList(currencyNotes)
	                								  .availableBalance(details.getNewBalance())
	                								  .availableOverdraft(details.getNewOverdraft())
	                								  .build();
	        return responseAdapter.buildResponse(withdrawResponse);	
		}
		else {
			
			RollbackWithdrawRequest rollbackWithdrawRequest = RollbackWithdrawRequest.builder()
															  .accountNumber(accountNumber)
															  .amount(balanceResponse.getBalance())
															  .overdraft(balanceResponse.getMaxWithdrawalAmount().subtract(balanceResponse.getBalance()))
															  .build();
			BalanceResponse rollbackResponse = accountService.rollbackWithdrawal(rollbackWithdrawRequest);
        	throw new AtmException("Error occurred while updating available currency in ATM");
        }
		
	}
	
	
	public ResponseEntity<BalanceResponse> getAccountBalance(AccountBalanceRequest account) throws AtmException, JSONException {
				
		HttpEntity<AccountBalanceRequest> request = new HttpEntity<>(account);
		BalanceResponse details = accountService.getAccountBalance(account);
		return responseAdapter.buildResponse(details);
	}
	
    private List<CurrencyResponse> getCurrencyNotes(Integer amount, Map<Integer,CurrencyResponse> currencyMap) throws AtmException {

        AtmUtility atmUtil = new AtmUtility();       

        List<CurrencyResponse> currencyNotes = atmUtil.getCurrencyNotes(amount, currencyMap);               

        return currencyNotes;
    }
	
	private boolean updateCurrency(int amount, Map<Integer, CurrencyResponse> currencyMap) throws AtmException {
        
		try {
			List<Currency> currencyList = currencyRepository.findAll();        
	
	        currencyList.forEach(currency -> {
			    CurrencyResponse currencyResponse = currencyMap.get(currency.getCurrency());
			    currency.setQuantity(currencyResponse.getQuantity());
			    currencyRepository.save(currency);
			});
	        return true;
		} catch(RuntimeException ex) {
			return false;
		}
        
		
    }


}
