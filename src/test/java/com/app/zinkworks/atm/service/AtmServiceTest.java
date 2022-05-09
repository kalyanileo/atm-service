package com.app.zinkworks.atm.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.zinkworks.atm.adapter.ResponseAdapter;
import com.app.zinkworks.atm.exception.AtmException;
import com.app.zinkworks.atm.model.Currency;
import com.app.zinkworks.atm.model.request.AccountBalanceRequest;
import com.app.zinkworks.atm.model.request.WithdrawAmountRequest;
import com.app.zinkworks.atm.model.response.BalanceResponse;
import com.app.zinkworks.atm.model.response.CurrencyResponse;
import com.app.zinkworks.atm.model.response.WithdrawAccountBalanceResponse;
import com.app.zinkworks.atm.model.response.WithdrawAmountResponse;
import com.app.zinkworks.atm.repository.CurrencyRepository;
import com.app.zinkworks.atm.utils.AtmUtility;
import com.app.zinkworks.atm.service.AccountService;

@ExtendWith(MockitoExtension.class)
public class AtmServiceTest {
	
	@InjectMocks
	AtmService atmService;
	
	@Mock
	private AccountService accountService;
	
	@Mock
	private ResponseAdapter responseAdapter;
	
	@Mock
	private AtmUtility atmUtility;
	
	@Mock
	private CurrencyRepository currencyRepository;
	
	private static List<Currency> currency = new ArrayList<>();
	
	@BeforeAll
    public static void setup() {
		
		currency.add(Currency.builder()
				.id(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"))
                .quantity(10)
                .currency(50)
                .build());
		currency.add(Currency.builder()
				.id(UUID.fromString("4f24e452-f656-4644-96b8-40de91ef1b9b"))
                .quantity(30)
                .currency(20)
                .build());

    }
	
	@Test
	public void checkBalanceValidAccount() throws AtmException {
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)
													  .build();	
		
		BalanceResponse balanceResponse = BalanceResponse.builder()
										  .accountNumber(123456789)
										  .balance(new BigDecimal(800))
										  .maxWithdrawalAmount(new BigDecimal(1000))
										  .build();
				
	
		when(accountService.getAccountBalance(accountBalanceRequest)).thenReturn(balanceResponse);
	
		when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(balanceResponse, HttpStatus.OK));
		ResponseEntity<BalanceResponse> response = atmService.getAccountBalance(accountBalanceRequest);	
				
		assertEquals(new BigDecimal(800),response.getBody().getBalance());
		assertEquals(new BigDecimal(1000),response.getBody().getMaxWithdrawalAmount());
		
	}
	
	@Test
	public void checkInvalidAccount() throws AtmException {
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
													  .accountNumber(1234567890)						  
													  .pin(1234)
													  .build();
	
		when(accountService.getAccountBalance(accountBalanceRequest)).thenThrow(new AtmException("Account with number 1234567890 does not exist"));
		
		try {
			ResponseEntity<BalanceResponse> response = atmService.getAccountBalance(accountBalanceRequest);
		} catch (AtmException ex) {			
			assertEquals("-1",ex.getErrorCode());
		}
	
	}
	
	@Test
	public void checkInvalidPin() throws AtmException {
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
													  .accountNumber(1234567890)						  
													  .pin(1234)
													  .build();	
		
		when(accountService.getAccountBalance(accountBalanceRequest)).thenThrow(new AtmException("Invalid Pin"));

		
		try {
			ResponseEntity<BalanceResponse> response = atmService.getAccountBalance(accountBalanceRequest);
		} catch (AtmException ex) {			
			assertEquals("-1",ex.getErrorCode());
		}
	
	}
	
	@Test
	public void checkwithdrawMoneySuccess() throws AtmException {
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)
													  .amount(new BigDecimal(100))
													  .build();	
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)
													  .build();	
		
		BalanceResponse balanceResponse = BalanceResponse.builder()
													  .accountNumber(123456789)
													  .balance(new BigDecimal(800))
													  .maxWithdrawalAmount(new BigDecimal(1000))
													  .build();
		WithdrawAccountBalanceResponse withdrawAccountBalanceResponse = WithdrawAccountBalanceResponse.builder()
																		.accountNumber(123456789)
																		.newBalance(new BigDecimal(700))
																		.newOverdraft(new BigDecimal(200))
																		.build();
		
		
		Map<Integer, CurrencyResponse> currencyMap = new HashMap<>();
		
		for(Currency currencyIter : currency)
			currencyMap.put(currencyIter.getCurrency(), CurrencyResponse.builder()
														.currency(currencyIter.getCurrency())
														.quantity(currencyIter.getQuantity())
														.build());	
		
		List<CurrencyResponse> currencyResponse = Arrays.asList(new CurrencyResponse[] {
													CurrencyResponse.builder()
									                        .currency(50)
									                        .quantity(2)
									                        .build()
        											});
		WithdrawAmountResponse withdrawAmountResponse = WithdrawAmountResponse.builder()
				.currencyList(currencyResponse)
				.availableBalance(new BigDecimal(700))
				.availableOverdraft(new BigDecimal(200))
				.build();		
		
		when(currencyRepository.findAll()).thenReturn(currency);		
		when(accountService.withdrawAmount(withdrawAmountRequest)).thenReturn(withdrawAccountBalanceResponse);
		when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));

		ResponseEntity<WithdrawAmountResponse> response = atmService.withdrawMoney(withdrawAmountRequest);
		
		assertEquals(50,response.getBody().getCurrencyList().get(0).getCurrency());
		assertEquals(2,response.getBody().getCurrencyList().get(0).getQuantity());
		assertEquals(new BigDecimal(700),response.getBody().getAvailableBalance());
		assertEquals(new BigDecimal(200),response.getBody().getAvailableOverdraft());

	}
	
	@Test
	public void checkUpdateCurrencyFail() throws AtmException {
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)
													  .amount(new BigDecimal(100))
													  .build();	
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)													  
													  .build();	
		
		BalanceResponse balanceResponse = BalanceResponse.builder()
													  .accountNumber(123456789)
													  .balance(new BigDecimal(800))
													  .maxWithdrawalAmount(new BigDecimal(1000))
													  .build();
		
		WithdrawAccountBalanceResponse withdrawAccountBalanceResponse = WithdrawAccountBalanceResponse.builder()
																		.accountNumber(123456789)
																		.newBalance(new BigDecimal(700))
																		.newOverdraft(new BigDecimal(200))
																		.build();
		
		
		Map<Integer, CurrencyResponse> currencyMap = new HashMap<>();
		
		for(Currency currencyIter : currency)
			currencyMap.put(currencyIter.getCurrency(), CurrencyResponse.builder()
														.currency(currencyIter.getCurrency())
														.quantity(currencyIter.getQuantity())
														.build());	
		
		List<CurrencyResponse> currencyResponse = Arrays.asList(new CurrencyResponse[] {
													CurrencyResponse.builder()
									                        .currency(50)
									                        .quantity(2)
									                        .build()
        											});
		WithdrawAmountResponse withdrawAmountResponse = WithdrawAmountResponse.builder()
				.currencyList(currencyResponse)
				.availableBalance(new BigDecimal(700))
				.availableOverdraft(new BigDecimal(200))
				.build();
		
		Currency updatedCurrency1 = Currency.builder().id(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")).currency(50).quantity(8).build();
		Currency updatedCurrency2 = Currency.builder().id(UUID.fromString("4f24e452-f656-4644-96b8-40de91ef1b9b")).currency(20).quantity(30).build();
				
		when(currencyRepository.findAll()).thenReturn(currency);		
		when(accountService.getAccountBalance(any())).thenReturn(balanceResponse);				
		when(currencyRepository.save(updatedCurrency1)).thenThrow(new RuntimeException("Invalid"));
		
		
		try {
			ResponseEntity<WithdrawAmountResponse> response = atmService.withdrawMoney(withdrawAmountRequest);			
		} catch (AtmException ex) {			
			assertEquals("-1",ex.getErrorCode());
		}

	}
	
	@Test
	public void checkwithdrawMultipleCurrencies() throws AtmException {
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)
													  .amount(new BigDecimal(800))
													  .build();	
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
													  .accountNumber(123456789)						  
													  .pin(1234)
													  .build();	
		
		BalanceResponse balanceResponse = BalanceResponse.builder()
													  .accountNumber(123456789)
													  .balance(new BigDecimal(800))
													  .maxWithdrawalAmount(new BigDecimal(1000))
													  .build();
		WithdrawAccountBalanceResponse withdrawAccountBalanceResponse = WithdrawAccountBalanceResponse.builder()
																		.accountNumber(123456789)
																		.newBalance(new BigDecimal(0))
																		.newOverdraft(new BigDecimal(200))
																		.build();
		
		
		Map<Integer, CurrencyResponse> currencyMap = new HashMap<>();
		
		for(Currency currencyIter : currency)
			currencyMap.put(currencyIter.getCurrency(), CurrencyResponse.builder()
														.currency(currencyIter.getCurrency())
														.quantity(currencyIter.getQuantity())
														.build());	
		
		List<CurrencyResponse> currencyResponse = Arrays.asList(new CurrencyResponse[] {
													CurrencyResponse.builder()
									                        .currency(50)
									                        .quantity(10)
									                        .build()
        											,
													CurrencyResponse.builder()
									                .currency(20)
									                .quantity(15)
									                .build()
													}
													
												);
		WithdrawAmountResponse withdrawAmountResponse = WithdrawAmountResponse.builder()
				.currencyList(currencyResponse)
				.availableBalance(new BigDecimal(0))
				.availableOverdraft(new BigDecimal(200))
				.build();
		
		when(currencyRepository.findAll()).thenReturn(currency);		
		when(accountService.withdrawAmount(withdrawAmountRequest)).thenReturn(withdrawAccountBalanceResponse);
		when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));

		ResponseEntity<WithdrawAmountResponse> response = atmService.withdrawMoney(withdrawAmountRequest);
		
		assertEquals(50,response.getBody().getCurrencyList().get(0).getCurrency());
		assertEquals(10,response.getBody().getCurrencyList().get(0).getQuantity());
		assertEquals(20,response.getBody().getCurrencyList().get(1).getCurrency());		
		assertEquals(15,response.getBody().getCurrencyList().get(1).getQuantity());
		assertEquals(new BigDecimal(0),response.getBody().getAvailableBalance());
		assertEquals(new BigDecimal(200),response.getBody().getAvailableOverdraft());

	}




}
