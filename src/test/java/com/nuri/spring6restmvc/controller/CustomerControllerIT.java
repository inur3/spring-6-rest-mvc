package com.nuri.spring6restmvc.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.nuri.spring6restmvc.entities.Customer;
import com.nuri.spring6restmvc.mappers.CustomerMapper;
import com.nuri.spring6restmvc.model.BeerDTO;
import com.nuri.spring6restmvc.model.CustomerDTO;
import com.nuri.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest // Load the whole spring context
class CustomerControllerIT {
    /*
     * Implement integration tests between the controller and JPA services
     * that persists to the hibernate database. For that reason no, test
     * splicing and use @SpringBootTest to bring the whole application context.
     */
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;


    @Test
    void patchByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.patchById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void patchById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO patchCustomer = customerMapper.customerToCustomerDto(customer);
        final String updatedName = "New Name Yo!";
        patchCustomer.setId(null);
        patchCustomer.setVersion(null);
        patchCustomer.setCustomerName(updatedName);

        ResponseEntity<CustomerDTO> responseEntity = customerController
                .patchById(customer.getId(), patchCustomer);

        // let's check if the patch was implemented.
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Customer patchedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(patchedCustomer).isNotNull();
        assertThat(patchedCustomer.getCustomerName()).isEqualTo(updatedName);


    }

    @Test
    void deleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteById() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity<CustomerDTO> responseEntity = customerController.deleteById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId())).isEmpty();

    }

    @Test
    void updateByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController
                .updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void updateById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO updatedCustomer = customerMapper.customerToCustomerDto(customer);
        final String updatedName = "New Name Yo!";
        updatedCustomer.setId(null);
        updatedCustomer.setVersion(null);
        updatedCustomer.setCustomerName(updatedName);


        ResponseEntity<CustomerDTO>  responseEntity = customerController
                .updateCustomerById(customer.getId(), updatedCustomer);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Customer savedUpdate = customerRepository.findById(customer.getId()).get();
        assertThat(savedUpdate).isNotNull();
        assertThat(savedUpdate.getCustomerName()).isEqualTo(updatedName);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        // first test the repository by saving a new beer...
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("Dameer Qooqay")
                .build();

        // Now test the response coming from the controller.
        ResponseEntity<CustomerDTO> responseEntity = customerController.saveCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer savedCustomer = customerRepository.findById(savedUUID).orElse(null);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerName()).isEqualTo(customerDTO.getCustomerName());

    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();

    }

    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () ->
                customerController.getCustomerById(UUID.randomUUID()));
    }
    @Test
    void testGetAllCustomers() {
        List<CustomerDTO> dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(6);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyCustomersList() {
        // we'll need to delete the test data first to test this.
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.getAllCustomers();

        assertThat(dtos.size()).isEqualTo(0);

    }


}