package com.javatechie.springbatch.config;

import com.javatechie.springbatch.entity.Customer;
import com.javatechie.springbatch.entity.CustomerDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<CustomerDetail, Customer> {
    private static final Logger log = LoggerFactory.getLogger(CustomerProcessor.class);
    @Override
    public Customer process(CustomerDetail customerDetail) throws Exception {
        log.info("processing user data.....{}", customerDetail);
        Customer transformedCustomer = new Customer();
        transformedCustomer.setFirstName(customerDetail.getFirstName());
        transformedCustomer.setLastName(customerDetail.getLastName());
        transformedCustomer.setEmail(customerDetail.getEmail());
        transformedCustomer.setGender(customerDetail.getEmail());
        transformedCustomer.setContact(customerDetail.getContact());
        transformedCustomer.setCountry(customerDetail.getCountry());
        transformedCustomer.setDob(customerDetail.getDob());
        return transformedCustomer;
    }
}
