package com.app.zinkworks.atm.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.zinkworks.atm.model.Currency;
import com.app.zinkworks.atm.repository.CurrencyRepository;

@Component
public class AtmLoader implements CommandLineRunner {
	
	@Autowired
	private CurrencyRepository currencyRepository;

    @Override
    public void run(String... args) throws Exception {
        loadAtm();
    }

    /**
     * Loads the ATM on the startup
     */
    private void loadAtm() {
                    currencyRepository.save(
            		Currency.builder()
                    .quantity(10)
                    .currency(50)
                    .build());

            currencyRepository.save(Currency.builder()
                    .quantity(30)
                    .currency(20)
                    .build());

            currencyRepository.save(Currency.builder()
                    .quantity(30)
                    .currency(10)
                    .build());

            currencyRepository.save(Currency.builder()
                    .quantity(20)
                    .currency(5)
                    .build());
        
    }

}
