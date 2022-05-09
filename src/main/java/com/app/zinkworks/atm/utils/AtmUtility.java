package com.app.zinkworks.atm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.zinkworks.atm.exception.AtmException;
import com.app.zinkworks.atm.model.Currency;
import com.app.zinkworks.atm.model.response.CurrencyResponse;
import com.app.zinkworks.atm.repository.CurrencyRepository;


public class AtmUtility {
	
    public List<CurrencyResponse> getCurrencyNotes(Integer amount, Map<Integer, CurrencyResponse> currencyMap)
            throws AtmException {
        
        List<CurrencyResponse> currencyNotes = new ArrayList<>();       
        int remainingAmount = amount;
        int size = currencyMap.size();
                
        Set<Integer> keySet = currencyMap.keySet();
        
        ArrayList<Integer> denominations = new ArrayList<Integer>(keySet);
        Collections.sort(denominations,Collections.reverseOrder());	
        

        for (int i = 0; i < size  && remainingAmount != 0; i++) { 
            
            int requiredNotes = remainingAmount / denominations.get(i);
            
            if(currencyMap.get(denominations.get(i)).getQuantity() >= 0) {
                CurrencyResponse currency = currencyMap.get(denominations.get(i));
                int totalNotes = currency.getQuantity();                
                
                int dispenseNotes = (totalNotes >= requiredNotes)? requiredNotes : totalNotes;
                
                currency.setQuantity(totalNotes-dispenseNotes);
                currencyMap.put(denominations.get(i), currency);
                
                currencyNotes.add(CurrencyResponse.builder()
                        .quantity(dispenseNotes)
                        .currency(denominations.get(i))
                        .build());
                
                remainingAmount -= dispenseNotes * denominations.get(i);
            }
        }

        if(remainingAmount != 0) 
            throw new AtmException("No currency to dispense");

        return currencyNotes;
    }

}
