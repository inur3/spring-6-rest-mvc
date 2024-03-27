package com.nuri.spring6restmvc.services;

import com.nuri.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private  Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Babaloo Johnson")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("BamBaam Dameer")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Hey Vamos")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Optional<CustomerDTO> patchById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }
        return Optional.of(existing);
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {

        CustomerDTO existingCustomer = customerMap.get(customerId); // To be updated

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setCreatedDate(LocalDateTime.now());
        existingCustomer.setUpdateDate(LocalDateTime.now());


        customerMap.put(customerId, existingCustomer);

        return Optional.of(existingCustomer);
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        log.debug("Returning list of customers!");
        return  new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> customerById(UUID id) {
        log.debug("Returning customer via ID.");
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(customer.getVersion())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }
}
