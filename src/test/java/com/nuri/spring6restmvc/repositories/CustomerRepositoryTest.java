package com.nuri.spring6restmvc.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nuri.spring6restmvc.entities.Beer;
import com.nuri.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;


    @Test
    void testSaveCustomer() {
        Customer customer = customerRepository.save((Customer.builder()
                .customerName("New Name")
                .build()));

        assertThat(customer.getId()).isNotNull();

    }

    @Test
    void testCustomerCount() throws IOException {
        // Load the beer repository with test data.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/customers.json")) {
            customerRepository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Customer>>() {
            }));
        }

        List<Customer> customers = customerRepository.findAll();

        assertThat(customerRepository.count()).isGreaterThan(3L);
        assertThat(customers.get(0).getId()).isNotNull();
    }


}