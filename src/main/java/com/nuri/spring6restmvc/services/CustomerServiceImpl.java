package com.nuri.spring6restmvc.services;

import com.nuri.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private  Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Babaloo Johnson")
                .version("1")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("BamBaam Dameer")
                .version("2")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Hey Vamos")
                .version("3")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public void patchById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {

        Customer existingCustomer = customerMap.get(customerId); // To be updated

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setCreatedDate(LocalDateTime.now());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());


        customerMap.put(customerId, existingCustomer);
    }

    @Override
    public void deleteById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public List<Customer> listCustomers() {
        log.debug("Returning list of customers!");
        return  new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer customerById(UUID id) {
        log.debug("Returning customer via ID.");
        return customerMap.get(id);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(customer.getVersion())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }
}
