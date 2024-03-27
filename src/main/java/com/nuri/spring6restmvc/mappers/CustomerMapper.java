package com.nuri.spring6restmvc.mappers;

import com.nuri.spring6restmvc.entities.Customer;
import com.nuri.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);

}
