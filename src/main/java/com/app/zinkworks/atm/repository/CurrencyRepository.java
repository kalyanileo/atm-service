package com.app.zinkworks.atm.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.app.zinkworks.atm.model.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer>{
	List<Currency> findAll();
	Currency save(Currency currency);

}
