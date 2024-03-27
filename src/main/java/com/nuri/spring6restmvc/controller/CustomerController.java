package com.nuri.spring6restmvc.controller;

import com.nuri.spring6restmvc.model.CustomerDTO;
import com.nuri.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static  final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String  CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;


    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> patchById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {

        if(customerService.patchById(customerId, customer).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> deleteById(@PathVariable("customerId") UUID customerId) {

        if(!customerService.deleteById(customerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {

        if(customerService.updateCustomerById(customerId, customer).isEmpty()) {
            throw new NotFoundException();
        };

        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customer) {

        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> getAllCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        return customerService.customerById(customerId).orElseThrow(NotFoundException::new);
    }

}
