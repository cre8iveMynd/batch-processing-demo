package com.javatechie.springbatch.repository;

import com.javatechie.springbatch.entity.CustomerDetail;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepository extends MongoRepository<CustomerDetail, Integer> {
}
