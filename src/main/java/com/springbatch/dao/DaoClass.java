package com.springbatch.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springbatch.entity.KycCustomerData;

public interface DaoClass extends CrudRepository<KycCustomerData, String> {
	List<KycCustomerData> findByStatusAndCountLessThan(String status, Long count);
}
