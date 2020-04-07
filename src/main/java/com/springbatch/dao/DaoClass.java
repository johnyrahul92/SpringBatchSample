package com.springbatch.dao;

import com.springbatch.entity.KycCustomerData;
import org.springframework.data.repository.CrudRepository;

public interface DaoClass extends CrudRepository<KycCustomerData, String> {
}
