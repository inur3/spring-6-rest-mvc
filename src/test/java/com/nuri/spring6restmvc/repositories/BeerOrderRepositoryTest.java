package com.nuri.spring6restmvc.repositories;

import com.nuri.spring6restmvc.entities.Beer;
import com.nuri.spring6restmvc.entities.BeerOrder;
import com.nuri.spring6restmvc.entities.BeerOrderShipment;
import com.nuri.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Rollback
    @Transactional
    @Test
    void testBeerOrders() {

        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test Order")
                .customer(testCustomer)
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("1234r")
                        .build())
                .build();

        // will return the saved object, good way to test persistence
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        assertThat(savedBeerOrder.getCustomer()).isEqualTo(testCustomer);
    }
}