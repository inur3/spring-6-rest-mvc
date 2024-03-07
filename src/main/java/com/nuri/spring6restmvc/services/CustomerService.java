package com.nuri.spring6restmvc.services;

import com.nuri.spring6restmvc.model.Customer;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();

    Customer customerById(UUID id);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteById(UUID customerId);

    void patchById(UUID customerId, Customer customer);
}
